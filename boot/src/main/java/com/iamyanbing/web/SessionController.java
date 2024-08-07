package com.iamyanbing.web;

import com.iamyanbing.domain.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * 认证成功之后，用来设置用户信息
 *
 * <pre>
 * getAttribute(String);获取session的节点，比如你在其中一个Servlet中通过setAttribute(String,Object)配置了相关的session信息，
 * 然后通过另一个Servlet获取这个Servlet中的session信息，就要用到getAttribute(String)这个方法
 *
 * setAttribute(String,Object);设置Session的值
 * removeAttribute(String);移除Session的值
 * getCreationTime();当前session创建的时间
 * getLastAccessedTime();最近的一次访问这个session的时间。
 * getId();获取session的唯一标识
 * getServletContext();代表整个web服务
 * isNew();判断一个session是否是一个新的session
 * invalidate();注销session的
 * </pre>
 * 服务器创建session出来后，会把session的id号，以cookie的形式返回给客户端，这样，只要客户端的浏览器不关，再去访问服务器时，
 * 都会带着session的id号去，服务器发现客户端浏览器带session id过来了，就会使用内存中与之对应的session为之服务。
 * @Author huangyanbing
 * @Date 2022/2/10 21:17
 * @description
 */
@RestController
@RequestMapping("/session")
@Slf4j
public class SessionController {

    /**
     * 第一次访问时，服务器会创建一个新的sesion，并且把session的Id以cookie的形式发送给客户端
     * 响应头为 Set-Cookie
     *
     * 完整响应头信息如下：
     * Set-Cookie : JSESSIONID=BBAABC3F017A61B9FC9F73D43F1D8DFA; Path=/; HttpOnly
     *
     * 猜想request.getSession()方法内部新创建了Session之后一定是做了如下的处理:
     * //获取session的Id
     * String sessionId = session.getId();
     * //将session的Id存储到名字为JSESSIONID的cookie中
     * Cookie cookie = new Cookie("JSESSIONID", sessionId);
     * //设置cookie的有效路径
     * cookie.setPath(request.getContextPath());
     * response.addCookie(cookie);
     *
     * @param req
     * @return
     */
    @GetMapping(value = "/generate")
    public String generateSession(HttpServletRequest req) {
        HttpSession httpSession = req.getSession();

        //用于获取 session的 id（唯一）
        log.info(httpSession.getId());

        //用于判断 session 是不是新建的，true 表示是刚创建的，false 表示获取之前就已经创建了
        log.info(httpSession.isNew() + "");

        //认证成功之后，用来设置用户信息
        httpSession.setAttribute("name", "hyb");
        User user = new User();
        user.setId(1L);
        user.setName("hyb");
        user.setAge(25);
        httpSession.setAttribute("user", user);

        return  "success";
    }

    /**
     * 请求头设置方式如下:
     * key : Cookie
     * value : JSESSIONID=BBAABC3F017A61B9FC9F73D43F1D8DFA
     * BBAABC3F017A61B9FC9F73D43F1D8DFA 是 HttpSession对象 id
     * @param req
     * @return
     */
    @GetMapping(value = "/get")
    public String getSession(HttpServletRequest req) {
        HttpSession httpSession = req.getSession();

        //用于获取 session的 id（唯一）
        log.info(httpSession.getId());

        //用于判断 session 是不是新建的，true 表示是刚创建的，false 表示获取之前就已经创建了
        log.info(httpSession.isNew() + "");

        //认证成功之后，用来设置用户信息
        String name = (String) httpSession.getAttribute("name");

        User user = (User) httpSession.getAttribute("user");

        return  "success";
    }
}
