package com.ruoyiblog.domain.dto;

/**
 * 博客前台用户登录请求
 *
 * @author ruoyi
 * @date 2026-06-20
 */
public class LoginDTO
{
    /** 账号（用户名或邮箱） */
    private String account;

    /** 密码 */
    private String password;

    /** 验证码uuid */
    private String uuid;

    /** 验证码 */
    private String code;

    public String getAccount()
    {
        return account;
    }

    public void setAccount(String account)
    {
        this.account = account;
    }

    public String getPassword()
    {
        return password;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }

    public String getUuid()
    {
        return uuid;
    }

    public void setUuid(String uuid)
    {
        this.uuid = uuid;
    }

    public String getCode()
    {
        return code;
    }

    public void setCode(String code)
    {
        this.code = code;
    }
}
