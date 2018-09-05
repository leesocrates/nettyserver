package com.lee.wx.handler;

import com.google.gson.reflect.TypeToken;
import com.lee.retrofit.account.Account;
import com.lee.retrofit.account.AccountServiceManager;
import com.lee.retrofit.account.IAccountService;
import com.lee.retrofit.handler.JsonBaseHander;
import com.lee.utils.Constants;
import com.lee.utils.SafeUtils;
import com.lee.wx.entity.Message;
import io.netty.channel.ChannelHandlerContext;

import java.lang.reflect.Type;

public class WxMessageHandler extends JsonBaseHander<Message> {

    @Override
    protected Type getRequestContentGsonType() {
        return new TypeToken<Message>() {
        }.getType();
    }

    @Override
    protected void handleRequest(ChannelHandlerContext ctx, Message message) {
        System.out.println("get message :"+message);
        if(message!=null){
            if(message.MsgType.equals("text")){
                handleSuccessResponse(ctx, "success");
            } else {
                handleSuccessResponse(ctx, "success");
            }

        } else{
            handleSuccessResponse(ctx, "success");
        }
    }

    private void loginFail(ChannelHandlerContext ctx){
        handleFailResponse(ctx, Constants.Status.STATUS_LOGIN_FAIL, "the user name and password do not match");
    }

}