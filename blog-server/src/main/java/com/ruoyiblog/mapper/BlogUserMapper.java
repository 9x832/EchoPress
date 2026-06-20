package com.ruoyiblog.mapper;

import java.util.List;
import com.ruoyiblog.domain.BlogUser;

/**
 * 博客用户Mapper接口
 *
 * @author ruoyi
 * @date 2026-06-20
 */
public interface BlogUserMapper
{
    /**
     * 查询博客用户
     *
     * @param userId 博客用户主键
     * @return 博客用户
     */
    public BlogUser selectBlogUserByUserId(Long userId);

    /**
     * 查询博客用户列表
     *
     * @param blogUser 博客用户
     * @return 博客用户集合
     */
    public List<BlogUser> selectBlogUserList(BlogUser blogUser);

    /**
     * 新增博客用户
     *
     * @param blogUser 博客用户
     * @return 结果
     */
    public int insertBlogUser(BlogUser blogUser);

    /**
     * 修改博客用户
     *
     * @param blogUser 博客用户
     * @return 结果
     */
    public int updateBlogUser(BlogUser blogUser);

    /**
     * 删除博客用户
     *
     * @param userId 博客用户主键
     * @return 结果
     */
    public int deleteBlogUserByUserId(Long userId);

    /**
     * 批量删除博客用户
     *
     * @param userIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteBlogUserByUserIds(Long[] userIds);

    /**
     * 通过用户名查询博客用户
     *
     * @param userName 用户名
     * @return 博客用户
     */
    public BlogUser selectBlogUserByUserName(String userName);

    /**
     * 校验用户名是否唯一
     *
     * @param userName 用户名
     * @return 结果
     */
    public BlogUser checkUserNameUnique(String userName);

    /**
     * 校验邮箱是否唯一
     *
     * @param email 邮箱
     * @return 结果
     */
    public BlogUser checkEmailUnique(String email);
}
