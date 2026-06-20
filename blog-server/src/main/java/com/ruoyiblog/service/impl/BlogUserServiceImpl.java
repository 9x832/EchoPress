package com.ruoyiblog.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import com.ruoyiblog.domain.BlogUser;
import com.ruoyiblog.domain.dto.LoginDTO;
import com.ruoyiblog.domain.dto.RegisterDTO;
import com.ruoyiblog.domain.vo.BlogLoginUser;
import com.ruoyiblog.mapper.BlogUserMapper;
import com.ruoyiblog.service.IBlogUserService;
import com.ruoyiblog.common.constant.CacheConstants;
import com.ruoyiblog.common.core.redis.RedisCache;
import com.ruoyiblog.common.exception.ServiceException;
import com.ruoyiblog.common.utils.JwtUtils;
import com.ruoyiblog.common.utils.DateUtils;
import com.ruoyiblog.common.utils.StringUtils;
import com.ruoyiblog.common.utils.IpUtils;

/**
 * 博客用户Service业务层处理
 *
 * @author ruoyi
 * @date 2026-06-20
 */
@Service
public class BlogUserServiceImpl implements IBlogUserService
{
    @Autowired
    private BlogUserMapper blogUserMapper;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private RedisCache redisCache;

    @Override
    public BlogUser selectBlogUserByUserId(Long userId)
    {
        return blogUserMapper.selectBlogUserByUserId(userId);
    }

    @Override
    public List<BlogUser> selectBlogUserList(BlogUser blogUser)
    {
        return blogUserMapper.selectBlogUserList(blogUser);
    }

    @Override
    public int insertBlogUser(BlogUser blogUser)
    {
        blogUser.setCreateTime(DateUtils.getNowDate());
        return blogUserMapper.insertBlogUser(blogUser);
    }

    @Override
    public int updateBlogUser(BlogUser blogUser)
    {
        blogUser.setUpdateTime(DateUtils.getNowDate());
        return blogUserMapper.updateBlogUser(blogUser);
    }

    @Override
    public int deleteBlogUserByUserIds(Long[] userIds)
    {
        return blogUserMapper.deleteBlogUserByUserIds(userIds);
    }

    @Override
    public int deleteBlogUserByUserId(Long userId)
    {
        return blogUserMapper.deleteBlogUserByUserId(userId);
    }

    @Override
    public boolean checkUserNameUnique(String userName)
    {
        return blogUserMapper.checkUserNameUnique(userName) == null;
    }

    @Override
    public boolean checkEmailUnique(String email)
    {
        return blogUserMapper.checkEmailUnique(email) == null;
    }

    @Override
    public Long register(RegisterDTO dto)
    {
        if (StringUtils.isEmpty(dto.getUserName()))
        {
            throw new IllegalArgumentException("用户名不能为空");
        }
        if (dto.getUserName().length() < 2 || dto.getUserName().length() > 30)
        {
            throw new IllegalArgumentException("用户名长度必须在2到30个字符之间");
        }
        if (StringUtils.isEmpty(dto.getNickName()))
        {
            throw new IllegalArgumentException("昵称不能为空");
        }
        if (dto.getNickName().length() < 2 || dto.getNickName().length() > 30)
        {
            throw new IllegalArgumentException("昵称长度必须在2到30个字符之间");
        }
        if (StringUtils.isEmpty(dto.getEmail()))
        {
            throw new IllegalArgumentException("邮箱不能为空");
        }
        if (!dto.getEmail().matches("^[\\w.\\-]+@[\\w\\-]+(\\.[\\w\\-]+)+$"))
        {
            throw new IllegalArgumentException("邮箱格式不正确");
        }
        if (StringUtils.isEmpty(dto.getPassword()))
        {
            throw new IllegalArgumentException("密码不能为空");
        }
        if (dto.getPassword().length() < 6 || dto.getPassword().length() > 100)
        {
            throw new IllegalArgumentException("密码长度必须在6到100个字符之间");
        }

        validateCaptcha(dto.getCode(), dto.getUuid());

        if (!checkUserNameUnique(dto.getUserName()))
        {
            throw new IllegalArgumentException("用户名已存在");
        }
        if (!checkEmailUnique(dto.getEmail()))
        {
            throw new IllegalArgumentException("邮箱已存在");
        }

        BlogUser user = new BlogUser();
        user.setUserName(dto.getUserName());
        user.setNickName(dto.getNickName());
        user.setEmail(dto.getEmail());
        user.setPassword(bCryptPasswordEncoder.encode(dto.getPassword()));
        user.setUserType("00");
        user.setStatus("0");
        user.setDelFlag("0");
        user.setCreateTime(DateUtils.getNowDate());
        blogUserMapper.insertBlogUser(user);
        return user.getUserId();
    }

    @Override
    public String login(LoginDTO dto)
    {
        validateCaptcha(dto.getCode(), dto.getUuid());

        BlogUser user = blogUserMapper.selectBlogUserByUserName(dto.getAccount());
        if (user == null)
        {
            user = blogUserMapper.checkEmailUnique(dto.getAccount());
        }
        if (user == null)
        {
            throw new IllegalArgumentException("账号或密码错误");
        }

        if (!bCryptPasswordEncoder.matches(dto.getPassword(), user.getPassword()))
        {
            throw new IllegalArgumentException("账号或密码错误");
        }
        if (!"0".equals(user.getStatus()))
        {
            throw new IllegalArgumentException("账号已被停用");
        }

        BlogLoginUser loginUser = new BlogLoginUser(user.getUserId(), user.getUserName(),
                user.getNickName(), user.getUserType());
        String token = jwtUtils.createToken(loginUser);

        BlogUser updateUser = new BlogUser();
        updateUser.setUserId(user.getUserId());
        updateUser.setLoginIp(IpUtils.getIpAddr());
        updateUser.setLoginDate(DateUtils.getNowDate());
        updateUser.setLoginCount(user.getLoginCount() == null ? 1L : user.getLoginCount() + 1);
        blogUserMapper.updateBlogUser(updateUser);

        return token;
    }

    private void validateCaptcha(String code, String uuid)
    {
        String verifyKey = CacheConstants.CAPTCHA_CODE_KEY + StringUtils.nvl(uuid, "");
        String captcha = redisCache.getCacheObject(verifyKey);
        redisCache.deleteObject(verifyKey);
        if (captcha == null)
        {
            throw new ServiceException("验证码已过期");
        }
        if (!code.equalsIgnoreCase(captcha))
        {
            throw new ServiceException("验证码错误");
        }
    }
}
