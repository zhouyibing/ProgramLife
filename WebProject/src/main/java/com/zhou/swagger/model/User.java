package com.zhou.swagger.model;

import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;

/**
 * Created by Zhou Yibing on 2015/11/10.
 */
@ApiModel("User")
public class User {
    @ApiModelProperty("id")
    private long id;
    @ApiModelProperty("姓名")
    private String name;
    @ApiModelProperty("性别")
    private int sex;
    @ApiModelProperty("年龄")
    private int age;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
