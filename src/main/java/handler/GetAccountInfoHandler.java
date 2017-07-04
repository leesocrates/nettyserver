package handler;

import static io.netty.handler.codec.http.HttpResponseStatus.OK;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

import com.lee.server.retrofit.entity.BaseResponse;
import com.lee.server.retrofit.utils.Constants;
import com.lee.server.retrofit.utils.HttpUtils;
import com.lee.server.retrofit.utils.Utils;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.router.Routed;

public class GetAccountInfoHandler extends SimpleChannelInboundHandler<FullHttpRequest> {

	private int contentLength = 0;

	@Override
	protected void messageReceived(ChannelHandlerContext ctx, FullHttpRequest msg) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		if (msg instanceof Routed) {
			HttpRequest httpRequest = ((Routed) msg).request();
			HttpHeaders headers = httpRequest.headers();
			contentLength = headers.getInt(HttpHeaderNames.CONTENT_LENGTH, 0);
			System.out.println("http request header is : " + headers.toString());
			final String uri = httpRequest.uri();
			System.out.println("http request uri is : " + uri);

			handleRequestContent(ctx, "");
		}
	}

	private void handleRequestContent(ChannelHandlerContext ctx, String content) {
		handleResponse(ctx);
	}

	private void handleResponse(ChannelHandlerContext ctx) {
		BaseResponse baseResponse = new BaseResponse(true, Constants.Status.STATUS_SUCCESS,
				"the account  ");
		String responseContent = Utils.getJsonString(baseResponse);
		System.out.println("response content is : " + responseContent);

		ByteBuf byteBuf = ctx.alloc().buffer(responseContent.length());
		byteBuf.writeBytes(responseContent.getBytes());
		FullHttpResponse response = new DefaultFullHttpResponse(HTTP_1_1, OK, byteBuf);
		HttpUtils.addCommonHttpHeader(response, responseContent, 0, "");
		HttpUtils.addCacheHeader(response);
		ctx.writeAndFlush(response);
	}
}