package com.example.firstapp;

public class UserItem {
    private String UserId;
    private String Room;

    public UserItem(String UserId, String Room){
        this.UserId=UserId;
        this.Room=Room;
    }
    public UserItem(){

    }
    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }


}
