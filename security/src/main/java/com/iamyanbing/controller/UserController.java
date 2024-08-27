package com.iamyanbing.controller;

import com.iamyanbing.entity.LoginUser;
import com.iamyanbing.entity.SysMenu;
import com.iamyanbing.res.ResponseResult;
import com.iamyanbing.res.RouterVO;
import com.iamyanbing.service.SysMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Objects;

@RestController
public class UserController {
    @Autowired
    SysMenuService service;

    /**
     * 获取路由信息
     * <p>
     * admin、test 用户才可以访问
     */
    @PreAuthorize("@permsExpression.hasRole('test')")
    @GetMapping("/getRouters")
    public ResponseResult getRouters() {
        UsernamePasswordAuthenticationToken authenticationToken =
                (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();

        if (Objects.isNull(authenticationToken)) {
            // 交给 AuthenticationEntryPointImpl 类处理
            throw new RuntimeException("获取用户认证信息失败!");
        }

        LoginUser loginUser = (LoginUser) authenticationToken.getPrincipal();
        Long userId = loginUser.getSysUser().getUserId();

        //获取菜单列表
        List<SysMenu> menus = service.selectMenuTreeByUserId(userId);
        //将获取到的菜单列表转换为 前端需要的路由列表
        List<RouterVO> routerVoList = service.buildMenus(menus);

        return ResponseResult.success(routerVoList);
    }
}
