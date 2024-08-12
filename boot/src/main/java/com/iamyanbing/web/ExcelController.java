package com.iamyanbing.web;

import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import com.iamyanbing.excel.ExcelUtils;
import com.iamyanbing.excel.Product;
import com.iamyanbing.excel.ProductImport;
import com.iamyanbing.excel.Repository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * @Author huangyanbing
 * @Date 2022/2/10 21:17
 * @description
 */
@RestController
@RequestMapping("/excel")
@Slf4j
public class ExcelController {

    @GetMapping("/export")
    public void export(HttpServletResponse response) {
        ExportParams exportParams = new ExportParams("商品信息列表", "商品信息");
        ExcelUtils.exportExcel(this.obtainProductInfos(),
                Product.class,
                "商品信息列表.xls",
                response,
                exportParams);
    }

    @GetMapping("/save")
    public String save() {
        ExportParams exportParams = new ExportParams("商品信息列表", "商品信息");
        ExcelUtils.saveLocalExcel(this.obtainProductInfos(),
                Product.class,
                "商品信息列表.xls",
                exportParams);
        return "success";
    }

    @PostMapping("/import")
    public String importExcel(MultipartFile excelFile) {
        ImportParams params = new ImportParams();
        params.setTitleRows(1); //表标题的行数
        params.setHeadRows(1); //表头行数
        params.setNeedSave(false);
//        params.setNeedSave(true);
//        params.setSaveUrl("I:\\msb_hejiayun\\easypoi_boot\\src\\main\\resources\\static");

        List<ProductImport> products = null;
        try {
            products = ExcelImportUtil.importExcel(excelFile.getInputStream(), ProductImport.class, params);
        } catch (Exception e) {
            log.error("解析excel异常", e);
        }
        for (ProductImport product :
                products) {
            log.info("{}", product);
        }
        return "success";
    }

    @PostMapping("/importMultiSheet")
    public String importMultiSheet(MultipartFile excelFile) {
        ImportParams params = new ImportParams();

        // 第几个sheet页
        params.setStartSheetIndex(1);
        params.setTitleRows(1);//表标题的行数
        params.setHeadRows(1);//表头行数

        //是否保存本次上传的excel
        params.setNeedSave(false);

        //表示表头必须包含的字段,不包含 就报错.
        params.setImportFields(new String[]{"商品名称"});

        List<ProductImport> products = null;

        try {
            products = ExcelImportUtil.importExcel(excelFile.getInputStream(), ProductImport.class, params);
        } catch (Exception e) {
            log.error("解析excel异常", e);
        }
        for (ProductImport product :
                products) {
            log.info("{}", product);
        }
        return "success";
    }

    @GetMapping("/saveLocalMultiSheet")
    public String saveLocalMultiSheet() {

        //第一个sheet
        //创建参数对象,用于设定Excel的sheet页内容等信息
        ExportParams productParams = new ExportParams();
        //设置sheet的名称
        productParams.setSheetName("商品信息");
        productParams.setTitle("商品信息列表");

        //使用map创建sheet1
        HashMap<String, Object> sheet1Map = new HashMap<>();
        //设置title
        sheet1Map.put("title", productParams);
        //设置导出的实体类型
        sheet1Map.put("entity", Product.class);
        //sheet中要填充的数据
        sheet1Map.put("data", this.obtainProductInfos());

        //第二个sheet
        //创建参数对象,用于设定Excel的sheet页内容等信息
        ExportParams productParams2 = new ExportParams();
        //设置sheet的名称
        productParams2.setSheetName("商品信息2");
        productParams2.setTitle("商品信息列表2");

        //使用map创建sheet2
        HashMap<String, Object> sheet2Map = new HashMap<>();
        //设置title
        sheet2Map.put("title", productParams2);
        //设置导出的实体类型
        sheet2Map.put("entity", Product.class);
        //sheet中要填充的数据
        sheet2Map.put("data", this.obtainProductInfos());

        //将sheet1和sheet2 进行包装
        List<Map<String, Object>> sheetList = new ArrayList<>();
        sheetList.add(sheet1Map);
        sheetList.add(sheet2Map);
        ExcelUtils.saveLocalMultiSheetExcel(sheetList, "商品信息列表MultiSheet.xls");
        return "success";
    }

    private List<Product> obtainProductInfos() {
        List<Product> products = new ArrayList<>();
        Product iphone = new Product();
        iphone.setId(1L);
        iphone.setName("iphone");
        iphone.setPrice(5000);
        iphone.setProductionDate(new Date());
        iphone.setWarrantyDate(180L);
        iphone.setBrief("好用的智能手机");
        iphone.setStock(1);
        List<String> labelList = new ArrayList<>();
        labelList.add("全球");
        labelList.add("顶级");
        labelList.add("牛逼");
        iphone.setLabelList(labelList);
        Repository repository = new Repository();
        repository.setId(1000L);
        repository.setName("上海1号仓库");
        iphone.setRepository(repository);
        Product ipad = new Product();
        ipad.setId(2L);
        ipad.setName("ipad");
        ipad.setPrice(8000);
        ipad.setProductionDate(new Date());
        ipad.setWarrantyDate(380L);
        ipad.setBrief("好用的平板电脑");
        ipad.setStock(0);
        List<String> labelListIpad = new ArrayList<>();
        labelListIpad.add("全球");
        labelListIpad.add("顶级");
        labelListIpad.add("牛逼");
        ipad.setLabelList(labelListIpad);
        Repository repositoryIpad = new Repository();
        repositoryIpad.setId(1001L);
        repositoryIpad.setName("上海2号仓库");
        ipad.setRepository(repositoryIpad);
        products.add(iphone);
        products.add(ipad);
        return products;
    }
}
