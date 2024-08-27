package com.iamyanbing.filter;

import com.iamyanbing.common.Constants;
import com.iamyanbing.entity.LoginUser;
import com.iamyanbing.enums.CommonStatusEnum;
import com.iamyanbing.exception.AuthException;
import com.iamyanbing.util.JwtUtil;
import com.iamyanbing.util.LoginUserContextUtil;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

/**
 * 自定义认证过滤器,用来校验用户请求中携带的Token
 */
@Component
@Slf4j
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {

    /**
     * 封装过滤器的执行逻辑
     *
     * @param request
     * @param response
     * @param filterChain
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        // 1.从请求头中获取 token
        String token = request.getHeader(Constants.TOKEN);

        // 2.判断 token 是否为空,为空直接放行
        // 为什么为空直接放行？ 因为登录时 token 为空
        if (!StringUtils.hasText(token)) {
            //所有 token 为空都放行，会不会存在问题？  答案：不会
            //在 SecurityConfig 类 securityFilterChain 方法中已经配置好了登录接口.(antMatchers)
            //只要不是配置的登录接口，放行之后都会通过 AuthenticationEntryPointImpl 类，给客户端响应 401
            //框架怎么判断是否认证成功？ 见第五步
            filterChain.doFilter(request, response);

            //return 的目的 是返回响应的时候,避免走下面的逻辑
            return;
        }
        // 兼容 OAuth2.0 协议
        // JWT 标准写法 Authorization: Bearer aaa.bbb.ccc
        if (token.startsWith("Bearer ")) {
            token = token.substring("Bearer ".length());
        }


        // 3.解析 token
        String userId;
        try {
            Claims claims = JwtUtil.parseJWT(token);
            userId = (String) claims.get("userId");
        } catch (Exception e) {
            log.error("解析token异常", e);
            // Filter 中抛出的异常，GlobalExceptionHandler、AccessDeniedHandlerImpl、AuthenticationEntryPointImpl 不捕获
            // 所以异常交给 ExceptionController 类 handleAuthException 方法处理
            request.setAttribute("authException", new AuthException(CommonStatusEnum.USER_TOKEN.getCode(),
                    CommonStatusEnum.USER_TOKEN.getMessage()));
            request.getRequestDispatcher(Constants.AUTH_EXCRPTION_PATH).forward(request, response);
            return;
        }

        // 4.从redis中获取用户信息，除了用户名等信息之外，还包括用户角色信息、用户权限信息
        // 如果JWT设置了有效期，则在这一步要判断JWT是否过期
        // String redisKey = "login:" + userId;
        // LoginUser loginUser = redisCache.getCacheObject(redisKey);
        // 本项目直接从本地缓存中获取 用户信息
        LoginUser loginUser = LoginUserContextUtil.getLoginUser(Long.parseLong(userId));
        if (Objects.isNull(loginUser)) {
            // Filter 中抛出的异常，GlobalExceptionHandler、AccessDeniedHandlerImpl、AuthenticationEntryPointImpl 不捕获
            // 所以异常交给 ExceptionController 类 handleAuthException 方法处理
            request.setAttribute("authException", new AuthException(CommonStatusEnum.USER_LOGIN.getCode(),
                    CommonStatusEnum.USER_LOGIN.getMessage()));
            request.getRequestDispatcher(Constants.AUTH_EXCRPTION_PATH).forward(request, response);
            return;
        }

        // 5.将用户信息保存至 SecurityContextHolder,方便后续的访问控制和授权操作
        // UsernamePasswordAuthenticationToken 三个参数的构造方法:
        // principal：表示认证请求的主体，通常是一个 用户名 或者 其他识别主体的信息。
        // credentials：表示认证请求的凭据，通常是密码或者其他证明主体身份的信息。
        // authorities：权限信息
        // LoginServiceImpl 类也有使用 UsernamePasswordAuthenticationToken
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(loginUser, null, loginUser.getAuthorities());

        // 组装一个 authentication 对象，通过 setAuthentication 方法把它放在上下文对象中，
        // 这样后面的过滤器看到我们上下文对象中有 authentication 对象，
        // 就相当于我们已经认证过了。
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);

        // 6.放行
        filterChain.doFilter(request, response);
    }
}
