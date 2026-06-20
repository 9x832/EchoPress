package com.ruoyiblog.mapper;

import java.util.List;
import com.ruoyiblog.domain.BlogSubscribe;

/**
 * 邮件订阅Mapper接口
 *
 * @author ruoyi
 * @date 2026-06-20
 */
public interface BlogSubscribeMapper
{
    /**
     * 查询邮件订阅
     *
     * @param subscribeId 邮件订阅主键
     * @return 邮件订阅
     */
    public BlogSubscribe selectBlogSubscribeBySubscribeId(Long subscribeId);

    /**
     * 查询邮件订阅列表
     *
     * @param blogSubscribe 邮件订阅
     * @return 邮件订阅集合
     */
    public List<BlogSubscribe> selectBlogSubscribeList(BlogSubscribe blogSubscribe);

    /**
     * 新增邮件订阅
     *
     * @param blogSubscribe 邮件订阅
     * @return 结果
     */
    public int insertBlogSubscribe(BlogSubscribe blogSubscribe);

    /**
     * 修改邮件订阅
     *
     * @param blogSubscribe 邮件订阅
     * @return 结果
     */
    public int updateBlogSubscribe(BlogSubscribe blogSubscribe);

    /**
     * 删除邮件订阅
     *
     * @param subscribeId 邮件订阅主键
     * @return 结果
     */
    public int deleteBlogSubscribeBySubscribeId(Long subscribeId);

    /**
     * 批量删除邮件订阅
     *
     * @param subscribeIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteBlogSubscribeBySubscribeIds(Long[] subscribeIds);
}
