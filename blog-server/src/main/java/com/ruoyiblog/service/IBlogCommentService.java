package com.ruoyiblog.service;

import java.util.List;
import com.ruoyiblog.domain.BlogComment;

/**
 * 评论Service接口
 *
 * @author ruoyi
 * @date 2026-06-20
 */
public interface IBlogCommentService
{
    /**
     * 查询评论
     *
     * @param commentId 评论主键
     * @return 评论
     */
    public BlogComment selectBlogCommentByCommentId(Long commentId);

    /**
     * 查询评论列表
     *
     * @param blogComment 评论
     * @return 评论集合
     */
    public List<BlogComment> selectBlogCommentList(BlogComment blogComment);

    /**
     * 新增评论
     *
     * @param blogComment 评论
     * @return 结果
     */
    public int insertBlogComment(BlogComment blogComment);

    /**
     * 修改评论
     *
     * @param blogComment 评论
     * @return 结果
     */
    public int updateBlogComment(BlogComment blogComment);

    /**
     * 批量删除评论
     *
     * @param commentIds 需要删除的评论主键集合
     * @return 结果
     */
    public int deleteBlogCommentByCommentIds(Long[] commentIds);

    /**
     * 删除评论信息
     *
     * @param commentId 评论主键
     * @return 结果
     */
    public int deleteBlogCommentByCommentId(Long commentId);
}
