package com.ruoyiblog.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyiblog.common.core.domain.BaseEntity;

/**
 * 独立页面对象 blog_page
 *
 * @author ruoyi
 * @date 2026-06-20
 */
public class BlogPage extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 页面ID */
    private Long pageId;

    /** 页面标题 */
    private String title;

    /** 页面别名（/about、/contact） */
    private String slug;

    /** 页面内容 */
    private String content;

    /** 页面封面 */
    private String cover;

    /** 是否显示在导航栏（0隐藏 1显示） */
    private String isShow;

    /** 显示顺序 */
    private Long orderNum;

    /** 状态（0正常 1停用） */
    private String status;

    public void setPageId(Long pageId)
    {
        this.pageId = pageId;
    }

    public Long getPageId()
    {
        return pageId;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public String getTitle()
    {
        return title;
    }

    public void setSlug(String slug)
    {
        this.slug = slug;
    }

    public String getSlug()
    {
        return slug;
    }

    public void setContent(String content)
    {
        this.content = content;
    }

    public String getContent()
    {
        return content;
    }

    public void setCover(String cover)
    {
        this.cover = cover;
    }

    public String getCover()
    {
        return cover;
    }

    public void setIsShow(String isShow)
    {
        this.isShow = isShow;
    }

    public String getIsShow()
    {
        return isShow;
    }

    public void setOrderNum(Long orderNum)
    {
        this.orderNum = orderNum;
    }

    public Long getOrderNum()
    {
        return orderNum;
    }

    public void setStatus(String status)
    {
        this.status = status;
    }

    public String getStatus()
    {
        return status;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("pageId", getPageId())
            .append("title", getTitle())
            .append("slug", getSlug())
            .append("content", getContent())
            .append("cover", getCover())
            .append("isShow", getIsShow())
            .append("orderNum", getOrderNum())
            .append("status", getStatus())
            .append("createTime", getCreateTime())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
