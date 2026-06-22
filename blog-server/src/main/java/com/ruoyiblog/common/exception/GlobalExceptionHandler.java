package com.ruoyiblog.common.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import com.ruoyiblog.common.core.AjaxResult;

@RestControllerAdvice
public class GlobalExceptionHandler
{
    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(ServiceException.class)
    public AjaxResult handleServiceException(ServiceException e)
    {
        Integer code = e.getCode();
        if (code != null)
        {
            return AjaxResult.error(code, e.getMessage());
        }
        return AjaxResult.error(e.getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public AjaxResult handleIllegalArgumentException(IllegalArgumentException e)
    {
        return AjaxResult.error(e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public AjaxResult handleException(Exception e)
    {
        log.error("服务器内部错误", e);
        return AjaxResult.error("服务器内部错误");
    }
}
