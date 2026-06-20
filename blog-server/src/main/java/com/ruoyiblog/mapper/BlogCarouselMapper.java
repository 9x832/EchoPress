package com.ruoyiblog.mapper;

import java.util.List;
import com.ruoyiblog.domain.BlogCarousel;

/**
 * 首页轮播图Mapper接口
 *
 * @author ruoyi
 * @date 2026-06-20
 */
public interface BlogCarouselMapper
{
    /**
     * 查询首页轮播图
     *
     * @param carouselId 首页轮播图主键
     * @return 首页轮播图
     */
    public BlogCarousel selectBlogCarouselByCarouselId(Long carouselId);

    /**
     * 查询首页轮播图列表
     *
     * @param blogCarousel 首页轮播图
     * @return 首页轮播图集合
     */
    public List<BlogCarousel> selectBlogCarouselList(BlogCarousel blogCarousel);

    /**
     * 新增首页轮播图
     *
     * @param blogCarousel 首页轮播图
     * @return 结果
     */
    public int insertBlogCarousel(BlogCarousel blogCarousel);

    /**
     * 修改首页轮播图
     *
     * @param blogCarousel 首页轮播图
     * @return 结果
     */
    public int updateBlogCarousel(BlogCarousel blogCarousel);

    /**
     * 删除首页轮播图
     *
     * @param carouselId 首页轮播图主键
     * @return 结果
     */
    public int deleteBlogCarouselByCarouselId(Long carouselId);

    /**
     * 批量删除首页轮播图
     *
     * @param carouselIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteBlogCarouselByCarouselIds(Long[] carouselIds);
}
