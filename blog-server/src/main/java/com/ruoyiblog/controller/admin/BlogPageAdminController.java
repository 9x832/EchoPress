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
import com.ruoyiblog.domain.BlogPage;
import com.ruoyiblog.service.IBlogPageService;
import com.ruoyiblog.common.core.page.TableDataInfo;
import com.ruoyiblog.interceptor.AdminRequired;

/**
 * 独立页面Controller
 *
 * @author ruoyi
 * @date 2026-06-20
 */
@RestController
@RequestMapping("/blog/admin/page")
@AdminRequired
public class BlogPageAdminController extends BaseController
{
    @Autowired
    private IBlogPageService blogPageService;

    /**
     * 查询独立页面列表
     */
    @GetMapping("/list")
    public TableDataInfo list(BlogPage blogPage)
    {
        startPage();
        List<BlogPage> list = blogPageService.selectBlogPageList(blogPage);
        return getDataTable(list);
    }

    /**
     * 获取独立页面详细信息
     */
    @GetMapping(value = "/{pageId}")
    public AjaxResult getInfo(@PathVariable("pageId") Long pageId)
    {
        return success(blogPageService.selectBlogPageByPageId(pageId));
    }

    /**
     * 新增独立页面
     */
    @PostMapping
    public AjaxResult add(@RequestBody BlogPage blogPage)
    {
        return toAjax(blogPageService.insertBlogPage(blogPage));
    }

    /**
     * 修改独立页面
     */
    @PutMapping
    public AjaxResult edit(@RequestBody BlogPage blogPage)
    {
        return toAjax(blogPageService.updateBlogPage(blogPage));
    }

    /**
     * 删除独立页面
     */
    @DeleteMapping("/{pageIds}")
    public AjaxResult remove(@PathVariable Long[] pageIds)
    {
        return toAjax(blogPageService.deleteBlogPageByPageIds(pageIds));
    }
}
