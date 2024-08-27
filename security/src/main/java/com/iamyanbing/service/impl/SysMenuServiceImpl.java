package com.iamyanbing.service.impl;

import com.iamyanbing.common.Constants;
import com.iamyanbing.entity.SysMenu;
import com.iamyanbing.res.MetaVO;
import com.iamyanbing.res.RouterVO;
import com.iamyanbing.service.SysMenuService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;


@Service
public class SysMenuServiceImpl implements SysMenuService {

    @Override
    public List<SysMenu> selectMenuTreeByUserId(Long userId) {

        List<SysMenu> menus = null;

        if (userId != null && userId == 1000L) {
            // 管理员查询出所有目录、菜单
            menus = obtainMenuAll();
        } else {
            menus = obtainMenuByUserId(userId);
        }

        // 封装子菜单。方式一：未知 parentId
        getChildPerms(menus);

        // 封装子菜单。方式二：已知 parentId
        return getChildPerms(menus, 0);
    }

    @Override
    public List<RouterVO> buildMenus(List<SysMenu> menus) {

        List<RouterVO> routers = new ArrayList<>();

        for (SysMenu menu : menus) {
            RouterVO routerVo = new RouterVO();
            //设置路由名称 例如: System 开头字母大写
            routerVo.setName(getRouterName(menu));
            //设置路由地址
            //一级菜单,即菜单类型为 M(目录)。 path前面才加 /
            //例如: 根目录 /system , 二级目录 user
            routerVo.setPath(getRouterPath(menu));
            //设置组件地址
            routerVo.setComponent(getComponent(menu));
            //设置是否隐藏 ,隐藏后侧边栏不会出现
            routerVo.setHidden("1".equals(menu.getVisible()));
            //基础元素
            routerVo.setMeta(new MetaVO(menu.getMenuName(), menu.getIcon(), "1".equals(menu.getIsCache())));
            //子菜单
            List<SysMenu> subMenuList = menu.getChildren();
            //子菜单不为空 && 类型为M 菜单类型（目录 顶级父菜单）
            if (!subMenuList.isEmpty() && subMenuList.size() > 0 && Constants.TYPE_DIR.equals(menu.getMenuType())) {
                //下面有子路由
                routerVo.setAlwaysShow(true);
                //在导航栏中不可点击。即点击弹出子菜单。
                routerVo.setRedirect("noRedirect");
                //递归设置子菜单
                routerVo.setChildren(buildMenus(subMenuList));
            }
            routers.add(routerVo);
        }
        return routers;
    }

    /**
     * 获取组件信息
     *
     * @param menu
     * @return: 组件信息
     */
    public String getComponent(SysMenu menu) {
        // 一级菜单，点击会弹出子菜单
        String component = Constants.LAYOUT;
        if (StringUtils.isNotBlank(menu.getComponent())) {
            // 最后一级菜单，点击会跳转页面
            component = menu.getComponent();
        } else if (menu.getParentId().intValue() != 0 && Constants.TYPE_DIR.equals(menu.getMenuType())) {
            // 非一级菜单，非最后一级菜单，点击弹出子菜单
            component = Constants.PARENT_VIEW;
        }

        return component;
    }


    /**
     * 获取路由地址
     *
     * @param menu
     * @return: java.lang.String
     */
    private String getRouterPath(SysMenu menu) {
        String path = menu.getPath();
        // 一级目录,并且菜单类型为 M(目录)
        // 满足上面条件，path前面才加 /
        if (menu.getParentId().intValue() == 0 && Constants.TYPE_DIR.equals(menu.getMenuType())) {
            path = "/" + menu.getPath();
        }

        return path;
    }

    /**
     * 获取路由名称
     *
     * @param menu
     * @return: java.lang.String
     */
    private String getRouterName(SysMenu menu) {

        String routerPath = menu.getPath();
        String routerName = StringUtils.capitalize(routerPath);
        return routerName;
    }

    public List<SysMenu> getChildPerms(List<SysMenu> menus) {
        List<SysMenu> returnList = new ArrayList<>();

        //获取所有菜单id
        List<Long> menuIds = menus.stream().map(SysMenu::getMenuId).collect(Collectors.toList());

        menus.forEach(menu -> {
            //如果是顶级节点,遍历该父节点下的所有子节点
            if (!menuIds.contains(menu.getParentId())) {
                recursionFn(menus, menu);
                returnList.add(menu);
            }
        });

        // 下面是方法二
        //获取所有菜单id
//        List<Long> menuIdss = new ArrayList<>();
//        for (SysMenu sysMenu : menus) {
//            menuIdss.add(sysMenu.getMenuId());
//        }
//
//        menus.stream()
//                .filter(menu -> !menuIdss.contains(menu.getParentId()))
//                .forEach(menu -> {
//                    //递归获取子节点
//                    recursionFn(menus, menu);
//                    returnList.add(menu);
//                });


        return returnList;
    }

    /**
     * 根据父节点ID 获取子节点
     *
     * @param menus    菜单列表
     * @param parentId 父菜单Id
     * @return: 有父子结构的菜单集合
     */
    public List<SysMenu> getChildPerms(List<SysMenu> menus, int parentId) {

        List<SysMenu> returnList = new ArrayList<>();
        menus.stream()
                .filter(m -> m.getParentId() == parentId)
                .forEach(m -> {
                    recursionFn(menus, m);
                    returnList.add(m);
                });

        return returnList;
    }

