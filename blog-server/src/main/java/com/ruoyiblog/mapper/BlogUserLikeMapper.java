package com.ruoyiblog.mapper;

import java.util.List;
import com.ruoyiblog.domain.BlogUserLike;
import org.apache.ibatis.annotations.Param;

/**
 * 用户点赞记录Mapper接口
 *
 * @author ruoyi
 * @date 2026-06-20
 */
public interface BlogUserLikeMapper
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
     * 删除用户点赞记录
     *
     * @param userId 用户点赞记录主键
     * @return 结果
     */
    public int deleteBlogUserLikeByUserId(Long userId);

    /**
     * 批量删除用户点赞记录
     *
     * @param userIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteBlogUserLikeByUserIds(Long[] userIds);

    /**
     * 根据用户ID和文章ID查询点赞记录
     *
     * @param userId 用户ID
     * @param articleId 文章ID
     * @return 用户点赞记录
     */
    public BlogUserLike selectByUserIdAndArticleId(@Param("userId") Long userId, @Param("articleId") Long articleId);

    /**
     * 根据用户ID和文章ID删除点赞记录
     *
     * @param userId 用户ID
     * @param articleId 文章ID
     * @return 结果
     */
    public int deleteByUserIdAndArticleId(@Param("userId") Long userId, @Param("articleId") Long articleId);

    /**
     * 查询用户已点赞的文章ID列表
     *
     * @param userId 用户ID
     * @param articleIds 文章ID数组
     * @return 已点赞的文章ID列表
     */
    public List<Long> selectLikedArticleIds(@Param("userId") Long userId, @Param("articleIds") Long[] articleIds);
}
