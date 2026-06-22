package com.ruoyiblog.domain;

import java.util.Date;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyiblog.common.core.domain.BaseEntity;

/**
 * 文章对象 blog_article
 *
 * @author ruoyi
 * @date 2026-06-20
 */
public class BlogArticle extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 文章ID */
    private Long articleId;

    /** 作者用户ID */
    private Long userId;

    /** 文章标题 */
    private String title;

    /** 文章别名（SEO友好URL） */
    private String slug;

    /** 文章摘要 */
    private String summary;

    /** 文章内容 */
    private String content;

    /** 封面图片URL */
    private String cover;

    /** 分类ID */
    private Long categoryId;

    /** 文章类型（0原创 1转载 2翻译） */
    private String articleType;

    /** 转载来源 */
    private String articleSource;

    /** 原文链接 */
    private String sourceUrl;

    /** 是否置顶（0否 1是） */
    private Integer isTop;

    /** 是否推荐（0否 1是） */
    private Integer isRecommend;

    /** 是否允许评论（0允许 1禁止） */
    private String isComment;

    /** 浏览次数 */
    private Long viewCount;

    /** 点赞次数 */
    private Long likeCount;

    /** 评论次数 */
    private Long commentCount;

    /** 文章字数 */
    private Long wordCount;

    /** 文章状态（0草稿 1已发布 2私密 3待审核） */
    private String status;

    /** 删除标志（0代表存在 2代表删除） */
    private String delFlag;

    /** 发布时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date publishTime;

    /** 筛选用 - 标签ID（非DB字段） */
    private Long tagId;

    /** 文章标签ID列表（非DB字段） */
    private List<Long> tagIds;

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

    public void setSummary(String summary)
    {
        this.summary = summary;
    }

    public String getSummary()
    {
        return summary;
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

    public void setCategoryId(Long categoryId)
    {
        this.categoryId = categoryId;
    }

    public Long getCategoryId()
    {
        return categoryId;
    }

    public void setArticleType(String articleType)
    {
        this.articleType = articleType;
    }

    public String getArticleType()
    {
        return articleType;
    }

    public void setArticleSource(String articleSource)
    {
        this.articleSource = articleSource;
    }

    public String getArticleSource()
    {
        return articleSource;
    }

    public void setSourceUrl(String sourceUrl)
    {
        this.sourceUrl = sourceUrl;
    }

    public String getSourceUrl()
    {
        return sourceUrl;
    }

    public void setIsTop(Integer isTop)
    {
        this.isTop = isTop;
    }

    public Integer getIsTop()
    {
        return isTop;
    }

    public void setIsRecommend(Integer isRecommend)
    {
        this.isRecommend = isRecommend;
    }

    public Integer getIsRecommend()
    {
        return isRecommend;
    }

    public void setIsComment(String isComment)
    {
        this.isComment = isComment;
    }

    public String getIsComment()
    {
        return isComment;
    }

    public void setViewCount(Long viewCount)
    {
        this.viewCount = viewCount;
    }

    public Long getViewCount()
    {
        return viewCount;
    }

    public void setLikeCount(Long likeCount)
    {
        this.likeCount = likeCount;
    }

    public Long getLikeCount()
    {
        return likeCount;
    }

    public void setCommentCount(Long commentCount)
    {
        this.commentCount = commentCount;
    }

    public Long getCommentCount()
    {
        return commentCount;
    }

    public void setWordCount(Long wordCount)
    {
        this.wordCount = wordCount;
    }

    public Long getWordCount()
    {
        return wordCount;
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

    public String getDelFlag()
    {
        return delFlag;
    }

    public void setPublishTime(Date publishTime)
    {
        this.publishTime = publishTime;
    }

    public Date getPublishTime()
    {
        return publishTime;
    }

    public void setTagId(Long tagId)
    {
        this.tagId = tagId;
    }

    public Long getTagId()
    {
        return tagId;
    }

    public void setTagIds(List<Long> tagIds)
    {
        this.tagIds = tagIds;
    }

    public List<Long> getTagIds()
    {
        return tagIds;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("articleId", getArticleId())
            .append("userId", getUserId())
            .append("title", getTitle())
            .append("slug", getSlug())
            .append("summary", getSummary())
            .append("content", getContent())
            .append("cover", getCover())
            .append("categoryId", getCategoryId())
            .append("articleType", getArticleType())
            .append("articleSource", getArticleSource())
            .append("sourceUrl", getSourceUrl())
            .append("isTop", getIsTop())
            .append("isRecommend", getIsRecommend())
            .append("isComment", getIsComment())
            .append("viewCount", getViewCount())
            .append("likeCount", getLikeCount())
            .append("commentCount", getCommentCount())
            .append("wordCount", getWordCount())
            .append("status", getStatus())
            .append("delFlag", getDelFlag())
            .append("publishTime", getPublishTime())
            .append("tagId", getTagId())
            .append("tagIds", getTagIds())
            .append("createTime", getCreateTime())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
