package com.travelplanner.model;

import java.util.List;

public class ApiResponse<T> {
    private Integer code;
    private String message;
    private T data;

    public Integer getCode() { return code; }
    public void setCode(Integer code) { this.code = code; }
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    public T getData() { return data; }
    public void setData(T data) { this.data = data; }

    public boolean isSuccess() {
        return code != null && code == 200;
    }
}
