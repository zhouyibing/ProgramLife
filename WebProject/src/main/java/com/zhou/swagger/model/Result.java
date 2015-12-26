package com.zhou.swagger.model;
/**
 * Created by Zhou Yibing on 2015/11/10.
 */
public class Result<T> {

    private String code;
    private T data;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
