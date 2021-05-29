package com.cl.mapper;

import com.cl.pojo.FriendsRequest;
import com.cl.vo.FriendsRequestVO;
import com.cl.vo.MyFriendsVo;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;
@Mapper
@Component
public interface UsersMapperCustom {
    //好友列表集
    List<FriendsRequestVO> queryFriendRequest(String acceptUserId);

    //好友列表查询
    List<MyFriendsVo> queryMyFriends(String userId);

    void batchUpdateMsgSigned(List<String> msgIdList);



}
