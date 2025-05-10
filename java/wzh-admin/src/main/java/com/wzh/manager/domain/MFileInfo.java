package com.wzh.manager.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.wzh.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.wzh.common.annotation.Excel;

import java.util.Date;

/**
 * 文件信息对象 m_file_info
 * 
 * @author wzh
 * @date 2025-03-23
 */
public class MFileInfo extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** $column.columnComment */
    private Long id;

    /** 文件uuid */
    @Excel(name = "文件uuid")
    private String uuid;

    /** 文件逻辑位置 */
    @Excel(name = "文件逻辑位置")
    private String folder;

    /** 逻辑文件夹id */
    @Excel(name = "逻辑文件夹id")
    private Long folderId;

    /** 文件服务器位置 */
    @Excel(name = "文件服务器位置")
    private String fileRealFolder;

    /** 文件原始名 */
    @Excel(name = "文件原始名")
    private String fileRawName;

    /** 文件名(uuid+原始名) */
    @Excel(name = "文件名(uuid+原始名)")
    private String fileName;

    /** 文件后缀名 */
    @Excel(name = "文件后缀名")
    private String fileExtentions;

    /** 文件oss地址 */
    @Excel(name = "文件oss地址")
    private String ossUrl;

    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss")
    private Date uploadTime;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }

    public void setUuid(String uuid)
    {
        this.uuid = uuid;
    }

    public String getUuid()
    {
        return uuid;
    }

    public void setFolder(String folder) 
    {
        this.folder = folder;
    }

    public String getFolder() 
    {
        return folder;
    }

    public void setFolderId(Long folderId) 
    {
        this.folderId = folderId;
    }

    public Long getFolderId() 
    {
        return folderId;
    }

    public void setFileRealFolder(String fileRealFolder) 
    {
        this.fileRealFolder = fileRealFolder;
    }

    public String getFileRealFolder() 
    {
        return fileRealFolder;
    }

    public void setFileRawName(String fileRawName) 
    {
        this.fileRawName = fileRawName;
    }

    public String getFileRawName() 
    {
        return fileRawName;
    }

    public void setFileName(String fileName) 
    {
        this.fileName = fileName;
    }

    public String getFileName() 
    {
        return fileName;
    }

    public void setFileExtentions(String fileExtentions) 
    {
        this.fileExtentions = fileExtentions;
    }

    public String getFileExtentions() 
    {
        return fileExtentions;
    }

    public void setOssUrl(String ossUrl) 
    {
        this.ossUrl = ossUrl;
    }

    public String getOssUrl() 
    {
        return ossUrl;
    }


    public Date getUploadTime() {
        return uploadTime;
    }

    public void setUploadTime(Date uploadTime) {
        this.uploadTime = uploadTime;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("uuid", getUuid())
            .append("folder", getFolder())
            .append("folderId", getFolderId())
            .append("fileRealFolder", getFileRealFolder())
            .append("fileRawName", getFileRawName())
            .append("fileName", getFileName())
            .append("fileExtentions", getFileExtentions())
            .append("ossUrl", getOssUrl())
            .append("uploadTime", getUploadTime())
            .toString();
    }
}
