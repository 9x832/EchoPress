package com.ruoyiblog.mapper;

import java.util.List;
import com.ruoyiblog.domain.BlogTag;

/**
 * 文章标签Mapper接口
 *
 * @author ruoyi
 * @date 2026-06-20
 */
public interface BlogTagMapper
{
    /**
     * 查询文章标签
     *
     * @param tagId 文章标签主键
     * @return 文章标签
     */
    public BlogTag selectBlogTagByTagId(Long tagId);

    /**
     * 查询文章标签列表
     *
     * @param blogTag 文章标签
     * @return 文章标签集合
     */
    public List<BlogTag> selectBlogTagList(BlogTag blogTag);

    /**
     * 新增文章标签
     *
     * @param blogTag 文章标签
     * @return 结果
     */
    public int insertBlogTag(BlogTag blogTag);

    /**
     * 修改文章标签
     *
     * @param blogTag 文章标签
     * @return 结果
     */
    public int updateBlogTag(BlogTag blogTag);

    /**
     * 删除文章标签
     *
     * @param tagId 文章标签主键
     * @return 结果
     */
    public int deleteBlogTagByTagId(Long tagId);

    /**
     * 批量删除文章标签
     *
     * @param tagIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteBlogTagByTagIds(Long[] tagIds);
}
