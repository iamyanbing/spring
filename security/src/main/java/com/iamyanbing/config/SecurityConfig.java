package com.iamyanbing.config;

import com.iamyanbing.filter.JwtAuthenticationTokenFilter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Arrays;

/**
 * 设置 prePostEnabled = true ： 开启 Spring Security 的方法级别安全控制
 * pre 表示在方法执行前进行授权校验， post 表示在方法执行后进行授权校验
 * 对应 @PreAuthorize 和 @PostAuthorize 注解
 * <p>
 * SpringSecurity 5.4.x以上新用法配置
 * <p>
 *
 * @EnableWebSecurity 开启 Spring Security的功能 代替了 implements WebSecurityConfigurerAdapter ?
 * 见云笔记 《103.EnableWebSecurity》
 */
@EnableGlobalMethodSecurity(prePostEnabled = true)
@Configuration
@Slf4j
public class SecurityConfig {
    @Autowired
    private JwtAuthenticationTokenFilter jwtAuthenticationTokenFilter;

    @Autowired
    private AuthenticationEntryPoint authenticationEntryPoint;

    @Autowired
    private AccessDeniedHandler accessDeniedHandler;

    @Autowired
    private AuthenticationSuccessHandler successHandler;

    @Autowired
    private AuthenticationFailureHandler failureHandler;

    @Autowired
    private LogoutSuccessHandler logoutSuccessHandler;

    @Autowired
    private CorsFilter corsFilter;

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
    public AuthenticationManager authenticationManagerBean(
            AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    /**
     * 用于配置 HTTP 请求的安全处理
     * <p>
     * 注意：放行资源必须放在所有认证请求之前！
     * <p>
     * authorizeRequests() 和 authorizeHttpRequests() 是两种配置的方式。
     * authorizeRequests() 是较早版本 Spring Security 使用的配置方法，
     * authorizeHttpRequest() 是官方在未来版本中将要主要推荐的配置方式。
     * 具体差异见官方文档： https://docs.spring.io/spring-security/reference/servlet/authorization/authorize-http-requests.html
     *
     * @param http
     * @return
     * @throws Exception
     */
    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        //关闭csrf
        http.csrf().disable();
        //允许跨域
//        http.cors();
//        http.cors().configurationSource(corsConfigurationSource());

        http.
                //不会去创建会话,每个请求都被视为独立的请求,STATELESS表示无状态
                //基于token，所以不需要 session
                        sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                //定义请求授权规则
                .authorizeHttpRequests()
                //对登录接口、登录页面、获取验证码接口，允许匿名访问
                //主要是通过访问 /login.html 查看验证码效果，不去实际登录
                .antMatchers("/user/login", "/code/image", "/captchaImage", "/login.html").permitAll()
                // 所有的静态资源允许匿名访问
//                .antMatchers(
//                        "/css/**",
//                        "/js/**",
//                        "/images/**",
//                        "/fonts/**",
//                        "/favicon.ico"
//                ).permitAll()
                //基于配置的权限控制
                //.antMatchers("/hasAuthority").hasAuthority("system:user:list")
                //除了上面的资源,其他的请求都要经过认证或者授权
                .anyRequest().authenticated();

        //开启表单认证
        //formLogin() 适合前后端不分离项目，前后端分离项目不用。
        //调用 /user/login 接口失败，会调用 failureHandler 对象处理，不会调用 authenticationEntryPoint 对象
        // loginProcessingUrl() 设置的值必须和登录表单 form 中 action 的地址，不然不会调用 successHandler、failureHandler
        // loginProcessingUrl 的作用是用来拦截前端页面对 /user/login 这个的请求的，拦截到了就走框架自己的处理流程，不会调用 /user/login 这个接口
        // 所以这里不演示
//        http.formLogin()
//                .loginPage("/login.html")  //用户未登录时，访问任何资源都转跳到该路径，即登录页面
//                .loginProcessingUrl("/user/login") //登录表单 form 中 action 的地址
//                .usernameParameter("userName") //登录表单 form 中用户名输入框 input 的 name 值，不修改的话默认是 username
//                .passwordParameter("password") //登录表单 form 中密码输入框 input 的 name 值，不修改的话默认是 password
//                .successHandler(successHandler) //认证成功处理器
//                .failureHandler(failureHandler); //认证失败处理器

        //开启注销配置
        http.logout()
                .logoutUrl("/user/logout") //指定退出登录请求地址，默认是 GET 请求，路径为 /logout
                .invalidateHttpSession(true) //退出时 session是否失效,默认true
                .clearAuthentication(true) //退出时 是否清除认证信息,默认true
                .logoutSuccessHandler(logoutSuccessHandler);

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

        //添加 CORS filter
        http.addFilterBefore(corsFilter, JwtAuthenticationTokenFilter.class);
        //确保在用户注销时,响应头中依然包含跨域的字段
        http.addFilterBefore(corsFilter, LogoutFilter.class);

        return http.build();
    }

    public CorsConfigurationSource corsConfigurationSource(){
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.addAllowedOriginPattern("*");
        corsConfiguration.addAllowedHeader("*");
        corsConfiguration.addAllowedMethod("*");
        corsConfiguration.setAllowCredentials(true);
//        corsConfiguration.setAllowedOrigins(Arrays.asList("*"));
//        corsConfiguration.setAllowedMethods(Arrays.asList("*"));
//        corsConfiguration.setAllowedHeaders(Arrays.asList("*"));

        UrlBasedCorsConfigurationSource ub = new UrlBasedCorsConfigurationSource();
        ub.registerCorsConfiguration("/**", corsConfiguration);
        return ub;
    }

    public static void main(String[] args) {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String e1 = passwordEncoder.encode("123456");
        String e2 = passwordEncoder.encode("123456");
        log.info("e1: {}", e1);
        log.info("e2: {}", e2);
        System.out.println(e1.equals(e2));
        log.info("e1.equals(e2): {}", e1.equals(e2));

        //$2a$10$0CS95XYw7GyDQNXq6FO7FuWDHR4yLTVyFXgQICjgTddWIG9OJ6isy
        boolean b = passwordEncoder.matches("123456",
                "$2a$10$0CS95XYw7GyDQNXq6FO7FuWDHR4yLTVyFXgQICjgTddWIG9OJ6isy");

        log.info("=============== : {}", b);

        String admin = passwordEncoder.encode("admin");
        String test = passwordEncoder.encode("test");
        log.info("admin: {}", admin);
        log.info("test: {}", test);
    }
}
