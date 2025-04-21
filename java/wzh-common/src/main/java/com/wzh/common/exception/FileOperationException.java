package com.wzh.common.exception;

/**
 * 文件操作异常类
 */
public class FileOperationException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public FileOperationException(String message) {
        super(message);
    }

    public FileOperationException(String message, Throwable cause) {
        super(message, cause);
    }
}