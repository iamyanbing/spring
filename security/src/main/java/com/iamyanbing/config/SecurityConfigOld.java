package com.iamyanbing.config;

import com.iamyanbing.filter.JwtAuthenticationTokenFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * 设置prePostEnabled = true ： 开启 Spring Security 的方法级别安全控制
 * pre 表示在方法执行前进行授权校验， post 表示在方法执行后进行授权校验
 * <p>
 * SpringSecurity 5.4.x以下 用法配置
 */
//@EnableGlobalMethodSecurity(prePostEnabled = true)
//@Configuration
public class SecurityConfigOld extends WebSecurityConfigurerAdapter {

    @Autowired
    private JwtAuthenticationTokenFilter jwtAuthenticationTokenFilter;

    @Autowired
    private AuthenticationEntryPoint authenticationEntryPoint;

    @Autowired
    private AccessDeniedHandler accessDeniedHandler;

    /**
     * BCryptPasswordEncoder注入到Spring容器
     *
     * @return
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * 注入 AuthenticationManager,供外部类使用。
     * 比如 LoginServiceImpl 类使用
     */
    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    /**
     * 用于配置 HTTP 请求的安全处理
     *
     * @param http
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {

        //关闭csrf
        http.csrf().disable();
        //允许跨域
        http.cors();

        http.
                sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)//不会去创建会话,每个请求都被视为独立的请求,STATELESS表示无状态
                .and()
                //定义请求授权规则
                .authorizeRequests()
                //对登录接口 允许匿名访问
                .antMatchers("/user/login").anonymous()
                //基于配置的权限控制
                //.antMatchers("/hasAuthority").hasAuthority("system:user:list")
                //除上面外的所有请求 就全部需要鉴权认证
                .anyRequest().authenticated();

        // 将自定义认证过滤器 添加到过滤器链
        // addFilterBefore 的语义是添加一个 filter 到 beforeFilter 之前，
        // 因为 filter 的执行是有顺序的，
        // 所以必须要把我们的 JwtAuthenticationTokenFilter 放在过滤器链中 UsernamePasswordAuthenticationFilter 之前，
        // 这样才会起到自动认证的效果。
        http.addFilterBefore(jwtAuthenticationTokenFilter, UsernamePasswordAuthenticationFilter.class);

        //配置异常处理器
        http.exceptionHandling()
                //配置认证异常处理器  401
                .authenticationEntryPoint(authenticationEntryPoint)
                //配置授权异常处理器  403
                .accessDeniedHandler(accessDeniedHandler);

    }
}
