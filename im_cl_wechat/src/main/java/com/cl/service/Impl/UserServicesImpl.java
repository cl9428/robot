package com.cl.service.Impl;

import com.cl.enums.MsgSignFlagEnum;
import com.cl.enums.search;
import com.cl.mapper.*;
import com.cl.netty.ChatMsg;
import com.cl.pojo.FriendsRequest;
import com.cl.pojo.MyFriends;
import com.cl.pojo.TUsers;
import com.cl.service.UserServices;
import com.cl.utils.FastDFSClient;
import com.cl.utils.FileUtils;
import com.cl.utils.QRCodeUtils;
import com.cl.vo.FriendsRequestVO;
import com.cl.vo.MyFriendsVo;
import com.n3r.idworker.Sid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Date;
import java.util.List;


@Service
public class UserServicesImpl implements UserServices {

    @Autowired
    TUsersMapper usersMapper;
    @Autowired
    Sid sid;
    @Autowired
    QRCodeUtils qrCode;
    @Autowired
    FastDFSClient fastDFSClient;
    @Autowired
    MyFriendsMapper myFriendsMapper;
    @Autowired
    FriendsRequestMapper friendsRequestMapper;

    @Autowired
    ChatMsgMapper chatMsgMapper;

    @Autowired
    UsersMapperCustom usersMapperCustom;


    @Override
    public TUsers getUseById(String id) {
        return usersMapper.selectByPrimaryKey(id);
    }

    @Override
    public TUsers queryUsernameIsExits(String username) {
        TUsers users = usersMapper.queryUsernameIsExits(username);
        return users;
    }

    @Override
    public TUsers insert(TUsers users) {
        String userId = sid.nextShort();
        //生成用户唯一 二维码
        String qrCodePath = "D:\\" + userId + "qrcode.png";
        //创建二维码对象信息
        qrCode.createQRCode(qrCodePath, "img_qrcode:" + users.getUsername());
        MultipartFile qrCodeFile = FileUtils.fileToMultipart(qrCodePath);
        String qrCodeUrl = "";
        try {
            qrCodeUrl = fastDFSClient.uploadQRCode(qrCodeFile);
        } catch (Exception e) {
            e.printStackTrace();
        }

        users.setId(userId);
        users.setQrcode(qrCodeUrl);
        usersMapper.insert(users);
        return users;
    }

    @Override
    public TUsers updateUserInfo(TUsers users) {
        usersMapper.updateByPrimaryKeySelective(users);
        TUsers ResultUsers = usersMapper.selectByPrimaryKey(users.getId());
        return ResultUsers;

    }

    @Override
    public Integer preconditionSearch(String myUserId, String friendUserName) {
        TUsers users = queryUsernameIsExits(friendUserName);
        //1.无，返回不存在
        if (users == null) {
            return search.USER_NOT_EXIST.status;
        }
        if (myUserId.equals(users.getId())) {//2.不能添加自己
            return search.NOT_YOURSELF.status;
        }
        //3.搜索好友已经是你的好友

        MyFriends myfriend = new MyFriends();
        myfriend.setMyUserId(myUserId);
        myfriend.setMyFriendUserId(users.getId());
        MyFriends myFriends = myFriendsMapper.selectOneByExample(myfriend);
        if (myFriends != null) {
            return search.ALREADY_FRIENDS.status;
        }

        return search.SUCCESS.status;
    }

    @Override
    public void sendFriendRquest(String myUserId, String friendUserName) {
        TUsers users = queryUsernameIsExits(friendUserName);
        MyFriends myfriend = new MyFriends();
        myfriend.setMyUserId(myUserId);
        myfriend.setMyFriendUserId(users.getId());
        MyFriends myFriends = myFriendsMapper.selectOneByExample(myfriend);
        if (myFriends == null) {
            FriendsRequest friendsRequest = new FriendsRequest();
            String requestId = sid.nextShort();
            friendsRequest.setId(requestId);
            friendsRequest.setSendUserId(myUserId);
            friendsRequest.setAcceptUserId(users.getId());
            friendsRequest.setRequestDateTime(new Date());

            friendsRequestMapper.insert(friendsRequest);
        }

    }

    @Override
    public List<FriendsRequestVO> queryFriendRequest(String acceptUserId) {

        return usersMapperCustom.queryFriendRequest(acceptUserId);

    }

    @Override
    public void ignoreFriendRequest(FriendsRequest friendsRequest) {
        friendsRequestMapper.ignoreFriendRequest(friendsRequest);
    }

    @Override
    public void passFriendRequest(String sendUserId,String acceptUserId) {
        //双向好友保存
        saveFriends(sendUserId,acceptUserId);
        saveFriends(acceptUserId,sendUserId);

        FriendsRequest friendsRequest = new FriendsRequest();
        friendsRequest.setSendUserId(sendUserId);
        friendsRequest.setAcceptUserId(acceptUserId);
        ignoreFriendRequest(friendsRequest);
    }
    //通过好友请求并保存数据
    private void saveFriends(String acceptUserId,String sendUserId){
        MyFriends myFriends = new MyFriends();
        String recordId = sid.nextShort();
        myFriends.setId(recordId);
        myFriends.setMyUserId(sendUserId);
        myFriends.setMyFriendUserId(acceptUserId);

        myFriendsMapper.insert(myFriends);
    }

    @Override
    public List<MyFriendsVo> queryMyFriends(String userId) {
        return usersMapperCustom.queryMyFriends(userId);
    }

    @Override
    public String saveMsg(ChatMsg chatMsg) {
        com.cl.pojo.ChatMsg msgDB = new com.cl.pojo.ChatMsg();
        String msgId = sid.nextShort();
        msgDB.setId(msgId);
        msgDB.setAcceptUserId(chatMsg.getReceiverId());
        msgDB.setSendUserId(chatMsg.getSenderId());
        msgDB.setCreateTime(new Date());
        msgDB.setSignFlag(MsgSignFlagEnum.unsign.type);
        msgDB.setMsg(chatMsg.getMsg());

        chatMsgMapper.insert(msgDB);

        return msgId;
    }

    @Override
    public void updateMsgSigned(List<String> msgIdList) {
        usersMapperCustom.batchUpdateMsgSigned(msgIdList);
    }

    @Override
    public List<com.cl.pojo.ChatMsg> getUnReadMsgList(String acceptUserId) {
        List<com.cl.pojo.ChatMsg> result = chatMsgMapper.getUnReadMsgListByAcceptUid(acceptUserId);
        return result;
    }

    /* @Override
    public TUsers updateUserFace(TUsers users) {
        usersMapper.updateByPrimaryKeySelective(users);
        TUsers ResultUser = usersMapper.selectByPrimaryKey(users.getId());
        return ResultUser;
    }*/
}
