package com.ruoyiblog.mapper;

import java.util.List;
import com.ruoyiblog.domain.BlogPage;

/**
 * 独立页面Mapper接口
 *
 * @author ruoyi
 * @date 2026-06-20
 */
public interface BlogPageMapper
{
    /**
     * 查询独立页面
     *
     * @param pageId 独立页面主键
     * @return 独立页面
     */
    public BlogPage selectBlogPageByPageId(Long pageId);

    /**
     * 查询独立页面列表
     *
     * @param blogPage 独立页面
     * @return 独立页面集合
     */
    public List<BlogPage> selectBlogPageList(BlogPage blogPage);

    /**
     * 新增独立页面
     *
     * @param blogPage 独立页面
     * @return 结果
     */
    public int insertBlogPage(BlogPage blogPage);

    /**
     * 修改独立页面
     *
     * @param blogPage 独立页面
     * @return 结果
     */
    public int updateBlogPage(BlogPage blogPage);

    /**
     * 删除独立页面
     *
     * @param pageId 独立页面主键
     * @return 结果
     */
    public int deleteBlogPageByPageId(Long pageId);

    /**
     * 批量删除独立页面
     *
     * @param pageIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteBlogPageByPageIds(Long[] pageIds);
}
