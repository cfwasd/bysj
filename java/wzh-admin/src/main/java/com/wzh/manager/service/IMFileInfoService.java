package com.wzh.manager.service;

import java.util.List;
import com.wzh.manager.domain.MFileInfo;

/**
 * 文件信息Service接口
 * 
 * @author wzh
 * @date 2025-03-23
 */
public interface IMFileInfoService 
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
     * 批量删除文件信息
     * 
     * @param ids 需要删除的文件信息主键集合
     * @return 结果
     */
    public int deleteMFileInfoByIds(Long[] ids);

    /**
     * 删除文件信息信息
     * 
     * @param id 文件信息主键
     * @return 结果
     */
    public int deleteMFileInfoById(Long id);

    List<MFileInfo> selectMFileInfoByTreeId(Long treeId);
}
