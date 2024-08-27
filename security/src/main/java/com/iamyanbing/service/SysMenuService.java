package com.iamyanbing.service;

import com.iamyanbing.entity.SysMenu;
import com.iamyanbing.res.RouterVO;

import java.util.List;

/**
 * 菜单 业务层
 **/
public interface SysMenuService {

    /**
     * 根据用户Id 查询菜单信息
     * 树结构
     */
    List<SysMenu> selectMenuTreeByUserId(Long userId);

    /**
     * 构建前端路由所需要的菜单
     */
    List<RouterVO> buildMenus(List<SysMenu> menus);

}
