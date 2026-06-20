package com.ruoyiblog.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyiblog.common.core.domain.BaseEntity;

/**
 * 评论对象 blog_comment
 *
 * @author ruoyi
 * @date 2026-06-20
 */
public class BlogComment extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 评论ID */
    private Long commentId;

    /** 文章ID */
    private Long articleId;

    /** 父评论ID */
    private Long parentId;

    /** 回复目标用户ID */
    private Long replyTo;

    /** 评论用户ID（blog_user.user_id） */
    private Long userId;

    /** 评论者昵称 */
    private String nickName;

    /** 评论者邮箱 */
    private String email;

    /** 评论者网站 */
    private String website;

    /** 评论内容 */
    private String content;

    /** 评论IP */
    private String ip;

    /** 评论者所在地 */
    private String location;

    /** 浏览器类型 */
    private String browser;

    /** 操作系统 */
    private String os;

    /** 评论状态（0待审核 1已通过 2已拒绝） */
    private String status;

    /** 是否置顶（0否 1是） */
    private Integer isTop;

    public void setCommentId(Long commentId)
    {
        this.commentId = commentId;
    }

    public Long getCommentId()
    {
        return commentId;
    }

    public void setArticleId(Long articleId)
    {
        this.articleId = articleId;
    }

    public Long getArticleId()
    {
        return articleId;
    }

    public void setParentId(Long parentId)
    {
        this.parentId = parentId;
    }

    public Long getParentId()
    {
        return parentId;
    }

    public void setReplyTo(Long replyTo)
    {
        this.replyTo = replyTo;
    }

    public Long getReplyTo()
    {
        return replyTo;
    }

    public void setUserId(Long userId)
    {
        this.userId = userId;
    }

    public Long getUserId()
    {
        return userId;
    }

    public void setNickName(String nickName)
    {
        this.nickName = nickName;
    }

    public String getNickName()
    {
        return nickName;
    }

    public void setEmail(String email)
    {
        this.email = email;
    }

    public String getEmail()
    {
        return email;
    }

    public void setWebsite(String website)
    {
        this.website = website;
    }

    public String getWebsite()
    {
        return website;
    }

    public void setContent(String content)
    {
        this.content = content;
    }

    public String getContent()
    {
        return content;
    }

    public void setIp(String ip)
    {
        this.ip = ip;
    }

    public String getIp()
    {
        return ip;
    }

    public void setLocation(String location)
    {
        this.location = location;
    }

    public String getLocation()
    {
        return location;
    }

    public void setBrowser(String browser)
    {
        this.browser = browser;
    }

    public String getBrowser()
    {
        return browser;
    }

    public void setOs(String os)
    {
        this.os = os;
    }

    public String getOs()
    {
        return os;
    }

    public void setStatus(String status)
    {
        this.status = status;
    }

    public String getStatus()
    {
        return status;
    }

    public void setIsTop(Integer isTop)
    {
        this.isTop = isTop;
    }

    public Integer getIsTop()
    {
        return isTop;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("commentId", getCommentId())
            .append("articleId", getArticleId())
            .append("parentId", getParentId())
            .append("replyTo", getReplyTo())
            .append("userId", getUserId())
            .append("nickName", getNickName())
            .append("email", getEmail())
            .append("website", getWebsite())
            .append("content", getContent())
            .append("ip", getIp())
            .append("location", getLocation())
            .append("browser", getBrowser())
            .append("os", getOs())
            .append("status", getStatus())
            .append("isTop", getIsTop())
            .append("createTime", getCreateTime())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
