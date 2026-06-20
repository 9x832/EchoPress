package com.ruoyiblog.service.impl;

import java.util.List;
import com.ruoyiblog.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyiblog.mapper.BlogArticleMapper;
import com.ruoyiblog.domain.BlogArticle;
import com.ruoyiblog.service.IBlogArticleService;

/**
 * 文章Service业务层处理
 *
 * @author ruoyi
 * @date 2026-06-20
 */
@Service
public class BlogArticleServiceImpl implements IBlogArticleService
{
    @Autowired
    private BlogArticleMapper blogArticleMapper;

    /**
     * 查询文章
     *
     * @param articleId 文章主键
     * @return 文章
     */
    @Override
    public BlogArticle selectBlogArticleByArticleId(Long articleId)
    {
        return blogArticleMapper.selectBlogArticleByArticleId(articleId);
    }

    /**
     * 查询文章列表
     *
     * @param blogArticle 文章
     * @return 文章
     */
    @Override
    public List<BlogArticle> selectBlogArticleList(BlogArticle blogArticle)
    {
        return blogArticleMapper.selectBlogArticleList(blogArticle);
    }

    /**
     * 新增文章
     *
     * @param blogArticle 文章
     * @return 结果
     */
    @Override
    public int insertBlogArticle(BlogArticle blogArticle)
    {
        blogArticle.setCreateTime(DateUtils.getNowDate());
        return blogArticleMapper.insertBlogArticle(blogArticle);
    }

    /**
     * 修改文章
     *
     * @param blogArticle 文章
     * @return 结果
     */
    @Override
    public int updateBlogArticle(BlogArticle blogArticle)
    {
        blogArticle.setUpdateTime(DateUtils.getNowDate());
        return blogArticleMapper.updateBlogArticle(blogArticle);
    }

    /**
     * 批量删除文章
     *
     * @param articleIds 需要删除的文章主键
     * @return 结果
     */
    @Override
    public int deleteBlogArticleByArticleIds(Long[] articleIds)
    {
        return blogArticleMapper.deleteBlogArticleByArticleIds(articleIds);
    }

    /**
     * 删除文章信息
     *
     * @param articleId 文章主键
     * @return 结果
     */
    @Override
    public int deleteBlogArticleByArticleId(Long articleId)
    {
        return blogArticleMapper.deleteBlogArticleByArticleId(articleId);
    }
}
