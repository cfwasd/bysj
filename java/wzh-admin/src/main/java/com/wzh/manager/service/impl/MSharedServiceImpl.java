package com.wzh.manager.service.impl;

import java.util.ArrayList;
import java.util.List;

import com.wzh.common.annotation.ServiceExceptionCatch;
import com.wzh.common.core.domain.AjaxResult;
import com.wzh.common.exception.ServiceException;
import com.wzh.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.wzh.manager.mapper.MSharedMapper;
import com.wzh.manager.domain.MShared;
import com.wzh.manager.service.IMSharedService;

/**
 * 文件分享相关Service业务层处理
 *
 * @author wzh
 * @date 2025-05-02
 */
@Service
@ServiceExceptionCatch
public class MSharedServiceImpl implements IMSharedService
{
    @Autowired
    private MSharedMapper mSharedMapper;

    /**
     * 查询文件分享相关
     *
     * @param id 文件分享相关主键
     * @return 文件分享相关
     */
    @Override
    public MShared selectMSharedById(Long id)
    {
        return mSharedMapper.selectMSharedById(id);
    }

    /**
     * 查询文件分享相关列表
     *
     * @param mShared 文件分享相关
     * @return 文件分享相关
     */
    @Override
    public List<MShared> selectMSharedList(MShared mShared)
    {
        return mSharedMapper.selectMSharedList(mShared);
    }

    /**
     * 新增文件分享相关
     *
     * @param mShared 文件分享相关
     * @return 结果
     */
    @Override
    public AjaxResult insertMShared(MShared mShared)
    {
        mShared.setCreateTime(DateUtils.getNowDate());
        if (mShared.getFileId().contains(",")){
            List<Long> ids = new ArrayList<>();
            String[] split = mShared.getFileId().split(",");
            List<MShared> list = new ArrayList<>();
            for (String s : split) {
                ids.add(Long.valueOf(s));
                MShared m = new MShared();
                m.setSharedCode(mShared.getSharedCode());
                m.setFileId(s);
                m.setType(mShared.getType());
                m.setIsOutdate(mShared.getIsOutdate());
                m.setUpdateDate(DateUtils.getNowDate());
                m.setCreateTime(DateUtils.getNowDate());
                list.add(m);
            }
            List<MShared> checkIsExist = mSharedMapper.checkIsExist(mShared.getSharedCode(),ids.toArray(new Long[0]));
            if (checkIsExist != null && !checkIsExist.isEmpty()){
                throw new ServiceException("分享文件已经存在");
            }
            mSharedMapper.insertMSharedList(list);
            return AjaxResult.success("添加成功");
        }else {
            List<MShared> m = mSharedMapper.selectMSharedList(mShared);
            if (m != null && !m.isEmpty()){
                throw new ServiceException("分享文件已经存在");
            }
            mSharedMapper.insertMShared(mShared);
            return AjaxResult.success("添加成功");
        }
    }

    /**
     * 修改文件分享相关
     *
     * @param mShared 文件分享相关
     * @return 结果
     */
    @Override
    public int updateMShared(MShared mShared)
    {
        return mSharedMapper.updateMShared(mShared);
    }

    /**
     * 批量删除文件分享相关
     *
     * @param ids 需要删除的文件分享相关主键
     * @return 结果
     */
    @Override
    public int deleteMSharedByIds(Long[] ids)
    {
        return mSharedMapper.deleteMSharedByIds(ids);
    }

    /**
     * 删除文件分享相关信息
     *
     * @param id 文件分享相关主键
     * @return 结果
     */
    @Override
    public int deleteMSharedById(Long id)
    {
        return mSharedMapper.deleteMSharedById(id);
    }
}
