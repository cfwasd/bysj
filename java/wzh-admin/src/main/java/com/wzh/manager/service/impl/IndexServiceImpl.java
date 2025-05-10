package com.wzh.manager.service.impl;

import com.wzh.common.core.domain.AjaxResult;
import com.wzh.manager.mapper.IndexMapper;
import com.wzh.manager.service.IndexService;
import com.wzh.manager.utils.FileTypeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class IndexServiceImpl implements IndexService {

    @Autowired
    private IndexMapper indexMapper;

    public AjaxResult getTotal(){
        Map<String,Object> res = new HashMap<>();
        Long num = indexMapper.getTotal().get("count");
        res.put("fileTotal", num);
        res.put("markdownTotal", indexMapper.getTotal1().get("count"));
        res.put("resentUpload", indexMapper.resentUpload());
        res.put("word",indexMapper.getHeaderTotal(FileTypeUtils.word.toArray(new String[0])));
        res.put("excel",indexMapper.getHeaderTotal(FileTypeUtils.excel.toArray(new String[0])));
        res.put("powerpoint",indexMapper.getHeaderTotal(FileTypeUtils.powerpoint.toArray(new String[0])));
        res.put("video",indexMapper.getHeaderTotal(FileTypeUtils.video.toArray(new String[0])));
        res.put("pic",indexMapper.getHeaderTotal(FileTypeUtils.pic.toArray(new String[0])));
        res.put("pdf",indexMapper.getHeaderTotal(FileTypeUtils.pdf.toArray(new String[0])));
        res.put("other",indexMapper.getHeaderTotal(new String[]{"txt","html","md","zip","rar","7z","jar","apk","ipa","doc","docx","xls","xlsx","ppt","pptx"}));
        return AjaxResult.success(res);
    }
}
