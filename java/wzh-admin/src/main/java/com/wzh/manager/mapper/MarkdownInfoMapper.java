package com.wzh.manager.mapper;

import java.util.List;
import com.wzh.manager.domain.MarkdownInfo;
import org.apache.ibatis.annotations.Param;

/**
 * 【请填写功能名称】Mapper接口
 *
 * @author wzh
 * @date 2025-04-23
 */
public interface MarkdownInfoMapper
{
    /**
     * 查询【请填写功能名称】
     *
     * @param id 【请填写功能名称】主键
     * @return 【请填写功能名称】
     */
    public MarkdownInfo selectMarkdownInfoById(Long id);

    /**
     * 查询【请填写功能名称】列表
     *
     * @param mMarkdownInfo 【请填写功能名称】
     * @return 【请填写功能名称】集合
     */
    public List<MarkdownInfo> selectMarkdownInfoList(MarkdownInfo mMarkdownInfo);

    /**
     * 新增【请填写功能名称】
     *
     * @param mMarkdownInfo 【请填写功能名称】
     * @return 结果
     */
    public int insertMarkdownInfo(MarkdownInfo mMarkdownInfo);

    /**
     * 修改【请填写功能名称】
     *
     * @param mMarkdownInfo 【请填写功能名称】
     * @return 结果
     */
    public int updateMarkdownInfo(MarkdownInfo mMarkdownInfo);

    /**
     * 删除【请填写功能名称】
     *
     * @param id 【请填写功能名称】主键
     * @return 结果
     */
    public int deleteMarkdownInfoById(Long id);

    /**
     * 批量删除【请填写功能名称】
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteMarkdownInfoByIds(Long[] ids);

    List<MarkdownInfo> selectMFileInfoListByCode(@Param("code") String code, @Param("fileName") String fileName);
}
