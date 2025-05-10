package com.wzh.front.service.impl;

import com.wzh.common.constant.CacheConstants;
import com.wzh.common.core.domain.AjaxResult;
import com.wzh.common.core.redis.RedisCache;
import com.wzh.common.utils.StringUtils;
import com.wzh.front.service.SharedService;
import com.wzh.manager.domain.MFileInfo;
import com.wzh.manager.domain.MShared;
import com.wzh.manager.domain.MSharedCode;
import com.wzh.manager.domain.MarkdownInfo;
import com.wzh.manager.mapper.MFileInfoMapper;
import com.wzh.manager.mapper.MSharedCodeMapper;
import com.wzh.manager.mapper.MSharedMapper;
import com.wzh.manager.mapper.MarkdownInfoMapper;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class SharedServiceImpl implements SharedService {

    @Autowired
    private MSharedMapper mSharedMapper;

    @Autowired
    private MSharedCodeMapper mSharedCodeMapper;

    @Autowired
    private MFileInfoMapper mFileInfoMapper;

    @Autowired
    private MarkdownInfoMapper markdownInfoMapper;

    @Autowired
    private RedisCache redisCache;

    @Override
    public List<Object> queryBySharedCode(String code) {
        List<Object> res = new ArrayList<>();
        List<MShared> list = mSharedMapper.selectMSharedListByCode(code);
        for (MShared mShared : list){
            if (mShared.getType().equals("file")){
                MFileInfo mFileInfo = mFileInfoMapper.selectMFileInfoById(Long.valueOf(mShared.getFileId()));
                res.add(mFileInfo);
            }else {
                MarkdownInfo markdownInfo = markdownInfoMapper.selectMarkdownInfoById(Long.valueOf(mShared.getFileId()));
                res.add(markdownInfo);
            }
        }
        return res;
    }

    @Override
    public MFileInfo queryFileInfoByCode(Long fileId) {
        return mFileInfoMapper.selectMFileInfoById(fileId);
    }

    @Override
    public MarkdownInfo queryMarkdownInfoByCode(Long fileId) {
        return markdownInfoMapper.selectMarkdownInfoById(fileId);
    }

    @Override
    public boolean queryCodeIsExist(String code) {
        MSharedCode mSharedCode = new MSharedCode();
        mSharedCode.setCode(code);
        List<String> codelist = redisCache.getCacheList(CacheConstants.TEMP_FILE_CACHE);
        if (codelist.contains(code)){
            return true;
        }else {
            List<MSharedCode> list = mSharedCodeMapper.selectMSharedCodeList(mSharedCode);
            return !StringUtils.isEmpty(list);
        }
    }

    @Override
    public List<MFileInfo> queryFileInfoByName(String code,String fileName) {
        return mFileInfoMapper.selectMFileInfoListByCode(code,  fileName);
    }

    @Override
    public List<MarkdownInfo> queryMarkdownInfoByName(String code,String fileName) {
        return markdownInfoMapper.selectMFileInfoListByCode(code,  fileName);
    }
}
