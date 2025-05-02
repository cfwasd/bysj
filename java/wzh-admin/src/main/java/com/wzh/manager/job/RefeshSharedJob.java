package com.wzh.manager.job;

import com.wzh.common.constant.CacheConstants;
import com.wzh.common.core.redis.RedisCache;
import com.wzh.common.utils.DateUtils;
import com.wzh.manager.domain.MSharedCode;
import com.wzh.manager.mapper.MSharedCodeMapper;
import com.wzh.manager.mapper.MSharedMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Component
@Slf4j
public class RefeshSharedJob {

    @Autowired
    private MSharedCodeMapper mSharedCodeMapper;

    @Autowired
    private MSharedMapper mSharedMapper;

    @Autowired
    private RedisCache redisCache;


    @PostConstruct
    public void  init(){
        List<String> list = mSharedCodeMapper.selectUnOutDate();
        redisCache.setCacheList(CacheConstants.TEMP_FILE_CACHE,list);
    }

    @Scheduled(cron = "0 0 0/1 * * ?")
    public void refresh(){
        List<MSharedCode> codeList = mSharedCodeMapper.selectMSharedCodeList(new MSharedCode());
        List<String> code = new ArrayList<>();
        for (MSharedCode mSharedCode : codeList) {
            Date date = new Date();
            long diffInMillis = Math.abs((date.getTime()+3600000)-mSharedCode.getCreateTime().getTime());
            long time = TimeUnit.MILLISECONDS.toHours(diffInMillis);
            if (time>72){
                code.add(mSharedCode.getCode());
            }
        }
        mSharedMapper.updateMSharedByIds(new Date(),code.toArray(new Long[code.size()]));
        mSharedCodeMapper.updateMSharedCodeByCode(code.toArray(new String[code.size()]));
        List<String> list = redisCache.getCacheList(CacheConstants.TEMP_FILE_CACHE);
        List<String> newList = new ArrayList<>();
        for (String s : list){
            if (!code.contains(s)){
                newList.add(s);
            }
        }
        redisCache.setCacheList(CacheConstants.TEMP_FILE_CACHE,newList);
        log.info("刷新共享码成功");
    }

}
