package com.ruoyiblog.service.impl;

import java.util.List;
import com.ruoyiblog.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyiblog.mapper.BlogSubscribeMapper;
import com.ruoyiblog.domain.BlogSubscribe;
import com.ruoyiblog.service.IBlogSubscribeService;

/**
 * 邮件订阅Service业务层处理
 *
 * @author ruoyi
 * @date 2026-06-20
 */
@Service
public class BlogSubscribeServiceImpl implements IBlogSubscribeService
{
    @Autowired
    private BlogSubscribeMapper blogSubscribeMapper;

    /**
     * 查询邮件订阅
     *
     * @param subscribeId 邮件订阅主键
     * @return 邮件订阅
     */
    @Override
    public BlogSubscribe selectBlogSubscribeBySubscribeId(Long subscribeId)
    {
        return blogSubscribeMapper.selectBlogSubscribeBySubscribeId(subscribeId);
    }

    /**
     * 查询邮件订阅列表
     *
     * @param blogSubscribe 邮件订阅
     * @return 邮件订阅
     */
    @Override
    public List<BlogSubscribe> selectBlogSubscribeList(BlogSubscribe blogSubscribe)
    {
        return blogSubscribeMapper.selectBlogSubscribeList(blogSubscribe);
    }

    /**
     * 新增邮件订阅
     *
     * @param blogSubscribe 邮件订阅
     * @return 结果
     */
    @Override
    public int insertBlogSubscribe(BlogSubscribe blogSubscribe)
    {
        blogSubscribe.setCreateTime(DateUtils.getNowDate());
        return blogSubscribeMapper.insertBlogSubscribe(blogSubscribe);
    }

    /**
     * 修改邮件订阅
     *
     * @param blogSubscribe 邮件订阅
     * @return 结果
     */
    @Override
    public int updateBlogSubscribe(BlogSubscribe blogSubscribe)
    {
        blogSubscribe.setUpdateTime(DateUtils.getNowDate());
        return blogSubscribeMapper.updateBlogSubscribe(blogSubscribe);
    }

    /**
     * 批量删除邮件订阅
     *
     * @param subscribeIds 需要删除的邮件订阅主键
     * @return 结果
     */
    @Override
    public int deleteBlogSubscribeBySubscribeIds(Long[] subscribeIds)
    {
        return blogSubscribeMapper.deleteBlogSubscribeBySubscribeIds(subscribeIds);
    }

    /**
     * 删除邮件订阅信息
     *
     * @param subscribeId 邮件订阅主键
     * @return 结果
     */
    @Override
    public int deleteBlogSubscribeBySubscribeId(Long subscribeId)
    {
        return blogSubscribeMapper.deleteBlogSubscribeBySubscribeId(subscribeId);
    }
}
