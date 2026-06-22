package com.ruoyiblog.mapper;

import java.util.List;
import com.ruoyiblog.domain.BlogArticleTag;

/**
 * 文章标签关联Mapper接口
 *
 * @author ruoyi
 * @date 2026-06-20
 */
public interface BlogArticleTagMapper
{
    /**
     * 查询文章标签关联
     *
     * @param articleId 文章标签关联主键
     * @return 文章标签关联
     */
    public BlogArticleTag selectBlogArticleTagByArticleId(Long articleId);

    /**
     * 查询文章标签关联列表
     *
     * @param blogArticleTag 文章标签关联
     * @return 文章标签关联集合
     */
    public List<BlogArticleTag> selectBlogArticleTagList(BlogArticleTag blogArticleTag);

    /**
     * 新增文章标签关联
     *
     * @param blogArticleTag 文章标签关联
     * @return 结果
     */
    public int insertBlogArticleTag(BlogArticleTag blogArticleTag);

    /**
     * 修改文章标签关联
     *
     * @param blogArticleTag 文章标签关联
     * @return 结果
     */
    public int updateBlogArticleTag(BlogArticleTag blogArticleTag);

    /**
     * 删除文章标签关联
     *
     * @param articleId 文章标签关联主键
     * @return 结果
     */
    public int deleteBlogArticleTagByArticleId(Long articleId);

    /**
     * 批量删除文章标签关联
     *
     * @param articleIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteBlogArticleTagByArticleIds(Long[] articleIds);

    public int insertBlogArticleTagBatch(List<BlogArticleTag> list);

    public List<BlogArticleTag> selectBlogArticleTagByArticleIds(Long[] articleIds);
}
