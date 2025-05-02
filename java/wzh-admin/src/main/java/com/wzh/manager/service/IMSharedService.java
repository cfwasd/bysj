package com.wzh.manager.service;

import java.util.List;

import com.wzh.common.core.domain.AjaxResult;
import com.wzh.manager.domain.MShared;

/**
 * 文件分享相关Service接口
 *
 * @author wzh
 * @date 2025-05-02
 */
public interface IMSharedService
{
    /**
     * 查询文件分享相关
     *
     * @param id 文件分享相关主键
     * @return 文件分享相关
     */
    public MShared selectMSharedById(Long id);

    /**
     * 查询文件分享相关列表
     *
     * @param mShared 文件分享相关
     * @return 文件分享相关集合
     */
    public List<MShared> selectMSharedList(MShared mShared);

    /**
     * 新增文件分享相关
     *
     * @param mShared 文件分享相关
     * @return 结果
     */
    public AjaxResult insertMShared(MShared mShared);

    /**
     * 修改文件分享相关
     *
     * @param mShared 文件分享相关
     * @return 结果
     */
    public int updateMShared(MShared mShared);

    /**
     * 批量删除文件分享相关
     *
     * @param ids 需要删除的文件分享相关主键集合
     * @return 结果
     */
    public int deleteMSharedByIds(Long[] ids);

    /**
     * 删除文件分享相关信息
     *
     * @param id 文件分享相关主键
     * @return 结果
     */
    public int deleteMSharedById(Long id);
}
