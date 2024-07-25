package com.spring.demo.dto;

import java.io.Serializable;

public class Result<T> implements Serializable {
    private static final long serialVersionUID = 1444605237688228650L;
    private T data;
    private boolean success;
    private int code;
    private String message;

    private Result() {
        this.success = Boolean.FALSE;
        this.code = 200;
    }

    public static <T> Result<T> success() {
        Result<T> result = new Result();
        result.setData(null);
        result.setSuccess(true);
        return result;
    }

    public static <T> Result<T> success(T data) {
        Result<T> result = new Result();
        result.setData(data);
        result.setSuccess(true);
        return result;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
