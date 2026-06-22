package com.ruoyiblog.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyiblog.common.core.domain.BaseEntity;

/**
 * 文章分类对象 blog_category
 *
 * @author ruoyi
 * @date 2026-06-20
 */
public class BlogCategory extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 分类ID */
    private Long categoryId;

    /** 父分类ID */
    private Long parentId;

    /** 祖级列表 */
    private String ancestors;

    /** 分类名称 */
    private String categoryName;

    /** 显示顺序 */
    private Long orderNum;

    /** 分类图标 */
    private String icon;

    /** 状态（0正常 1停用） */
    private String status;

    /** 删除标志（0代表存在 2代表删除） */
    private String delFlag;

    /** 文章数量（非数据库字段） */
    private Integer articleCount;

    public void setCategoryId(Long categoryId)
    {
        this.categoryId = categoryId;
    }

    public Long getCategoryId()
    {
        return categoryId;
    }

    public void setParentId(Long parentId)
    {
        this.parentId = parentId;
    }

    public Long getParentId()
    {
        return parentId;
    }

    public void setAncestors(String ancestors)
    {
        this.ancestors = ancestors;
    }

    public String getAncestors()
    {
        return ancestors;
    }

    public void setCategoryName(String categoryName)
    {
        this.categoryName = categoryName;
    }

    public String getCategoryName()
    {
        return categoryName;
    }

    public void setOrderNum(Long orderNum)
    {
        this.orderNum = orderNum;
    }

    public Long getOrderNum()
    {
        return orderNum;
    }

    public void setIcon(String icon)
    {
        this.icon = icon;
    }

    public String getIcon()
    {
        return icon;
    }

    public void setStatus(String status)
    {
        this.status = status;
    }

    public String getStatus()
    {
        return status;
    }

    public void setDelFlag(String delFlag)
    {
        this.delFlag = delFlag;
    }

    public Integer getArticleCount()
    {
        return articleCount;
    }

    public void setArticleCount(Integer articleCount)
    {
        this.articleCount = articleCount;
    }

    public String getDelFlag()
    {
        return delFlag;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("categoryId", getCategoryId())
            .append("parentId", getParentId())
            .append("ancestors", getAncestors())
            .append("categoryName", getCategoryName())
            .append("orderNum", getOrderNum())
            .append("icon", getIcon())
            .append("status", getStatus())
            .append("delFlag", getDelFlag())
            .append("createTime", getCreateTime())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
