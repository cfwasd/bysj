package com.wzh.manager.mapper;

import java.util.List;
import com.wzh.manager.domain.MFolder;

/**
 * 逻辑文件夹Mapper接口
 * 
 * @author ruoyi
 * @date 2025-03-23
 */
public interface MFolderMapper 
{
    /**
     * 查询逻辑文件夹
     * 
     * @param id 逻辑文件夹主键
     * @return 逻辑文件夹
     */
    public MFolder selectMFolderById(Long id);

    /**
     * 查询逻辑文件夹列表
     * 
     * @param mFolder 逻辑文件夹
     * @return 逻辑文件夹集合
     */
    public List<MFolder> selectMFolderList(MFolder mFolder);

    /**
     * 新增逻辑文件夹
     * 
     * @param mFolder 逻辑文件夹
     * @return 结果
     */
    public int insertMFolder(MFolder mFolder);

    /**
     * 修改逻辑文件夹
     * 
     * @param mFolder 逻辑文件夹
     * @return 结果
     */
    public int updateMFolder(MFolder mFolder);

    /**
     * 删除逻辑文件夹
     * 
     * @param id 逻辑文件夹主键
     * @return 结果
     */
    public int deleteMFolderById(Long id);

    /**
     * 批量删除逻辑文件夹
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteMFolderByIds(Long[] ids);
}
