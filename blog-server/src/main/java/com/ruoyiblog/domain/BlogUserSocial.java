package com.ruoyiblog.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyiblog.common.core.domain.BaseEntity;

/**
 * 用户第三方登录绑定对象 blog_user_social
 *
 * @author ruoyi
 * @date 2026-06-20
 */
public class BlogUserSocial extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 绑定记录ID */
    private Long socialId;

    /** 用户ID */
    private Long userId;

    /** 平台类型（github/wechat/qq/weibo/gitee） */
    private String platform;

    /** 平台唯一标识 */
    private String openId;

    /** 平台UnionID */
    private String unionId;

    /** 访问令牌 */
    private String accessToken;

    /** 刷新令牌 */
    private String refreshToken;

    /** 令牌过期时间（秒） */
    private Long expiresIn;

    /** 平台昵称 */
    private String nickName;

    /** 平台头像 */
    private String avatar;

    /** 平台原始信息（JSON） */
    private String rawInfo;

    public void setSocialId(Long socialId)
    {
        this.socialId = socialId;
    }

    public Long getSocialId()
    {
        return socialId;
    }

    public void setUserId(Long userId)
    {
        this.userId = userId;
    }

    public Long getUserId()
    {
        return userId;
    }

    public void setPlatform(String platform)
    {
        this.platform = platform;
    }

    public String getPlatform()
    {
        return platform;
    }

    public void setOpenId(String openId)
    {
        this.openId = openId;
    }

    public String getOpenId()
    {
        return openId;
    }

    public void setUnionId(String unionId)
    {
        this.unionId = unionId;
    }

    public String getUnionId()
    {
        return unionId;
    }

    public void setAccessToken(String accessToken)
    {
        this.accessToken = accessToken;
    }

    public String getAccessToken()
    {
        return accessToken;
    }

    public void setRefreshToken(String refreshToken)
    {
        this.refreshToken = refreshToken;
    }

    public String getRefreshToken()
    {
        return refreshToken;
    }

    public void setExpiresIn(Long expiresIn)
    {
        this.expiresIn = expiresIn;
    }

    public Long getExpiresIn()
    {
        return expiresIn;
    }

    public void setNickName(String nickName)
    {
        this.nickName = nickName;
    }

    public String getNickName()
    {
        return nickName;
    }

    public void setAvatar(String avatar)
    {
        this.avatar = avatar;
    }

    public String getAvatar()
    {
        return avatar;
    }

    public void setRawInfo(String rawInfo)
    {
        this.rawInfo = rawInfo;
    }

    public String getRawInfo()
    {
        return rawInfo;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("socialId", getSocialId())
            .append("userId", getUserId())
            .append("platform", getPlatform())
            .append("openId", getOpenId())
            .append("unionId", getUnionId())
            .append("accessToken", getAccessToken())
            .append("refreshToken", getRefreshToken())
            .append("expiresIn", getExpiresIn())
            .append("nickName", getNickName())
            .append("avatar", getAvatar())
            .append("rawInfo", getRawInfo())
            .append("createTime", getCreateTime())
            .toString();
    }
}
