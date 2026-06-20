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
import com.ruoyiblog.domain.BlogLink;
import com.ruoyiblog.service.IBlogLinkService;
import com.ruoyiblog.common.core.page.TableDataInfo;
import com.ruoyiblog.interceptor.AdminRequired;

/**
 * 友情链接Controller
 *
 * @author ruoyi
 * @date 2026-06-20
 */
@RestController
@RequestMapping("/blog/admin/link")
@AdminRequired
public class BlogLinkAdminController extends BaseController
{
    @Autowired
    private IBlogLinkService blogLinkService;

    /**
     * 查询友情链接列表
     */
    @GetMapping("/list")
    public TableDataInfo list(BlogLink blogLink)
    {
        startPage();
        List<BlogLink> list = blogLinkService.selectBlogLinkList(blogLink);
        return getDataTable(list);
    }

    /**
     * 获取友情链接详细信息
     */
    @GetMapping(value = "/{linkId}")
    public AjaxResult getInfo(@PathVariable("linkId") Long linkId)
    {
        return success(blogLinkService.selectBlogLinkByLinkId(linkId));
    }

    /**
     * 新增友情链接
     */
    @PostMapping
    public AjaxResult add(@RequestBody BlogLink blogLink)
    {
        return toAjax(blogLinkService.insertBlogLink(blogLink));
    }

    /**
     * 修改友情链接
     */
    @PutMapping
    public AjaxResult edit(@RequestBody BlogLink blogLink)
    {
        return toAjax(blogLinkService.updateBlogLink(blogLink));
    }

    /**
     * 删除友情链接
     */
    @DeleteMapping("/{linkIds}")
    public AjaxResult remove(@PathVariable Long[] linkIds)
    {
        return toAjax(blogLinkService.deleteBlogLinkByLinkIds(linkIds));
    }
}
