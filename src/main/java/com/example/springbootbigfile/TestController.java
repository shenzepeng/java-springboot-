package com.example.springbootbigfile;

import com.example.springbootbigfile.utils.ConverterUtils;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Controller;
import org.springframework.util.NumberUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Auther:沈泽鹏
 * @Date: 2019/3/28 23:36
 * @Description: 沈泽鹏写点注释吧
 */
@Controller
public class TestController {

    /**
     * 单个文件同步接口
     *
     * @param request
     * @return
     * @throws JsonGenerationException
     * @throws JsonMappingException
     * @throws IOException
     */
    @SuppressWarnings("resource")
    @RequestMapping(value = "/test-sync/sync", produces = "text/plain;charset=UTF-8")
    @ResponseBody
    public String sync(
            @RequestParam(value = "file", required = false) MultipartFile file,
            HttpServletRequest request, HttpServletResponse response)
            throws JsonGenerationException, JsonMappingException, IOException {


        if (request.getParameter("chunk") == null) {

            String realPath = request.getSession().getServletContext()
                    .getRealPath("/Upload/");
            String fileName = file.getOriginalFilename();

            File targetFile = new File(realPath, fileName);
            if (!targetFile.exists()) {
                targetFile.mkdirs();
            }
            file.transferTo(targetFile); // 小文件，直接拷贝

            return "";
        } else {
            int chunk = Integer.parseInt(request.getParameter("chunk")); // 当前分片
            int chunks = Integer.parseInt(request.getParameter("chunks")); // 分片总计

            String realPath = request.getSession().getServletContext()
                    .getRealPath("/Upload/");

            String Ogfilename = file.getOriginalFilename();

            File tempFile = new File(realPath, Ogfilename);
            OutputStream outputStream = new FileOutputStream(tempFile, true);
            InputStream inputStream = file.getInputStream();

            byte buffer[] = new byte[1024];
            int len = 0;
            while ((len = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, len);
            }
            inputStream.close();
            outputStream.close();

            return "";
        }

    }
}
