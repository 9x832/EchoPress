package com.ruoyiblog.mapper;

import java.util.List;
import com.ruoyiblog.domain.BlogArticle;

/**
 * 文章Mapper接口
 *
 * @author ruoyi
 * @date 2026-06-20
 */
public interface BlogArticleMapper
{
    /**
     * 查询文章
     *
     * @param articleId 文章主键
     * @return 文章
     */
    public BlogArticle selectBlogArticleByArticleId(Long articleId);

    /**
     * 查询文章列表
     *
     * @param blogArticle 文章
     * @return 文章集合
     */
    public List<BlogArticle> selectBlogArticleList(BlogArticle blogArticle);

    /**
     * 新增文章
     *
     * @param blogArticle 文章
     * @return 结果
     */
    public int insertBlogArticle(BlogArticle blogArticle);

    /**
     * 修改文章
     *
     * @param blogArticle 文章
     * @return 结果
     */
    public int updateBlogArticle(BlogArticle blogArticle);

    /**
     * 删除文章
     *
     * @param articleId 文章主键
     * @return 结果
     */
    public int deleteBlogArticleByArticleId(Long articleId);

    /**
     * 批量删除文章
     *
     * @param articleIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteBlogArticleByArticleIds(Long[] articleIds);

    /**
     * 原子增加文章点赞数
     *
     * @param articleId 文章主键
     * @return 结果
     */
    public int incrementLikeCount(Long articleId);

    /**
     * 原子减少文章点赞数
     *
     * @param articleId 文章主键
     * @return 结果
     */
    public int decrementLikeCount(Long articleId);
}
