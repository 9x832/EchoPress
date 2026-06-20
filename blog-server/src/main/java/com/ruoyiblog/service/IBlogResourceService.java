package com.ruoyiblog.service;

import java.util.List;
import com.ruoyiblog.domain.BlogResource;

/**
 * 资源上传记录Service接口
 *
 * @author ruoyi
 * @date 2026-06-20
 */
public interface IBlogResourceService
{
    /**
     * 查询资源上传记录
     *
     * @param resourceId 资源上传记录主键
     * @return 资源上传记录
     */
    public BlogResource selectBlogResourceByResourceId(Long resourceId);

    /**
     * 查询资源上传记录列表
     *
     * @param blogResource 资源上传记录
     * @return 资源上传记录集合
     */
    public List<BlogResource> selectBlogResourceList(BlogResource blogResource);

    /**
     * 新增资源上传记录
     *
     * @param blogResource 资源上传记录
     * @return 结果
     */
    public int insertBlogResource(BlogResource blogResource);

    /**
     * 修改资源上传记录
     *
     * @param blogResource 资源上传记录
     * @return 结果
     */
    public int updateBlogResource(BlogResource blogResource);

    /**
     * 批量删除资源上传记录
     *
     * @param resourceIds 需要删除的资源上传记录主键集合
     * @return 结果
     */
    public int deleteBlogResourceByResourceIds(Long[] resourceIds);

    /**
     * 删除资源上传记录信息
     *
     * @param resourceId 资源上传记录主键
     * @return 结果
     */
    public int deleteBlogResourceByResourceId(Long resourceId);
}
