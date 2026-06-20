package com.ruoyiblog.mapper;

import java.util.List;
import com.ruoyiblog.domain.BlogArticleView;

/**
 * 文章浏览记录Mapper接口
 *
 * @author ruoyi
 * @date 2026-06-20
 */
public interface BlogArticleViewMapper
{
    /**
     * 查询文章浏览记录
     *
     * @param viewId 文章浏览记录主键
     * @return 文章浏览记录
     */
    public BlogArticleView selectBlogArticleViewByViewId(Long viewId);

    /**
     * 查询文章浏览记录列表
     *
     * @param blogArticleView 文章浏览记录
     * @return 文章浏览记录集合
     */
    public List<BlogArticleView> selectBlogArticleViewList(BlogArticleView blogArticleView);

    /**
     * 新增文章浏览记录
     *
     * @param blogArticleView 文章浏览记录
     * @return 结果
     */
    public int insertBlogArticleView(BlogArticleView blogArticleView);

    /**
     * 修改文章浏览记录
     *
     * @param blogArticleView 文章浏览记录
     * @return 结果
     */
    public int updateBlogArticleView(BlogArticleView blogArticleView);

    /**
     * 删除文章浏览记录
     *
     * @param viewId 文章浏览记录主键
     * @return 结果
     */
    public int deleteBlogArticleViewByViewId(Long viewId);

    /**
     * 批量删除文章浏览记录
     *
     * @param viewIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteBlogArticleViewByViewIds(Long[] viewIds);
}
