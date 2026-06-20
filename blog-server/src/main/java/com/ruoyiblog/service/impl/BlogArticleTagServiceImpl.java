package com.ruoyiblog.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyiblog.mapper.BlogArticleTagMapper;
import com.ruoyiblog.domain.BlogArticleTag;
import com.ruoyiblog.service.IBlogArticleTagService;

/**
 * 文章标签关联Service业务层处理
 *
 * @author ruoyi
 * @date 2026-06-20
 */
@Service
public class BlogArticleTagServiceImpl implements IBlogArticleTagService
{
    @Autowired
    private BlogArticleTagMapper blogArticleTagMapper;

    /**
     * 查询文章标签关联
     *
     * @param articleId 文章标签关联主键
     * @return 文章标签关联
     */
    @Override
    public BlogArticleTag selectBlogArticleTagByArticleId(Long articleId)
    {
        return blogArticleTagMapper.selectBlogArticleTagByArticleId(articleId);
    }

    /**
     * 查询文章标签关联列表
     *
     * @param blogArticleTag 文章标签关联
     * @return 文章标签关联
     */
    @Override
    public List<BlogArticleTag> selectBlogArticleTagList(BlogArticleTag blogArticleTag)
    {
        return blogArticleTagMapper.selectBlogArticleTagList(blogArticleTag);
    }

    /**
     * 新增文章标签关联
     *
     * @param blogArticleTag 文章标签关联
     * @return 结果
     */
    @Override
    public int insertBlogArticleTag(BlogArticleTag blogArticleTag)
    {
        return blogArticleTagMapper.insertBlogArticleTag(blogArticleTag);
    }

    /**
     * 修改文章标签关联
     *
     * @param blogArticleTag 文章标签关联
     * @return 结果
     */
    @Override
    public int updateBlogArticleTag(BlogArticleTag blogArticleTag)
    {
        return blogArticleTagMapper.updateBlogArticleTag(blogArticleTag);
    }

    /**
     * 批量删除文章标签关联
     *
     * @param articleIds 需要删除的文章标签关联主键
     * @return 结果
     */
    @Override
    public int deleteBlogArticleTagByArticleIds(Long[] articleIds)
    {
        return blogArticleTagMapper.deleteBlogArticleTagByArticleIds(articleIds);
    }

    /**
     * 删除文章标签关联信息
     *
     * @param articleId 文章标签关联主键
     * @return 结果
     */
    @Override
    public int deleteBlogArticleTagByArticleId(Long articleId)
    {
        return blogArticleTagMapper.deleteBlogArticleTagByArticleId(articleId);
    }
}
