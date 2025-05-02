package com.wzh.manager.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.wzh.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.wzh.common.annotation.Excel;

/**
 * 文件分享相关对象 m_shared
 *
 * @author wzh
 * @date 2025-05-02
 */
public class MShared extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** $column.columnComment */
    private Long id;

    /** 分享码 */
    @Excel(name = "分享码")
    private String sharedCode;

    /** 文件id或者markdown */
    @Excel(name = "文件id或者markdown")
    private String fileId;

    /** 文件类型，md为markdown，file为文件 */
    @Excel(name = "文件类型，md为markdown，file为文件")
    private String type;

    /** 1为过期，0为未过期 */
    @Excel(name = "1为过期，0为未过期")
    private Long isOutdate;

    /** 数据更新时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "数据更新时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date updateDate;

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId()
    {
        return id;
    }

    public void setSharedCode(String sharedCode)
    {
        this.sharedCode = sharedCode;
    }

    public String getSharedCode()
    {
        return sharedCode;
    }

    public void setFileId(String fileId)
    {
        this.fileId = fileId;
    }

    public String getFileId()
    {
        return fileId;
    }

    public void setType(String type)
    {
        this.type = type;
    }

    public String getType()
    {
        return type;
    }

    public void setIsOutdate(Long isOutdate)
    {
        this.isOutdate = isOutdate;
    }

    public Long getIsOutdate()
    {
        return isOutdate;
    }

    public void setUpdateDate(Date updateDate)
    {
        this.updateDate = updateDate;
    }

    public Date getUpdateDate()
    {
        return updateDate;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("sharedCode", getSharedCode())
                .append("fileId", getFileId())
                .append("type", getType())
                .append("isOutdate", getIsOutdate())
                .append("updateDate", getUpdateDate())
                .append("createTime", getCreateTime())
                .toString();
    }
}
