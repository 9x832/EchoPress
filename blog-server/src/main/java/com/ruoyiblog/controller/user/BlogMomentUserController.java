package com.ruoyiblog.controller.user;

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
import com.ruoyiblog.common.core.page.TableDataInfo;
import com.ruoyiblog.common.utils.SecurityUtils;
import com.ruoyiblog.domain.BlogMoment;
import com.ruoyiblog.interceptor.LoginRequired;
import com.ruoyiblog.service.IBlogMomentService;

/**
 * 用户中心 - 动态管理
 *
 * @author ruoyi
 * @date 2026-06-21
 */
@RestController
@RequestMapping("/blog/user/moment")
@LoginRequired
public class BlogMomentUserController extends BaseController
{
    @Autowired
    private IBlogMomentService blogMomentService;

    /**
     * 查询我的动态列表
     */
    @GetMapping("/list")
    public TableDataInfo list(BlogMoment blogMoment)
    {
        startPage();
        blogMoment.setUserId(SecurityUtils.getUserId());
        blogMoment.setDelFlag("0");
        List<BlogMoment> list = blogMomentService.selectBlogMomentList(blogMoment);
        return getDataTable(list);
    }

    /**
     * 获取我的动态详情
     */
    @GetMapping("/{momentId}")
    public AjaxResult getInfo(@PathVariable("momentId") Long momentId)
    {
        BlogMoment moment = blogMomentService.selectBlogMomentByMomentId(momentId);
        if (moment == null || !SecurityUtils.getUserId().equals(moment.getUserId()))
        {
            return AjaxResult.error("动态不存在");
        }
        return AjaxResult.success(moment);
    }

    /**
     * 新增动态
     */
    @PostMapping
    public AjaxResult add(@RequestBody BlogMoment blogMoment)
    {
        blogMoment.setUserId(SecurityUtils.getUserId());
        if (blogMoment.getStatus() == null)
        {
            blogMoment.setStatus("0");
        }
        return toAjax(blogMomentService.insertBlogMoment(blogMoment));
    }

    /**
     * 修改动态
     */
    @PutMapping
    public AjaxResult edit(@RequestBody BlogMoment blogMoment)
    {
        BlogMoment existing = blogMomentService.selectBlogMomentByMomentId(blogMoment.getMomentId());
        if (existing == null || !SecurityUtils.getUserId().equals(existing.getUserId()))
        {
            return AjaxResult.error("动态不存在");
        }
        blogMoment.setUserId(existing.getUserId());
        return toAjax(blogMomentService.updateBlogMoment(blogMoment));
    }

    /**
     * 删除动态
     */
    @DeleteMapping("/{momentIds}")
    public AjaxResult remove(@PathVariable Long[] momentIds)
    {
        for (Long id : momentIds)
        {
            BlogMoment moment = blogMomentService.selectBlogMomentByMomentId(id);
            if (moment == null || !SecurityUtils.getUserId().equals(moment.getUserId()))
            {
                return AjaxResult.error("动态不存在，无法删除");
            }
        }
        return toAjax(blogMomentService.deleteBlogMomentByMomentIds(momentIds));
    }
}
