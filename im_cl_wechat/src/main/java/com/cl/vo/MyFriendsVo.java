package com.cl.vo;

public class MyFriendsVo {
    private String friendsUserId;
    private String friendUsername;
    private String friendFaceImage;
    private String friendNickName;

    public String getFriendsUserId() {
        return friendsUserId;
    }

    public void setFriendsUserId(String friendsUserId) {
        this.friendsUserId = friendsUserId;
    }

    public String getFriendUsername() {
        return friendUsername;
    }

    public void setFriendUsername(String friendUsername) {
        this.friendUsername = friendUsername;
    }

    public String getFriendFaceImage() {
        return friendFaceImage;
    }

    public void setFriendFaceImage(String friendFaceImage) {
        this.friendFaceImage = friendFaceImage;
    }

    public String getFriendNickName() {
        return friendNickName;
    }

    public void setFriendNickName(String friendNickName) {
        this.friendNickName = friendNickName;
    }
}
