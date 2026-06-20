package com.ruoyiblog.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyiblog.common.core.domain.BaseEntity;

/**
 * 友情链接对象 blog_link
 *
 * @author ruoyi
 * @date 2026-06-20
 */
public class BlogLink extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 链接ID */
    private Long linkId;

    /** 网站名称 */
    private String name;

    /** 网站地址 */
    private String url;

    /** 网站Logo */
    private String logo;

    /** 网站描述 */
    private String description;

    /** 联系人 */
    private String contact;

    /** 联系邮箱 */
    private String email;

    /** 显示顺序 */
    private Long orderNum;

    /** 状态（0正常 1停用） */
    private String status;

    public void setLinkId(Long linkId)
    {
        this.linkId = linkId;
    }

    public Long getLinkId()
    {
        return linkId;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getName()
    {
        return name;
    }

    public void setUrl(String url)
    {
        this.url = url;
    }

    public String getUrl()
    {
        return url;
    }

    public void setLogo(String logo)
    {
        this.logo = logo;
    }

    public String getLogo()
    {
        return logo;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public String getDescription()
    {
        return description;
    }

    public void setContact(String contact)
    {
        this.contact = contact;
    }

    public String getContact()
    {
        return contact;
    }

    public void setEmail(String email)
    {
        this.email = email;
    }

    public String getEmail()
    {
        return email;
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
            .append("linkId", getLinkId())
            .append("name", getName())
            .append("url", getUrl())
            .append("logo", getLogo())
            .append("description", getDescription())
            .append("contact", getContact())
            .append("email", getEmail())
            .append("orderNum", getOrderNum())
            .append("status", getStatus())
            .append("createTime", getCreateTime())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
