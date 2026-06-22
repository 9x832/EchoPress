package com.ruoyiblog.controller.admin;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyiblog.common.core.AjaxResult;
import com.ruoyiblog.mapper.BlogArticleMapper;
import com.ruoyiblog.mapper.BlogArticleViewMapper;
import com.ruoyiblog.service.IBlogCategoryService;
import com.ruoyiblog.service.IBlogCommentService;
import com.ruoyiblog.service.IBlogMomentService;
import com.ruoyiblog.service.IBlogResourceService;
import com.ruoyiblog.service.IBlogTagService;
import com.ruoyiblog.service.IBlogUserService;
import com.ruoyiblog.domain.BlogArticle;
import com.ruoyiblog.domain.BlogCategory;
import com.ruoyiblog.domain.BlogComment;
import com.ruoyiblog.domain.BlogMoment;
import com.ruoyiblog.domain.BlogResource;
import com.ruoyiblog.domain.BlogTag;
import com.ruoyiblog.domain.BlogUser;

@RestController
@RequestMapping("/blog/admin/dashboard")
public class BlogDashboardAdminController
{
    @Autowired
    private BlogArticleMapper blogArticleMapper;

    @Autowired
    private BlogArticleViewMapper blogArticleViewMapper;

    @Autowired
    private IBlogCategoryService blogCategoryService;

    @Autowired
    private IBlogTagService blogTagService;

    @Autowired
    private IBlogUserService blogUserService;

    @Autowired
    private IBlogCommentService blogCommentService;

    @Autowired
    private IBlogMomentService blogMomentService;

    @Autowired
    private IBlogResourceService blogResourceService;

    @GetMapping
    public AjaxResult dashboard()
    {
        Map<String, Object> stats = new HashMap<>();
        stats.put("articleCount", blogArticleMapper.selectBlogArticleList(new BlogArticle()).size());
        stats.put("categoryCount", blogCategoryService.selectBlogCategoryList(new BlogCategory()).size());
        stats.put("tagCount", blogTagService.selectBlogTagList(new BlogTag()).size());
        stats.put("userCount", blogUserService.selectBlogUserList(new BlogUser()).size());
        stats.put("commentCount", blogCommentService.selectBlogCommentList(new BlogComment()).size());
        stats.put("momentCount", blogMomentService.selectBlogMomentList(new BlogMoment()).size());
        stats.put("totalViews", blogArticleMapper.selectTotalViews());
        stats.put("resourceCount", blogResourceService.selectBlogResourceList(new BlogResource()).size());

        List<Map<String, Object>> publishTrend = blogArticleMapper.selectPublishTrend(7);
        List<Map<String, Object>> viewTrend = blogArticleViewMapper.selectViewTrend(7);

        Map<String, Object> result = new HashMap<>();
        result.put("stats", stats);
        result.put("publishTrend", publishTrend);
        result.put("viewTrend", viewTrend);

        return AjaxResult.success(result);
    }
}
