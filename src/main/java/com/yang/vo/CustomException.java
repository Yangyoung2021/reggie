package com.yang.vo;

/**
 * 自定义异常信息
 */
public class CustomException extends RuntimeException {
    public CustomException(String message) {
        super(message);
    }
}
