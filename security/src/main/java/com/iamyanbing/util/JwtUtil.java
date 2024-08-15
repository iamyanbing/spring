package com.iamyanbing.util;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SecureDigestAlgorithm;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * JWT工具类
 */
public class JwtUtil {

    /**
     * 有效期
     * 60 * 60 *1000  一个小时
     */
    public static final Long JWT_TTL = 60 * 60 * 1000L;

    /**
     * secretKey： 新版本强制要求 secretKey 长度必须大于32个字符，即必须大于256位
     * 错误信息：
     * The JWT JWA Specification (RFC 7518, Section 3.2) states that keys used
     * with HMAC-SHA algorithms MUST have a size >= 256 bits
     * the key size must be greater than or equal to the hash output size).
     * <p>
     * 注意 ： jwt秘钥 此秘钥一定要保留好在服务端, 不能暴露出去, 否则sign就可以被伪造, 如果对接多个客户端建议改造成多个
     */
    public static final String SECRE_KEY = "iamyanbing!@#$%^&*()_+<>?/.,;:'~";

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


    /**
     * 生成jwt
     *
     * @param claims 自定义claims，存储更多的信息（例如userId、userName等）
     * @param ttlMillis jwt过期时间(毫秒)
     * @return
     */
    public static String createJWT(Map<String, Object> claims, Long ttlMillis) {
        long nowMillis = System.currentTimeMillis();
        Date nowDate = new Date(nowMillis);
        if (ttlMillis == null) {
            ttlMillis = JwtUtil.JWT_TTL;
        }
        long expMillis = nowMillis + ttlMillis;
        Date expDate = new Date(expMillis);

        //生成 HMAC 密钥，根据提供的字节数组长度选择适当的 HMAC 算法，并返回相应的 SecretKey 对象。
        //因为 SECRE_KEY 长度为32，小于48，所以返回 HmacSHA256。 见源码
        SecretKey key = Keys.hmacShaKeyFor(SECRE_KEY.getBytes(StandardCharsets.UTF_8));

        //指定加密算法
        SecureDigestAlgorithm<SecretKey, SecretKey> algorithm = Jwts.SIG.HS256;

        JwtBuilder builder = Jwts.builder()
                .id(getUUID()) //唯一的ID
                .subject("security") // 主题（该jwt是用于干嘛的）
                .issuer("yb") // 签发者
                .issuedAt(nowDate) // 签发时间
                //设置签名使用的签名算法和签名使用的秘钥
//                .signWith(key, algorithm)
                // 根据 key 判断使用什么 签名算法。key值：HmacSHA256、HmacSHA384、HmacSHA512
                // 这里 签名算法 使用 HS256
                .signWith(key)
                .claims(claims) //设置自定义负荷信息
                .expiration(expDate);
        return builder.compact();
    }

    /**
     * jwt解密
     *
     * @param token 加密后的token
     * @return
     */
    public static Claims parseJWT(String token) {
        //说明见上面 createJWT方法
        SecretKey key = Keys.hmacShaKeyFor(SECRE_KEY.getBytes(StandardCharsets.UTF_8));

        Jws<Claims> jws = Jwts.parser()
                // 设置签名的秘钥
                .verifyWith(key)
                .build()
                .parseSignedClaims(token);
        Claims claims = jws.getPayload();
        return claims;
    }


    public static void main(String[] args) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", "1001");
        claims.put("userName", "yanbing");
        String token = createJWT(claims, null);
        System.out.println(token);

        Claims claimsRes = parseJWT(token);
        //claims就是在Pyload中存放的用户信息
        System.out.println(claimsRes.get("userId"));
        System.out.println(claimsRes.get("userName"));

    }

}