package com.ruoyiblog.controller;

import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyiblog.common.core.AjaxResult;
import com.ruoyiblog.common.utils.SecurityUtils;
import com.ruoyiblog.interceptor.LoginRequired;
import com.ruoyiblog.service.IBlogUserLikeService;

@RestController
@RequestMapping("/blog/user/article")
@LoginRequired
public class BlogArticleLikeController
{
    @Autowired
    private IBlogUserLikeService blogUserLikeService;

    @PostMapping("/like/{articleId}")
    public AjaxResult like(@PathVariable("articleId") Long articleId)
    {
        Long userId = SecurityUtils.getUserId();
        Map<String, Object> result = blogUserLikeService.toggleLike(userId, articleId);
        return AjaxResult.success(result);
    }

    @GetMapping("/liked-status")
    public AjaxResult likedStatus(@RequestParam("ids") Long[] ids)
    {
        Long userId = SecurityUtils.getUserId();
        Map<Long, Boolean> result = blogUserLikeService.getLikedArticleIds(userId, ids);
        return AjaxResult.success(result);
    }
}
