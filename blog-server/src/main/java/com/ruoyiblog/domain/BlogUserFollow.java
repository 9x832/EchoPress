package com.ruoyiblog.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyiblog.common.core.domain.BaseEntity;

/**
 * 用户关注关系对象 blog_user_follow
 *
 * @author ruoyi
 * @date 2026-06-20
 */
public class BlogUserFollow extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 关注者ID */
    private Long followerId;

    /** 被关注者ID */
    private Long followeeId;

    public void setFollowerId(Long followerId)
    {
        this.followerId = followerId;
    }

    public Long getFollowerId()
    {
        return followerId;
    }

    public void setFolloweeId(Long followeeId)
    {
        this.followeeId = followeeId;
    }

    public Long getFolloweeId()
    {
        return followeeId;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("followerId", getFollowerId())
            .append("followeeId", getFolloweeId())
            .append("createTime", getCreateTime())
            .toString();
    }
}
