package com.ruoyiblog.mapper;

import java.util.List;
import com.ruoyiblog.domain.BlogUserSocial;

/**
 * 用户第三方登录绑定Mapper接口
 *
 * @author ruoyi
 * @date 2026-06-20
 */
public interface BlogUserSocialMapper
{
    /**
     * 查询用户第三方登录绑定
     *
     * @param socialId 用户第三方登录绑定主键
     * @return 用户第三方登录绑定
     */
    public BlogUserSocial selectBlogUserSocialBySocialId(Long socialId);

    /**
     * 查询用户第三方登录绑定列表
     *
     * @param blogUserSocial 用户第三方登录绑定
     * @return 用户第三方登录绑定集合
     */
    public List<BlogUserSocial> selectBlogUserSocialList(BlogUserSocial blogUserSocial);

    /**
     * 新增用户第三方登录绑定
     *
     * @param blogUserSocial 用户第三方登录绑定
     * @return 结果
     */
    public int insertBlogUserSocial(BlogUserSocial blogUserSocial);

    /**
     * 修改用户第三方登录绑定
     *
     * @param blogUserSocial 用户第三方登录绑定
     * @return 结果
     */
    public int updateBlogUserSocial(BlogUserSocial blogUserSocial);

    /**
     * 删除用户第三方登录绑定
     *
     * @param socialId 用户第三方登录绑定主键
     * @return 结果
     */
    public int deleteBlogUserSocialBySocialId(Long socialId);

    /**
     * 批量删除用户第三方登录绑定
     *
     * @param socialIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteBlogUserSocialBySocialIds(Long[] socialIds);
}
