package socket;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

public class SocketHandler extends ChannelHandlerAdapter {

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		// TODO Auto-generated method stub
		if (msg instanceof ByteBuf) {
			byte[] bytes = ((ByteBuf) msg).array();
			String s = new String(bytes);
			System.out.println(new String(bytes));
		}
		super.channelRead(ctx, msg);
	}
}
