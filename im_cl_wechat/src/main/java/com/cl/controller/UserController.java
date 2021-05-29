package com.cl.controller;

import com.alibaba.fastjson.JSON;
import com.cl.enums.OperatorFriendRequestTypeEnum;
import com.cl.enums.search;
import com.cl.pojo.FriendsRequest;
import com.cl.pojo.TUsers;
import com.cl.service.UserServices;
import com.cl.uplod.UserFace;
import com.cl.utils.FastDFSClient;
import com.cl.utils.FileUtils;
import com.cl.utils.JSONResult;
import com.cl.utils.MD5Utils;
import com.cl.vo.FriendsRequestVO;
import com.cl.vo.MyFriendsVo;
import com.cl.vo.TUsersVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


@RestController
@RequestMapping("/user")
public class UserController {
    final
    UserServices userServices;
    final
    FastDFSClient fastDFSClient;
    public UserController(UserServices userServices, FastDFSClient fastDFSClient) {
        this.userServices = userServices;
        this.fastDFSClient = fastDFSClient;
    }

    //用户登录与注册一体化方法
    @RequestMapping("/regLogin")
    @ResponseBody
    public JSONResult registerLogin(TUsers users){
        TUsers userResult =  userServices.queryUsernameIsExits(users.getUsername());
        if(userResult!=null){
            if(!userResult.getPassword().equals(MD5Utils.getPwd(users.getPassword()))){
                return JSONResult.errorMsg("登录失败，密码错误！");
            }
        }else{
            users.setNickname(users.getUsername());
            users.setPassword(MD5Utils.getPwd(users.getPassword()));
            users.setFaceImage("");
            users.setFaceImageBig("");
            users.setQrcode("");
           userResult =  userServices.insert(users);
        }
        TUsersVo UsersVo = new TUsersVo();
        BeanUtils.copyProperties(userResult,UsersVo);
        return JSONResult.ok(UsersVo);
    }

    //修改昵称方法
    @RequestMapping("/updateUserInfo")
    public JSONResult updateUserInfo(TUsers users){
        TUsers userResult = userServices.updateUserInfo(users);
        return JSONResult.ok(userResult);
    }

    @RequestMapping("/uploadFace")
    //用户头像
    public JSONResult uploadFace(@RequestBody UserFace userFace) throws Exception {
        //获取前端传过来的base64的图片字符串，然后转为文件对象上传
        String base64face = userFace.getUserFace();
        String userFaceBath = "/usr/local/face/"+userFace.getUserId()+"userFaceBase64.png";
        //调用工具类FileUtils方法将前端传入的字符串转为文件对象
        FileUtils.base64ToFile(userFaceBath,base64face);
        MultipartFile multipartFile = FileUtils.fileToMultipart(userFaceBath);
        String url = fastDFSClient.uploadBase64(multipartFile);
        System.out.println(url);
        String thump = "_150x150.";
        String [] s = url.split("\\.");
        String thumpFace = s[0]+thump+s[1];

        TUsers users = new TUsers();
        users.setId(userFace.getUserId());
        users.setFaceImage(thumpFace);
        users.setFaceImageBig(url);
        TUsers ResultUser = userServices.updateUserInfo(users);

        return  JSONResult.ok(ResultUser);
    }

    //搜索好友方法
    @RequestMapping("/searchFriend")
    public JSONResult searchFriend(String myUserId,String friendUserName){
        /*
        * 搜索好友是否存在
        * 1.无，返回不存在
        * 2.不能添加自己
        * 3.不能重复添加
        * */
        Integer status = userServices.preconditionSearch(myUserId,friendUserName);
        if(status == search.SUCCESS.status){
            TUsers users = userServices.queryUsernameIsExits(friendUserName);
            TUsersVo vo = new TUsersVo();
            BeanUtils.copyProperties(users,vo);
            return JSONResult.ok(vo);
        }else {
            String msg = search.getMsg(status);
            return JSONResult.errorMsg(msg);
        }
    }

    //添加好友方法
    @RequestMapping("/addFriend")
    public JSONResult addFriend(String myUserId,String friendUserName){
        if(StringUtils.isBlank(myUserId)||StringUtils.isBlank(friendUserName)){
            return  JSONResult.errorMsg("好友信息为空");
        }
        /*
         * 搜索好友是否存在
         * 1.无，返回不存在
         * 2.不能添加自己
         * 3.不能重复添加
         * */
        Integer status = userServices.preconditionSearch(myUserId,friendUserName);
        if(status == search.SUCCESS.status){
            userServices.sendFriendRquest(myUserId,friendUserName);

        }else {
            String msg = search.getMsg(status);
            return JSONResult.errorMsg(msg);
        }
        return  JSONResult.ok();
    }

    //好友列表请求方法
    @RequestMapping("/sendFriendList")
    public JSONResult sendFriendList(String userId){
        List<FriendsRequestVO> friendsRequestVOS = userServices.queryFriendRequest(userId);
        return JSONResult.ok(friendsRequestVOS);
    }

    //忽略好友请求
    @RequestMapping("/ignoreFriendRequest")
    public JSONResult ignoreFriendRequest(String acceptUserId,String sendUserId,Integer operType){
        FriendsRequest friendsRequest = new FriendsRequest();
        friendsRequest.setAcceptUserId(acceptUserId);
        friendsRequest.setSendUserId(sendUserId);
        if(operType == OperatorFriendRequestTypeEnum.IGNORE.type){
            //删除
            userServices.ignoreFriendRequest(friendsRequest);

        }else if(operType == OperatorFriendRequestTypeEnum.PASS.type){
            //通过
            userServices.passFriendRequest(sendUserId,acceptUserId);
        }

        //查询好友列表
        List<MyFriendsVo> myFriendsVo = userServices.queryMyFriends(acceptUserId);
        return JSONResult.ok(myFriendsVo);
    }

    /*处理通讯录好友展示
    * */
    @RequestMapping("/myFriends")
    public JSONResult myFriends(String userId){
        if(StringUtils.isBlank(userId)){
            return JSONResult.errorMsg("用户ID为空");
        }
        //数据库查询好友
        List<MyFriendsVo> myFriends = userServices.queryMyFriends(userId);
        return JSONResult.ok(myFriends);

    }



    //测试
    @RequestMapping("/getUser")
    public String getUser(String id, Model model) {
        TUsers tUsers = userServices.getUseById(id);
        model.addAttribute("tUsers", tUsers);
        return "user_list";
    }
}
