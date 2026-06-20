package com.ruoyiblog.service.impl;

import java.util.List;
import com.ruoyiblog.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyiblog.mapper.BlogResourceMapper;
import com.ruoyiblog.domain.BlogResource;
import com.ruoyiblog.service.IBlogResourceService;

/**
 * 资源上传记录Service业务层处理
 *
 * @author ruoyi
 * @date 2026-06-20
 */
@Service
public class BlogResourceServiceImpl implements IBlogResourceService
{
    @Autowired
    private BlogResourceMapper blogResourceMapper;

    /**
     * 查询资源上传记录
     *
     * @param resourceId 资源上传记录主键
     * @return 资源上传记录
     */
    @Override
    public BlogResource selectBlogResourceByResourceId(Long resourceId)
    {
        return blogResourceMapper.selectBlogResourceByResourceId(resourceId);
    }

    /**
     * 查询资源上传记录列表
     *
     * @param blogResource 资源上传记录
     * @return 资源上传记录
     */
    @Override
    public List<BlogResource> selectBlogResourceList(BlogResource blogResource)
    {
        return blogResourceMapper.selectBlogResourceList(blogResource);
    }

    /**
     * 新增资源上传记录
     *
     * @param blogResource 资源上传记录
     * @return 结果
     */
    @Override
    public int insertBlogResource(BlogResource blogResource)
    {
        blogResource.setCreateTime(DateUtils.getNowDate());
        return blogResourceMapper.insertBlogResource(blogResource);
    }

    /**
     * 修改资源上传记录
     *
     * @param blogResource 资源上传记录
     * @return 结果
     */
    @Override
    public int updateBlogResource(BlogResource blogResource)
    {
        blogResource.setUpdateTime(DateUtils.getNowDate());
        return blogResourceMapper.updateBlogResource(blogResource);
    }

    /**
     * 批量删除资源上传记录
     *
     * @param resourceIds 需要删除的资源上传记录主键
     * @return 结果
     */
    @Override
    public int deleteBlogResourceByResourceIds(Long[] resourceIds)
    {
        return blogResourceMapper.deleteBlogResourceByResourceIds(resourceIds);
    }

    /**
     * 删除资源上传记录信息
     *
     * @param resourceId 资源上传记录主键
     * @return 结果
     */
    @Override
    public int deleteBlogResourceByResourceId(Long resourceId)
    {
        return blogResourceMapper.deleteBlogResourceByResourceId(resourceId);
    }
}
