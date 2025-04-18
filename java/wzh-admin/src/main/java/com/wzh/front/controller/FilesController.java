package com.wzh.front.controller;

import com.wzh.common.core.controller.BaseController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/files")
public class FilesController extends BaseController {

    @GetMapping("/preview")
    public String preview(String fileName) {
        return "http://localhost:8080/files/preview/" + fileName;
    }

    @GetMapping("/download")
    public String preview(String fileName, String bucketName) {
        return "http://localhost:8080/files/preview/" + fileName;
    }
}
