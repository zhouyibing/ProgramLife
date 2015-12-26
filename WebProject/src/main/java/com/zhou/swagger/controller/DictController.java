package com.zhou.swagger.controller;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import com.zhou.swagger.model.Dictionary;
import com.zhou.swagger.model.Result;
import com.zhou.swagger.service.DictService;
import com.zhou.swagger.utils.ResultUtil;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * Created by Zhou Yibing on 2015/12/1.
 * 数据字典操作controller
 */
@Api(value="dict-api",description="数据字典相关操作")
@Controller
@RequestMapping("/dict")
public class DictController {
    @Autowired
    private DictService dictService;
    private Logger logger = Logger.getLogger(DictController.class);

    @ApiOperation(value="添加",notes="添加数据字典",httpMethod = "POST", response = ResponseEntity.class,produces = "application/json; charset=utf-8")
    @ResponseBody
    @RequestMapping(value={"/add"},method = RequestMethod.POST)
    public ResponseEntity<?> addDict(@ApiParam(value="填写字典",required = true) @RequestBody Dictionary dictionary){
        dictService.insertDict(dictionary);
        Result<String> result = ResultUtil.buildSuccessResult("添加成功");
        return new ResponseEntity< Result<String>>(result, HttpStatus.OK);
    }

    @ApiOperation(value = "根据字典key查找", notes = "返回数据字典对象", response = Dictionary.class,httpMethod = "GET",produces = "application/json; charset=utf-8")
    @RequestMapping(value = { "/{dictKey:.+}" }, method = RequestMethod.GET)
    public @ResponseBody ResponseEntity<?> findByDictKey(
            @ApiParam(value = "填写字典key", required = true) @PathVariable("dictKey") String dictKey) {
        Result<Dictionary> result = ResultUtil.buildSuccessResult(dictService.selectDict(dictKey));
        return new ResponseEntity<Result<Dictionary>>(result, HttpStatus.OK);
    }

    @ApiOperation(value = "获取所有数据字典", notes = "返回所有字典实体对象集合",httpMethod = "GET",response = ResponseEntity.class)
    @RequestMapping(value = "/findAll", method = RequestMethod.GET)
    public @ResponseBody ResponseEntity<?>  findAll() {
        logger.info("recive a request!");
        Result<List<Dictionary>> result = ResultUtil.buildSuccessResult(dictService.getAllDicts());
        return  new ResponseEntity<Result<List<Dictionary>>>(result, HttpStatus.OK);
    }

    @ApiOperation(value = "更新字典", notes = "返回更新的字典实体对象",httpMethod = "PUT",response = Dictionary.class,produces = "application/json; charset=utf-8")
    @RequestMapping(value = "/update/{dictKey:.+}", method = RequestMethod.PUT)
    public ResponseEntity<?> updateByDictKey(
            @ApiParam(value = "填写字典key", required = true) @PathVariable("dictKey") String dictKey, @RequestBody Dictionary dictionary) {
        dictionary.setDictKey(dictKey);
        dictService.updateDict(dictionary, dictKey);
        Result<Dictionary> result = ResultUtil.buildSuccessResult(dictionary);
        return new ResponseEntity<Result<Dictionary>>(result, HttpStatus.OK);
    }

    @ApiOperation(value = "删除字典", notes = "根据key删除字典",httpMethod = "GET",response = ResponseEntity.class,produces = "application/json; charset=utf-8")
    @RequestMapping(value = "/delete/{dictKey:.+}", method = RequestMethod.GET)
    public ResponseEntity<?> deleteByPk(
            @ApiParam(value = "填写字典key", required = true) @PathVariable("dictKey") String dictKey) {
        dictService.delteDict(dictKey);
        Result<String> result = ResultUtil.buildSuccessResult("删除成功");
        return new ResponseEntity<Result<String>>(result, HttpStatus.OK);
    }
}
