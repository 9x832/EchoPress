package com.ruoyiblog.service;

import java.util.List;
import com.ruoyiblog.domain.BlogLink;

/**
 * 友情链接Service接口
 *
 * @author ruoyi
 * @date 2026-06-20
 */
public interface IBlogLinkService
{
    /**
     * 查询友情链接
     *
     * @param linkId 友情链接主键
     * @return 友情链接
     */
    public BlogLink selectBlogLinkByLinkId(Long linkId);

    /**
     * 查询友情链接列表
     *
     * @param blogLink 友情链接
     * @return 友情链接集合
     */
    public List<BlogLink> selectBlogLinkList(BlogLink blogLink);

    /**
     * 新增友情链接
     *
     * @param blogLink 友情链接
     * @return 结果
     */
    public int insertBlogLink(BlogLink blogLink);

    /**
     * 修改友情链接
     *
     * @param blogLink 友情链接
     * @return 结果
     */
    public int updateBlogLink(BlogLink blogLink);

    /**
     * 批量删除友情链接
     *
     * @param linkIds 需要删除的友情链接主键集合
     * @return 结果
     */
    public int deleteBlogLinkByLinkIds(Long[] linkIds);

    /**
     * 删除友情链接信息
     *
     * @param linkId 友情链接主键
     * @return 结果
     */
    public int deleteBlogLinkByLinkId(Long linkId);
}
