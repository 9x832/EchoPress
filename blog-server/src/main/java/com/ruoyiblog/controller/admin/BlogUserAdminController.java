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
import com.ruoyiblog.domain.BlogUser;
import com.ruoyiblog.service.IBlogUserService;
import com.ruoyiblog.common.core.page.TableDataInfo;
import com.ruoyiblog.interceptor.AdminRequired;

/**
 * 博客用户Controller
 *
 * @author ruoyi
 * @date 2026-06-20
 */
@RestController
@RequestMapping("/blog/admin/user")
@AdminRequired
public class BlogUserAdminController extends BaseController
{
    @Autowired
    private IBlogUserService blogUserService;

    /**
     * 查询博客用户列表
     */
    @GetMapping("/list")
    public TableDataInfo list(BlogUser blogUser)
    {
        startPage();
        List<BlogUser> list = blogUserService.selectBlogUserList(blogUser);
        return getDataTable(list);
    }

    /**
     * 获取博客用户详细信息
     */
    @GetMapping(value = "/{userId}")
    public AjaxResult getInfo(@PathVariable("userId") Long userId)
    {
        return success(blogUserService.selectBlogUserByUserId(userId));
    }

    /**
     * 新增博客用户
     */
    @PostMapping
    public AjaxResult add(@RequestBody BlogUser blogUser)
    {
        return toAjax(blogUserService.insertBlogUser(blogUser));
    }

    /**
     * 修改博客用户
     */
    @PutMapping
    public AjaxResult edit(@RequestBody BlogUser blogUser)
    {
        return toAjax(blogUserService.updateBlogUser(blogUser));
    }

    /**
     * 删除博客用户
     */
    @DeleteMapping("/{userIds}")
    public AjaxResult remove(@PathVariable Long[] userIds)
    {
        return toAjax(blogUserService.deleteBlogUserByUserIds(userIds));
    }
}
