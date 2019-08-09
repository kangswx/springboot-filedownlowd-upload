package com.kangswx.springbootfileupload.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.List;

@Controller
@RequestMapping("/api/v1/file")
public class FileController {

    private static final Logger logger = LoggerFactory.getLogger(FileController.class);

    private static final String filePath = "E:/project/springboot-filedownlowd-upload/src/main/resources/static/file/";

    /**
     * 单个文件上传页面跳转
     * @return
     */
    @GetMapping("upload")
    public String upload(){
        logger.info("====upload=====");
        return "upload";
    }

    /**
     * 上传单个文件
     * @param file
     * @param redirectAttributes
     * @return
     */
    @PostMapping("upload")
    public String upload(@RequestParam(value = "file") MultipartFile file, RedirectAttributes redirectAttributes){
        if(file.isEmpty()){
            return "文件上传失败，请重新选择文件";
        }

        String fileName = file.getOriginalFilename();
        File dest = new File(filePath + fileName);

        try {
            file.transferTo(dest);
            logger.info("文件上传成功");
            redirectAttributes.addAttribute("message","文件上传成功");
            return "redirect:/api/v1/file/upload";
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "文件上传失败";
    }

    /**
     * 多个文件上传页面跳转
     * @return
     */
    @GetMapping("multiupload")
    public String multi(){
        return "multiupload";
    }

    /**
     * 多个文件上传
     * @param request
     * @return
     */
    @PostMapping("multiupload")
    public String multi(HttpServletRequest request){
        List<MultipartFile> files = ((MultipartHttpServletRequest) request).getFiles("file");

        for (int i = 0; i < files.size(); i++) {
            MultipartFile file = files.get(i);

            if (file.isEmpty()) {
                return "上传第" + (i++) + "个文件失败";
            }
            String fileName = file.getOriginalFilename();
            File dest = new File(filePath + fileName);
            try {
                file.transferTo(dest);
                logger.info("第" + (i + 1) + "个文件上传成功");
            } catch (IOException e) {
                logger.error(e.toString(), e);
            }
        }
        return "multiupload";
    }

    /**
     * 文件下载页面跳转
     * @return
     */
    @GetMapping("download")
    public String download(){
        return "download";
    }

    /**
     * 文件下载
     * @param fileName
     * @param request
     * @param response
     * @return
     */
    @PostMapping("download")
    public String download(String fileName, HttpServletRequest request, HttpServletResponse response){
        response.setContentType("text/html;charset=utf-8");

        try {
            request.setCharacterEncoding("UTF-8");
        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
        }

        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;

        String downLoadPath = filePath + File.separator + fileName;  //注意不同系统的分隔符

        try {
            long fileLength = new File(downLoadPath).length();
            response.setContentType("application/x-msdownload;");
            response.setHeader("Content-disposition", "attachment; filename=" + new String(fileName.getBytes("utf-8"), "ISO8859-1"));
            response.setHeader("Content-Length", String.valueOf(fileLength));
            bis = new BufferedInputStream(new FileInputStream(downLoadPath));
            bos = new BufferedOutputStream(response.getOutputStream());
            byte[] buff = new byte[2048];
            int bytesRead;
            while (-1 != (bytesRead = bis.read(buff, 0, buff.length))) {
                bos.write(buff, 0, bytesRead);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (bis != null)
                try {
                    bis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            if (bos != null)
                try {
                    bos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }
        return null;
    }

}
