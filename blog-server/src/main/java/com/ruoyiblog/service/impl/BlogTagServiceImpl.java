package com.ruoyiblog.service.impl;

import java.util.List;
import com.ruoyiblog.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyiblog.mapper.BlogTagMapper;
import com.ruoyiblog.domain.BlogTag;
import com.ruoyiblog.service.IBlogTagService;

/**
 * 文章标签Service业务层处理
 *
 * @author ruoyi
 * @date 2026-06-20
 */
@Service
public class BlogTagServiceImpl implements IBlogTagService
{
    @Autowired
    private BlogTagMapper blogTagMapper;

    /**
     * 查询文章标签
     *
     * @param tagId 文章标签主键
     * @return 文章标签
     */
    @Override
    public BlogTag selectBlogTagByTagId(Long tagId)
    {
        return blogTagMapper.selectBlogTagByTagId(tagId);
    }

    /**
     * 查询文章标签列表
     *
     * @param blogTag 文章标签
     * @return 文章标签
     */
    @Override
    public List<BlogTag> selectBlogTagList(BlogTag blogTag)
    {
        return blogTagMapper.selectBlogTagList(blogTag);
    }

    /**
     * 新增文章标签
     *
     * @param blogTag 文章标签
     * @return 结果
     */
    @Override
    public int insertBlogTag(BlogTag blogTag)
    {
        blogTag.setCreateTime(DateUtils.getNowDate());
        return blogTagMapper.insertBlogTag(blogTag);
    }

    /**
     * 修改文章标签
     *
     * @param blogTag 文章标签
     * @return 结果
     */
    @Override
    public int updateBlogTag(BlogTag blogTag)
    {
        blogTag.setUpdateTime(DateUtils.getNowDate());
        return blogTagMapper.updateBlogTag(blogTag);
    }

    /**
     * 批量删除文章标签
     *
     * @param tagIds 需要删除的文章标签主键
     * @return 结果
     */
    @Override
    public int deleteBlogTagByTagIds(Long[] tagIds)
    {
        return blogTagMapper.deleteBlogTagByTagIds(tagIds);
    }

    /**
     * 删除文章标签信息
     *
     * @param tagId 文章标签主键
     * @return 结果
     */
    @Override
    public int deleteBlogTagByTagId(Long tagId)
    {
        return blogTagMapper.deleteBlogTagByTagId(tagId);
    }
}
