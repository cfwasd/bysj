package com.wzh.manager.service;

import com.wzh.common.core.domain.AjaxResult;
import org.springframework.web.multipart.MultipartFile;

public interface IMUploadService {
    public AjaxResult upload(Integer id, MultipartFile[] files);
}
