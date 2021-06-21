package com.example.firstapp;

public class MessageItem {
    private String name;
    private String message;
    private String time;
    private String profile_url;
    private String profile;
    private String userId;

    public MessageItem(String name,String message,String time, String profile_url,String userId){
        this.name=name;
        this.message=message;
        this.time = time;
        this.profile_url=profile_url;
        this.userId=userId;
    }
    public MessageItem(){}

    public String getProfile_url() {
        return profile_url;
    }

    public void setProfile_url(String profile_url) {
        this.profile_url = profile_url;
    }

    public String getuserId() {
        return userId;
    }

    public void setuserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }


}
