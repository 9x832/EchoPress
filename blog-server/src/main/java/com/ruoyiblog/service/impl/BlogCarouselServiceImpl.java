package com.ruoyiblog.service.impl;

import java.util.List;
import com.ruoyiblog.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyiblog.mapper.BlogCarouselMapper;
import com.ruoyiblog.domain.BlogCarousel;
import com.ruoyiblog.service.IBlogCarouselService;

/**
 * 首页轮播图Service业务层处理
 *
 * @author ruoyi
 * @date 2026-06-20
 */
@Service
public class BlogCarouselServiceImpl implements IBlogCarouselService
{
    @Autowired
    private BlogCarouselMapper blogCarouselMapper;

    /**
     * 查询首页轮播图
     *
     * @param carouselId 首页轮播图主键
     * @return 首页轮播图
     */
    @Override
    public BlogCarousel selectBlogCarouselByCarouselId(Long carouselId)
    {
        return blogCarouselMapper.selectBlogCarouselByCarouselId(carouselId);
    }

    /**
     * 查询首页轮播图列表
     *
     * @param blogCarousel 首页轮播图
     * @return 首页轮播图
     */
    @Override
    public List<BlogCarousel> selectBlogCarouselList(BlogCarousel blogCarousel)
    {
        return blogCarouselMapper.selectBlogCarouselList(blogCarousel);
    }

    /**
     * 新增首页轮播图
     *
     * @param blogCarousel 首页轮播图
     * @return 结果
     */
    @Override
    public int insertBlogCarousel(BlogCarousel blogCarousel)
    {
        blogCarousel.setCreateTime(DateUtils.getNowDate());
        return blogCarouselMapper.insertBlogCarousel(blogCarousel);
    }

    /**
     * 修改首页轮播图
     *
     * @param blogCarousel 首页轮播图
     * @return 结果
     */
    @Override
    public int updateBlogCarousel(BlogCarousel blogCarousel)
    {
        blogCarousel.setUpdateTime(DateUtils.getNowDate());
        return blogCarouselMapper.updateBlogCarousel(blogCarousel);
    }

    /**
     * 批量删除首页轮播图
     *
     * @param carouselIds 需要删除的首页轮播图主键
     * @return 结果
     */
    @Override
    public int deleteBlogCarouselByCarouselIds(Long[] carouselIds)
    {
        return blogCarouselMapper.deleteBlogCarouselByCarouselIds(carouselIds);
    }

    /**
     * 删除首页轮播图信息
     *
     * @param carouselId 首页轮播图主键
     * @return 结果
     */
    @Override
    public int deleteBlogCarouselByCarouselId(Long carouselId)
    {
        return blogCarouselMapper.deleteBlogCarouselByCarouselId(carouselId);
    }
}
