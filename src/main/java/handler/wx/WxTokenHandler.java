package handler.wx;

import static io.netty.handler.codec.http.HttpResponseStatus.OK;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;
import handler.JsonBaseHander;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpResponse;

import java.lang.reflect.Type;

import com.google.gson.reflect.TypeToken;
import com.lee.server.retrofit.account.Account;
import com.lee.server.retrofit.utils.Constants;
import com.lee.server.retrofit.utils.HttpUtils;

public class WxTokenHandler extends JsonBaseHander<WxTokenRequest> {

	@Override
	protected Type getRequestContentGsonType() {
		return new TypeToken<Account>() {
		}.getType();
	}

	@Override
	protected void handleRequest(ChannelHandlerContext ctx, WxTokenRequest t) {
		String responseContent = t.echostr;
		System.out.println("response content is : " + responseContent);
		ByteBuf byteBuf = ctx.alloc().buffer(responseContent.length());
		byteBuf.writeBytes(responseContent.getBytes());
		FullHttpResponse response = new DefaultFullHttpResponse(HTTP_1_1, OK, byteBuf);
		HttpUtils.addCommonHttpHeader(response, responseContent, 0, "");
		HttpUtils.addCacheHeader(response);
		response.headers().add(Constants.HEADER_KEY_CONTENT_TYPE, Constants.HEADER_VALUE_CONTENT_TYPE_JSON);
		ctx.writeAndFlush(response);
	}

}
