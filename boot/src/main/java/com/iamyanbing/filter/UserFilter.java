package com.iamyanbing.filter;

import com.google.gson.Gson;
import com.iamyanbing.domain.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Base64;

/**
 * @description
 */
@Slf4j
public class UserFilter implements Filter {

    /**
     * 用户信息在请求头中传递。
     * 在这里获取用户信息，获取之后存入ThreadLocal。
     * 后续使用从ThreadLocal 取
     * eg：/users/getFilterUser
     * @param servletRequest
     * @param servletResponse
     * @param filterChain
     * @throws IOException
     * @throws ServletException
     */
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        User userInfo = getUserInfo(request);
        UserContext.setUser(userInfo);
        filterChain.doFilter(request, response);
    }

    private User getUserInfo(HttpServletRequest request) {
        String userInfoStr = request.getHeader("userInfo");
        if (StringUtils.isEmpty(userInfoStr)) {
            log.info("【用户拦截器】当前请求[{}]:无用户信息!", request.getServletPath());
            return defaultUser();
        }
        String userInfoDec = new String(Base64.getDecoder().decode(userInfoStr));
        return new Gson().fromJson(userInfoDec, User.class);
    }

    private User defaultUser() {
        User user = new User();
        user.setAge(18);
        user.setId(25L);
        user.setName("yanbing");
        return user;
    }
}
