package com.iamyanbing.res;


import com.iamyanbing.entity.SysMenu;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 构建树形结构实体类
 * <p>
 * 与 RouterVO 区别：
 * 1：封装属性更精简，即响应的字段更少，这里只响应 id、name
 * 2：更通用。比如：部门信息、菜单信息都可以通过本对象响应给前端
 **/
@Data
public class TreeSelectVO {

    //节点id
    private Long id;

    //节点名称
    private String label;

    //子节点
    private List<TreeSelectVO> children;

    public TreeSelectVO() {
    }

    //构造方法 接收菜单对象,构建 TreeSelect
    public TreeSelectVO(SysMenu menu) {
        this.id = menu.getMenuId();
        this.label = menu.getMenuName();
        //流式处理: 通过 map 方法将菜单列表中的每个菜单转换为 TreeSelectVO 对象,并将其赋值给当前 TreeSelectVO 对象的 children 属性
        this.children = menu.getChildren().stream().map(TreeSelectVO::new).collect(Collectors.toList());
    }
}
