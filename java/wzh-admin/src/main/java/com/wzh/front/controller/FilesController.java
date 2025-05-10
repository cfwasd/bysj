package com.wzh.front.controller;

import com.wzh.common.core.controller.BaseController;
import com.wzh.common.utils.file.MinioUtil;
import com.wzh.manager.service.IMUploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@RestController
@RequestMapping("/files")
public class FilesController extends BaseController {

    @Autowired
    private IMUploadService iMUploadService;

    @Value("${minio.bucketName}")
    private String bucketName;

    @GetMapping("/preview")
    public String preview(@RequestParam("fileId")Integer fileId) {
        return iMUploadService.preview(fileId);
    }

    @GetMapping("/download")
    public ResponseEntity<Resource> download(@RequestParam("fileId")Integer fileId) throws IOException {
        String str = iMUploadService.download(fileId);
        String fileName = str.split("split")[0];
        String rawFileName = str.split("split")[1];
        InputStream inputStream = MinioUtil.downloadAsStream(bucketName, fileName);

        Resource resource = new InputStreamResource(inputStream);
        // 移除调试输出
        String contentType = "application/octet-stream";
        HttpHeaders headers = new HttpHeaders();
        // 修改为RFC 5987编码格式支持中文文件名
        String encodedFileName = URLEncoder.encode(rawFileName, StandardCharsets.UTF_8.toString())
                .replaceAll("\\+", "%20");
        headers.add(HttpHeaders.CONTENT_DISPOSITION, 
                "attachment; filename=\"" + encodedFileName + "\"; filename*=utf-8''" + encodedFileName);
        
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .headers(headers)
                .body(resource);
    }
}
