package handler;

import static io.netty.handler.codec.http.HttpResponseStatus.OK;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

import java.io.File;

import javax.activation.MimetypesFileTypeMap;

import com.lee.server.retrofit.utils.FileUtils;

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

public class GetFileHandle extends SimpleChannelInboundHandler<FullHttpRequest> {

	@Override
	protected void messageReceived(ChannelHandlerContext ctx, FullHttpRequest msg) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		if (msg instanceof Routed) {
			HttpRequest httpRequest = ((Routed) msg).request();
			HttpHeaders headers = httpRequest.headers();
			System.out.println("http request header is : " + headers.toString());
			final String uri = httpRequest.uri();
			System.out.println("http request uri is : " + uri);

			handleRequestContent(ctx, uri);
		}
	}

	private void handleRequestContent(ChannelHandlerContext ctx, String uri) {
		String fileName = uri.substring("/getFile/".length());
		handleResponse(ctx, fileName);
	}

	private void handleResponse(ChannelHandlerContext ctx, String filename) {
		byte[] bytes = FileUtils.getFileContent(filename);

		ByteBuf byteBuf = ctx.alloc().buffer(bytes.length);
		byteBuf.writeBytes(bytes);
		FullHttpResponse response = new DefaultFullHttpResponse(HTTP_1_1, OK, byteBuf);
		MimetypesFileTypeMap mimeTypeMap = new MimetypesFileTypeMap();
		File file = new File(filename);
		response.headers().set(HttpHeaderNames.CONTENT_TYPE, mimeTypeMap.getContentType(file));
		response.headers().set(HttpHeaderNames.CONTENT_LENGTH, bytes.length + "");

		System.out.println("http response header is : " + response.headers().toString());
		ctx.writeAndFlush(response);

	}
}