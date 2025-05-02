package com.wzh.manager.domain;

import com.wzh.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.wzh.common.annotation.Excel;

/**
 * 分享码管理对象 m_shared_code
 * 
 * @author wzh
 * @date 2025-05-02
 */
public class MSharedCode extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** $column.columnComment */
    private Long id;

    /** 分享码 */
    @Excel(name = "分享码")
    private String code;

    /** 0为正常，1为停用 */
    @Excel(name = "0为正常，1为停用")
    private Long status;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }

    public void setCode(String code) 
    {
        this.code = code;
    }

    public String getCode() 
    {
        return code;
    }

    public void setStatus(Long status) 
    {
        this.status = status;
    }

    public Long getStatus() 
    {
        return status;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("code", getCode())
            .append("status", getStatus())
            .append("createTime", getCreateTime())
            .toString();
    }
}
