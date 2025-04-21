package com.wzh.manager.service.impl;

import com.wzh.common.core.domain.AjaxResult;
import com.wzh.common.exception.FileOperationException;
import com.wzh.common.utils.file.FileUploadUtils;
import com.wzh.common.utils.file.FileUtils;
import com.wzh.common.utils.file.MinioUtil;
import com.wzh.common.utils.spring.SpringUtils;
import com.wzh.manager.domain.MFileInfo;
import com.wzh.manager.domain.MFolder;
import com.wzh.manager.service.IMFileInfoService;
import com.wzh.manager.service.IMFolderService;
import com.wzh.manager.service.IMUploadService;
import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class IMUploadServiceImpl implements IMUploadService {


    @Value("${minio.preview}")
    private String preUrl;

    @Value("${minio.url}")
    private String minio;

    @Value("${wzh.profile}")
    private String saveAddr;

    @Value("${minio.bucketName}")
    private String bucketName;

    @Resource
    private IMFileInfoService mFileInfoService;

    @Resource
    private IMFolderService imFolderService;

    @Override
    public AjaxResult upload(Integer id, MultipartFile[] files) {
        MFolder mFolder = imFolderService.selectMFolderById(Long.valueOf(id));
        if (mFolder == null){
            return AjaxResult.error("文件夹不存在");
        }
        List<AjaxResult> err = new ArrayList<>();
        for (MultipartFile file : files) {
            AjaxResult ajaxResult = uploadMinio(file);
            if (ajaxResult.isError()){
                err.add(ajaxResult);
                continue;
            }
            MFileInfo mFileInfo = (MFileInfo) ajaxResult.get("mf");
            mFileInfo.setFolderId(Long.valueOf(id));
            mFileInfo.setFolder(mFolder.getFloderName());


            SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
            String format=sdf.format(new Date());
            File folder=new File(saveAddr+format);
            if(!folder.isDirectory()){
                if(!folder.mkdirs()){
                    //TODO`
                }
            }
            String oldName=file.getOriginalFilename();
            String newName= mFileInfo.getUuid()+oldName.substring(oldName.lastIndexOf("."),oldName.length());
            try {
                File file1=new File(folder, newName);
                file.transferTo(file1);
                mFileInfo.setFileName(newName);
                mFileInfo.setFileRealFolder(saveAddr+format);
                mFileInfoService.insertMFileInfo(mFileInfo);
            } catch (Exception e) {
                System.out.println(e.getMessage());
                AjaxResult ajax =AjaxResult.error("文件:"+file.getOriginalFilename()+"保存到服务器本地失败");
                err.add(ajax);
            }
        }

        if (err.size()>0){
            List<String> list = new ArrayList<>();
            for (AjaxResult ajaxResult : err) {
                list.add((String) ajaxResult.get("msg"));
                return AjaxResult.error("文件上传失败",String.join(",",list));
            }
        }
        return AjaxResult.success("上传成功");
    }


    private AjaxResult uploadMinio(MultipartFile file){
        try
        {
            MFileInfo mFileInfo = new MFileInfo();

            String uuid = UUID.randomUUID().toString();
            String shortUuid = uuid.replace("-", "").substring(0, 16);

            mFileInfo.setUuid(shortUuid);
            mFileInfo.setFileRawName(file.getOriginalFilename());
            mFileInfo.setFileExtentions(FileUploadUtils.getExtension(file));

            upload(bucketName,shortUuid+"."+FileUploadUtils.getExtension(file),file.getInputStream());
            // 上传并返回新文件名称
            String fileNewName = MinioUtil.getPreviewUrl(bucketName, shortUuid+FileUploadUtils.getExtension(file),60*60);
            fileNewName = fileNewName.replace(minio+"/file/",preUrl);
            AjaxResult ajax = AjaxResult.success();
            mFileInfo.setOssUrl(fileNewName);
            ajax.put("mf",mFileInfo);
            return ajax;
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
            return AjaxResult.error("文件:"+file.getOriginalFilename()+"上传到云端失败");
        }
    }

    /**
     * 获取预览地址，过期时间默认一个小时
     * @param fileId
     * @return
     */
    public String preview(Integer fileId) {
        MFileInfo mFileInfo = mFileInfoService.selectMFileInfoById(Long.valueOf(fileId));
        if (mFileInfo == null){
            throw new FileOperationException("文件不存在");
        }
        String fileName = mFileInfo.getFileName();
        try {
            String url = MinioUtil.getPreviewUrl( bucketName, fileName,60 * 60);
            return url.replace("http://154.201.84.30:9000/file/",preUrl);
        } catch (IOException e) {
            throw new FileOperationException("获取文件预览地址失败");
        }
    }

    @Override
    public String download(Integer fileId) {
        MFileInfo mFileInfo = mFileInfoService.selectMFileInfoById(Long.valueOf(fileId));
        if (mFileInfo == null){
            throw new FileOperationException("文件不存在");
        }
        return mFileInfo.getFileName()+"split"+mFileInfo.getFileRawName();
    }

    /**
     * 上传文件到MinIO
     * @param bucketName 存储桶名称
     * @param fileName 文件名
     * @param inputStream 文件流
     */
    private  void upload(String bucketName, String fileName, InputStream inputStream) {
        MinioClient minioClient = SpringUtils.getBean(MinioClient.class);
        try {
            // 检查存储桶是否存在，不存在则创建
            boolean isExist = minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
            if (!isExist) {
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
            }

            // 上传文件
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(bucketName)
                            .object(fileName)
                            .stream(inputStream, inputStream.available(), -1)
                            .build());
        } catch (Exception e) {
            throw new RuntimeException("上传文件失败", e);
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
