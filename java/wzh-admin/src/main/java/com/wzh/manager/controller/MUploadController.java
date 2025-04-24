package com.wzh.manager.controller;


import com.wzh.common.core.controller.BaseController;
import com.wzh.common.core.domain.AjaxResult;
import com.wzh.manager.service.IMUploadService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;

@RestController
@RequestMapping("/manager/file")
public class MUploadController extends BaseController {

    @Resource
    private IMUploadService imUploadService;


    @PostMapping("/uploadMinio")
    public AjaxResult uploadFileMinio(@RequestParam("file") MultipartFile[] files, @RequestParam("folderId") Integer id)  {
        return imUploadService.upload(id, files);
    }
}
