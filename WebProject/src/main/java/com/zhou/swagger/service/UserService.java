package com.zhou.swagger.service;

import com.zhou.swagger.model.User;

import java.util.List;

/**
 * Created by Zhou Yibing on 2015/11/10.
 */
public interface UserService {
    void save(User user);

    User findByPk(Integer userPk);

    void delete(Integer userPk);

    void update(User user);

    List<User> findAll();
}
