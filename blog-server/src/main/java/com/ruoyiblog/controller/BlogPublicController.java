package com.ruoyiblog.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyiblog.common.core.AjaxResult;
import com.ruoyiblog.domain.BlogArticle;
import com.ruoyiblog.domain.BlogCarousel;
import com.ruoyiblog.domain.BlogCategory;
import com.ruoyiblog.domain.BlogLink;
import com.ruoyiblog.domain.BlogMoment;
import com.ruoyiblog.domain.BlogUser;
import com.ruoyiblog.domain.BlogPage;
import com.ruoyiblog.domain.BlogTag;
import com.ruoyiblog.domain.BlogSiteConfig;
import com.ruoyiblog.domain.BlogSubscribe;
import com.ruoyiblog.service.IBlogArticleService;
import com.ruoyiblog.service.IBlogCarouselService;
import com.ruoyiblog.service.IBlogCategoryService;
import com.ruoyiblog.service.IBlogLinkService;
import com.ruoyiblog.service.IBlogMomentService;
import com.ruoyiblog.service.IBlogPageService;
import com.ruoyiblog.service.IBlogSiteConfigService;
import com.ruoyiblog.service.IBlogSubscribeService;
import com.ruoyiblog.service.IBlogTagService;
import com.ruoyiblog.service.IBlogUserService;
import com.ruoyiblog.common.utils.StringUtils;

/**
 * 博客前台 - 分类/标签/友链/页面公开接口
 */
@RestController
public class BlogPublicController
{
    @Autowired
    private IBlogCategoryService categoryService;

    @Autowired
    private IBlogTagService tagService;

    @Autowired
    private IBlogLinkService linkService;

    @Autowired
    private IBlogPageService pageService;

    @Autowired
    private IBlogCarouselService carouselService;

    @Autowired
    private IBlogSiteConfigService siteConfigService;

    @Autowired
    private IBlogSubscribeService subscribeService;

    @Autowired
    private IBlogMomentService momentService;

    @Autowired
    private IBlogUserService blogUserService;

    @Autowired
    private IBlogArticleService blogArticleService;

    @GetMapping("/blog/category/list")
    public AjaxResult categoryList()
    {
        BlogCategory cond = new BlogCategory();
        cond.setStatus("0");
        List<BlogCategory> list = categoryService.selectBlogCategoryList(cond);
        BlogArticle articleCond = new BlogArticle();
        articleCond.setStatus("1");
        articleCond.setDelFlag("0");
        List<BlogArticle> articles = blogArticleService.selectBlogArticleList(articleCond);
        Map<Long, Integer> countMap = new HashMap<>();
        for (BlogArticle a : articles) {
            if (a.getCategoryId() != null) {
                countMap.merge(a.getCategoryId(), 1, Integer::sum);
            }
        }
        for (BlogCategory cat : list) {
            cat.setArticleCount(countMap.getOrDefault(cat.getCategoryId(), 0));
        }
        return AjaxResult.success(list);
    }

    @GetMapping("/blog/category/{categoryId}")
    public AjaxResult categoryInfo(@PathVariable("categoryId") Long categoryId)
    {
        BlogCategory cat = categoryService.selectBlogCategoryByCategoryId(categoryId);
        if (cat == null)
        {
            return AjaxResult.error("分类不存在");
        }
        return AjaxResult.success(cat);
    }

    @GetMapping("/blog/tag/list")
    public AjaxResult tagList()
    {
        BlogTag cond = new BlogTag();
        cond.setStatus("0");
        List<BlogTag> list = tagService.selectBlogTagList(cond);
        return AjaxResult.success(list);
    }

    @GetMapping("/blog/tag/{tagId}")
    public AjaxResult tagInfo(@PathVariable("tagId") Long tagId)
    {
        BlogTag tag = tagService.selectBlogTagByTagId(tagId);
        if (tag == null)
        {
            return AjaxResult.error("标签不存在");
        }
        return AjaxResult.success(tag);
    }

    @GetMapping("/blog/link/list")
    public AjaxResult linkList()
    {
        BlogLink cond = new BlogLink();
        cond.setStatus("0");
        List<BlogLink> list = linkService.selectBlogLinkList(cond);
        return AjaxResult.success(list);
    }

