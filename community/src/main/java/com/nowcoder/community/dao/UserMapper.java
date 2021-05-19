package com.nowcoder.community.dao;

import com.nowcoder.community.entity.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper {//要实现它需要配置文件，给每一个方法提供所需的sql
    //增删改查的方法
    User selectById(int id);

    User selectByName(String username);

    User selectByEmail(String email);

    int insertUser(User user);

    int updateStatus(int id,int status);

    int updateHeadr(int id,String headerUrl);//更新头像的路径

    int updatePassword(int id,String password);//更新密码

}
