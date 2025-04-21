package com.wzh.front.controller;

import com.wzh.common.exception.FileOperationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import com.wzh.common.core.domain.AjaxResult;

/**
 * 全局异常处理器
 */
@RestControllerAdvice
public class FrontGlobalExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(FrontGlobalExceptionHandler.class);

    /**
     * 处理文件操作异常
     */
    @ExceptionHandler(FileOperationException.class)
    public AjaxResult handleFileOperationException(FileOperationException e) {
        logger.error(e.getMessage(), e);
        return AjaxResult.error(e.getMessage());
    }

    /**
     * 处理系统异常
     */
    @ExceptionHandler(Exception.class)
    public AjaxResult handleException(Exception e) {
        logger.error(e.getMessage(), e);
        return AjaxResult.error("系统繁忙，请稍后再试");
    }
}