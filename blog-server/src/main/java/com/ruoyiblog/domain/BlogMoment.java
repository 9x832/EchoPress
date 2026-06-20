package com.ruoyiblog.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyiblog.common.core.domain.BaseEntity;

/**
 * 动态/说说对象 blog_moment
 *
 * @author ruoyi
 * @date 2026-06-20
 */
public class BlogMoment extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 动态ID */
    private Long momentId;

    /** 动态内容 */
    private String content;

    /** 图片列表（JSON数组） */
    private String images;

    /** 发布位置 */
    private String location;

    /** 点赞次数 */
    private Long likeCount;

    /** 状态（0公开 1私密 2草稿） */
    private String status;

    /** 删除标志（0代表存在 2代表删除） */
    private String delFlag;

    public void setMomentId(Long momentId)
    {
        this.momentId = momentId;
    }

    public Long getMomentId()
    {
        return momentId;
    }

    public void setContent(String content)
    {
        this.content = content;
    }

    public String getContent()
    {
        return content;
    }

    public void setImages(String images)
    {
        this.images = images;
    }

    public String getImages()
    {
        return images;
    }

    public void setLocation(String location)
    {
        this.location = location;
    }

    public String getLocation()
    {
        return location;
    }

    public void setLikeCount(Long likeCount)
    {
        this.likeCount = likeCount;
    }

    public Long getLikeCount()
    {
        return likeCount;
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

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("momentId", getMomentId())
            .append("content", getContent())
            .append("images", getImages())
            .append("location", getLocation())
            .append("likeCount", getLikeCount())
            .append("status", getStatus())
            .append("delFlag", getDelFlag())
            .append("createTime", getCreateTime())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
