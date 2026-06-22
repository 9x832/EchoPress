package com.ruoyiblog.controller.user;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyiblog.common.core.BaseController;
import com.ruoyiblog.common.core.AjaxResult;
import com.ruoyiblog.common.core.page.TableDataInfo;
import com.ruoyiblog.common.utils.SecurityUtils;
import com.ruoyiblog.domain.BlogArticle;
import com.ruoyiblog.domain.BlogArticleTag;
import com.ruoyiblog.interceptor.LoginRequired;
import com.ruoyiblog.service.IBlogArticleService;
import com.ruoyiblog.service.IBlogArticleTagService;

/**
 * 用户中心 - 文章管理
 *
 * @author ruoyi
 * @date 2026-06-21
 */
@RestController
@RequestMapping("/blog/user/article")
@LoginRequired
public class BlogArticleUserController extends BaseController
{
    @Autowired
    private IBlogArticleService blogArticleService;

    @Autowired
    private IBlogArticleTagService blogArticleTagService;

    /**
     * 查询我的文章列表
     */
    @GetMapping("/list")
    public TableDataInfo list(BlogArticle blogArticle)
    {
        startPage();
        blogArticle.setUserId(SecurityUtils.getUserId());
        blogArticle.setDelFlag("0");
        List<BlogArticle> list = blogArticleService.selectBlogArticleList(blogArticle);
        return getDataTable(list);
    }

    /**
     * 获取我的文章详情
     */
    @GetMapping("/{articleId}")
    public AjaxResult getInfo(@PathVariable("articleId") Long articleId)
    {
        BlogArticle article = blogArticleService.selectBlogArticleByArticleId(articleId);
        if (article == null || !SecurityUtils.getUserId().equals(article.getUserId()))
        {
            return AjaxResult.error("文章不存在");
        }
        BlogArticleTag cond = new BlogArticleTag();
        cond.setArticleId(articleId);
        List<BlogArticleTag> tags = blogArticleTagService.selectBlogArticleTagList(cond);
        article.setTagIds(tags.stream().map(BlogArticleTag::getTagId).collect(Collectors.toList()));
        return AjaxResult.success(article);
    }

    /**
     * 新增文章
     */
    @PostMapping
    public AjaxResult add(@RequestBody BlogArticle blogArticle)
    {
        blogArticle.setUserId(SecurityUtils.getUserId());
        if (blogArticle.getStatus() == null)
        {
            blogArticle.setStatus("0");
        }
        int rows = blogArticleService.insertBlogArticle(blogArticle);
        if (rows > 0 && blogArticle.getTagIds() != null)
        {
            blogArticleTagService.saveArticleTags(blogArticle.getArticleId(), blogArticle.getTagIds());
        }
        return toAjax(rows);
    }

    /**
     * 修改文章
     */
    @PutMapping
    public AjaxResult edit(@RequestBody BlogArticle blogArticle)
    {
        BlogArticle existing = blogArticleService.selectBlogArticleByArticleId(blogArticle.getArticleId());
        if (existing == null || !SecurityUtils.getUserId().equals(existing.getUserId()))
        {
            return AjaxResult.error("文章不存在");
        }
        blogArticle.setUserId(existing.getUserId());
        int rows = blogArticleService.updateBlogArticle(blogArticle);
        if (rows > 0 && blogArticle.getTagIds() != null)
        {
            blogArticleTagService.saveArticleTags(blogArticle.getArticleId(), blogArticle.getTagIds());
        }
        return toAjax(rows);
    }

    /**
     * 删除文章
     */
    @DeleteMapping("/{articleIds}")
    public AjaxResult remove(@PathVariable Long[] articleIds)
    {
        for (Long id : articleIds)
        {
            BlogArticle article = blogArticleService.selectBlogArticleByArticleId(id);
            if (article == null || !SecurityUtils.getUserId().equals(article.getUserId()))
            {
                return AjaxResult.error("文章不存在，无法删除");
            }
        }
        return toAjax(blogArticleService.deleteBlogArticleByArticleIds(articleIds));
    }
}
