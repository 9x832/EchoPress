package com.ruoyiblog.controller.admin;

import java.util.List;
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
import com.ruoyiblog.domain.BlogArticle;
import com.ruoyiblog.service.IBlogArticleService;
import com.ruoyiblog.common.core.page.TableDataInfo;
import com.ruoyiblog.interceptor.AdminRequired;

/**
 * 文章Controller
 *
 * @author ruoyi
 * @date 2026-06-20
 */
@RestController
@RequestMapping("/blog/admin/article")
@AdminRequired
public class BlogArticleAdminController extends BaseController
{
    @Autowired
    private IBlogArticleService blogArticleService;

    /**
     * 查询文章列表
     */
    @GetMapping("/list")
    public TableDataInfo list(BlogArticle blogArticle)
    {
        startPage();
        List<BlogArticle> list = blogArticleService.selectBlogArticleList(blogArticle);
        return getDataTable(list);
    }

    /**
     * 获取文章详细信息
     */
    @GetMapping(value = "/{articleId}")
    public AjaxResult getInfo(@PathVariable("articleId") Long articleId)
    {
        return success(blogArticleService.selectBlogArticleByArticleId(articleId));
    }

    /**
     * 新增文章
     */
    @PostMapping
    public AjaxResult add(@RequestBody BlogArticle blogArticle)
    {
        return toAjax(blogArticleService.insertBlogArticle(blogArticle));
    }

    /**
     * 修改文章
     */
    @PutMapping
    public AjaxResult edit(@RequestBody BlogArticle blogArticle)
    {
        return toAjax(blogArticleService.updateBlogArticle(blogArticle));
    }

    /**
     * 删除文章
     */
    @DeleteMapping("/{articleIds}")
    public AjaxResult remove(@PathVariable Long[] articleIds)
    {
        return toAjax(blogArticleService.deleteBlogArticleByArticleIds(articleIds));
    }
}
