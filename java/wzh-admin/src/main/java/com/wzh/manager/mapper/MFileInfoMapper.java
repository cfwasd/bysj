package com.wzh.manager.mapper;

import java.util.List;
import com.wzh.manager.domain.MFileInfo;

/**
 * 文件信息Mapper接口
 * 
 * @author wzh
 * @date 2025-03-23
 */
public interface MFileInfoMapper 
{
    /**
     * 查询文件信息
     * 
     * @param id 文件信息主键
     * @return 文件信息
     */
    public MFileInfo selectMFileInfoById(Long id);

    /**
     * 查询文件信息列表
     * 
     * @param mFileInfo 文件信息
     * @return 文件信息集合
     */
    public List<MFileInfo> selectMFileInfoList(MFileInfo mFileInfo);

    /**
     * 新增文件信息
     * 
     * @param mFileInfo 文件信息
     * @return 结果
     */
    public int insertMFileInfo(MFileInfo mFileInfo);

    /**
     * 修改文件信息
     * 
     * @param mFileInfo 文件信息
     * @return 结果
     */
    public int updateMFileInfo(MFileInfo mFileInfo);

    /**
     * 删除文件信息
     * 
     * @param id 文件信息主键
     * @return 结果
     */
    public int deleteMFileInfoById(Long id);

    /**
     * 批量删除文件信息
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteMFileInfoByIds(Long[] ids);
}
