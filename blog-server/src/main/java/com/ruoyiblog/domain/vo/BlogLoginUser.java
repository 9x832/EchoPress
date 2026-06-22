package com.ruoyiblog.domain.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * 博客前台登录用户身份（Redis缓存主体）
 *
 * @author ruoyi
 * @date 2026-06-20
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class BlogLoginUser
{
    /** 用户ID */
    private Long userId;

    /** 用户名 */
    private String userName;

    /** 昵称 */
    private String nickName;

    /** 用户类型（00普通用户 01博主 02管理员） */
    private String userType;

    /** 用户唯一标识 */
    private String token;

    /** 登录时间 */
    private Long loginTime;

    /** 过期时间 */
    private Long expireTime;

    /** 登录IP */
    private String ipaddr;

    /** 登录地点 */
    private String loginLocation;

    /** 浏览器类型 */
    private String browser;

    /** 操作系统 */
    private String os;

    public BlogLoginUser()
    {
    }

    public BlogLoginUser(Long userId, String userName, String nickName, String userType)
    {
        this.userId = userId;
        this.userName = userName;
        this.nickName = nickName;
        this.userType = userType;
    }

    public Long getUserId()
    {
        return userId;
    }

    public void setUserId(Long userId)
    {
        this.userId = userId;
    }

    public String getUserName()
    {
        return userName;
    }

    public void setUserName(String userName)
    {
        this.userName = userName;
    }

    @JsonIgnore
    public String getPassword()
    {
        return null;
    }

    public String getUsername()
    {
        return userName;
    }

    @JsonIgnore
    public boolean isAccountNonExpired()
    {
        return true;
    }

    @JsonIgnore
    public boolean isAccountNonLocked()
    {
        return true;
    }

    @JsonIgnore
    public boolean isCredentialsNonExpired()
    {
        return true;
    }

    @JsonIgnore
    public boolean isEnabled()
    {
        return true;
    }

    public String getNickName()
    {
        return nickName;
    }

    public void setNickName(String nickName)
    {
        this.nickName = nickName;
    }

    public String getUserType()
    {
        return userType;
    }

    public void setUserType(String userType)
    {
        this.userType = userType;
    }

    public String getToken()
    {
        return token;
    }

    public void setToken(String token)
    {
        this.token = token;
    }

    public Long getLoginTime()
    {
        return loginTime;
    }

    public void setLoginTime(Long loginTime)
    {
        this.loginTime = loginTime;
    }

    public Long getExpireTime()
    {
        return expireTime;
    }

    public void setExpireTime(Long expireTime)
    {
        this.expireTime = expireTime;
    }

    public String getIpaddr()
    {
        return ipaddr;
    }

    public void setIpaddr(String ipaddr)
    {
        this.ipaddr = ipaddr;
    }

    public String getLoginLocation()
    {
        return loginLocation;
    }

    public void setLoginLocation(String loginLocation)
    {
        this.loginLocation = loginLocation;
    }

    public String getBrowser()
    {
        return browser;
    }

    public void setBrowser(String browser)
    {
        this.browser = browser;
    }

    public String getOs()
    {
        return os;
    }

    public void setOs(String os)
    {
        this.os = os;
    }
}
