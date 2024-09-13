package com.iamyanbing.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.util.Date;
import java.util.Map;
import java.util.UUID;

/**
 * 参考 security 项目 JwtUtil类
 * 区别：JWT 包不一样
 */
public class JwtUtils {

    /**
     * 有效期
     * 60 * 60 *1000  一个小时
     */
    public static final Long JWT_TTL = 60 * 60 * 1000L;

    // 盐
    private static final String SIGN = "iamyanbing!@#$%^&*()_+<>?/.,;:'~";


    /**
     * 用于设置 jwt id。 最好是用雪花算法生成的id
     * 作用：用于踢掉历史登录
     *
     * @return
     */
    public static String getUUID() {
        String token = UUID.randomUUID().toString().replaceAll("-", "");
        return token;
    }

    // 生成token
    public static String generatorToken(Map<String, Object> claims) {

        long nowMillis = System.currentTimeMillis();
        Date nowDate = new Date(nowMillis);
        long expMillis = nowMillis + JwtUtils.JWT_TTL;
        Date expDate = new Date(expMillis);

        JWTCreator.Builder builder = JWT.create();
        // 防止每次生成的token一样。
        builder.withJWTId(getUUID());
        builder.withSubject("security");
        builder.withIssuer("yb");
        builder.withIssuedAt(nowDate);
        builder.withExpiresAt(expDate);

        // 整合 claims
        claims.forEach(
                (k, v) -> {
                    if (v instanceof Integer) {
                        builder.withClaim(k, (Integer) v);
                    } else {
                        builder.withClaim(k, v.toString());
                    }
                }
        );
        // 生成 token
        String sign = builder.sign(Algorithm.HMAC256(SIGN));

        return sign;
    }


    // 解析token
    public static String parseToken(String token) {
        DecodedJWT verify = JWT.require(Algorithm.HMAC256(SIGN)).build().verify(token);
        String userId = verify.getClaim("userId").asString();
        Map<String, Claim> claimMap = verify.getClaims();
        return userId;
    }


    /**
     * 结合 security 项目 JwtUtil类来测试
     * 即 security 项目 JwtUtil类生成JWT，
     * 本类解析JWT
     *
     * @param args
     */
    public static void main(String[] args) {
        String jwt = "eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiIzYTViNjNlODQ4ODk0ZWEzYjZkMzA3MDUxNTQyMjkwNCIsInN1YiI6InNlY3VyaXR5IiwiaXNzIjoieWIiLCJpYXQiOjE3MjYyMDcyNzEsInVzZXJOYW1lIjoieWFuYmluZyIsInVzZXJJZCI6IjEwMDEiLCJleHAiOjE3MjYyMTA4NzF9.tT-Xl3Qa1noiOeTf6k6CPkPP3PTomJWT2MZDAp05Rpo";
        String userId = JwtUtils.parseToken(jwt);
        System.out.println(userId);
    }
}
