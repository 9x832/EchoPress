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
import com.ruoyiblog.domain.BlogMoment;
import com.ruoyiblog.service.IBlogMomentService;
import com.ruoyiblog.common.core.page.TableDataInfo;
import com.ruoyiblog.interceptor.AdminRequired;

/**
 * 动态/说说Controller
 *
 * @author ruoyi
 * @date 2026-06-20
 */
@RestController
@RequestMapping("/blog/admin/moment")
@AdminRequired
public class BlogMomentAdminController extends BaseController
{
    @Autowired
    private IBlogMomentService blogMomentService;

    /**
     * 查询动态/说说列表
     */
    @GetMapping("/list")
    public TableDataInfo list(BlogMoment blogMoment)
    {
        startPage();
        List<BlogMoment> list = blogMomentService.selectBlogMomentList(blogMoment);
        return getDataTable(list);
    }

    /**
     * 获取动态/说说详细信息
     */
    @GetMapping(value = "/{momentId}")
    public AjaxResult getInfo(@PathVariable("momentId") Long momentId)
    {
        return success(blogMomentService.selectBlogMomentByMomentId(momentId));
    }

    /**
     * 新增动态/说说
     */
    @PostMapping
    public AjaxResult add(@RequestBody BlogMoment blogMoment)
    {
        return toAjax(blogMomentService.insertBlogMoment(blogMoment));
    }

    /**
     * 修改动态/说说
     */
    @PutMapping
    public AjaxResult edit(@RequestBody BlogMoment blogMoment)
    {
        return toAjax(blogMomentService.updateBlogMoment(blogMoment));
    }

    /**
     * 删除动态/说说
     */
    @DeleteMapping("/{momentIds}")
    public AjaxResult remove(@PathVariable Long[] momentIds)
    {
        return toAjax(blogMomentService.deleteBlogMomentByMomentIds(momentIds));
    }
}
