package com.zhou.swagger.dao;

import com.zhou.swagger.model.User;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by Zhou Yibing on 2015/11/30.
 */
public interface UserDao {
    int insertUser(User user);
    int deleteUserByPrimaryKey(long id);
    int updateUserByPrimaryKey(@Param("user") User user,@Param("id")long id);
    List<User> selectAllUser();
    User selectByPrimaryKey(long id);
}
