package com.ruoyiblog.service.impl;

import java.util.List;
import com.ruoyiblog.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyiblog.mapper.BlogCategoryMapper;
import com.ruoyiblog.domain.BlogCategory;
import com.ruoyiblog.service.IBlogCategoryService;

/**
 * 文章分类Service业务层处理
 *
 * @author ruoyi
 * @date 2026-06-20
 */
@Service
public class BlogCategoryServiceImpl implements IBlogCategoryService
{
    @Autowired
    private BlogCategoryMapper blogCategoryMapper;

    /**
     * 查询文章分类
     *
     * @param categoryId 文章分类主键
     * @return 文章分类
     */
    @Override
    public BlogCategory selectBlogCategoryByCategoryId(Long categoryId)
    {
        return blogCategoryMapper.selectBlogCategoryByCategoryId(categoryId);
    }

    /**
     * 查询文章分类列表
     *
     * @param blogCategory 文章分类
     * @return 文章分类
     */
    @Override
    public List<BlogCategory> selectBlogCategoryList(BlogCategory blogCategory)
    {
        return blogCategoryMapper.selectBlogCategoryList(blogCategory);
    }

    /**
     * 新增文章分类
     *
     * @param blogCategory 文章分类
     * @return 结果
     */
    @Override
    public int insertBlogCategory(BlogCategory blogCategory)
    {
        blogCategory.setCreateTime(DateUtils.getNowDate());
        return blogCategoryMapper.insertBlogCategory(blogCategory);
    }

    /**
     * 修改文章分类
     *
     * @param blogCategory 文章分类
     * @return 结果
     */
    @Override
    public int updateBlogCategory(BlogCategory blogCategory)
    {
        blogCategory.setUpdateTime(DateUtils.getNowDate());
        return blogCategoryMapper.updateBlogCategory(blogCategory);
    }

    /**
     * 批量删除文章分类
     *
     * @param categoryIds 需要删除的文章分类主键
     * @return 结果
     */
    @Override
    public int deleteBlogCategoryByCategoryIds(Long[] categoryIds)
    {
        return blogCategoryMapper.deleteBlogCategoryByCategoryIds(categoryIds);
    }

    /**
     * 删除文章分类信息
     *
     * @param categoryId 文章分类主键
     * @return 结果
     */
    @Override
    public int deleteBlogCategoryByCategoryId(Long categoryId)
    {
        return blogCategoryMapper.deleteBlogCategoryByCategoryId(categoryId);
    }
}
