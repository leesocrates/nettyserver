package handler;

import java.lang.reflect.Type;

import com.google.gson.reflect.TypeToken;
import com.lee.server.retrofit.account.Account;
import com.lee.server.retrofit.account.AccountServiceManager;
import com.lee.server.retrofit.account.IAccountService;
import com.lee.server.retrofit.utils.Constants;

import io.netty.channel.ChannelHandlerContext;

public class RegisterHandler extends JsonBaseHander<Account> {

	@Override
	protected void handleRequest(ChannelHandlerContext ctx, Account t) {
		IAccountService accountService = AccountServiceManager.getInstance()
				.getAccountService(AccountServiceManager.SERVICE_TYPE_FILE);
		if (accountService.addAccount(t)) {
			handleSuccessResponse(ctx, "register success");
		} else {
			handleFailResponse(ctx, Constants.Status.STATUS_REGIST_FAIL, "userName already exist");
		}
	}

	@Override
	protected Type getRequestContentGsonType() {
		return new TypeToken<Account>() {
		}.getType();
	}

}
