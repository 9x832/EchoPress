package com.ruoyiblog.service.impl;

import java.util.List;
import com.ruoyiblog.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyiblog.mapper.BlogUserSocialMapper;
import com.ruoyiblog.domain.BlogUserSocial;
import com.ruoyiblog.service.IBlogUserSocialService;

/**
 * 用户第三方登录绑定Service业务层处理
 *
 * @author ruoyi
 * @date 2026-06-20
 */
@Service
public class BlogUserSocialServiceImpl implements IBlogUserSocialService
{
    @Autowired
    private BlogUserSocialMapper blogUserSocialMapper;

    /**
     * 查询用户第三方登录绑定
     *
     * @param socialId 用户第三方登录绑定主键
     * @return 用户第三方登录绑定
     */
    @Override
    public BlogUserSocial selectBlogUserSocialBySocialId(Long socialId)
    {
        return blogUserSocialMapper.selectBlogUserSocialBySocialId(socialId);
    }

    /**
     * 查询用户第三方登录绑定列表
     *
     * @param blogUserSocial 用户第三方登录绑定
     * @return 用户第三方登录绑定
     */
    @Override
    public List<BlogUserSocial> selectBlogUserSocialList(BlogUserSocial blogUserSocial)
    {
        return blogUserSocialMapper.selectBlogUserSocialList(blogUserSocial);
    }

    /**
     * 新增用户第三方登录绑定
     *
     * @param blogUserSocial 用户第三方登录绑定
     * @return 结果
     */
    @Override
    public int insertBlogUserSocial(BlogUserSocial blogUserSocial)
    {
        blogUserSocial.setCreateTime(DateUtils.getNowDate());
        return blogUserSocialMapper.insertBlogUserSocial(blogUserSocial);
    }

    /**
     * 修改用户第三方登录绑定
     *
     * @param blogUserSocial 用户第三方登录绑定
     * @return 结果
     */
    @Override
    public int updateBlogUserSocial(BlogUserSocial blogUserSocial)
    {
        return blogUserSocialMapper.updateBlogUserSocial(blogUserSocial);
    }

    /**
     * 批量删除用户第三方登录绑定
     *
     * @param socialIds 需要删除的用户第三方登录绑定主键
     * @return 结果
     */
    @Override
    public int deleteBlogUserSocialBySocialIds(Long[] socialIds)
    {
        return blogUserSocialMapper.deleteBlogUserSocialBySocialIds(socialIds);
    }

    /**
     * 删除用户第三方登录绑定信息
     *
     * @param socialId 用户第三方登录绑定主键
     * @return 结果
     */
    @Override
    public int deleteBlogUserSocialBySocialId(Long socialId)
    {
        return blogUserSocialMapper.deleteBlogUserSocialBySocialId(socialId);
    }
}
