package com.iamyanbing.excel;

import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.afterturn.easypoi.excel.annotation.ExcelTarget;
import lombok.Data;

import java.io.Serializable;

@ExcelTarget("repository")
@Data
public class Repository implements Serializable {
    //@Excel(name="身份证号",orderNum = "6")
    // 可以通过 orderNum 自定顺序
    @Excel(name = "仓库id")
    private Long id;
    @Excel(name = "仓库名称", width = 15.0)
    private String name;
}
