package com.example.firstapp;

public class roomitem {

    private String roomId;
    private String roomName;

    public roomitem(String roomId,String roomName){
        this.roomId = roomId;
        this.roomName = roomName;
    }
    public roomitem(){}

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }
}
