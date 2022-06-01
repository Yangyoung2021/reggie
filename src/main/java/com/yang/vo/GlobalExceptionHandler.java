package com.yang.vo;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLIntegrityConstraintViolationException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public R<Object> getException(SQLIntegrityConstraintViolationException exception) {
        String message = exception.getMessage();

        if (message.contains("Duplicate entry")) {
            //是预计出现的异常
            String[] msg = message.split(" ");
            String name = msg[2].substring(1, msg[2].length() - 1);
            return R.error(name + "已存在");
        }
        return R.error("未知错误");
    }

    @ExceptionHandler(CustomException.class)
    public R<Object> getException(CustomException exception) {
        String message = exception.getMessage();
        return R.error(message);
    }

}
