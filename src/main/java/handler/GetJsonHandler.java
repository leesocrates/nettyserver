package handler;
import static io.netty.handler.codec.http.HttpResponseStatus.OK;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

import java.io.InputStream;

import com.lee.server.retrofit.utils.Constants;
import com.lee.server.retrofit.utils.FileUtils;
import com.lee.server.retrofit.utils.HttpUtils;

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

public class GetJsonHandler extends SimpleChannelInboundHandler<FullHttpRequest> {

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

			handleRequestContent(ctx, uri);
		}
	}

	private void handleRequestContent(ChannelHandlerContext ctx, String uriPath) {
		String fileName = uriPath==null ? "test.json": uriPath.replace("/getJson/", "");
		InputStream in = GetHtmlHandler.class.getClassLoader()
				.getResourceAsStream("json/"+fileName);
		byte[] bytes = FileUtils.getContentFromStream(in);
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
