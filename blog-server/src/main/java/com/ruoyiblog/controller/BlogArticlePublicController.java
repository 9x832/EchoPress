package com.ruoyiblog.controller;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyiblog.common.core.AjaxResult;
import com.ruoyiblog.common.core.BaseController;
import com.ruoyiblog.common.core.page.TableDataInfo;
import com.ruoyiblog.domain.BlogArticle;
import com.ruoyiblog.domain.BlogArticleTag;
import com.ruoyiblog.service.IBlogArticleService;
import com.ruoyiblog.service.IBlogArticleTagService;

/**
 * 博客前台 - 文章公开接口
 */
@RestController
@RequestMapping("/blog/article")
public class BlogArticlePublicController extends BaseController
{
    @Autowired
    private IBlogArticleService blogArticleService;

    @Autowired
    private IBlogArticleTagService blogArticleTagService;

    @GetMapping("/list")
    public TableDataInfo list(BlogArticle blogArticle)
    {
        startPage();
        blogArticle.setStatus("1");
        blogArticle.setDelFlag("0");
        List<BlogArticle> list = blogArticleService.selectBlogArticleList(blogArticle);
        if (!list.isEmpty())
        {
            Long[] articleIds = list.stream().map(BlogArticle::getArticleId).toArray(Long[]::new);
            List<BlogArticleTag> allTags = blogArticleTagService.selectBlogArticleTagByArticleIds(articleIds);
            Map<Long, List<Long>> tagMap = allTags.stream().collect(Collectors.groupingBy(
                    BlogArticleTag::getArticleId,
                    Collectors.mapping(BlogArticleTag::getTagId, Collectors.toList())
            ));
            for (BlogArticle a : list)
            {
                a.setTagIds(tagMap.get(a.getArticleId()));
            }
        }
        return getDataTable(list);
    }

    @GetMapping("/{articleId}")
    public AjaxResult getInfo(@PathVariable("articleId") Long articleId)
    {
        BlogArticle article = blogArticleService.selectBlogArticleByArticleId(articleId);
        if (article == null || !"1".equals(article.getStatus()))
        {
            return AjaxResult.error("文章不存在");
        }
        BlogArticle update = new BlogArticle();
        update.setArticleId(articleId);
        update.setViewCount((article.getViewCount() == null ? 0 : article.getViewCount()) + 1);
        blogArticleService.updateBlogArticle(update);
        article.setViewCount(article.getViewCount() == null ? 1 : article.getViewCount() + 1);

        BlogArticleTag cond = new BlogArticleTag();
        cond.setArticleId(articleId);
        List<BlogArticleTag> tags = blogArticleTagService.selectBlogArticleTagList(cond);
        article.setTagIds(tags.stream().map(BlogArticleTag::getTagId).collect(Collectors.toList()));

        return AjaxResult.success(article);
    }
}
