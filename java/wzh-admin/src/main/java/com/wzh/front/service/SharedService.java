package com.wzh.front.service;

import com.wzh.common.core.domain.AjaxResult;
import com.wzh.manager.domain.MFileInfo;
import com.wzh.manager.domain.MShared;
import com.wzh.manager.domain.MSharedCode;
import com.wzh.manager.domain.MarkdownInfo;

import java.util.List;

public interface SharedService {

    /**
     * 根据分享码查询未过期分享信息
     * @param code
     * @return
     */
    public List<Object> queryBySharedCode(String code);

    public MFileInfo queryFileInfoByCode(Long fileId);

    public MarkdownInfo queryMarkdownInfoByCode(Long fileId);

    public boolean queryCodeIsExist(String code);

    List<MFileInfo> queryFileInfoByName(String code,String fileName);

    List<MarkdownInfo> queryMarkdownInfoByName(String code,String fileName);
}
