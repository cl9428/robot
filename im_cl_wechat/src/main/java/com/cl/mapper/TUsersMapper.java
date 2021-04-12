package com.cl.mapper;

import com.cl.pojo.TUsers;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

@Mapper
@Component
public interface TUsersMapper {
    int deleteByPrimaryKey(String id);

    int insert(TUsers record);

    int insertSelective(TUsers record);

    TUsers selectByPrimaryKey(String id);

    //    根据用户名查找用户对象
    TUsers queryUsernameIsExits(String username);

    int updateByPrimaryKeySelective(TUsers record);

    int updateByPrimaryKey(TUsers record);

}