package com.ruoyiblog.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyiblog.common.core.domain.BaseEntity;

/**
 * 用户点赞记录对象 blog_user_like
 *
 * @author ruoyi
 * @date 2026-06-20
 */
public class BlogUserLike extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 用户ID */
    private Long userId;

    /** 文章ID */
    private Long articleId;

    public void setUserId(Long userId)
    {
        this.userId = userId;
    }

    public Long getUserId()
    {
        return userId;
    }

    public void setArticleId(Long articleId)
    {
        this.articleId = articleId;
    }

    public Long getArticleId()
    {
        return articleId;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("userId", getUserId())
            .append("articleId", getArticleId())
            .append("createTime", getCreateTime())
            .toString();
    }
}
