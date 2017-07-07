package handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.router.Routed;

public abstract class GetBaseHandler extends SimpleChannelInboundHandler<FullHttpRequest> {

	@Override
	protected void messageReceived(ChannelHandlerContext ctx, FullHttpRequest msg) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		if (msg instanceof Routed) {
			HttpRequest httpRequest = ((Routed) msg).request();
			HttpHeaders headers = httpRequest.headers();
			final String uri = httpRequest.uri();
			System.out.println("http request header is : " + headers.toString());
			System.out.println("http request uri is : " + uri);
			
			if(!validateHeader(headers)){
				handleHeaderInvalidate();
				return;
			} 
			handleUri(ctx, uri);
		}
	}
	
	protected abstract boolean validateHeader(HttpHeaders headers);
	/**
	 * 当{@link #validateHeader(HttpHeaders)} 返回false时，这个方法会被调用，返回错误信息给客户端
	 */
	protected abstract void handleHeaderInvalidate();
	protected abstract void handleUri(ChannelHandlerContext ctx, String uri);

}