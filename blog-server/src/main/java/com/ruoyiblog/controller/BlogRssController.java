package com.ruoyiblog.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.HashMap;
import java.util.TimeZone;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyiblog.domain.BlogArticle;
import com.ruoyiblog.domain.BlogSiteConfig;
import com.ruoyiblog.service.IBlogArticleService;
import com.ruoyiblog.service.IBlogSiteConfigService;
import jakarta.servlet.http.HttpServletRequest;

@RestController
public class BlogRssController
{
    @Autowired
    private IBlogArticleService blogArticleService;

    @Autowired
    private IBlogSiteConfigService siteConfigService;

    @GetMapping(value = "/blog/rss", produces = MediaType.APPLICATION_XML_VALUE)
    public ResponseEntity<String> rss(HttpServletRequest request)
    {
        Map<String, String> cfg = new HashMap<>();
        for (BlogSiteConfig c : siteConfigService.selectBlogSiteConfigList(new BlogSiteConfig()))
        {
            cfg.put(c.getConfigKey(), c.getConfigValue());
        }
        String siteName = cfg.getOrDefault("site_name", "EchoPress");
        String siteDesc = cfg.getOrDefault("site_description", "");

        String baseUrl = request.getScheme() + "://" + request.getServerName();
        int port = request.getServerPort();
        if (port != 80 && port != 443)
        {
            baseUrl += ":" + port;
        }

        BlogArticle cond = new BlogArticle();
        cond.setStatus("1");
        cond.setDelFlag("0");
        List<BlogArticle> articles = blogArticleService.selectBlogArticleList(cond);
        if (articles.size() > 20)
        {
            articles = articles.subList(0, 20);
        }

        SimpleDateFormat rfc822 = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z", Locale.US);
        rfc822.setTimeZone(TimeZone.getTimeZone("GMT"));

        StringBuilder xml = new StringBuilder();
        xml.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
        xml.append("<rss version=\"2.0\" xmlns:atom=\"http://www.w3.org/2005/Atom\">\n");
        xml.append("<channel>\n");
        xml.append("<title>").append(escape(siteName)).append("</title>\n");
        xml.append("<link>").append(escape(baseUrl)).append("</link>\n");
        xml.append("<description>").append(escape(siteDesc)).append("</description>\n");
        xml.append("<atom:link href=\"").append(escape(baseUrl)).append("/blog/rss\" rel=\"self\" type=\"application/rss+xml\"/>\n");

        for (BlogArticle a : articles)
        {
            String link = baseUrl + "/article/" + a.getArticleId();
            String desc = a.getSummary() != null && !a.getSummary().isEmpty()
                    ? a.getSummary() : (a.getContent() != null ? a.getContent().substring(0, Math.min(200, a.getContent().length())) : "");
            Date pubDate = a.getPublishTime() != null ? a.getPublishTime() : a.getCreateTime();

            xml.append("<item>\n");
            xml.append("<title>").append(escape(a.getTitle())).append("</title>\n");
            xml.append("<link>").append(escape(link)).append("</link>\n");
            xml.append("<description>").append(escape(desc)).append("</description>\n");
            xml.append("<pubDate>").append(pubDate != null ? rfc822.format(pubDate) : "").append("</pubDate>\n");
            xml.append("<guid>").append(escape(link)).append("</guid>\n");
            xml.append("</item>\n");
        }

        xml.append("</channel>\n");
        xml.append("</rss>");

        return ResponseEntity.ok(xml.toString());
    }

    private String escape(String s)
    {
        if (s == null) return "";
        return s.replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&apos;");
    }
}
