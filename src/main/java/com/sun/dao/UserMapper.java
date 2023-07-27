package com.sun.dao;


import com.sun.entity.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper {
    void insertOrUpdate(User user); // 新增或更新用户
}

