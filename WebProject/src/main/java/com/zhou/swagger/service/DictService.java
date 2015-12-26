package com.zhou.swagger.service;

import com.zhou.swagger.model.Dictionary;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Zhou Yibing on 2015/12/1.
 */
public interface DictService {
    void insertDict(Dictionary dictionary);
    void delteDict(String key);
    void updateDict(Dictionary dictionary,String key);
    Dictionary selectDict(String key);
    List<Dictionary> getAllDicts();
}
