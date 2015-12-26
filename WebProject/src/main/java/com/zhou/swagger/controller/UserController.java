package com.zhou.swagger.controller;

import java.util.List;

import com.zhou.swagger.model.Result;
import com.zhou.swagger.model.User;
import com.zhou.swagger.service.UserService;
import com.zhou.swagger.utils.ResultUtil;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
/**
 * 用户操作Controller
 */

@Api(value = "user-api", description = "有关于用户的CURD操作", position = 5)
@Controller
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;
    private Logger logger = Logger.getLogger(UserController.class);
    /**
     * 注册用户
     * @param user
     */
    @ApiOperation(value = "注册", notes = "注册用户", position = 3,httpMethod = "POST", response = ResponseEntity.class,produces = "application/json; charset=utf-8")
    @ResponseBody
    @RequestMapping(value = { "/regist" }, method = RequestMethod.POST)
    public ResponseEntity<?> regist(@RequestBody User user) {
        userService.save(user);
        Result<String> result = ResultUtil.buildSuccessResult("注册成功");
        return new ResponseEntity<Result<String>>(result, HttpStatus.OK);
    }
     
    /**
     * 根据pk查找用户
     * @param userPk
     * @return
     */
    @ApiOperation(value = "根据pk查找用户", notes = "返回用户实体对象", response = User.class, position = 2,httpMethod = "GET",produces = "application/json; charset=utf-8")
    @ResponseBody
    @RequestMapping(value = { "/{userPk}" }, method = RequestMethod.GET)
    public ResponseEntity<?> findByPk(
            @ApiParam(value = "填写Pk", allowableValues = "range[1,5]", required = true, defaultValue = "userPk", allowMultiple = true) @PathVariable("userPk") Integer userPk) {
        Result<User> result = ResultUtil.buildSuccessResult(userService.findByPk(userPk));
        return new ResponseEntity<Result<User>>(result, HttpStatus.OK);
    }
     
    /**
     * 测试
     * @param who
     * @return
     */
    @ApiOperation(value = "Hellow World", notes = "测试功能", position = 1,httpMethod = "GET",response = ResponseEntity.class,produces = "application/json; charset=utf-8")
    @ResponseBody
    @RequestMapping(value = { "/hello/{who:.+}" }, method = RequestMethod.GET)
    public ResponseEntity<?> hello(
            @ApiParam(value = "填写名称") @PathVariable("who") String who) {
        Result<String> result = ResultUtil.buildSuccessResult( "Hello guys" + " " + who + "!");
        return new ResponseEntity<Result<String>>(result, HttpStatus.OK);
    }
    /**
     * 查询所有
     * @return
     */
    @ApiOperation(value = "获取所有用户", notes = "返回用户实体对象集合", position = 5,httpMethod = "GET",response = ResponseEntity.class,produces = "application/json; charset=utf-8")
    @RequestMapping(value = "/findAll", method = RequestMethod.GET)
    public @ResponseBody ResponseEntity<?>  findAll() {
        logger.info("recive a request!");
        Result<List<User>> result = ResultUtil.buildSuccessResult( userService.findAll());
        return  new ResponseEntity<Result<List<User>>>(result, HttpStatus.OK);
    }
     
    /**
     * 根据用户pk更新实体
     * @param userPk  用户pk
     * @param user 返回更新后的实体
     * @return
     */
    @ApiOperation(value = "更新用户", notes = "返回更新的用户实体对象",position = 5,httpMethod = "PUT",response = ResponseEntity.class,produces = "application/json; charset=utf-8")
    @RequestMapping(value = "/update/{userPk}", method = RequestMethod.PUT)
    public ResponseEntity<?> updateByPk(
            @PathVariable("userPk") Integer userPk, @RequestBody User user) {
        user.setId(userPk);
        userService.update(user);
        Result<User> result = ResultUtil.buildSuccessResult(user);
        return new ResponseEntity<Result<User>>(result, HttpStatus.OK);
    }
     
    /**
     * 根据用户pk删除实体
     * @param userPk  用户pk
     * @return
     */
    @ApiOperation(value = "删除用户", notes = "根据pk删除用户",position = 5,httpMethod = "GET",response = ResponseEntity.class,produces = "application/json; charset=utf-8")
    @RequestMapping(value = "/delete/{userPk}", method = RequestMethod.GET)
    public ResponseEntity<?> deleteByPk(
            @PathVariable("userPk") Integer userPk) {
        userService.delete(userPk);
        Result<String> result = ResultUtil.buildSuccessResult("删除成功");
        return new ResponseEntity<Result<String>>(result, HttpStatus.OK);
    }
}