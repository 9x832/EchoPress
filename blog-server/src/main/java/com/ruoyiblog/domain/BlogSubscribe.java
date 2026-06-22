package com.ruoyiblog.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyiblog.common.core.domain.BaseEntity;

/**
 * 邮件订阅对象 blog_subscribe
 *
 * @author ruoyi
 * @date 2026-06-20
 */
public class BlogSubscribe extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 订阅ID */
    private Long subscribeId;

    /** 订阅作者用户ID */
    private Long userId;

    /** 订阅邮箱 */
    private String email;

    /** 状态（0正常 1停用） */
    private String status;

    /** 验证码 */
    private String verifyCode;

    /** 是否已验证（0否 1是） */
    private String verified;

    /** 订阅时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date subscribeTime;

    public void setSubscribeId(Long subscribeId)
    {
        this.subscribeId = subscribeId;
    }

    public Long getSubscribeId()
    {
        return subscribeId;
    }

    public void setUserId(Long userId)
    {
        this.userId = userId;
    }

    public Long getUserId()
    {
        return userId;
    }

    public void setEmail(String email)
    {
        this.email = email;
    }

    public String getEmail()
    {
        return email;
    }

    public void setStatus(String status)
    {
        this.status = status;
    }

    public String getStatus()
    {
        return status;
    }

    public void setVerifyCode(String verifyCode)
    {
        this.verifyCode = verifyCode;
    }

    public String getVerifyCode()
    {
        return verifyCode;
    }

    public void setVerified(String verified)
    {
        this.verified = verified;
    }

    public String getVerified()
    {
        return verified;
    }

    public void setSubscribeTime(Date subscribeTime)
    {
        this.subscribeTime = subscribeTime;
    }

    public Date getSubscribeTime()
    {
        return subscribeTime;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("subscribeId", getSubscribeId())
            .append("userId", getUserId())
            .append("email", getEmail())
            .append("status", getStatus())
            .append("verifyCode", getVerifyCode())
            .append("verified", getVerified())
            .append("subscribeTime", getSubscribeTime())
            .append("createTime", getCreateTime())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
