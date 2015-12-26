package com.zhou.swagger.service.impl;

import com.zhou.swagger.dao.UserDao;
import com.zhou.swagger.model.User;
import com.zhou.swagger.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Zhou Yibing on 2015/11/10.
 */
@Service
public class UserServiceImpl implements UserService{

    @Autowired
    private UserDao userDao;

    @Override
    public void save(User user) {
        userDao.insertUser(user);
    }

    @Override
    public User findByPk(Integer userPk) {

       return userDao.selectByPrimaryKey(userPk);
    }

    @Override
    public void delete(Integer userPk) {
       userDao.deleteUserByPrimaryKey(userPk);
    }

    @Override
    public void update(User user) {
      userDao.updateUserByPrimaryKey(user,user.getId());
    }

    @Override
    public List<User> findAll() {
        return userDao.selectAllUser();
    }
}
