package handler;

import com.lee.server.retrofit.utils.Constants;
import com.lee.server.retrofit.utils.FileUtils;
import com.lee.server.retrofit.utils.HttpUtils;
import com.lee.server.retrofit.utils.SafeUtils;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpResponse;

import java.io.InputStream;
import java.util.Arrays;

import static io.netty.handler.codec.http.HttpResponseStatus.OK;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

public class WxAuthHandler extends GetJsonHandler {
    private static final String TOKEN = "socrateslee1988";

    protected void handleRequestContent(ChannelHandlerContext ctx, String uriPath) {
        if(uriPath.contains("?")){
            String query = uriPath.substring(uriPath.indexOf("?"));
            String[] parameters = query.split("&");
            String timestamp ,nonce, signature, echostr;
            timestamp=nonce=signature=echostr="";
            for (String parameter : parameters) {
                String[] kv = parameter.split("=");
                if(kv.length == 2 ){
                    if(kv[0].equals("timestamp")){
                        timestamp = kv[1];
                    } else if ("nonce".equals(kv[0])){
                        nonce = kv[1];
                    } else if ("signature".equals(kv[0])){
                        signature = kv[1];
                    } else  if("echostr".equals(kv[0])){
                        echostr = kv[1];
                    }
                }
            }

            String signatureGen = makeSignature(timestamp, nonce);
            System.out.println(timestamp+" , "+nonce+" , "+echostr+" , "+signature+" , "+signatureGen);
            if(signature.equals(signatureGen)){
                System.out.println("request from wechat");
                byte[] bytes = echostr.getBytes();
                ByteBuf byteBuf = ctx.alloc().buffer(bytes.length);
                byteBuf.writeBytes(bytes);
                FullHttpResponse response = new DefaultFullHttpResponse(HTTP_1_1, OK, byteBuf);
                HttpUtils.addCommonHttpHeader(response, bytes, 0, "");
                HttpUtils.addCacheHeader(response);
                response.headers().add(Constants.HEADER_KEY_CONTENT_TYPE, Constants.HEADER_VALUE_CONTENT_TYPE_HTML);
                ctx.writeAndFlush(response);
            } else {
                System.out.println("request not from wechat");
                byte[] bytes = "".getBytes();
                ByteBuf byteBuf = ctx.alloc().buffer(bytes.length);
                byteBuf.writeBytes(bytes);
                FullHttpResponse response = new DefaultFullHttpResponse(HTTP_1_1, OK, byteBuf);
                HttpUtils.addCommonHttpHeader(response, bytes, 0, "");
                HttpUtils.addCacheHeader(response);
                response.headers().add(Constants.HEADER_KEY_CONTENT_TYPE, Constants.HEADER_VALUE_CONTENT_TYPE_HTML);
                ctx.writeAndFlush(response);
            }
        }




    }

    private String makeSignature(String timestamp, String nonce){
        String[] s1 = new String[]{TOKEN, timestamp, nonce};
        Arrays.sort(s1);
        String all="";
        for(String s: s1){
            all+=s;
        }
        return SafeUtils.sha1Encrypt(all);
    }
}
