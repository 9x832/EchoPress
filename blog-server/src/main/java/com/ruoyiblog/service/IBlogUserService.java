package com.ruoyiblog.service;

import java.util.HashMap;
import java.util.List;
import com.ruoyiblog.domain.BlogUser;
import com.ruoyiblog.domain.dto.LoginDTO;
import com.ruoyiblog.domain.dto.RegisterDTO;

/**
 * 博客用户Service接口
 *
 * @author ruoyi
 * @date 2026-06-20
 */
public interface IBlogUserService
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
     * 批量删除博客用户
     *
     * @param userIds 需要删除的博客用户主键集合
     * @return 结果
     */
    public int deleteBlogUserByUserIds(Long[] userIds);

    /**
     * 删除博客用户信息
     *
     * @param userId 博客用户主键
     * @return 结果
     */
    public int deleteBlogUserByUserId(Long userId);

    /**
     * 校验用户名是否唯一
     *
     * @param userName 用户名
     * @return true 唯一 / false 不唯一
     */
    public boolean checkUserNameUnique(String userName);

    /**
     * 校验邮箱是否唯一
     *
     * @param email 邮箱
     * @return true 唯一 / false 不唯一
     */
    public boolean checkEmailUnique(String email);

    /**
     * 用户注册
     *
     * @param registerDTO 注册信息
     * @return 用户ID
     */
    public Long register(RegisterDTO registerDTO);

    /**
     * 用户登录
     *
     * @param loginDTO 登录信息
     * @return token + userType + nickName
     */
    public HashMap<String, Object> login(LoginDTO loginDTO);
}
