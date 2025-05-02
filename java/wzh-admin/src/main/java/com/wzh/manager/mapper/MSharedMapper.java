package com.wzh.manager.mapper;

import java.util.Date;
import java.util.List;
import com.wzh.manager.domain.MShared;
import org.apache.ibatis.annotations.Param;

/**
 * 文件分享相关Mapper接口
 *
 * @author wzh
 * @date 2025-05-02
 */
public interface MSharedMapper
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
    public int insertMShared(MShared mShared);

    /**
     * 修改文件分享相关
     *
     * @param mShared 文件分享相关
     * @return 结果
     */
    public int updateMShared(MShared mShared);

    /**
     * 删除文件分享相关
     *
     * @param id 文件分享相关主键
     * @return 结果
     */
    public int deleteMSharedById(Long id);

    /**
     * 批量删除文件分享相关
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteMSharedByIds(Long[] ids);

    int insertMSharedList(List<MShared> list);

    List<MShared> checkIsExist(@Param("sharedCode") String sharedCode, @Param("ids") Long[] ids);

    int updateMSharedByIds(@Param("updateDate") Date updateDate, @Param("ids") Long[] ids);
}
