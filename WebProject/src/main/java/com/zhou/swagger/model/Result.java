package com.zhou.swagger.model;

import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;

/**
 * Created by Zhou Yibing on 2015/11/10.
 */
@ApiModel("result")
public class Result<T> {

    @ApiModelProperty("响应码")
    private String code;
    @ApiModelProperty("响应数据")
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
