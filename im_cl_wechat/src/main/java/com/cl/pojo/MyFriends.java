package com.cl.pojo;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "my_friends")
public class MyFriends {
    @Id
    private String id;
    @Column(name = "my_user_id")
    private String myUserId;
    @Column(name = "my_friend__user_id")
    private String myFriendUserId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMyUserId() {
        return myUserId;
    }

    public void setMyUserId(String myUserId) {
        this.myUserId = myUserId;
    }

    public String getMyFriendUserId() {
        return myFriendUserId;
    }

    public void setMyFriendUserId(String myFriendUserId) {
        this.myFriendUserId = myFriendUserId;
    }
}