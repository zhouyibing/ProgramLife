package com.zhou.swagger.service.impl;

import com.zhou.swagger.dao.DictDao;
import com.zhou.swagger.model.Dictionary;
import com.zhou.swagger.service.DictService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Zhou Yibing on 2015/12/1.
 */
@Service
public class DictServiceImpl implements DictService{

    @Autowired
    private DictDao dictDao;

    @Override
    public void insertDict(Dictionary dictionary) {
        dictDao.insertDict(dictionary);
    }

    @Override
    public void delteDict(String key) {
        dictDao.deleteDictByKey(key);
    }

    @Override
    public void updateDict(Dictionary dictionary, String key) {
        dictDao.updateDictByKey(dictionary,key);
    }

    @Override
    public Dictionary selectDict(String key) {
        return dictDao.selectDictByKey(key);
    }

    @Override
    public List<Dictionary> getAllDicts() {
        return dictDao.selectAllDicts();
    }
}
