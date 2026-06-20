package com.ruoyiblog.mapper;

import java.util.List;
import com.ruoyiblog.domain.BlogSiteConfig;

/**
 * 站点配置Mapper接口
 *
 * @author ruoyi
 * @date 2026-06-20
 */
public interface BlogSiteConfigMapper
{
    /**
     * 查询站点配置
     *
     * @param configId 站点配置主键
     * @return 站点配置
     */
    public BlogSiteConfig selectBlogSiteConfigByConfigId(Long configId);

    /**
     * 查询站点配置列表
     *
     * @param blogSiteConfig 站点配置
     * @return 站点配置集合
     */
    public List<BlogSiteConfig> selectBlogSiteConfigList(BlogSiteConfig blogSiteConfig);

    /**
     * 新增站点配置
     *
     * @param blogSiteConfig 站点配置
     * @return 结果
     */
    public int insertBlogSiteConfig(BlogSiteConfig blogSiteConfig);

    /**
     * 修改站点配置
     *
     * @param blogSiteConfig 站点配置
     * @return 结果
     */
    public int updateBlogSiteConfig(BlogSiteConfig blogSiteConfig);

    /**
     * 删除站点配置
     *
     * @param configId 站点配置主键
     * @return 结果
     */
    public int deleteBlogSiteConfigByConfigId(Long configId);

    /**
     * 批量删除站点配置
     *
     * @param configIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteBlogSiteConfigByConfigIds(Long[] configIds);
}
