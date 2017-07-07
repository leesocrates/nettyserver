package handler;

import java.lang.reflect.Type;

import com.lee.server.retrofit.entity.AccountRecord;

import io.netty.channel.ChannelHandlerContext;

public class AddAccountRecordHandler extends JsonBaseHander<AccountRecord>{

	@Override
	protected Type getRequestContentGsonType() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void handleRequest(ChannelHandlerContext ctx, AccountRecord t) {
		// TODO Auto-generated method stub
		
	}

}
