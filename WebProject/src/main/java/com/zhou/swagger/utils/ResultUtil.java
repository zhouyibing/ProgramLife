package com.zhou.swagger.utils;

import com.zhou.swagger.model.Result;

/**
 * Created by Zhou Yibing on 2015/11/10.
 */
public class ResultUtil {
    public static final String SUCCESS="1";
    public  static <T> Result<T> buildSuccessResult(T data) {
        Result<T> result = new Result<>();
        result.setData(data);
        result.setCode(SUCCESS);
        return result;
    }

    public static <T> Result<T> buildFailureResult(String code,T message){
        Result<T> result = new Result<>();
        result.setCode(code);
        result.setData(message);
        return result;
    }
}
