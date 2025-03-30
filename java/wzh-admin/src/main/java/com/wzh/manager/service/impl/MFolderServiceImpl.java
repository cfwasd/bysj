package com.wzh.manager.service.impl;

import java.util.*;

import com.wzh.manager.domain.MFolderVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.wzh.manager.mapper.MFolderMapper;
import com.wzh.manager.domain.MFolder;
import com.wzh.manager.service.IMFolderService;

/**
 * 逻辑文件夹Service业务层处理
 * 
 * @author ruoyi
 * @date 2025-03-23
 */
@Service
public class MFolderServiceImpl implements IMFolderService 
{
    private static final Logger log = LoggerFactory.getLogger(MFolderServiceImpl.class);
    @Autowired
    private MFolderMapper mFolderMapper;

    /**
     * 查询逻辑文件夹
     * 
     * @param id 逻辑文件夹主键
     * @return 逻辑文件夹
     */
    @Override
    public MFolder selectMFolderById(Long id)
    {
        return mFolderMapper.selectMFolderById(id);
    }

    /**
     * 查询逻辑文件夹列表
     * 
     * @param mFolder 逻辑文件夹
     * @return 逻辑文件夹
     */
    @Override
    public List<MFolder> selectMFolderList(MFolder mFolder)
    {
        return mFolderMapper.selectMFolderList(mFolder);
    }

    /**
     * 新增逻辑文件夹
     * 
     * @param mFolder 逻辑文件夹
     * @return 结果
     */
    @Override
    public int insertMFolder(MFolder mFolder)
    {
        return mFolderMapper.insertMFolder(mFolder);
    }

    /**
     * 修改逻辑文件夹
     * 
     * @param mFolder 逻辑文件夹
     * @return 结果
     */
    @Override
    public int updateMFolder(MFolder mFolder)
    {
        return mFolderMapper.updateMFolder(mFolder);
    }

    /**
     * 批量删除逻辑文件夹
     * 
     * @param ids 需要删除的逻辑文件夹主键
     * @return 结果
     */
    @Override
    public int deleteMFolderByIds(Long[] ids)
    {
        return mFolderMapper.deleteMFolderByIds(ids);
    }

    /**
     * 删除逻辑文件夹信息
     * 
     * @param id 逻辑文件夹主键
     * @return 结果
     */
    @Override
    public int deleteMFolderById(Long id)
    {
        return mFolderMapper.deleteMFolderById(id);
    }

    @Override
    public List<MFolderVo> deepTree(MFolder mFolder) {
        List<MFolder> folders = mFolderMapper.selectMFolderList(mFolder);
        return this.buildTree(folders);
    }

    public List<MFolderVo> buildTree(List<MFolder> list) {
        Map<String, MFolderVo> result = new LinkedHashMap<>();
        result.put("/", new MFolderVo());
        for (MFolder folder : list) {
            if (folder.getpFloderName().equals("/")){
                MFolderVo p = result.get("/");
                MFolderVo mFolderVo = new MFolderVo();
                mFolderVo.setId(folder.getId());
                mFolderVo.setFloderName(folder.getFloderName());
                mFolderVo.setpFloderName(folder.getpFloderName());
                mFolderVo.setFileCount(folder.getFileCount());
                p.getChildren().add(mFolderVo);
                result.put("/", p);
            }
        }
        List<MFolderVo> children = result.get("/").getChildren();
        List<MFolderVo> res = new ArrayList<>();
//        log.info("{}",result);
        for (MFolderVo mFolderVo : children) {
            mFolderVo=deepTree2(false,mFolderVo,list);
            res.add(mFolderVo);
        }


        return res;
    }

    public MFolderVo deepTree2(boolean isFinish,MFolderVo vo, List<MFolder> list) {
        if (isFinish){
            return vo;
        }else {
            for (MFolder folder : list) {
                if (folder.getpFloderName().equals(vo.getFloderName())){
                    MFolderVo mFolderVo = new MFolderVo();
                    mFolderVo.setId(folder.getId());
                    mFolderVo.setFloderName(folder.getFloderName());
                    mFolderVo.setpFloderName(folder.getpFloderName());
                    mFolderVo.setFileCount(folder.getFileCount());
                    vo.getChildren().add(mFolderVo);
                    deepTree2(false,mFolderVo,list);
                }
                else {
                    deepTree2(true,vo,list);
                }
            }
        }
        return vo;
    }


}
