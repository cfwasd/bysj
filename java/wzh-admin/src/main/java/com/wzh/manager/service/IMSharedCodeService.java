package com.wzh.manager.service;

import java.util.List;
import com.wzh.manager.domain.MSharedCode;

/**
 * 分享码管理Service接口
 * 
 * @author wzh
 * @date 2025-05-02
 */
public interface IMSharedCodeService 
{
    /**
     * 查询分享码管理
     * 
     * @param id 分享码管理主键
     * @return 分享码管理
     */
    public MSharedCode selectMSharedCodeById(Long id);

    /**
     * 查询分享码管理列表
     * 
     * @param mSharedCode 分享码管理
     * @return 分享码管理集合
     */
    public List<MSharedCode> selectMSharedCodeList(MSharedCode mSharedCode);

    /**
     * 新增分享码管理
     * 
     * @param mSharedCode 分享码管理
     * @return 结果
     */
    public int insertMSharedCode(MSharedCode mSharedCode);

    /**
     * 修改分享码管理
     * 
     * @param mSharedCode 分享码管理
     * @return 结果
     */
    public int updateMSharedCode(MSharedCode mSharedCode);

    /**
     * 批量删除分享码管理
     * 
     * @param ids 需要删除的分享码管理主键集合
     * @return 结果
     */
    public int deleteMSharedCodeByIds(Long[] ids);

    /**
     * 删除分享码管理信息
     * 
     * @param id 分享码管理主键
     * @return 结果
     */
    public int deleteMSharedCodeById(Long id);
}
