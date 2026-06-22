package com.ruoyiblog.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyiblog.common.core.AjaxResult;
import com.ruoyiblog.common.core.BaseController;
import com.ruoyiblog.common.core.page.TableDataInfo;
import com.ruoyiblog.common.utils.IpUtils;
import com.ruoyiblog.common.utils.SecurityUtils;
import com.ruoyiblog.common.utils.StringUtils;
import com.ruoyiblog.domain.BlogComment;
import com.ruoyiblog.domain.vo.BlogLoginUser;
import com.ruoyiblog.interceptor.LoginRequired;
import com.ruoyiblog.service.IBlogCommentService;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/blog/comment")
public class BlogCommentPublicController extends BaseController
{
    @Autowired
    private IBlogCommentService commentService;

    @GetMapping("/list/{articleId}")
    public TableDataInfo listByArticle(@PathVariable("articleId") Long articleId)
    {
        startPage();
        BlogComment cond = new BlogComment();
        cond.setArticleId(articleId);
        cond.setStatus("1");
        List<BlogComment> list = commentService.selectBlogCommentList(cond);
        return getDataTable(list);
    }

    @PostMapping
    @LoginRequired
    public AjaxResult submit(@RequestBody BlogComment comment, HttpServletRequest request)
    {
        if (comment.getArticleId() == null)
        {
            return AjaxResult.error("文章ID不能为空");
        }
        if (StringUtils.isEmpty(comment.getContent()))
        {
            return AjaxResult.error("评论内容不能为空");
        }
        if (comment.getContent().length() > 500)
        {
            return AjaxResult.error("评论内容不能超过500字");
        }

        BlogLoginUser loginUser = SecurityUtils.getLoginUser();
        comment.setUserId(loginUser.getUserId());
        comment.setNickName(loginUser.getNickName());
        comment.setIp(IpUtils.getIpAddr(request));
        comment.setStatus("1");
        comment.setIsTop(0);

        int rows = commentService.insertBlogComment(comment);
        return rows > 0 ? AjaxResult.success() : AjaxResult.error("评论失败");
    }
}
