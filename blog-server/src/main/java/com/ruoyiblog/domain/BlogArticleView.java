package com.ruoyiblog.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyiblog.common.core.domain.BaseEntity;

/**
 * 文章浏览记录对象 blog_article_view
 *
 * @author ruoyi
 * @date 2026-06-20
 */
public class BlogArticleView extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 浏览记录ID */
    private Long viewId;

    /** 文章ID */
    private Long articleId;

    /** 用户ID（未登录为null） */
    private Long userId;

    /** 浏览IP */
    private String ip;

    /** User-Agent */
    private String userAgent;

    /** 浏览时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date viewTime;

    public void setViewId(Long viewId)
    {
        this.viewId = viewId;
    }

    public Long getViewId()
    {
        return viewId;
    }

    public void setArticleId(Long articleId)
    {
        this.articleId = articleId;
    }

    public Long getArticleId()
    {
        return articleId;
    }

    public void setUserId(Long userId)
    {
        this.userId = userId;
    }

    public Long getUserId()
    {
        return userId;
    }

    public void setIp(String ip)
    {
        this.ip = ip;
    }

    public String getIp()
    {
        return ip;
    }

    public void setUserAgent(String userAgent)
    {
        this.userAgent = userAgent;
    }

    public String getUserAgent()
    {
        return userAgent;
    }

    public void setViewTime(Date viewTime)
    {
        this.viewTime = viewTime;
    }

    public Date getViewTime()
    {
        return viewTime;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("viewId", getViewId())
            .append("articleId", getArticleId())
            .append("userId", getUserId())
            .append("ip", getIp())
            .append("userAgent", getUserAgent())
            .append("viewTime", getViewTime())
            .toString();
    }
}
