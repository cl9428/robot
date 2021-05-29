package com.cl.service;

import com.cl.enums.search;

import com.cl.netty.ChatMsg;
import com.cl.pojo.FriendsRequest;
import com.cl.pojo.MyFriends;
import com.cl.pojo.TUsers;
import com.cl.vo.FriendsRequestVO;
import com.cl.vo.MyFriendsVo;

import javax.persistence.criteria.CriteriaBuilder;
import java.util.List;

public interface UserServices {
    TUsers getUseById(String id);
//    根据用户名查找用户对象
    TUsers queryUsernameIsExits(String username);
    //保存注册用户
    TUsers insert(TUsers users);
    //修改用户昵称
    TUsers updateUserInfo(TUsers users);
    //搜索好友状态前置接口
    Integer preconditionSearch(String myUserId, String friendUserName);

    //发送好友请求
    void sendFriendRquest(String myUserId,String friendUserName);

    //好友请求列表
    List<FriendsRequestVO> queryFriendRequest(String acceptUserId);
    //忽略好友请求
    void ignoreFriendRequest(FriendsRequest friendsRequest);
    //通过好友请求
    void passFriendRequest(String sendUserId,String acceptUserId);

    //好友列表查询
    List<MyFriendsVo> queryMyFriends(String userId);


    //保存用户聊天消息
    String saveMsg(ChatMsg chatMsg);

    void updateMsgSigned(List<String> msgIdList);

    //获取未签收的消息列表
    List<com.cl.pojo.ChatMsg> getUnReadMsgList(String acceptUserId);

   /* //修改用户头像
    TUsers updateUserFace(TUsers users);*/
}
