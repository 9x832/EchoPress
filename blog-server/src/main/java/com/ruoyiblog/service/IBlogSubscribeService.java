package com.ruoyiblog.service;

import java.util.List;
import com.ruoyiblog.domain.BlogSubscribe;

/**
 * 邮件订阅Service接口
 *
 * @author ruoyi
 * @date 2026-06-20
 */
public interface IBlogSubscribeService
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
     * 批量删除邮件订阅
     *
     * @param subscribeIds 需要删除的邮件订阅主键集合
     * @return 结果
     */
    public int deleteBlogSubscribeBySubscribeIds(Long[] subscribeIds);

    /**
     * 删除邮件订阅信息
     *
     * @param subscribeId 邮件订阅主键
     * @return 结果
     */
    public int deleteBlogSubscribeBySubscribeId(Long subscribeId);
}
