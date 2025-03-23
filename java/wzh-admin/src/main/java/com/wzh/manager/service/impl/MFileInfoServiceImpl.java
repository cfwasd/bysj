package com.wzh.manager.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.wzh.manager.mapper.MFileInfoMapper;
import com.wzh.manager.domain.MFileInfo;
import com.wzh.manager.service.IMFileInfoService;

/**
 * 文件信息Service业务层处理
 * 
 * @author wzh
 * @date 2025-03-23
 */
@Service
public class MFileInfoServiceImpl implements IMFileInfoService 
{
    @Autowired
    private MFileInfoMapper mFileInfoMapper;

    /**
     * 查询文件信息
     * 
     * @param id 文件信息主键
     * @return 文件信息
     */
    @Override
    public MFileInfo selectMFileInfoById(Long id)
    {
        return mFileInfoMapper.selectMFileInfoById(id);
    }

    /**
     * 查询文件信息列表
     * 
     * @param mFileInfo 文件信息
     * @return 文件信息
     */
    @Override
    public List<MFileInfo> selectMFileInfoList(MFileInfo mFileInfo)
    {
        return mFileInfoMapper.selectMFileInfoList(mFileInfo);
    }

    /**
     * 新增文件信息
     * 
     * @param mFileInfo 文件信息
     * @return 结果
     */
    @Override
    public int insertMFileInfo(MFileInfo mFileInfo)
    {
        return mFileInfoMapper.insertMFileInfo(mFileInfo);
    }

    /**
     * 修改文件信息
     * 
     * @param mFileInfo 文件信息
     * @return 结果
     */
    @Override
    public int updateMFileInfo(MFileInfo mFileInfo)
    {
        return mFileInfoMapper.updateMFileInfo(mFileInfo);
    }

    /**
     * 批量删除文件信息
     * 
     * @param ids 需要删除的文件信息主键
     * @return 结果
     */
    @Override
    public int deleteMFileInfoByIds(Long[] ids)
    {
        return mFileInfoMapper.deleteMFileInfoByIds(ids);
    }

    /**
     * 删除文件信息信息
     * 
     * @param id 文件信息主键
     * @return 结果
     */
    @Override
    public int deleteMFileInfoById(Long id)
    {
        return mFileInfoMapper.deleteMFileInfoById(id);
    }
}
