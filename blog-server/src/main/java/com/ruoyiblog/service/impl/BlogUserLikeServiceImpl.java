package com.ruoyiblog.service.impl;

import java.util.List;
import com.ruoyiblog.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyiblog.mapper.BlogUserLikeMapper;
import com.ruoyiblog.domain.BlogUserLike;
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
}
