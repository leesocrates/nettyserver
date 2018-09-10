package com.lee.wx.handler;

import com.lee.retrofit.handler.GetHtmlHandler;
import com.lee.utils.Constants;
import com.lee.utils.FileUtils;
import com.lee.utils.HttpUtils;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.handler.codec.http.router.Routed;

import java.io.InputStream;

import static io.netty.handler.codec.http.HttpResponseStatus.OK;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

public class GetWxHtmlHandler extends
        SimpleChannelInboundHandler<FullHttpRequest> {

    private int contentLength = 0;

    @Override
    protected void messageReceived(ChannelHandlerContext ctx,
                                   FullHttpRequest msg) throws Exception {
        // TODO Auto-generated method stub

    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg)
            throws Exception {
        if (msg instanceof Routed) {
            HttpRequest httpRequest = ((Routed) msg).request();
            HttpHeaders headers = httpRequest.headers();
            contentLength = headers.getInt(HttpHeaderNames.CONTENT_LENGTH, 0);
            System.out
                    .println("http request header is : " + headers.toString());
            final String uri = httpRequest.uri();
            System.out.println("http request uri is : " + uri);

            handleRequestContent(ctx, uri);
        }
    }

    private void handleRequestContent(ChannelHandlerContext ctx, String uriPath) {
        if (uriPath != null && uriPath.length() > 0) {
            String queryStrs = uriPath.substring(uriPath.indexOf("?")+1);
            String[] splitstrs = queryStrs.split("&");
            System.out.println("splitStrs is : "+splitstrs);

        }
        String fileName = "test.html";
        System.out.println("fileName is : " + fileName);
        try {
            InputStream in = GetHtmlHandler.class.getClassLoader()
                    .getResourceAsStream("html/" + fileName);
            byte[] bytes = FileUtils.getContentFromStream(in);
            String responseContent = new String(bytes);
            System.out.println("response content is : " + responseContent);
            ByteBuf byteBuf = ctx.alloc().buffer(responseContent.length());
            byteBuf.writeBytes(bytes);
            FullHttpResponse response = new DefaultFullHttpResponse(HTTP_1_1, OK,
                    byteBuf);
            HttpUtils.addCommonHttpHeader(response, bytes, 0, "");
            HttpUtils.addCacheHeader(response);
            response.headers().add(Constants.HEADER_KEY_CONTENT_TYPE,
                    Constants.HEADER_VALUE_CONTENT_TYPE_HTML);
            ctx.writeAndFlush(response);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
