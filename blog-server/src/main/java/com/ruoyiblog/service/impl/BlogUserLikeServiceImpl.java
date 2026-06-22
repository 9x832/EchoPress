package com.ruoyiblog.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.ruoyiblog.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.ruoyiblog.mapper.BlogUserLikeMapper;
import com.ruoyiblog.mapper.BlogArticleMapper;
import com.ruoyiblog.domain.BlogUserLike;
import com.ruoyiblog.domain.BlogArticle;
import com.ruoyiblog.service.IBlogUserLikeService;

/**
 * 用户点赞记录Service业务层处理
 *
 * @author ruoyi
 * @date 2026-06-20
 */
@Service
public class BlogUserLikeServiceImpl implements IBlogUserLikeService
{
    @Autowired
    private BlogUserLikeMapper blogUserLikeMapper;

    @Autowired
    private BlogArticleMapper blogArticleMapper;

    /**
     * 查询用户点赞记录
     *
     * @param userId 用户点赞记录主键
     * @return 用户点赞记录
     */
    @Override
    public BlogUserLike selectBlogUserLikeByUserId(Long userId)
    {
        return blogUserLikeMapper.selectBlogUserLikeByUserId(userId);
    }

    /**
     * 查询用户点赞记录列表
     *
     * @param blogUserLike 用户点赞记录
     * @return 用户点赞记录
     */
    @Override
    public List<BlogUserLike> selectBlogUserLikeList(BlogUserLike blogUserLike)
    {
        return blogUserLikeMapper.selectBlogUserLikeList(blogUserLike);
    }

    /**
     * 新增用户点赞记录
     *
     * @param blogUserLike 用户点赞记录
     * @return 结果
     */
    @Override
    public int insertBlogUserLike(BlogUserLike blogUserLike)
    {
        blogUserLike.setCreateTime(DateUtils.getNowDate());
        return blogUserLikeMapper.insertBlogUserLike(blogUserLike);
    }

    /**
     * 修改用户点赞记录
     *
     * @param blogUserLike 用户点赞记录
     * @return 结果
     */
    @Override
    public int updateBlogUserLike(BlogUserLike blogUserLike)
    {
        return blogUserLikeMapper.updateBlogUserLike(blogUserLike);
    }

    /**
     * 批量删除用户点赞记录
     *
     * @param userIds 需要删除的用户点赞记录主键
     * @return 结果
     */
    @Override
    public int deleteBlogUserLikeByUserIds(Long[] userIds)
    {
        return blogUserLikeMapper.deleteBlogUserLikeByUserIds(userIds);
    }

    /**
     * 删除用户点赞记录信息
     *
     * @param userId 用户点赞记录主键
     * @return 结果
     */
    @Override
    public int deleteBlogUserLikeByUserId(Long userId)
    {
        return blogUserLikeMapper.deleteBlogUserLikeByUserId(userId);
    }

    /**
     * 切换用户点赞状态（点赞/取消点赞）
     *
     * @param userId 用户ID
     * @param articleId 文章ID
     * @return 包含点赞状态和点赞数的Map
     */
    @Override
    @Transactional
    public Map<String, Object> toggleLike(Long userId, Long articleId)
    {
        BlogUserLike existing = blogUserLikeMapper.selectByUserIdAndArticleId(userId, articleId);
        Map<String, Object> result = new HashMap<>();
        if (existing != null)
        {
            blogUserLikeMapper.deleteByUserIdAndArticleId(userId, articleId);
            blogArticleMapper.decrementLikeCount(articleId);
            result.put("liked", false);
        }
        else
        {
            BlogUserLike like = new BlogUserLike();
            like.setUserId(userId);
            like.setArticleId(articleId);
            like.setCreateTime(DateUtils.getNowDate());
            blogUserLikeMapper.insertBlogUserLike(like);
            blogArticleMapper.incrementLikeCount(articleId);
            result.put("liked", true);
        }
        BlogArticle updated = blogArticleMapper.selectBlogArticleByArticleId(articleId);
        result.put("likeCount", updated != null ? updated.getLikeCount() : 0L);
        return result;
    }

    /**
     * 批量查询用户对文章的点赞状态
     *
     * @param userId 用户ID
     * @param articleIds 文章ID数组
     * @return 文章ID与点赞状态的映射
     */
    @Override
    public Map<Long, Boolean> getLikedArticleIds(Long userId, Long[] articleIds)
    {
        Map<Long, Boolean> result = new HashMap<>();
        if (articleIds == null || articleIds.length == 0)
        {
            return result;
        }
        List<Long> likedIds = blogUserLikeMapper.selectLikedArticleIds(userId, articleIds);
        for (Long id : articleIds)
        {
            result.put(id, likedIds.contains(id));
        }
        return result;
    }
}
