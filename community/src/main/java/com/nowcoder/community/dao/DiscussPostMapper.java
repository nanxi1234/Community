package com.nowcoder.community.dao;

import com.nowcoder.community.entity.DiscussPost;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface DiscussPostMapper {
    List<DiscussPost> selectDiscussPosts(int userId, int offset, int limit);
        //分页 一共有多少条数据，一页放多少条数据
       int selectDiscussPostRows(@Param("userId") int userId);//@Param()起别名
    //如果只有一个参数，并且在<if>里使用，则必须加别名
}
