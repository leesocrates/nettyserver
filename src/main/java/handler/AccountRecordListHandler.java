package handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.HttpHeaders;

public class AccountRecordListHandler extends GetBaseHandler{

	@Override
	protected boolean validateHeader(HttpHeaders headers) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	protected void handleHeaderInvalidate() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void handleUri(ChannelHandlerContext ctx, String uri) {
		// TODO Auto-generated method stub
		
	}

}
