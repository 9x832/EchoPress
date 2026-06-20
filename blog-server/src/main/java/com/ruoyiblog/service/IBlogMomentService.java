package com.ruoyiblog.service;

import java.util.List;
import com.ruoyiblog.domain.BlogMoment;

/**
 * 动态/说说Service接口
 *
 * @author ruoyi
 * @date 2026-06-20
 */
public interface IBlogMomentService
{
    /**
     * 查询动态/说说
     *
     * @param momentId 动态/说说主键
     * @return 动态/说说
     */
    public BlogMoment selectBlogMomentByMomentId(Long momentId);

    /**
     * 查询动态/说说列表
     *
     * @param blogMoment 动态/说说
     * @return 动态/说说集合
     */
    public List<BlogMoment> selectBlogMomentList(BlogMoment blogMoment);

    /**
     * 新增动态/说说
     *
     * @param blogMoment 动态/说说
     * @return 结果
     */
    public int insertBlogMoment(BlogMoment blogMoment);

    /**
     * 修改动态/说说
     *
     * @param blogMoment 动态/说说
     * @return 结果
     */
    public int updateBlogMoment(BlogMoment blogMoment);

    /**
     * 批量删除动态/说说
     *
     * @param momentIds 需要删除的动态/说说主键集合
     * @return 结果
     */
    public int deleteBlogMomentByMomentIds(Long[] momentIds);

    /**
     * 删除动态/说说信息
     *
     * @param momentId 动态/说说主键
     * @return 结果
     */
    public int deleteBlogMomentByMomentId(Long momentId);
}
