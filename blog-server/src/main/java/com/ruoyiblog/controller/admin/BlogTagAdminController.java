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
import com.ruoyiblog.domain.BlogTag;
import com.ruoyiblog.service.IBlogTagService;
import com.ruoyiblog.common.core.page.TableDataInfo;
import com.ruoyiblog.interceptor.AdminRequired;

/**
 * 文章标签Controller
 *
 * @author ruoyi
 * @date 2026-06-20
 */
@RestController
@RequestMapping("/blog/admin/tag")
@AdminRequired
public class BlogTagAdminController extends BaseController
{
    @Autowired
    private IBlogTagService blogTagService;

    /**
     * 查询文章标签列表
     */
    @GetMapping("/list")
    public TableDataInfo list(BlogTag blogTag)
    {
        startPage();
        List<BlogTag> list = blogTagService.selectBlogTagList(blogTag);
        return getDataTable(list);
    }

    /**
     * 获取文章标签详细信息
     */
    @GetMapping(value = "/{tagId}")
    public AjaxResult getInfo(@PathVariable("tagId") Long tagId)
    {
        return success(blogTagService.selectBlogTagByTagId(tagId));
    }

    /**
     * 新增文章标签
     */
    @PostMapping
    public AjaxResult add(@RequestBody BlogTag blogTag)
    {
        return toAjax(blogTagService.insertBlogTag(blogTag));
    }

    /**
     * 修改文章标签
     */
    @PutMapping
    public AjaxResult edit(@RequestBody BlogTag blogTag)
    {
        return toAjax(blogTagService.updateBlogTag(blogTag));
    }

    /**
     * 删除文章标签
     */
    @DeleteMapping("/{tagIds}")
    public AjaxResult remove(@PathVariable Long[] tagIds)
    {
        return toAjax(blogTagService.deleteBlogTagByTagIds(tagIds));
    }
}
