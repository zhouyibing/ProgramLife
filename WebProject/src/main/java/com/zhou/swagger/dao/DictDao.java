package com.zhou.swagger.dao;

import com.zhou.swagger.model.Dictionary;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by Zhou Yibing on 2015/12/1.
 */
public interface DictDao {
    int insertDict(Dictionary dictionary);
    int deleteDictByKey(String dictKey);
    int deleteDictById(long id);
    int updateDictById(@Param("dict")Dictionary dict,@Param("id")long id);
    int updateDictByKey(@Param("dict")Dictionary dict,@Param("dictKey")String key);
    Dictionary selectDictById(long id);
    Dictionary selectDictByKey(String key);
    List<Dictionary> selectAllDicts();
}
