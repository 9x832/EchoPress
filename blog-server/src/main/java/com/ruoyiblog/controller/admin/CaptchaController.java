package com.ruoyiblog.controller.admin;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import javax.imageio.ImageIO;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import com.google.code.kaptcha.Producer;
import com.ruoyiblog.common.constant.CacheConstants;
import com.ruoyiblog.common.constant.Constants;
import com.ruoyiblog.common.core.AjaxResult;
import com.ruoyiblog.common.core.redis.RedisCache;

@RestController
public class CaptchaController
{
    @Autowired
    private Producer captchaProducer;

    @Autowired
    private RedisCache redisCache;

    @GetMapping("/captchaImage")
    public AjaxResult getCode(HttpServletResponse response) throws IOException
    {
        String uuid = UUID.randomUUID().toString().replaceAll("-", "");
        String verifyKey = CacheConstants.CAPTCHA_CODE_KEY + uuid;

        String capStr = captchaProducer.createText();
        BufferedImage image = captchaProducer.createImage(capStr);
        redisCache.setCacheObject(verifyKey, capStr, Constants.CAPTCHA_EXPIRATION, TimeUnit.MINUTES);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(image, "jpg", baos);
        String base64 = Base64.getEncoder().encodeToString(baos.toByteArray());

        Map<String, Object> result = new HashMap<>();
        result.put("uuid", uuid);
        result.put("img", base64);
        return AjaxResult.success(result);
    }
}
