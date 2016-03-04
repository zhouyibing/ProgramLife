package com.zhou.swagger.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by Zhou Yibing on 2016/3/4.
 */
@Controller
@RequestMapping("/test")
public class TestController {

    @ResponseBody
    @RequestMapping(value = { "/testJsonp" }, method = RequestMethod.GET)
    public String testJsonp(HttpServletRequest request){
        String callbackName  = request.getParameter("jsoncallback");
        return callbackName+"({message:'jsonp response'})";
    }
}
