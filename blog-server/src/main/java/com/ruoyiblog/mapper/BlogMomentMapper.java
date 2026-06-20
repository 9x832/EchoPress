package com.ruoyiblog.mapper;

import java.util.List;
import com.ruoyiblog.domain.BlogMoment;

/**
 * 动态/说说Mapper接口
 *
 * @author ruoyi
 * @date 2026-06-20
 */
public interface BlogMomentMapper
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
     * 删除动态/说说
     *
     * @param momentId 动态/说说主键
     * @return 结果
     */
    public int deleteBlogMomentByMomentId(Long momentId);

    /**
     * 批量删除动态/说说
     *
     * @param momentIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteBlogMomentByMomentIds(Long[] momentIds);
}
