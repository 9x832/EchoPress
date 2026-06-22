package com.ruoyiblog.common.utils;

import com.ruoyiblog.domain.vo.BlogLoginUser;

public class SecurityUtils
{
    private static final ThreadLocal<BlogLoginUser> LOGIN_USER_HOLDER = new ThreadLocal<>();

    public static void setLoginUser(BlogLoginUser loginUser)
    {
        LOGIN_USER_HOLDER.set(loginUser);
    }

    public static BlogLoginUser getLoginUser()
    {
        return LOGIN_USER_HOLDER.get();
    }

    public static Long getUserId()
    {
        BlogLoginUser user = getLoginUser();
        return user != null ? user.getUserId() : null;
    }

    public static void remove()
    {
        LOGIN_USER_HOLDER.remove();
    }
}
