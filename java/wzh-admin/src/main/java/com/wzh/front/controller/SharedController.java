package com.wzh.front.controller;

import com.wzh.common.core.controller.BaseController;
import com.wzh.common.core.domain.AjaxResult;
import com.wzh.common.core.page.TableDataInfo;
import com.wzh.common.core.redis.RedisCache;
import com.wzh.common.exception.ServiceException;
import com.wzh.common.utils.StringUtils;
import com.wzh.common.utils.file.MinioUtil;
import com.wzh.front.scheduling.CodeHandel;
import com.wzh.front.service.SharedService;
import com.wzh.manager.domain.MShared;
import com.wzh.manager.domain.MSharedCode;
import com.wzh.manager.service.IMUploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;

/**
 * @author wzh
 * 前台分享功能
 * @date 2020/1/14 - 14:01
 */
@RestController
@RequestMapping("/shared")
public class SharedController extends BaseController {

    @Autowired
    private SharedService sharedService;

    @Autowired
    private CodeHandel codeHandel;

    @Autowired
    private IMUploadService iMUploadService;

    @Autowired
    private RedisCache redisCache;

    @Value("${minio.bucketName}")
    private String bucketName;

    @GetMapping("/login")
    public AjaxResult login(@RequestParam("code") String code, HttpServletRequest request){
        codeHandel.codeHandel();

        boolean mSharedCode = sharedService.queryCodeIsExist(code);
        if (mSharedCode){
            return AjaxResult.success();
        }else {
            checkHeaders(request);
            return AjaxResult.warn("文件码不存在");
        }
    }

    @GetMapping("/code")
    public TableDataInfo getCode(HttpServletRequest request){
        startPage();
        checkHeaders(request);
        String code = request.getHeader("code");
        return getDataTable(sharedService.queryBySharedCode(code));
    }

    @GetMapping("/file")
    public AjaxResult getFile(@RequestParam("fileId") Long fileId,
                              @RequestParam("type") String type,
                                HttpServletRequest request){
        checkHeaders(request);
        if ("file".equals(type)){
            return AjaxResult.success(sharedService.queryFileInfoByCode(fileId));
        }else if("md".equals(type)){
            return AjaxResult.success(sharedService.queryMarkdownInfoByCode(fileId));
        }else {
            throw new ServiceException("文件不存在");
        }
    }

    @GetMapping("/queryfile")
    public TableDataInfo queryfile(@RequestParam("fileName") String  fileName,
                              @RequestParam("type") String type,
                              HttpServletRequest request){
        startPage();
        checkHeaders(request);
        String code = request.getHeader("code");
        if ("file".equals(type)){
            return getDataTable(sharedService.queryFileInfoByName(code,fileName));
        }else if("md".equals(type)){
            return getDataTable(sharedService.queryMarkdownInfoByName(code,fileName));
        }else {
            throw new ServiceException("文件不存在");
        }
    }



    @GetMapping("/preview")
    public String preview(@RequestParam("fileId")Integer fileId,
                          HttpServletRequest request) {
        checkHeaders(request);
        return iMUploadService.preview(fileId);
    }

    @GetMapping("/download")
    public ResponseEntity<Resource> download(@RequestParam("fileId")Integer fileId,HttpServletRequest request) throws IOException {
        checkHeaders(request);
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

    private void checkHeaders(HttpServletRequest request){
        boolean isExist = redisCache.hasKey("error");
        if (isExist){
            Integer num  = redisCache.getCacheObject("error");
            if (num >= 5){
                throw new ServiceException("错误次数过多，请五分钟后重试");
            }
        }
        String code = "";
        try {
            code = request.getHeader("code");
        }catch (Exception e){
            if (!isExist){
                redisCache.setCacheObject("error",1, 10,TimeUnit.MINUTES);
            }else {
                Integer num = redisCache.getCacheObject("error");
                redisCache.setCacheObject("error",num+1);
            }
            throw new ServiceException("权限不足");
        }
        if (StringUtils.isEmpty(code)){
            if (!isExist){
                redisCache.setCacheObject("error",1, 10,TimeUnit.MINUTES);
            }else {
                Integer num = redisCache.getCacheObject("error");
                redisCache.setCacheObject("error",num+1);
            }
            throw new ServiceException("权限不足");
        }
        boolean mSharedCode = sharedService.queryCodeIsExist(code);
        if (!mSharedCode){
            if (!isExist){
                redisCache.setCacheObject("error",1, 10,TimeUnit.MINUTES);
            }else {
                Integer num = redisCache.getCacheObject("error");
                redisCache.setCacheObject("error",num+1);
            }
            throw new ServiceException("文件码不存在");
        }
    }
}
