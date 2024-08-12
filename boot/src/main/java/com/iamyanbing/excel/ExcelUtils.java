package com.iamyanbing.excel;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.enmus.ExcelType;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;

public class ExcelUtils {
    private static final Logger log = LoggerFactory.getLogger(ExcelUtils.class);

    /**
     * excel 下载
     *
     * @param list         数据列表
     * @param pojoClass    pojo类型
     * @param fileName     导出时的excel名称
     * @param response
     * @param exportParams 出参数（标题、sheet名称、是否创建表头，表格类型）
     */
    public static void exportExcel(List<?> list,
                                   Class<?> pojoClass,
                                   String fileName,
                                   HttpServletResponse response,
                                   ExportParams exportParams) {
        Workbook workbook = ExcelExportUtil.exportExcel(exportParams, pojoClass, list);

        ServletOutputStream outputStream = null;

        try {
            //下载相关内容见 UploadDownloadController 类
            response.setCharacterEncoding("UTF-8");
            response.setHeader("content-disposition", "attachment;fileName=" + URLEncoder.encode(fileName, "UTF-8"));

            outputStream = response.getOutputStream();
            workbook.write(outputStream);
        } catch (Exception e) {
            log.error("导出Excel异常：{}", e.getMessage(), e);
        } finally {
            try {
                outputStream.close();
                workbook.close();
            } catch (IOException e) {
                log.error("导出Excel异常. 关闭流时报错：{}", e.getMessage(), e);
            }
        }
    }

    /**
     * excel 保存在本地
     *
     * @param list         数据列表
     * @param pojoClass    pojo类型
     * @param fileName     导出时的excel名称
     * @param exportParams 出参数（标题、sheet名称、是否创建表头，表格类型）
     */
    public static void saveLocalExcel(List<?> list,
                                      Class<?> pojoClass,
                                      String fileName,
                                      ExportParams exportParams) {
        Workbook workbook = ExcelExportUtil.exportExcel(exportParams, pojoClass, list);

        FileOutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream("E:\\" + fileName);
            workbook.write(outputStream);
        } catch (Exception e) {
            log.error("保存Excel异常：{}", e.getMessage());
        } finally {
            try {
                outputStream.close();
                workbook.close();
            } catch (IOException e) {
                log.error("保存Excel异常. 关闭流时报错：{}", e.getMessage());
            }
        }
    }


    /**
     * @param sheetList
     * @param fileName
     */
    public static void saveLocalMultiSheetExcel(List<Map<String, Object>> sheetList,
                                                String fileName) {
        Workbook workbook = ExcelExportUtil.exportExcel(sheetList, ExcelType.HSSF);

        FileOutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream("E:\\" + fileName);
            workbook.write(outputStream);
        } catch (Exception e) {
            log.error("保存Excel异常：{}", e.getMessage());
        } finally {
            try {
                outputStream.close();
                workbook.close();
            } catch (IOException e) {
                log.error("保存Excel异常. 关闭流时报错：{}", e.getMessage());
            }
        }
    }


}
