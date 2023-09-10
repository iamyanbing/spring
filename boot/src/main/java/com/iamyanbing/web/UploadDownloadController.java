package com.iamyanbing.web;

import com.iamyanbing.snowflake.UtilIdHub;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;

@RestController
@Repository
@RequestMapping("/uploadDownload")
public class UploadDownloadController {
    @Resource
    private UtilIdHub utilIdHub;
    private static final Logger LOGGER = LoggerFactory.getLogger(UploadDownloadController.class);


    /**
     * 缺点：一次性把所有文件加载到内存并保存
     *
     * @param file
     * @return
     */
    @ApiOperation("文件上传")
    @PostMapping(value = "/upload")
    @ResponseBody
    public String fieldUpload(@RequestParam("file") MultipartFile file) {
        //文件保存路径
        String fieldPath = "";
        if (file.isEmpty()) {
            return "上传文件为空";
        }
        if (ObjectUtils.isEmpty(fieldPath)) {
            return "文件地址为空";
        }
        LOGGER.info("存储文件的地址====》" + fieldPath);
        // 获取文件全名a.py
        String fileName = file.getOriginalFilename();
        // 获取文件的后缀名
        String suffixName = fileName.substring(fileName.lastIndexOf("."));
        //获取文件名
        String prefixName = fileName.substring(0, fileName.lastIndexOf("."));
        //变更后的名字
        String newFileName = prefixName + "_" + utilIdHub.nextId() + suffixName;
        //存放文件的文件路径
        String templatePath = fieldPath;
        File dest0 = new File(templatePath);
        File dest = new File(dest0, File.separator + newFileName);
        //文件上传-覆盖
        try {
            // 检测是否存在目录
            if (!dest0.getParentFile().exists()) {
                dest0.getParentFile().mkdirs();
                //检测文件是否存在
            }
            if (!dest.getParentFile().exists()) {
                dest.getParentFile().mkdirs();
            }
            file.transferTo(dest);
            return dest.getCanonicalPath();
        } catch (Exception e) {
            LOGGER.error("上传文件失败", e);
        }
        return null;
    }

    /**
     * 优点：一个文件切片下载，避免一次性把整个文件加载到内存，影响性能。
     *
     * 该接口就是一个可下载链接，可以直接提供给前端下载使用
     *
     * @param response
     */
    @ApiOperation(value = "下载附件", notes = "下载附件")
    @GetMapping(value = "/download")
    public void fileDownload(HttpServletResponse response) {
        String fieldPath = "";
        // path是指欲下载的文件的路径。
        File file = new File(fieldPath);
        // 取得文件名。
        String filename = file.getName();
        // 取得文件的后缀名。
        String suffix = filename.substring(filename.lastIndexOf("."));
        String originalFilename = filename.substring(0, filename.lastIndexOf("_"));
        String downloadFilename = originalFilename + suffix;

        try (FileInputStream is = new FileInputStream(fieldPath);
             InputStream fis = new BufferedInputStream(is);
             OutputStream toClient = new BufferedOutputStream(response.getOutputStream())) {

            // 清空response
            response.reset();
            // 设置response的Header
            response.setCharacterEncoding("UTF-8");
            response.addHeader("Content-Length", "" + file.length());
            //Content-Disposition的作用：告知浏览器以何种方式显示响应返回的文件，用浏览器打开还是以附件的形式下载到本地保存
            //attachment表示以附件方式下载;inline表示在线打开 "Content-Disposition: inline; filename=文件名.mp3"
            // filename表示文件的默认名称，因为网络传输只支持URL编码的相关支付，因此需要将文件名URL编码后进行传输,前端收到后需要反编码才能获取到真正的名称
            response.addHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(downloadFilename, "UTF-8"));
            response.setContentType("application/octet-stream");
            int len;
            byte[] buffer = new byte[1024];
            //从输入流中读取一定数量的字节，并将其存储在缓冲区字节数组中，读到末尾返回-1
            while ((len = fis.read(buffer)) > 0) {
                toClient.write(buffer, 0, len);
                toClient.flush();
            }
        } catch (IOException ex) {
            LOGGER.error("下载文件失败", ex);
        }
    }
}
