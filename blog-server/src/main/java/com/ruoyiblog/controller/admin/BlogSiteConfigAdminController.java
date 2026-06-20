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
import com.ruoyiblog.domain.BlogSiteConfig;
import com.ruoyiblog.service.IBlogSiteConfigService;
import com.ruoyiblog.common.core.page.TableDataInfo;
import com.ruoyiblog.interceptor.AdminRequired;

/**
 * 站点配置Controller
 *
 * @author ruoyi
 * @date 2026-06-20
 */
@RestController
@RequestMapping("/blog/admin/config")
@AdminRequired
public class BlogSiteConfigAdminController extends BaseController
{
    @Autowired
    private IBlogSiteConfigService blogSiteConfigService;

    /**
     * 查询站点配置列表
     */
    @GetMapping("/list")
    public TableDataInfo list(BlogSiteConfig blogSiteConfig)
    {
        startPage();
        List<BlogSiteConfig> list = blogSiteConfigService.selectBlogSiteConfigList(blogSiteConfig);
        return getDataTable(list);
    }

    /**
     * 获取站点配置详细信息
     */
    @GetMapping(value = "/{configId}")
    public AjaxResult getInfo(@PathVariable("configId") Long configId)
    {
        return success(blogSiteConfigService.selectBlogSiteConfigByConfigId(configId));
    }

    /**
     * 新增站点配置
     */
    @PostMapping
    public AjaxResult add(@RequestBody BlogSiteConfig blogSiteConfig)
    {
        return toAjax(blogSiteConfigService.insertBlogSiteConfig(blogSiteConfig));
    }

    /**
     * 修改站点配置
     */
    @PutMapping
    public AjaxResult edit(@RequestBody BlogSiteConfig blogSiteConfig)
    {
        return toAjax(blogSiteConfigService.updateBlogSiteConfig(blogSiteConfig));
    }

    /**
     * 删除站点配置
     */
    @DeleteMapping("/{configIds}")
    public AjaxResult remove(@PathVariable Long[] configIds)
    {
        return toAjax(blogSiteConfigService.deleteBlogSiteConfigByConfigIds(configIds));
    }
}
