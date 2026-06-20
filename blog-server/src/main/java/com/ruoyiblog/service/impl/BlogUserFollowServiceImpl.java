package com.ruoyiblog.service.impl;

import java.util.List;
import com.ruoyiblog.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyiblog.mapper.BlogUserFollowMapper;
import com.ruoyiblog.domain.BlogUserFollow;
import com.ruoyiblog.service.IBlogUserFollowService;

/**
 * 用户关注关系Service业务层处理
 *
 * @author ruoyi
 * @date 2026-06-20
 */
@Service
public class BlogUserFollowServiceImpl implements IBlogUserFollowService
{
    @Autowired
    private BlogUserFollowMapper blogUserFollowMapper;

    /**
     * 查询用户关注关系
     *
     * @param followerId 用户关注关系主键
     * @return 用户关注关系
     */
    @Override
    public BlogUserFollow selectBlogUserFollowByFollowerId(Long followerId)
    {
        return blogUserFollowMapper.selectBlogUserFollowByFollowerId(followerId);
    }

    /**
     * 查询用户关注关系列表
     *
     * @param blogUserFollow 用户关注关系
     * @return 用户关注关系
     */
    @Override
    public List<BlogUserFollow> selectBlogUserFollowList(BlogUserFollow blogUserFollow)
    {
        return blogUserFollowMapper.selectBlogUserFollowList(blogUserFollow);
    }

    /**
     * 新增用户关注关系
     *
     * @param blogUserFollow 用户关注关系
     * @return 结果
     */
    @Override
    public int insertBlogUserFollow(BlogUserFollow blogUserFollow)
    {
        blogUserFollow.setCreateTime(DateUtils.getNowDate());
        return blogUserFollowMapper.insertBlogUserFollow(blogUserFollow);
    }

    /**
     * 修改用户关注关系
     *
     * @param blogUserFollow 用户关注关系
     * @return 结果
     */
    @Override
    public int updateBlogUserFollow(BlogUserFollow blogUserFollow)
    {
        return blogUserFollowMapper.updateBlogUserFollow(blogUserFollow);
    }

    /**
     * 批量删除用户关注关系
     *
     * @param followerIds 需要删除的用户关注关系主键
     * @return 结果
     */
    @Override
    public int deleteBlogUserFollowByFollowerIds(Long[] followerIds)
    {
        return blogUserFollowMapper.deleteBlogUserFollowByFollowerIds(followerIds);
    }

    /**
     * 删除用户关注关系信息
     *
     * @param followerId 用户关注关系主键
     * @return 结果
     */
    @Override
    public int deleteBlogUserFollowByFollowerId(Long followerId)
    {
        return blogUserFollowMapper.deleteBlogUserFollowByFollowerId(followerId);
    }
}
