package com.ruoyiblog.common.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import com.ruoyiblog.common.constant.CacheConstants;
import com.ruoyiblog.common.constant.Constants;
import com.ruoyiblog.common.core.redis.RedisCache;
import com.ruoyiblog.domain.vo.BlogLoginUser;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JwtUtils
{
    @Value("${blog.token.secret}")
    private String secret;

    @Value("${blog.token.expireTime}")
    private int expireTime;

    @Autowired
    private RedisCache redisCache;

    public String createToken(BlogLoginUser loginUser)
    {
        String token = java.util.UUID.randomUUID().toString().replaceAll("-", "");
        loginUser.setToken(token);
        loginUser.setLoginTime(System.currentTimeMillis());
        loginUser.setExpireTime(loginUser.getLoginTime() + expireTime * 60 * 1000L);
        loginUser.setIpaddr(IpUtils.getIpAddr());

        String userKey = getTokenKey(token);
        redisCache.setCacheObject(userKey, loginUser, expireTime, TimeUnit.MINUTES);

        Map<String, Object> claims = new HashMap<>();
        claims.put(Constants.LOGIN_USER_KEY, token);
        claims.put(Constants.JWT_USERNAME, loginUser.getUsername());
        return Jwts.builder()
                .setClaims(claims)
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }

    public BlogLoginUser getLoginUser(HttpServletRequest request)
    {
        String token = getTokenFromRequest(request);
        if (StringUtils.isEmpty(token))
        {
            return null;
        }
        try
        {
            Claims claims = parseToken(token);
            String uuid = (String) claims.get(Constants.LOGIN_USER_KEY);
            String userKey = getTokenKey(uuid);
            BlogLoginUser user = redisCache.getCacheObject(userKey);
            if (user != null)
            {
                refreshToken(user);
            }
            return user;
        }
        catch (Exception e)
        {
            return null;
        }
    }

    public void refreshToken(BlogLoginUser loginUser)
    {
        loginUser.setLoginTime(System.currentTimeMillis());
        loginUser.setExpireTime(loginUser.getLoginTime() + expireTime * 60 * 1000L);
        String userKey = getTokenKey(loginUser.getToken());
        redisCache.setCacheObject(userKey, loginUser, expireTime, TimeUnit.MINUTES);
    }

    private String getTokenFromRequest(HttpServletRequest request)
    {
        String token = request.getHeader("Authorization");
        if (StringUtils.isNotEmpty(token) && token.startsWith(Constants.TOKEN_PREFIX))
        {
            token = token.substring(Constants.TOKEN_PREFIX.length());
        }
        return token;
    }

    private Claims parseToken(String token)
    {
        return Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody();
    }

    private String getTokenKey(String uuid)
    {
        return CacheConstants.LOGIN_TOKEN_KEY + uuid;
    }
}
