package com.ruoyiblog.service.impl;

import java.util.List;
import com.ruoyiblog.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyiblog.mapper.BlogSiteConfigMapper;
import com.ruoyiblog.domain.BlogSiteConfig;
import com.ruoyiblog.service.IBlogSiteConfigService;

/**
 * 站点配置Service业务层处理
 *
 * @author ruoyi
 * @date 2026-06-20
 */
@Service
public class BlogSiteConfigServiceImpl implements IBlogSiteConfigService
{
    @Autowired
    private BlogSiteConfigMapper blogSiteConfigMapper;

    /**
     * 查询站点配置
     *
     * @param configId 站点配置主键
     * @return 站点配置
     */
    @Override
    public BlogSiteConfig selectBlogSiteConfigByConfigId(Long configId)
    {
        return blogSiteConfigMapper.selectBlogSiteConfigByConfigId(configId);
    }

    /**
     * 查询站点配置列表
     *
     * @param blogSiteConfig 站点配置
     * @return 站点配置
     */
    @Override
    public List<BlogSiteConfig> selectBlogSiteConfigList(BlogSiteConfig blogSiteConfig)
    {
        return blogSiteConfigMapper.selectBlogSiteConfigList(blogSiteConfig);
    }

    /**
     * 新增站点配置
     *
     * @param blogSiteConfig 站点配置
     * @return 结果
     */
    @Override
    public int insertBlogSiteConfig(BlogSiteConfig blogSiteConfig)
    {
        blogSiteConfig.setCreateTime(DateUtils.getNowDate());
        return blogSiteConfigMapper.insertBlogSiteConfig(blogSiteConfig);
    }

    /**
     * 修改站点配置
     *
     * @param blogSiteConfig 站点配置
     * @return 结果
     */
    @Override
    public int updateBlogSiteConfig(BlogSiteConfig blogSiteConfig)
    {
        blogSiteConfig.setUpdateTime(DateUtils.getNowDate());
        return blogSiteConfigMapper.updateBlogSiteConfig(blogSiteConfig);
    }

    /**
     * 批量删除站点配置
     *
     * @param configIds 需要删除的站点配置主键
     * @return 结果
     */
    @Override
    public int deleteBlogSiteConfigByConfigIds(Long[] configIds)
    {
        return blogSiteConfigMapper.deleteBlogSiteConfigByConfigIds(configIds);
    }

    /**
     * 删除站点配置信息
     *
     * @param configId 站点配置主键
     * @return 结果
     */
    @Override
    public int deleteBlogSiteConfigByConfigId(Long configId)
    {
        return blogSiteConfigMapper.deleteBlogSiteConfigByConfigId(configId);
    }
}
