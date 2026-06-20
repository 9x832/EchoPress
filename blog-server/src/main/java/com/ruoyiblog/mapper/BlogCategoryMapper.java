package com.ruoyiblog.mapper;

import java.util.List;
import com.ruoyiblog.domain.BlogCategory;

/**
 * 文章分类Mapper接口
 *
 * @author ruoyi
 * @date 2026-06-20
 */
public interface BlogCategoryMapper
{
    /**
     * 查询文章分类
     *
     * @param categoryId 文章分类主键
     * @return 文章分类
     */
    public BlogCategory selectBlogCategoryByCategoryId(Long categoryId);

    /**
     * 查询文章分类列表
     *
     * @param blogCategory 文章分类
     * @return 文章分类集合
     */
    public List<BlogCategory> selectBlogCategoryList(BlogCategory blogCategory);

    /**
     * 新增文章分类
     *
     * @param blogCategory 文章分类
     * @return 结果
     */
    public int insertBlogCategory(BlogCategory blogCategory);

    /**
     * 修改文章分类
     *
     * @param blogCategory 文章分类
     * @return 结果
     */
    public int updateBlogCategory(BlogCategory blogCategory);

    /**
     * 删除文章分类
     *
     * @param categoryId 文章分类主键
     * @return 结果
     */
    public int deleteBlogCategoryByCategoryId(Long categoryId);

    /**
     * 批量删除文章分类
     *
     * @param categoryIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteBlogCategoryByCategoryIds(Long[] categoryIds);
}
