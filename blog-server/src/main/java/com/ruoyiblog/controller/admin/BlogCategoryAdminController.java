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
import com.ruoyiblog.domain.BlogCategory;
import com.ruoyiblog.service.IBlogCategoryService;
import com.ruoyiblog.common.core.page.TableDataInfo;
import com.ruoyiblog.interceptor.AdminRequired;

/**
 * 文章分类Controller
 *
 * @author ruoyi
 * @date 2026-06-20
 */
@RestController
@RequestMapping("/blog/admin/category")
@AdminRequired
public class BlogCategoryAdminController extends BaseController
{
    @Autowired
    private IBlogCategoryService blogCategoryService;

    /**
     * 查询文章分类列表
     */
    @GetMapping("/list")
    public TableDataInfo list(BlogCategory blogCategory)
    {
        startPage();
        List<BlogCategory> list = blogCategoryService.selectBlogCategoryList(blogCategory);
        return getDataTable(list);
    }

    /**
     * 获取文章分类详细信息
     */
    @GetMapping(value = "/{categoryId}")
    public AjaxResult getInfo(@PathVariable("categoryId") Long categoryId)
    {
        return success(blogCategoryService.selectBlogCategoryByCategoryId(categoryId));
    }

    /**
     * 新增文章分类
     */
    @PostMapping
    public AjaxResult add(@RequestBody BlogCategory blogCategory)
    {
        return toAjax(blogCategoryService.insertBlogCategory(blogCategory));
    }

    /**
     * 修改文章分类
     */
    @PutMapping
    public AjaxResult edit(@RequestBody BlogCategory blogCategory)
    {
        return toAjax(blogCategoryService.updateBlogCategory(blogCategory));
    }

    /**
     * 删除文章分类
     */
    @DeleteMapping("/{categoryIds}")
    public AjaxResult remove(@PathVariable Long[] categoryIds)
    {
        return toAjax(blogCategoryService.deleteBlogCategoryByCategoryIds(categoryIds));
    }
}
