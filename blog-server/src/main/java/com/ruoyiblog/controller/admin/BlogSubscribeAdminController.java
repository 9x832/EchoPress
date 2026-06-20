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
import com.ruoyiblog.domain.BlogSubscribe;
import com.ruoyiblog.service.IBlogSubscribeService;
import com.ruoyiblog.common.core.page.TableDataInfo;
import com.ruoyiblog.interceptor.AdminRequired;

/**
 * 邮件订阅Controller
 *
 * @author ruoyi
 * @date 2026-06-20
 */
@RestController
@RequestMapping("/blog/admin/subscribe")
@AdminRequired
public class BlogSubscribeAdminController extends BaseController
{
    @Autowired
    private IBlogSubscribeService blogSubscribeService;

    /**
     * 查询邮件订阅列表
     */
    @GetMapping("/list")
    public TableDataInfo list(BlogSubscribe blogSubscribe)
    {
        startPage();
        List<BlogSubscribe> list = blogSubscribeService.selectBlogSubscribeList(blogSubscribe);
        return getDataTable(list);
    }

    /**
     * 获取邮件订阅详细信息
     */
    @GetMapping(value = "/{subscribeId}")
    public AjaxResult getInfo(@PathVariable("subscribeId") Long subscribeId)
    {
        return success(blogSubscribeService.selectBlogSubscribeBySubscribeId(subscribeId));
    }

    /**
     * 新增邮件订阅
     */
    @PostMapping
    public AjaxResult add(@RequestBody BlogSubscribe blogSubscribe)
    {
        return toAjax(blogSubscribeService.insertBlogSubscribe(blogSubscribe));
    }

    /**
     * 修改邮件订阅
     */
    @PutMapping
    public AjaxResult edit(@RequestBody BlogSubscribe blogSubscribe)
    {
        return toAjax(blogSubscribeService.updateBlogSubscribe(blogSubscribe));
    }

    /**
     * 删除邮件订阅
     */
    @DeleteMapping("/{subscribeIds}")
    public AjaxResult remove(@PathVariable Long[] subscribeIds)
    {
        return toAjax(blogSubscribeService.deleteBlogSubscribeBySubscribeIds(subscribeIds));
    }
}
