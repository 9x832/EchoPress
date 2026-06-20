package com.ruoyiblog.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyiblog.mapper.BlogArticleViewMapper;
import com.ruoyiblog.domain.BlogArticleView;
import com.ruoyiblog.service.IBlogArticleViewService;

/**
 * 文章浏览记录Service业务层处理
 *
 * @author ruoyi
 * @date 2026-06-20
 */
@Service
public class BlogArticleViewServiceImpl implements IBlogArticleViewService
{
    @Autowired
    private BlogArticleViewMapper blogArticleViewMapper;

    /**
     * 查询文章浏览记录
     *
     * @param viewId 文章浏览记录主键
     * @return 文章浏览记录
     */
    @Override
    public BlogArticleView selectBlogArticleViewByViewId(Long viewId)
    {
        return blogArticleViewMapper.selectBlogArticleViewByViewId(viewId);
    }

    /**
     * 查询文章浏览记录列表
     *
     * @param blogArticleView 文章浏览记录
     * @return 文章浏览记录
     */
    @Override
    public List<BlogArticleView> selectBlogArticleViewList(BlogArticleView blogArticleView)
    {
        return blogArticleViewMapper.selectBlogArticleViewList(blogArticleView);
    }

    /**
     * 新增文章浏览记录
     *
     * @param blogArticleView 文章浏览记录
     * @return 结果
     */
    @Override
    public int insertBlogArticleView(BlogArticleView blogArticleView)
    {
        return blogArticleViewMapper.insertBlogArticleView(blogArticleView);
    }

    /**
     * 修改文章浏览记录
     *
     * @param blogArticleView 文章浏览记录
     * @return 结果
     */
    @Override
    public int updateBlogArticleView(BlogArticleView blogArticleView)
    {
        return blogArticleViewMapper.updateBlogArticleView(blogArticleView);
    }

    /**
     * 批量删除文章浏览记录
     *
     * @param viewIds 需要删除的文章浏览记录主键
     * @return 结果
     */
    @Override
    public int deleteBlogArticleViewByViewIds(Long[] viewIds)
    {
        return blogArticleViewMapper.deleteBlogArticleViewByViewIds(viewIds);
    }

    /**
     * 删除文章浏览记录信息
     *
     * @param viewId 文章浏览记录主键
     * @return 结果
     */
    @Override
    public int deleteBlogArticleViewByViewId(Long viewId)
    {
        return blogArticleViewMapper.deleteBlogArticleViewByViewId(viewId);
    }
}
