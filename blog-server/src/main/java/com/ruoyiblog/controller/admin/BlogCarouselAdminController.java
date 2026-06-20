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
import com.ruoyiblog.domain.BlogCarousel;
import com.ruoyiblog.service.IBlogCarouselService;
import com.ruoyiblog.common.core.page.TableDataInfo;
import com.ruoyiblog.interceptor.AdminRequired;

/**
 * 首页轮播图Controller
 *
 * @author ruoyi
 * @date 2026-06-20
 */
@RestController
@RequestMapping("/blog/admin/carousel")
@AdminRequired
public class BlogCarouselAdminController extends BaseController
{
    @Autowired
    private IBlogCarouselService blogCarouselService;

    /**
     * 查询首页轮播图列表
     */
    @GetMapping("/list")
    public TableDataInfo list(BlogCarousel blogCarousel)
    {
        startPage();
        List<BlogCarousel> list = blogCarouselService.selectBlogCarouselList(blogCarousel);
        return getDataTable(list);
    }

    /**
     * 获取首页轮播图详细信息
     */
    @GetMapping(value = "/{carouselId}")
    public AjaxResult getInfo(@PathVariable("carouselId") Long carouselId)
    {
        return success(blogCarouselService.selectBlogCarouselByCarouselId(carouselId));
    }

    /**
     * 新增首页轮播图
     */
    @PostMapping
    public AjaxResult add(@RequestBody BlogCarousel blogCarousel)
    {
        return toAjax(blogCarouselService.insertBlogCarousel(blogCarousel));
    }

    /**
     * 修改首页轮播图
     */
    @PutMapping
    public AjaxResult edit(@RequestBody BlogCarousel blogCarousel)
    {
        return toAjax(blogCarouselService.updateBlogCarousel(blogCarousel));
    }

    /**
     * 删除首页轮播图
     */
    @DeleteMapping("/{carouselIds}")
    public AjaxResult remove(@PathVariable Long[] carouselIds)
    {
        return toAjax(blogCarouselService.deleteBlogCarouselByCarouselIds(carouselIds));
    }
}
