package com.lee.wx.entity;

public class Message {
    public String ToUserName;
    public String FromUserName;
    public String CreateTime;
    public String MsgType;
    public String Content;
    public String MsgId;

    @Override
    public String toString() {
        return "Message{" +
                "ToUserName='" + ToUserName + '\'' +
                ", FromUserName='" + FromUserName + '\'' +
                ", CreateTime='" + CreateTime + '\'' +
                ", MsgType='" + MsgType + '\'' +
                ", Content='" + Content + '\'' +
                ", MsgId='" + MsgId + '\'' +
                '}';
    }
}
