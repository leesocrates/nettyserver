package handler.wx;

import static io.netty.handler.codec.http.HttpResponseStatus.OK;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;
import handler.GetHtmlHandler;
import handler.JsonBaseHander;
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

import java.io.InputStream;
import java.lang.reflect.Type;

import com.google.gson.reflect.TypeToken;
import com.lee.server.retrofit.utils.Constants;
import com.lee.server.retrofit.utils.FileUtils;
import com.lee.server.retrofit.utils.HttpUtils;

public class WxTokenHandler extends
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
		int index = uriPath.indexOf("?");
		String echostr = "";
		if (index != -1) {
			String paramstr = uriPath.substring(index+1);
			if (paramstr != null) {
				String[] params = paramstr.split("&");
				for (int i = 0; i < params.length; i++) {
					String param = params[i];
					String[] ps = param.split("=");
					if (ps != null && ps.length == 2) {
						if ("echostr".equals(ps[0])) {
							echostr = ps[1];
						}
					}
				}
			}
		}
		//这里没有做验证，只是简单的返回了echostr这个字段，真实的环境要做验证的，验证成功后才能返回echostr，否则不返回
		try {
			String responseContent = echostr;
//					"<!DOCTYPE html><html><body>" + echostr+ "</body></html>";
			System.out.println("response content is : " + responseContent);
			ByteBuf byteBuf = ctx.alloc().buffer(responseContent.length());
			byteBuf.writeBytes(responseContent.getBytes());
			FullHttpResponse response = new DefaultFullHttpResponse(HTTP_1_1,
					OK, byteBuf);
			HttpUtils.addCommonHttpHeader(response, responseContent, 0, "");
			HttpUtils.addCacheHeader(response);
			response.headers().add(Constants.HEADER_KEY_CONTENT_TYPE,
					Constants.HEADER_VALUE_CONTENT_TYPE_TEXT);
			ctx.writeAndFlush(response);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
