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
import com.ruoyiblog.domain.BlogResource;
import com.ruoyiblog.service.IBlogResourceService;
import com.ruoyiblog.common.core.page.TableDataInfo;
import com.ruoyiblog.interceptor.AdminRequired;

/**
 * 资源上传记录Controller
 *
 * @author ruoyi
 * @date 2026-06-20
 */
@RestController
@RequestMapping("/blog/admin/resource")
@AdminRequired
public class BlogResourceAdminController extends BaseController
{
    @Autowired
    private IBlogResourceService blogResourceService;

    /**
     * 查询资源上传记录列表
     */
    @GetMapping("/list")
    public TableDataInfo list(BlogResource blogResource)
    {
        startPage();
        List<BlogResource> list = blogResourceService.selectBlogResourceList(blogResource);
        return getDataTable(list);
    }

    /**
     * 获取资源上传记录详细信息
     */
    @GetMapping(value = "/{resourceId}")
    public AjaxResult getInfo(@PathVariable("resourceId") Long resourceId)
    {
        return success(blogResourceService.selectBlogResourceByResourceId(resourceId));
    }

    /**
     * 新增资源上传记录
     */
    @PostMapping
    public AjaxResult add(@RequestBody BlogResource blogResource)
    {
        return toAjax(blogResourceService.insertBlogResource(blogResource));
    }

    /**
     * 修改资源上传记录
     */
    @PutMapping
    public AjaxResult edit(@RequestBody BlogResource blogResource)
    {
        return toAjax(blogResourceService.updateBlogResource(blogResource));
    }

    /**
     * 删除资源上传记录
     */
    @DeleteMapping("/{resourceIds}")
    public AjaxResult remove(@PathVariable Long[] resourceIds)
    {
        return toAjax(blogResourceService.deleteBlogResourceByResourceIds(resourceIds));
    }
}
