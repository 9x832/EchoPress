package com.ruoyiblog.service.impl;

import java.util.List;
import com.ruoyiblog.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyiblog.mapper.BlogMomentMapper;
import com.ruoyiblog.domain.BlogMoment;
import com.ruoyiblog.service.IBlogMomentService;

/**
 * 动态/说说Service业务层处理
 *
 * @author ruoyi
 * @date 2026-06-20
 */
@Service
public class BlogMomentServiceImpl implements IBlogMomentService
{
    @Autowired
    private BlogMomentMapper blogMomentMapper;

    /**
     * 查询动态/说说
     *
     * @param momentId 动态/说说主键
     * @return 动态/说说
     */
    @Override
    public BlogMoment selectBlogMomentByMomentId(Long momentId)
    {
        return blogMomentMapper.selectBlogMomentByMomentId(momentId);
    }

    /**
     * 查询动态/说说列表
     *
     * @param blogMoment 动态/说说
     * @return 动态/说说
     */
    @Override
    public List<BlogMoment> selectBlogMomentList(BlogMoment blogMoment)
    {
        return blogMomentMapper.selectBlogMomentList(blogMoment);
    }

    /**
     * 新增动态/说说
     *
     * @param blogMoment 动态/说说
     * @return 结果
     */
    @Override
    public int insertBlogMoment(BlogMoment blogMoment)
    {
        blogMoment.setCreateTime(DateUtils.getNowDate());
        return blogMomentMapper.insertBlogMoment(blogMoment);
    }

    /**
     * 修改动态/说说
     *
     * @param blogMoment 动态/说说
     * @return 结果
     */
    @Override
    public int updateBlogMoment(BlogMoment blogMoment)
    {
        blogMoment.setUpdateTime(DateUtils.getNowDate());
        return blogMomentMapper.updateBlogMoment(blogMoment);
    }

    /**
     * 批量删除动态/说说
     *
     * @param momentIds 需要删除的动态/说说主键
     * @return 结果
     */
    @Override
    public int deleteBlogMomentByMomentIds(Long[] momentIds)
    {
        return blogMomentMapper.deleteBlogMomentByMomentIds(momentIds);
    }

    /**
     * 删除动态/说说信息
     *
     * @param momentId 动态/说说主键
     * @return 结果
     */
    @Override
    public int deleteBlogMomentByMomentId(Long momentId)
    {
        return blogMomentMapper.deleteBlogMomentByMomentId(momentId);
    }
}
