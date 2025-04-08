package com.wzh.manager.controller;


import com.wzh.common.core.controller.BaseController;
import com.wzh.common.core.domain.AjaxResult;
import com.wzh.common.utils.file.FileUploadUtils;
import com.wzh.common.utils.file.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/manager/file")
public class MUploadController extends BaseController {

    @Value("${minio.preview}")
    private String preUrl;

    @Value("${minio.url}")
    private String minio;

    @PostMapping("/uploadMinio")
    public AjaxResult uploadFileMinio(@RequestParam("file") MultipartFile file) throws Exception
    {
        try
        {
            // 上传并返回新文件名称
            String fileName = FileUploadUtils.uploadMinio(file);
            AjaxResult ajax = AjaxResult.success();
            fileName = fileName.replace(minio+"/file/",preUrl);
            ajax.put("url", fileName);
            ajax.put("fileName", fileName);
            ajax.put("newFileName", FileUtils.getName(fileName));
            ajax.put("originalFilename", file.getOriginalFilename());
            return ajax;
        }
        catch (Exception e)
        {
            return AjaxResult.error(e.getMessage());
        }
    }
}
