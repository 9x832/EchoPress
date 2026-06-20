package com.ruoyiblog.service.impl;

import java.util.List;
import com.ruoyiblog.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyiblog.mapper.BlogPageMapper;
import com.ruoyiblog.domain.BlogPage;
import com.ruoyiblog.service.IBlogPageService;

/**
 * 独立页面Service业务层处理
 *
 * @author ruoyi
 * @date 2026-06-20
 */
@Service
public class BlogPageServiceImpl implements IBlogPageService
{
    @Autowired
    private BlogPageMapper blogPageMapper;

    /**
     * 查询独立页面
     *
     * @param pageId 独立页面主键
     * @return 独立页面
     */
    @Override
    public BlogPage selectBlogPageByPageId(Long pageId)
    {
        return blogPageMapper.selectBlogPageByPageId(pageId);
    }

    /**
     * 查询独立页面列表
     *
     * @param blogPage 独立页面
     * @return 独立页面
     */
    @Override
    public List<BlogPage> selectBlogPageList(BlogPage blogPage)
    {
        return blogPageMapper.selectBlogPageList(blogPage);
    }

    /**
     * 新增独立页面
     *
     * @param blogPage 独立页面
     * @return 结果
     */
    @Override
    public int insertBlogPage(BlogPage blogPage)
    {
        blogPage.setCreateTime(DateUtils.getNowDate());
        return blogPageMapper.insertBlogPage(blogPage);
    }

    /**
     * 修改独立页面
     *
     * @param blogPage 独立页面
     * @return 结果
     */
    @Override
    public int updateBlogPage(BlogPage blogPage)
    {
        blogPage.setUpdateTime(DateUtils.getNowDate());
        return blogPageMapper.updateBlogPage(blogPage);
    }

    /**
     * 批量删除独立页面
     *
     * @param pageIds 需要删除的独立页面主键
     * @return 结果
     */
    @Override
    public int deleteBlogPageByPageIds(Long[] pageIds)
    {
        return blogPageMapper.deleteBlogPageByPageIds(pageIds);
    }

    /**
     * 删除独立页面信息
     *
     * @param pageId 独立页面主键
     * @return 结果
     */
    @Override
    public int deleteBlogPageByPageId(Long pageId)
    {
        return blogPageMapper.deleteBlogPageByPageId(pageId);
    }
}
