package com.ruoyiblog.service;

import java.util.List;
import com.ruoyiblog.domain.BlogUserFollow;

/**
 * 用户关注关系Service接口
 *
 * @author ruoyi
 * @date 2026-06-20
 */
public interface IBlogUserFollowService
{
    /**
     * 查询用户关注关系
     *
     * @param followerId 用户关注关系主键
     * @return 用户关注关系
     */
    public BlogUserFollow selectBlogUserFollowByFollowerId(Long followerId);

    /**
     * 查询用户关注关系列表
     *
     * @param blogUserFollow 用户关注关系
     * @return 用户关注关系集合
     */
    public List<BlogUserFollow> selectBlogUserFollowList(BlogUserFollow blogUserFollow);

    /**
     * 新增用户关注关系
     *
     * @param blogUserFollow 用户关注关系
     * @return 结果
     */
    public int insertBlogUserFollow(BlogUserFollow blogUserFollow);

    /**
     * 修改用户关注关系
     *
     * @param blogUserFollow 用户关注关系
     * @return 结果
     */
    public int updateBlogUserFollow(BlogUserFollow blogUserFollow);

    /**
     * 批量删除用户关注关系
     *
     * @param followerIds 需要删除的用户关注关系主键集合
     * @return 结果
     */
    public int deleteBlogUserFollowByFollowerIds(Long[] followerIds);

    /**
     * 删除用户关注关系信息
     *
     * @param followerId 用户关注关系主键
     * @return 结果
     */
    public int deleteBlogUserFollowByFollowerId(Long followerId);
}
