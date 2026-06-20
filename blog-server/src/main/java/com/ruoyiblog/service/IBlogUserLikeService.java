package com.ruoyiblog.service;

import java.util.List;
import com.ruoyiblog.domain.BlogUserLike;

/**
 * 用户点赞记录Service接口
 *
 * @author ruoyi
 * @date 2026-06-20
 */
public interface IBlogUserLikeService
{
    /**
     * 查询用户点赞记录
     *
     * @param userId 用户点赞记录主键
     * @return 用户点赞记录
     */
    public BlogUserLike selectBlogUserLikeByUserId(Long userId);

    /**
     * 查询用户点赞记录列表
     *
     * @param blogUserLike 用户点赞记录
     * @return 用户点赞记录集合
     */
    public List<BlogUserLike> selectBlogUserLikeList(BlogUserLike blogUserLike);

    /**
     * 新增用户点赞记录
     *
     * @param blogUserLike 用户点赞记录
     * @return 结果
     */
    public int insertBlogUserLike(BlogUserLike blogUserLike);

    /**
     * 修改用户点赞记录
     *
     * @param blogUserLike 用户点赞记录
     * @return 结果
     */
    public int updateBlogUserLike(BlogUserLike blogUserLike);

    /**
     * 批量删除用户点赞记录
     *
     * @param userIds 需要删除的用户点赞记录主键集合
     * @return 结果
     */
    public int deleteBlogUserLikeByUserIds(Long[] userIds);

    /**
     * 删除用户点赞记录信息
     *
     * @param userId 用户点赞记录主键
     * @return 结果
     */
    public int deleteBlogUserLikeByUserId(Long userId);
}
