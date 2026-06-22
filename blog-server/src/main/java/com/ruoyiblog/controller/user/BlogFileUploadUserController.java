package com.ruoyiblog.controller.user;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import com.ruoyiblog.common.core.AjaxResult;
import com.ruoyiblog.interceptor.LoginRequired;

@RestController
@RequestMapping("/blog/user/upload")
@LoginRequired
public class BlogFileUploadUserController
{
    private static final String UPLOAD_BASE = "upload/images/";
    private static final long MAX_SIZE = 5 * 1024 * 1024; // 5MB

    @PostMapping("/image")
    public AjaxResult uploadImage(@RequestParam("file") MultipartFile file)
    {
        if (file.isEmpty())
        {
            return AjaxResult.error("文件为空");
        }
        if (file.getSize() > MAX_SIZE)
        {
            return AjaxResult.error("文件大小不能超过5MB");
        }

        String originalName = file.getOriginalFilename();
        String ext = "";
        if (originalName != null && originalName.contains("."))
        {
            ext = originalName.substring(originalName.lastIndexOf(".")).toLowerCase();
        }

        if (!".jpg".equals(ext) && !".jpeg".equals(ext) && !".png".equals(ext)
                && !".gif".equals(ext) && !".webp".equals(ext) && !".bmp".equals(ext))
        {
            return AjaxResult.error("不支持的图片格式");
        }

        String dateDir = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy/MM"));
        String fileName = UUID.randomUUID().toString().replace("-", "") + ext;

        File dir = new File(UPLOAD_BASE + dateDir);
        if (!dir.exists())
        {
            dir.mkdirs();
        }

        try
        {
            File dest = new File(dir, fileName);
            java.nio.file.Files.write(dest.toPath(), file.getBytes());
        }
        catch (IOException e)
        {
            return AjaxResult.error("上传失败: " + e.getMessage());
        }

        Map<String, String> data = new HashMap<>();
        data.put("url", "/" + UPLOAD_BASE + dateDir + "/" + fileName);
        return AjaxResult.success(data);
    }
}
