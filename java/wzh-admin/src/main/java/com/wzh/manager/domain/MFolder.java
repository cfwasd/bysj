package com.wzh.manager.domain;

import com.wzh.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.wzh.common.annotation.Excel;

/**
 * 逻辑文件夹对象 m_folder
 * 
 * @author ruoyi
 * @date 2025-03-23
 */
public class MFolder extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** $column.columnComment */
    private Long id;

    /** 文件夹名称 */
    @Excel(name = "文件夹名称")
    private String floderName;

    /** 父文件夹名称 */
    @Excel(name = "父文件夹名称")
    private String pFloderName;

    /** 文件个数 */
    @Excel(name = "文件个数")
    private String fileCount;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }

    public void setFloderName(String floderName) 
    {
        this.floderName = floderName;
    }

    public String getFloderName() 
    {
        return floderName;
    }

    public void setpFloderName(String pFloderName) 
    {
        this.pFloderName = pFloderName;
    }

    public String getpFloderName() 
    {
        return pFloderName;
    }

    public void setFileCount(String fileCount) 
    {
        this.fileCount = fileCount;
    }

    public String getFileCount() 
    {
        return fileCount;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("floderName", getFloderName())
            .append("pFloderName", getpFloderName())
            .append("fileCount", getFileCount())
            .toString();
    }
}
