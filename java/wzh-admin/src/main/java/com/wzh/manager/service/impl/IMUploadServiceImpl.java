package com.wzh.manager.service.impl;

import com.wzh.common.core.domain.AjaxResult;
import com.wzh.common.utils.file.FileUploadUtils;
import com.wzh.common.utils.file.FileUtils;
import com.wzh.manager.domain.MFileInfo;
import com.wzh.manager.domain.MFolder;
import com.wzh.manager.service.IMFileInfoService;
import com.wzh.manager.service.IMFolderService;
import com.wzh.manager.service.IMUploadService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
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

            // 上传并返回新文件名称
            String fileNewName = FileUploadUtils.uploadMinio(file);
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
}
