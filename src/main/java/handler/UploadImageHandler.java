package handler;

import static io.netty.handler.codec.http.HttpResponseStatus.CREATED;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

import java.lang.reflect.Type;
import java.util.ArrayList;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.lee.server.retrofit.entity.BaseResponse;
import com.lee.server.retrofit.entity.UserInfo;
import com.lee.server.retrofit.utils.FileUtils;
import com.lee.server.retrofit.utils.HttpUtils;
import com.lee.server.retrofit.utils.Utils;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.DefaultHttpContent;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.router.Routed;

public class UploadImageHandler extends SimpleChannelInboundHandler<FullHttpRequest> {

	private int contentLength = 0;
	private int curContentLength;
	private StringBuilder allContent = new StringBuilder(2 * 1024);

	@Override
	protected void messageReceived(ChannelHandlerContext ctx, FullHttpRequest msg) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		if (msg instanceof DefaultHttpContent) {
			ByteBuf bytebuf = ((DefaultHttpContent) msg).content();
			byte[] contents = new byte[bytebuf.readableBytes()];
			bytebuf.readBytes(contents);
			curContentLength += contents.length;
			allContent.append(new String(contents,"utf-8"));
			System.out.println("curContentLength is : " + curContentLength);
			if (curContentLength == contentLength) {
				System.out.println("content is : "+allContent.toString().substring(0, 1000));
				System.out.println("all body is accept success "+" curtime is : "+System.currentTimeMillis());
				handleRequestContent(ctx, allContent.toString().getBytes());
//				handleResponse(ctx);
			}
		} else if (msg instanceof Routed) {
			HttpRequest httpRequest = ((Routed) msg).request();
			HttpHeaders headers = httpRequest.headers();
			contentLength = headers.getInt(HttpHeaderNames.CONTENT_LENGTH, 0);
			System.out.println("http request header is : " + headers.toString()+" curtime is : "+System.currentTimeMillis());
			final String uri = httpRequest.uri();
			System.out.println("http request uri is : " + uri);
		}
	}

	private void handleRequestContent(ChannelHandlerContext ctx, byte[] imageBytes) {
		// UserInfo userInfo = Utils.getObject(content, UserInfo.class);
		// FileUtils.saveImageFile("", userInfo.getUserIcon());

		FileUtils.saveImageFile("", imageBytes);

		// FileUtils.saveImageFile("", content);
		handleResponse(ctx);
	}

	private void handleResponse(ChannelHandlerContext ctx) {
		BaseResponse baseResponse = new BaseResponse(true, "", "register success");
		String responseContent = Utils.getJsonString(baseResponse);
		System.out.println("response content is : " + responseContent);

		ByteBuf byteBuf = ctx.alloc().buffer(responseContent.length());
		byteBuf.writeBytes(responseContent.getBytes());
		FullHttpResponse response = new DefaultFullHttpResponse(HTTP_1_1, CREATED, byteBuf);
		HttpUtils.addCommonHttpHeader(response, responseContent, 0, "");
		ctx.writeAndFlush(response);
	}
}
