package com.ruoyiblog.service.impl;

import java.util.List;
import com.ruoyiblog.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyiblog.mapper.BlogLinkMapper;
import com.ruoyiblog.domain.BlogLink;
import com.ruoyiblog.service.IBlogLinkService;

/**
 * 友情链接Service业务层处理
 *
 * @author ruoyi
 * @date 2026-06-20
 */
@Service
public class BlogLinkServiceImpl implements IBlogLinkService
{
    @Autowired
    private BlogLinkMapper blogLinkMapper;

    /**
     * 查询友情链接
     *
     * @param linkId 友情链接主键
     * @return 友情链接
     */
    @Override
    public BlogLink selectBlogLinkByLinkId(Long linkId)
    {
        return blogLinkMapper.selectBlogLinkByLinkId(linkId);
    }

    /**
     * 查询友情链接列表
     *
     * @param blogLink 友情链接
     * @return 友情链接
     */
    @Override
    public List<BlogLink> selectBlogLinkList(BlogLink blogLink)
    {
        return blogLinkMapper.selectBlogLinkList(blogLink);
    }

    /**
     * 新增友情链接
     *
     * @param blogLink 友情链接
     * @return 结果
     */
    @Override
    public int insertBlogLink(BlogLink blogLink)
    {
        blogLink.setCreateTime(DateUtils.getNowDate());
        return blogLinkMapper.insertBlogLink(blogLink);
    }

    /**
     * 修改友情链接
     *
     * @param blogLink 友情链接
     * @return 结果
     */
    @Override
    public int updateBlogLink(BlogLink blogLink)
    {
        blogLink.setUpdateTime(DateUtils.getNowDate());
        return blogLinkMapper.updateBlogLink(blogLink);
    }

    /**
     * 批量删除友情链接
     *
     * @param linkIds 需要删除的友情链接主键
     * @return 结果
     */
    @Override
    public int deleteBlogLinkByLinkIds(Long[] linkIds)
    {
        return blogLinkMapper.deleteBlogLinkByLinkIds(linkIds);
    }

    /**
     * 删除友情链接信息
     *
     * @param linkId 友情链接主键
     * @return 结果
     */
    @Override
    public int deleteBlogLinkByLinkId(Long linkId)
    {
        return blogLinkMapper.deleteBlogLinkByLinkId(linkId);
    }
}
