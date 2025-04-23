package com.wzh.manager.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.wzh.manager.mapper.MarkdownInfoMapper;
import com.wzh.manager.domain.MarkdownInfo;
import com.wzh.manager.service.IMarkdownInfoService;

/**
 * 【请填写功能名称】Service业务层处理
 *
 * @author wzh
 * @date 2025-04-23
 */
@Service
public class MarkdownInfoServiceImpl implements IMarkdownInfoService
{
    @Autowired
    private MarkdownInfoMapper mMarkdownInfoMapper;

    /**
     * 查询【请填写功能名称】
     *
     * @param id 【请填写功能名称】主键
     * @return 【请填写功能名称】
     */
    @Override
    public MarkdownInfo selectMMarkdownInfoById(Long id)
    {
        return mMarkdownInfoMapper.selectMarkdownInfoById(id);
    }

    /**
     * 查询【请填写功能名称】列表
     *
     * @param mMarkdownInfo 【请填写功能名称】
     * @return 【请填写功能名称】
     */
    @Override
    public List<MarkdownInfo> selectMMarkdownInfoList(MarkdownInfo mMarkdownInfo)
    {
        return mMarkdownInfoMapper.selectMarkdownInfoList(mMarkdownInfo);
    }

    /**
     * 新增【请填写功能名称】
     *
     * @param mMarkdownInfo 【请填写功能名称】
     * @return 结果
     */
    @Override
    public int insertMMarkdownInfo(MarkdownInfo mMarkdownInfo)
    {
        mMarkdownInfoMapper.insertMarkdownInfo(mMarkdownInfo);
        return Math.toIntExact(mMarkdownInfo.getId());
    }

    /**
     * 修改【请填写功能名称】
     *
     * @param mMarkdownInfo 【请填写功能名称】
     * @return 结果
     */
    @Override
    public int updateMMarkdownInfo(MarkdownInfo mMarkdownInfo)
    {
        return mMarkdownInfoMapper.updateMarkdownInfo(mMarkdownInfo);
    }

    /**
     * 批量删除【请填写功能名称】
     *
     * @param ids 需要删除的【请填写功能名称】主键
     * @return 结果
     */
    @Override
    public int deleteMMarkdownInfoByIds(Long[] ids)
    {
        return mMarkdownInfoMapper.deleteMarkdownInfoByIds(ids);
    }

    /**
     * 删除【请填写功能名称】信息
     *
     * @param id 【请填写功能名称】主键
     * @return 结果
     */
    @Override
    public int deleteMMarkdownInfoById(Long id)
    {
        return mMarkdownInfoMapper.deleteMarkdownInfoById(id);
    }
}
