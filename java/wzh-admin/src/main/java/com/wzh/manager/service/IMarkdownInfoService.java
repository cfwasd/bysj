package com.wzh.manager.service;

import java.util.List;
import com.wzh.manager.domain.MarkdownInfo;

/**
 * 【请填写功能名称】Service接口
 *
 * @author wzh
 * @date 2025-04-23
 */
public interface IMarkdownInfoService
{
    /**
     * 查询【请填写功能名称】
     *
     * @param id 【请填写功能名称】主键
     * @return 【请填写功能名称】
     */
    public MarkdownInfo selectMMarkdownInfoById(Long id);

    /**
     * 查询【请填写功能名称】列表
     *
     * @param mMarkdownInfo 【请填写功能名称】
     * @return 【请填写功能名称】集合
     */
    public List<MarkdownInfo> selectMMarkdownInfoList(MarkdownInfo mMarkdownInfo);

    /**
     * 新增【请填写功能名称】
     *
     * @param mMarkdownInfo 【请填写功能名称】
     * @return 结果
     */
    public int insertMMarkdownInfo(MarkdownInfo mMarkdownInfo);

    /**
     * 修改【请填写功能名称】
     *
     * @param mMarkdownInfo 【请填写功能名称】
     * @return 结果
     */
    public int updateMMarkdownInfo(MarkdownInfo mMarkdownInfo);

    /**
     * 批量删除【请填写功能名称】
     *
     * @param ids 需要删除的【请填写功能名称】主键集合
     * @return 结果
     */
    public int deleteMMarkdownInfoByIds(Long[] ids);

    /**
     * 删除【请填写功能名称】信息
     *
     * @param id 【请填写功能名称】主键
     * @return 结果
     */
    public int deleteMMarkdownInfoById(Long id);
}