    @GetMapping("/blog/page/list")
    public AjaxResult pageList()
    {
        BlogPage cond = new BlogPage();
        cond.setStatus("0");
        cond.setIsShow("1");
        List<BlogPage> list = pageService.selectBlogPageList(cond);
        return AjaxResult.success(list);
    }

    @GetMapping("/blog/page/{slug}")
    public AjaxResult pageBySlug(@PathVariable("slug") String slug)
    {
        BlogPage cond = new BlogPage();
        cond.setSlug(slug);
        cond.setStatus("0");
        List<BlogPage> list = pageService.selectBlogPageList(cond);
        if (list.isEmpty())
        {
            return AjaxResult.error("页面不存在");
        }
        return AjaxResult.success(list.get(0));
    }

    @GetMapping("/blog/carousel/list")
    public AjaxResult carouselList()
    {
        BlogCarousel cond = new BlogCarousel();
        cond.setStatus("0");
        List<BlogCarousel> list = carouselService.selectBlogCarouselList(cond);
        return AjaxResult.success(list);
    }

    @GetMapping("/blog/site/config")
    public AjaxResult siteConfig()
    {
        List<BlogSiteConfig> list = siteConfigService.selectBlogSiteConfigList(new BlogSiteConfig());
        Map<String, String> configMap = new HashMap<>();
        for (BlogSiteConfig cfg : list)
        {
            configMap.put(cfg.getConfigKey(), cfg.getConfigValue());
        }
        return AjaxResult.success(configMap);
    }

    @PostMapping("/blog/subscribe")
    public AjaxResult subscribe(@RequestBody BlogSubscribe subscribe)
    {
        if (StringUtils.isEmpty(subscribe.getEmail()))
        {
            return AjaxResult.error("邮箱不能为空");
        }
        if (!subscribe.getEmail().matches("^[\\w.%+-]+@[\\w.-]+\\.[a-zA-Z]{2,}$"))
        {
            return AjaxResult.error("邮箱格式不正确");
        }
        Long targetUserId = subscribe.getUserId();
        if (targetUserId == null)
        {
            BlogUser cond = new BlogUser();
            cond.setUserType("01");
            List<BlogUser> owners = blogUserService.selectBlogUserList(cond);
            targetUserId = owners.isEmpty() ? 1L : owners.get(0).getUserId();
        }
        BlogSubscribe dupCond = new BlogSubscribe();
        dupCond.setEmail(subscribe.getEmail());
        dupCond.setUserId(targetUserId);
        if (!subscribeService.selectBlogSubscribeList(dupCond).isEmpty())
        {
            return AjaxResult.error("该邮箱已订阅该作者");
        }
        subscribe.setUserId(targetUserId);
        subscribe.setStatus("0");
        subscribe.setVerified("1");
        subscribeService.insertBlogSubscribe(subscribe);
        return AjaxResult.success();
    }

    @GetMapping("/blog/moment/list")
    public AjaxResult momentList()
    {
        BlogMoment cond = new BlogMoment();
        cond.setStatus("0");
        cond.setDelFlag("0");
        List<BlogMoment> list = momentService.selectBlogMomentList(cond);
        for (BlogMoment m : list) {
            if (m.getUserId() != null) {
                BlogUser user = blogUserService.selectBlogUserByUserId(m.getUserId());
                if (user != null) {
                    m.setNickName(user.getNickName());
                }
            }
        }
        return AjaxResult.success(list);
    }

    /** 获取站点主理人信息 */
    @GetMapping("/blog/site/owner")
    public AjaxResult siteOwner()
    {
        BlogUser cond = new BlogUser();
        cond.setUserType("01");
        List<BlogUser> owners = blogUserService.selectBlogUserList(cond);
        if (owners.isEmpty())
        {
            return AjaxResult.error("站点未初始化");
        }
        BlogUser owner = owners.get(0);
        Map<String, Object> result = new HashMap<>();
        result.put("userId", owner.getUserId());
        result.put("nickName", owner.getNickName());
        return AjaxResult.success(result);
    }
}
