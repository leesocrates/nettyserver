package com.lee.wx.handler;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lee.retrofit.handler.GetHtmlHandler;
import com.lee.utils.Constants;
import com.lee.utils.FileUtils;
import com.lee.utils.HttpUtils;
import com.lee.wx.entity.AccessToken;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.handler.codec.http.router.Routed;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;

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
            String[] codes = splitstrs[0].split("=");
            if(codes.length==2){
                String code = codes[1];
                try {
                    sendFetchTokenRequest(code);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        String fileName = "test1.html";
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

    public void sendFetchTokenRequest(String code) throws Exception {

        URL serverUrl = new URL("https://api.weixin.qq.com/sns/oauth2/access_token?appid=wxdfe88205b45a1aaa&secret=d58d13c8be552716c7d28811e77a7166&code="+code+"&grant_type=authorization_code");
        HttpURLConnection conn = (HttpURLConnection) serverUrl.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Content-type", "application/json");
        //必须设置false，否则会自动redirect到重定向后的地址
        conn.setInstanceFollowRedirects(false);
        conn.connect();
        String result = getReturn(conn);
        Gson gson = new Gson();
        Type type = new TypeToken<AccessToken>() {}.getType();
        AccessToken accessToken = gson.fromJson(result, type);

        System.out.println("getWxHtmlHandler result is : "+result);
    }

    /*请求url获取返回的内容*/
    private String getReturn(HttpURLConnection connection) throws IOException {
        StringBuffer buffer = new StringBuffer();
        //将返回的输入流转换成字符串
        try (InputStream inputStream = connection.getInputStream();
             InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("utf-8"));
             BufferedReader bufferedReader = new BufferedReader(inputStreamReader);) {
            String str = null;
            while ((str = bufferedReader.readLine()) != null) {
                buffer.append(str);
            }
            String result = buffer.toString();
            return result;
        }
    }

}
