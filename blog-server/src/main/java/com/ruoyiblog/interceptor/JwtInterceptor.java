package com.ruoyiblog.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import com.alibaba.fastjson2.JSON;
import com.ruoyiblog.common.core.AjaxResult;
import com.ruoyiblog.common.utils.JwtUtils;
import com.ruoyiblog.common.utils.SecurityUtils;
import com.ruoyiblog.domain.vo.BlogLoginUser;

@Component
public class JwtInterceptor implements HandlerInterceptor
{
    @Autowired
    private JwtUtils jwtUtils;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception
    {
        if (!(handler instanceof HandlerMethod))
        {
            return true;
        }

        HandlerMethod handlerMethod = (HandlerMethod) handler;

        AdminRequired adminClass = handlerMethod.getBeanType().getAnnotation(AdminRequired.class);
        AdminRequired adminMethod = handlerMethod.getMethodAnnotation(AdminRequired.class);
        LoginRequired loginClass = handlerMethod.getBeanType().getAnnotation(LoginRequired.class);
        LoginRequired loginMethod = handlerMethod.getMethodAnnotation(LoginRequired.class);

        boolean needAdmin = adminClass != null || adminMethod != null;
        boolean needLogin = needAdmin || loginClass != null || loginMethod != null;

        if (!needLogin)
        {
            return true;
        }

        BlogLoginUser loginUser = jwtUtils.getLoginUser(request);
        if (loginUser == null)
        {
            response.setStatus(401);
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write(JSON.toJSONString(AjaxResult.error("未登录或登录已过期")));
            return false;
        }

        SecurityUtils.setLoginUser(loginUser);

        if (needAdmin && !("01".equals(loginUser.getUserType()) || "02".equals(loginUser.getUserType())))
        {
            response.setStatus(403);
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write(JSON.toJSONString(AjaxResult.error("无权限访问")));
            return false;
        }

        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
    {
        SecurityUtils.remove();
    }
}
