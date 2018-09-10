package com.lee.wx.handler;


import com.google.gson.Gson;
import com.lee.retrofit.handler.GetJsonHandler;
import com.lee.utils.Constants;
import com.lee.utils.HttpUtils;
import com.lee.wx.WxTokenFetchServer;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpResponse;

import static com.lee.retrofit.handler.FileDownloadHandler.HTTP_1_1;
import static io.netty.handler.codec.http.HttpResponseStatus.OK;


public class WxAccessTokenHandler extends GetJsonHandler {

    protected void handleRequestContent(ChannelHandlerContext ctx, String uriPath) {
        if (WxTokenFetchServer.sAccessToken == null) {
            System.out.println("sAccessToken is null");
            try {
                WxTokenFetchServer.sendFetchTokenRequest();
            } catch (Exception e) {

            }
        } else {
            String responseJson = new Gson().toJson(WxTokenFetchServer.sAccessToken);
            byte[] bytes = responseJson.getBytes();
            String responseContent = new String(bytes);
            System.out.println("response content is : " + responseContent);
            ByteBuf byteBuf = ctx.alloc().buffer(responseContent.length());
            byteBuf.writeBytes(bytes);
            FullHttpResponse response = new DefaultFullHttpResponse(HTTP_1_1, OK, byteBuf);
            HttpUtils.addCommonHttpHeader(response, bytes, 0, "");
            HttpUtils.addCacheHeader(response);
            response.headers().add(Constants.HEADER_KEY_CONTENT_TYPE, Constants.HEADER_VALUE_CONTENT_TYPE_JSON);
            ctx.writeAndFlush(response);
        }


    }


}