    /**
     * 递归获取子菜单
     *
     * @param menus
     * @param m
     */
    private void recursionFn(List<SysMenu> menus, SysMenu m) {
        //1.获取子节点列表
        List<SysMenu> childMenu = getChildList(menus, m);
        m.setChildren(childMenu);
        for (SysMenu subMenu : childMenu) {
            //判断  子节点下面还有子节点
            if (getChildList(menus, subMenu).size() > 0 ? true : false) {
                recursionFn(menus, subMenu);
            }
        }
    }

    /**
     * 得到子节点列表
     *
     * @param menus
     * @param m
     * @return: java.util.List<com.msb.hjycommunity.system.domain.SysMenu>
     */
    private List<SysMenu> getChildList(List<SysMenu> menus, SysMenu m) {
        return menus.stream()
                .filter(sub -> sub.getParentId().longValue() == m.getMenuId().longValue())
                .collect(Collectors.toList());
    }

    private List<SysMenu> obtainMenuAll() {
        List<SysMenu> sysMenus = obtainMenuByUserId(null);
        SysMenu sysMenuRole = new SysMenu();
        sysMenuRole.setMenuId(101L);
        sysMenuRole.setParentId(1L);
        sysMenuRole.setMenuName("角色管理");
        sysMenuRole.setPath("role");
        sysMenuRole.setComponent("system/role/index");
        sysMenuRole.setMenuType(Constants.TYPE_MENU);
        sysMenus.add(sysMenuRole);

        SysMenu sysMenuM = new SysMenu();
        sysMenuM.setMenuId(102L);
        sysMenuM.setParentId(1L);
        sysMenuM.setMenuName("菜单管理");
        sysMenuM.setPath("menu");
        sysMenuM.setComponent("system/menu/index");
        sysMenuM.setMenuType(Constants.TYPE_MENU);
        sysMenus.add(sysMenuM);

        SysMenu sysMenuData = new SysMenu();
        sysMenuData.setMenuId(111L);
        sysMenuData.setParentId(2L);
        sysMenuData.setMenuName("数据监控");
        sysMenuData.setPath("druid");
        sysMenuData.setComponent("monitor/druid/index");
        sysMenuData.setMenuType(Constants.TYPE_MENU);
        sysMenus.add(sysMenuData);

        SysMenu sysMenuS = new SysMenu();
        sysMenuS.setMenuId(112L);
        sysMenuS.setParentId(2L);
        sysMenuS.setMenuName("服务监控");
        sysMenuS.setPath("server");
        sysMenuS.setComponent("monitor/server/index");
        sysMenuS.setMenuType(Constants.TYPE_MENU);
        sysMenus.add(sysMenuS);
        return sysMenus;
    }

    private List<SysMenu> obtainMenuByUserId(Long userId) {
        List<SysMenu> sysMenus = new ArrayList<>();
        SysMenu sysMenu = new SysMenu();
        sysMenu.setMenuId(1L);
        sysMenu.setParentId(0L);
        sysMenu.setMenuName("系统管理");
        sysMenu.setPath("system");
        sysMenu.setMenuType(Constants.TYPE_DIR);
        sysMenus.add(sysMenu);

        SysMenu sysMenuUser = new SysMenu();
        sysMenuUser.setMenuId(100L);
        sysMenuUser.setParentId(1L);
        sysMenuUser.setMenuName("用户管理");
        sysMenuUser.setPath("user");
        sysMenuUser.setComponent("system/user/index");
        sysMenuUser.setMenuType(Constants.TYPE_MENU);
        sysMenus.add(sysMenuUser);

        SysMenu sysMenuLog = new SysMenu();
        sysMenuLog.setMenuId(108L);
        sysMenuLog.setParentId(1L);
        sysMenuLog.setMenuName("日志管理");
        sysMenuLog.setPath("log");
        sysMenuLog.setMenuType(Constants.TYPE_DIR);
        sysMenus.add(sysMenuLog);

        SysMenu sysMenuOperlog = new SysMenu();
        sysMenuOperlog.setMenuId(500L);
        sysMenuOperlog.setParentId(108L);
        sysMenuOperlog.setMenuName("操作日志");
        sysMenuOperlog.setPath("operlog");
        sysMenuOperlog.setComponent("monitor/operlog/index");
        sysMenuOperlog.setMenuType(Constants.TYPE_MENU);
        sysMenus.add(sysMenuOperlog);

        SysMenu sysMenuLogininfor = new SysMenu();
        sysMenuLogininfor.setMenuId(501L);
        sysMenuLogininfor.setParentId(108L);
        sysMenuLogininfor.setMenuName("登录日志");
        sysMenuLogininfor.setPath("logininfor");
        sysMenuLogininfor.setComponent("monitor/logininfor/index");
        sysMenuLogininfor.setMenuType(Constants.TYPE_MENU);
        sysMenus.add(sysMenuLogininfor);

        SysMenu sysMenuMonitor = new SysMenu();
        sysMenuMonitor.setMenuId(2L);
        sysMenuMonitor.setParentId(0L);
        sysMenuMonitor.setMenuName("系统监控");
        sysMenuMonitor.setPath("monitor");
        sysMenuMonitor.setMenuType(Constants.TYPE_DIR);
        sysMenus.add(sysMenuMonitor);

        SysMenu sysMenuO = new SysMenu();
        sysMenuO.setMenuId(109L);
        sysMenuO.setParentId(2L);
        sysMenuO.setMenuName("在线用户");
        sysMenuO.setPath("online");
        sysMenuO.setComponent("monitor/online/index");
        sysMenuO.setMenuType(Constants.TYPE_MENU);
        sysMenus.add(sysMenuO);

        return sysMenus;
    }

}
