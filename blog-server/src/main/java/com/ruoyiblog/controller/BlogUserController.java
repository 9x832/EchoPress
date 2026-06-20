package com.ruoyiblog.controller;

import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyiblog.domain.BlogUser;
import com.ruoyiblog.domain.dto.LoginDTO;
import com.ruoyiblog.domain.dto.RegisterDTO;
import com.ruoyiblog.service.IBlogUserService;
import com.ruoyiblog.common.core.AjaxResult;

/**
 * 博客前台用户Controller
 *
 * @author ruoyi
 * @date 2026-06-20
 */
@RestController
@RequestMapping("/api/blog/user")
public class BlogUserController
{
    @Autowired
    private IBlogUserService blogUserService;

    /**
     * 用户注册
     */
    @PostMapping("/register")
    public AjaxResult register(@RequestBody RegisterDTO registerDTO)
    {
        Long userId = blogUserService.register(registerDTO);
        Map<String, Object> data = new HashMap<>();
        data.put("userId", userId);
        return AjaxResult.success(data);
    }

    /**
     * 用户登录
     */
    @PostMapping("/login")
    public AjaxResult login(@RequestBody LoginDTO loginDTO)
    {
        String token = blogUserService.login(loginDTO);
        Map<String, Object> data = new HashMap<>();
        data.put("token", token);
        return AjaxResult.success(data);
    }

    /**
     * 获取用户信息
     */
    @GetMapping("/info/{userId}")
    public AjaxResult getInfo(@PathVariable("userId") Long userId)
    {
        BlogUser user = blogUserService.selectBlogUserByUserId(userId);
        if (user == null)
        {
            return AjaxResult.error("用户不存在");
        }
        Map<String, Object> data = new HashMap<>();
        data.put("userId", user.getUserId());
        data.put("nickName", user.getNickName());
        data.put("avatar", user.getAvatar());
        data.put("bio", user.getBio());
        data.put("website", user.getWebsite());
        data.put("articleCount", user.getArticleCount());
        data.put("createTime", user.getCreateTime());
        return AjaxResult.success(data);
    }
}
