package com.wzh.manager.service.impl;

import java.security.MessageDigest;
import java.util.List;

import com.wzh.common.annotation.ServiceExceptionCatch;
import com.wzh.common.constant.CacheConstants;
import com.wzh.common.core.redis.RedisCache;
import com.wzh.common.exception.ServiceException;
import com.wzh.common.utils.DateUtils;
import com.wzh.common.utils.StringUtils;
import org.apache.commons.codec.binary.Hex;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.wzh.manager.mapper.MSharedCodeMapper;
import com.wzh.manager.domain.MSharedCode;
import com.wzh.manager.service.IMSharedCodeService;

import javax.annotation.PostConstruct;

/**
 * 分享码管理Service业务层处理
 * 
 * @author wzh
 * @date 2025-05-02
 */
@Service
@ServiceExceptionCatch
public class MSharedCodeServiceImpl implements IMSharedCodeService 
{
    @Autowired
    private MSharedCodeMapper mSharedCodeMapper;

    @Autowired
    private RedisCache redisCache;



    /**
     * 查询分享码管理
     * 
     * @param id 分享码管理主键
     * @return 分享码管理
     */
    @Override
    public MSharedCode selectMSharedCodeById(Long id)
    {
        return mSharedCodeMapper.selectMSharedCodeById(id);
    }

    /**
     * 查询分享码管理列表
     * 
     * @param mSharedCode 分享码管理
     * @return 分享码管理
     */
    @Override
    public List<MSharedCode> selectMSharedCodeList(MSharedCode mSharedCode)
    {
        return mSharedCodeMapper.selectMSharedCodeList(mSharedCode);
    }

    /**
     * 新增分享码管理
     * 
     * @param mSharedCode 分享码管理
     * @return 结果
     */
    @Override
    public int insertMSharedCode(MSharedCode mSharedCode)
    {
        mSharedCode.setCreateTime(DateUtils.getNowDate());
        mSharedCode.setStatus(0L);
        String code = createSharedCode();
        List<String> list = redisCache.getCacheList(CacheConstants.TEMP_FILE_CACHE);
        list.add(code);
        redisCache.setCacheList(CacheConstants.TEMP_FILE_CACHE,list);
        mSharedCode.setCode(code);
        return mSharedCodeMapper.insertMSharedCode(mSharedCode);
    }


    private String createSharedCode(){
        String code = "";
        try {
            MessageDigest  md5 = MessageDigest.getInstance("MD5");
            byte[] array = md5.digest(DateUtils.getNowDate().toString().getBytes("UTF-8"));
            String md5Hax = Hex.encodeHexString(array);
            code = md5Hax.substring(0, 8);
        }catch (Exception e){
            throw new ServiceException("分享码生成失败");
        }
        if (StringUtils.isEmpty(code)){
            throw new ServiceException("分享码生成失败");
        }
        return code;
    }

    /**
     * 修改分享码管理
     * 
     * @param mSharedCode 分享码管理
     * @return 结果
     */
    @Override
    public int updateMSharedCode(MSharedCode mSharedCode)
    {
        return mSharedCodeMapper.updateMSharedCode(mSharedCode);
    }

    /**
     * 批量删除分享码管理
     * 
     * @param ids 需要删除的分享码管理主键
     * @return 结果
     */
    @Override
    public int deleteMSharedCodeByIds(Long[] ids)
    {
        return mSharedCodeMapper.deleteMSharedCodeByIds(ids);
    }

    /**
     * 删除分享码管理信息
     * 
     * @param id 分享码管理主键
     * @return 结果
     */
    @Override
    public int deleteMSharedCodeById(Long id)
    {
        return mSharedCodeMapper.deleteMSharedCodeById(id);
    }
}
