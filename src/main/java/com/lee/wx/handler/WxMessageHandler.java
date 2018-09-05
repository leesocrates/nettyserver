package com.lee.wx.handler;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lee.utils.Constants;
import com.lee.utils.HttpUtils;
import com.lee.wx.entity.Message;
import com.lee.wx.entity.XmlMessage;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpResponse;
import org.json.JSONObject;
import org.json.XML;

import java.lang.reflect.Type;

import static io.netty.handler.codec.http.HttpResponseStatus.OK;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

public class WxMessageHandler extends XmlBaseHandler<XmlMessage> {

    @Override
    protected Type getRequestContentGsonType() {
        return new TypeToken<XmlMessage>() {
        }.getType();
    }

    @Override
    protected void handleRequest(ChannelHandlerContext ctx, XmlMessage xmlMessage) {
        Message message = xmlMessage.xml;
        System.out.println("get message :"+message);
        if(message!=null){
            if(message.MsgType.equals("text")){
                String from = message.FromUserName;
                String to = message.ToUserName;
                message.FromUserName = to;
                message.ToUserName = from;
                String jsonStr = new Gson().toJson(xmlMessage);
                String xml = XML.toString(new JSONObject(jsonStr));
                handleSuccessResponse(ctx, xml);
            } else {
                handleSuccessResponse(ctx, "success");
            }

        } else{
            handleSuccessResponse(ctx, "success");
        }
    }

    protected void handleSuccessResponse(ChannelHandlerContext ctx, String message) {
        String responseContent = message;
        System.out.println("response content is : " + responseContent);
        ByteBuf byteBuf = ctx.alloc().buffer(responseContent.length());
        byteBuf.writeBytes(responseContent.getBytes());
        FullHttpResponse response = new DefaultFullHttpResponse(HTTP_1_1, OK, byteBuf);
        HttpUtils.addCommonHttpHeader(response, responseContent, 0, "");
        response.headers().add(Constants.HEADER_KEY_CONTENT_TYPE, Constants.HEADER_VALUE_CONTENT_TYPE_XML);
        HttpUtils.addCacheHeader(response);
        ctx.writeAndFlush(response);
    }

    private void loginFail(ChannelHandlerContext ctx){
        handleFailResponse(ctx, Constants.Status.STATUS_LOGIN_FAIL, "the user name and password do not match");
    }

}