package com.smallmq.product.exception;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(basePackages = "com.smallmq.product.controller")
public class MyException {

    @ExceptionHandler(Exception.class)
    public void ValidException(Exception e){
        e.printStackTrace();
    }

}
