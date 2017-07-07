package handler;


import java.lang.reflect.Type;

import org.apache.commons.codec.digest.DigestUtils;

import com.google.gson.reflect.TypeToken;
import com.lee.server.retrofit.account.Account;
import com.lee.server.retrofit.account.AccountServiceManager;
import com.lee.server.retrofit.account.IAccountService;
import com.lee.server.retrofit.utils.Constants;
import com.lee.server.retrofit.utils.SafeUtils;

import io.netty.channel.ChannelHandlerContext;

public class LoginHandler  extends JsonBaseHander<Account> {

	@Override
	protected Type getRequestContentGsonType() {
		return new TypeToken<Account>() {
		}.getType();
	}
	
	@Override
	protected void handleRequest(ChannelHandlerContext ctx, Account t) {
		IAccountService accountService = AccountServiceManager.getInstance()
				.getAccountService(AccountServiceManager.SERVICE_TYPE_FILE);
		if(t!=null && t.userName!=null){
			Account account = accountService.getAccount(t.userName);
			if(account!=null){
				String password = SafeUtils.passwordEncrypt(t.password);
				if(password.equals(account.password)){
					//TODO should return token
					handleSuccessResponse(ctx, "login success");
				} else {
					loginFail(ctx);
				}
			} else {
				loginFail(ctx);
			}
		} else{
			loginFail(ctx);
		}
	}
	
	private void loginFail(ChannelHandlerContext ctx){
		handleFailResponse(ctx, Constants.Status.STATUS_LOGIN_FAIL, "the user name and password do not match");
	}

}
