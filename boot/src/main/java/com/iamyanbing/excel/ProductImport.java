package com.iamyanbing.excel;

import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.afterturn.easypoi.excel.annotation.ExcelEntity;
import cn.afterturn.easypoi.excel.annotation.ExcelIgnore;
import cn.afterturn.easypoi.excel.annotation.ExcelTarget;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@ExcelTarget("ProductImport")
@Data
public class ProductImport implements Serializable {

    @ExcelIgnore
    @Excel(name = "商品编号")
    private Long id;

    @Excel(name = "商品名称")
    private String name;

    @Excel(name = "价格")
    private double price;

    @Excel(name = "生产日期", width = 25.0, format = "yyyy-MM-dd HH:mm:ss")
    private Date productionDate;

    @Excel(name = "保修天数")
    private Long warrantyDate;

    /**
     * 注意： _ 必须要有，不然会报错
     * replace : 只能替换整体，不能替换部分
     */
    @Excel(name = "商品简介", replace = {"智能手机 Apple_智能手机"})
    private String brief;

    /**
     * 注意： _ 必须要有，不然会报错
     * 是否有库存
     * 1:有；0：无
     */
    @Excel(name = "是否有库存", replace = {"有_1","无_0"})
    private Integer stock;

    /**
     * 往往有时候导出的对象中含有数组或者集合,需要导出这样的数据可以直接使用@Excel进行导出
     * 这里为什么是@Excel，而不是@ExcelCollection？  运行之后看效果，才知道答案
     */
    @Excel(name = "标签(集合默认导出格式)", width = 25.0)
    private String labelList;

    /**
     * 改换数组或者集合 导出格式
     */
    @Excel(name = "标签(集合自定义导出格式)", width = 25.0)
    private String labels;

    /**
     * 仓库信息
     * 演示：导出对象中含有对象的Excel
     */
    //定义对象
    @ExcelEntity(name = "repository")
    private Repository repository;

}
