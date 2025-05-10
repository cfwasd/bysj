package com.wzh.front.scheduling;

import com.wzh.common.constant.CacheConstants;
import com.wzh.common.core.redis.RedisCache;
import com.wzh.manager.domain.MSharedCode;
import com.wzh.manager.mapper.MSharedCodeMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Slf4j
public class CodeHandel {
    @Autowired
    private MSharedCodeMapper mSharedCodeMapper;

    @Autowired
    private RedisCache redisCache;

    @PostConstruct
    public void init() {
        MSharedCode mSharedCode = new MSharedCode();
        mSharedCode.setStatus(0L);
        List<MSharedCode> codeList = mSharedCodeMapper.selectMSharedCodeList(mSharedCode);
        List<String> code = codeList.stream().map(MSharedCode::getCode).collect(Collectors.toList());
        List<String> newList = new ArrayList<>();
        for (String c : code){
            if (redisCache.hasKey(c)){
                newList.add(c);
            }
        }
        newList.add("temp");
        if (redisCache.hasKey(CacheConstants.TEMP_FILE_CACHE)){
            Collection keys = redisCache.keys(CacheConstants.TEMP_FILE_CACHE+"*");
            redisCache.deleteObject(keys);
        }
        redisCache.setCacheList(CacheConstants.TEMP_FILE_CACHE,newList);
        log.info("初始化共享码成功");
    }

    @Scheduled(	cron = "0 0 * * * *")
    public void codeHandel() {
        List<MSharedCode> codeList = mSharedCodeMapper.selectMSharedCodeList(new MSharedCode());
        List<MSharedCode> update = new ArrayList<>();
        List<String> newList = new ArrayList<>();
        for (MSharedCode mSharedCode : codeList){
           if (redisCache.hasKey(mSharedCode.getCode())){
               newList.add(mSharedCode.getCode());
           }else {
               mSharedCode.setStatus(1L);
               update.add(mSharedCode);
           }
        }
        for (MSharedCode mSharedCode : update){
            mSharedCodeMapper.updateMSharedCode(mSharedCode);
        }
        Collection keys = redisCache.keys(CacheConstants.TEMP_FILE_CACHE+"*");
        redisCache.deleteObject(keys);
        redisCache.setCacheList(CacheConstants.TEMP_FILE_CACHE,newList);
        log.info("刷新共享码成功");
    }
}
