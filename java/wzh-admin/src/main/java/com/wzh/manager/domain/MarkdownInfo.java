package com.wzh.manager.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.wzh.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.wzh.common.annotation.Excel;

/**
 * Markdown文档信息实体类
 * 对应数据库表 m_markdown_info
 * 
 * @author wzh
 * @date 2025-04-23
 */
public class MarkdownInfo extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 文档ID */
    private Long id;

    /** 文档标题 */
    @Excel(name = "文档标题")
    private String title;

    /** Markdown内容 */
    @Excel(name = "文档内容")
    private String content;

    /** 创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createdAt;

    /** 更新时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updatedAt;

    /** 删除标志（0-未删除，1-已删除） */
    private Integer isDeleted;

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId()
    {
        return id;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public String getTitle()
    {
        return title;
    }

    public void setContent(String content)
    {
        this.content = content;
    }

    public String getContent()
    {
        return content;
    }

    public void setCreatedAt(Date createdAt)
    {
        this.createdAt = createdAt;
    }

    public Date getCreatedAt()
    {
        return createdAt;
    }

    public void setUpdatedAt(Date updatedAt)
    {
        this.updatedAt = updatedAt;
    }

    public Date getUpdatedAt()
    {
        return updatedAt;
    }

    public void setIsDeleted(Integer isDeleted)
    {
        this.isDeleted = isDeleted;
    }

    public Integer getIsDeleted()
    {
        return isDeleted;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("title", getTitle())
                .append("content", getContent())
                .append("createdAt", getCreatedAt())
                .append("updatedAt", getUpdatedAt())
                .append("isDeleted", getIsDeleted())
                .toString();
    }
}
