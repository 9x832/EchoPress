package com.ruoyiblog.service.impl;

import java.util.List;
import com.ruoyiblog.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyiblog.mapper.BlogCommentMapper;
import com.ruoyiblog.domain.BlogComment;
import com.ruoyiblog.service.IBlogCommentService;

/**
 * 评论Service业务层处理
 *
 * @author ruoyi
 * @date 2026-06-20
 */
@Service
public class BlogCommentServiceImpl implements IBlogCommentService
{
    @Autowired
    private BlogCommentMapper blogCommentMapper;

    /**
     * 查询评论
     *
     * @param commentId 评论主键
     * @return 评论
     */
    @Override
    public BlogComment selectBlogCommentByCommentId(Long commentId)
    {
        return blogCommentMapper.selectBlogCommentByCommentId(commentId);
    }

    /**
     * 查询评论列表
     *
     * @param blogComment 评论
     * @return 评论
     */
    @Override
    public List<BlogComment> selectBlogCommentList(BlogComment blogComment)
    {
        return blogCommentMapper.selectBlogCommentList(blogComment);
    }

    /**
     * 新增评论
     *
     * @param blogComment 评论
     * @return 结果
     */
    @Override
    public int insertBlogComment(BlogComment blogComment)
    {
        blogComment.setCreateTime(DateUtils.getNowDate());
        return blogCommentMapper.insertBlogComment(blogComment);
    }

    /**
     * 修改评论
     *
     * @param blogComment 评论
     * @return 结果
     */
    @Override
    public int updateBlogComment(BlogComment blogComment)
    {
        blogComment.setUpdateTime(DateUtils.getNowDate());
        return blogCommentMapper.updateBlogComment(blogComment);
    }

    /**
     * 批量删除评论
     *
     * @param commentIds 需要删除的评论主键
     * @return 结果
     */
    @Override
    public int deleteBlogCommentByCommentIds(Long[] commentIds)
    {
        return blogCommentMapper.deleteBlogCommentByCommentIds(commentIds);
    }

    /**
     * 删除评论信息
     *
     * @param commentId 评论主键
     * @return 结果
     */
    @Override
    public int deleteBlogCommentByCommentId(Long commentId)
    {
        return blogCommentMapper.deleteBlogCommentByCommentId(commentId);
    }
}
