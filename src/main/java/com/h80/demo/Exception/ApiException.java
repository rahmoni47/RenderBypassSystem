package com.h80.demo.Exception;

public class ApiException extends RuntimeException {
    public ApiException(String msg) {
        super(msg);
    }
}
