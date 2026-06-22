package com.ruoyiblog.service;

import java.util.List;
import java.util.Map;
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

    /**
     * 切换用户点赞状态（点赞/取消点赞）
     *
     * @param userId 用户ID
     * @param articleId 文章ID
     * @return 包含点赞状态和点赞数的Map
     */
    public Map<String, Object> toggleLike(Long userId, Long articleId);

    /**
     * 批量查询用户对文章的点赞状态
     *
     * @param userId 用户ID
     * @param articleIds 文章ID数组
     * @return 文章ID与点赞状态的映射
     */
    public Map<Long, Boolean> getLikedArticleIds(Long userId, Long[] articleIds);
}
