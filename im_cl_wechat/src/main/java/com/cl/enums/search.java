package com.cl.enums;

public enum search {
    /**
     *
     */
    SUCCESS(0, "OK"),
    USER_NOT_EXIST(1, "无此用户..."),
    NOT_YOURSELF(2, "不能添加你自己..."),
    ALREADY_FRIENDS(3, "该用户已经是你的好友...");

    public  final Integer status;
    public final  String msg;

    search(Integer status, String msg) {
        this.status = status;
        this.msg = msg;
    }
    public Integer getStatus(){return status;}

    public static String getMsg(Integer status ){
        for (search type:search.values()){
            if(type.status == status){
                return type.msg;
            }
        }
        return null;
    }


}
