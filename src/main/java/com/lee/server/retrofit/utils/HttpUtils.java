package com.lee.server.retrofit.utils;

import static io.netty.handler.codec.http.HttpResponseStatus.FORBIDDEN;
import static io.netty.handler.codec.http.HttpResponseStatus.OK;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.util.CharsetUtil;

import org.apache.commons.codec.digest.DigestUtils;

import com.lee.server.retrofit.entity.ClientErrorResponse;
import com.lee.server.retrofit.entity.ErrorResponse;

public class HttpUtils {

	public static void addHttpHeader(FullHttpResponse response,
			String responseJsonContent, long counter) {
		addCommonHttpHeader(response, responseJsonContent, counter, "");
	}

	public static void addCommonHttpHeader(FullHttpResponse response,
			String responseJsonContent, long counter, String appVersionNew) {
		HttpHeaders headers = response.headers();
//		headers.set(HttpHeaderNames.CONTENT_TYPE,
//				Constants.HEADER_VALUE_CONTENT_TYPE_JSON);
		headers.add(HttpHeaderNames.CONTENT_LENGTH,
				responseJsonContent.length() + "");
		headers.add(HttpHeaderNames.CACHE_CONTROL,
				Constants.HEADER_VALUE_CACHE_CONTROL);
		headers.add(HttpHeaderNames.SERVER, Constants.HEADER_VALUE_SERVER);
		headers.add(HttpHeaderNames.ETAG,
				DigestUtils.sha1Hex(responseJsonContent));
		headers.add(HttpHeaderNames.DATE, Utils.getHttpHeaderDate());
		headers.add(Constants.HEADER_KEY_X_DIAGNOSE_MEDIA_TYPE,
				Constants.HEADER_VALUE_X_DIAGNOSE_MEDIA_TYPE);
		headers.add(Constants.HEADER_KEY_STATUS, OK.toString());
		headers.add(Constants.HEADER_KEY_X_RATELIMIT_LIMIT,
				Constants.HEADER_VALUE_DEFAULT_X_RateLimit_Limit);
		headers.add(Constants.HEADER_KEY_X_RATELIMIT_REMAINING,
				FlowControlUtils.getRemainCounter(counter) + "");
		headers.add(Constants.HEADER_KEY_X_RATELIMIT_RESET,
				Utils.getNextSecondStr() + "");
		headers.add(Constants.HEADER_KEY_X_CONTENT_TYPE_OPTIONS,
				Constants.HEADER_VALUE_X_CONTENT_TYPE_OPTIONS);
		headers.add(Constants.HEADER_KEY_X_APPVERSION_NEW,
				appVersionNew == null ? "" : appVersionNew);
	}
	
	public static void addCacheHeader(FullHttpResponse response){
		HttpHeaders headers = response.headers();
		headers.add("Cache-Control", "max-age=60, only-if-cached, max-stale=120");
	}
	
	public static void addHttpHeader(HttpHeaders headers, String name, String value){
		headers.add(name, value);
	}

	public static void sendError(ChannelHandlerContext ctx,
			HttpResponseStatus status) {
		ClientErrorResponse clientErrorResponse = new ClientErrorResponse(
				status.toString(), null);
		HttpUtils.sendError(ctx, status, clientErrorResponse);
	}

	public static void sendError(ChannelHandlerContext ctx,
			HttpResponseStatus status, ErrorResponse errorResponse) {
		HttpUtils.sendError(ctx, status, Utils.getJsonString(errorResponse));
	}

	public static void sendError(ChannelHandlerContext ctx,
			HttpResponseStatus status, String errorContent) {
		sendError(ctx, status, errorContent,
				Constants.HEADER_VALUE_CONTENT_TYPE_JSON);
	}

	public static void sendError(ChannelHandlerContext ctx,
			HttpResponseStatus status, String errorContent, String content_type) {
		FullHttpResponse response = new DefaultFullHttpResponse(HTTP_1_1,
				status, Unpooled.copiedBuffer(errorContent, CharsetUtil.UTF_8));
		response.headers().set(HttpHeaderNames.CONTENT_TYPE, content_type);
		response.headers().set(HttpHeaderNames.CONNECTION, "close");
		ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
	}

	public static boolean validateUserAgent(HttpHeaders headers) {
		String userAgent = getUserAgent(headers);
		if (userAgent == null || userAgent.trim().length() == 0) {
			return false;
		}
		return true;
	}

	public static String getUserAgent(HttpHeaders headers) {
		return headers.getAndConvert(HttpHeaderNames.USER_AGENT);
	}

	public static String getVersionToken(String userAgent) {
		if (userAgent != null) {
			String[] agentList = userAgent.trim().split(" ");
			if (agentList != null && agentList.length > 0) {
				for (String agent : agentList) {
					if (agent != null
							&& agent.contains(Constants.APP_PACKAGE_NAME)) {
						return agent.trim().substring(agent.indexOf("/") + 1);
					}
				}
			}
		}
		return null;
	}
//
//	public static String getAppVersionValue(HttpHeaders headers) {
//		String newAppVersion = null;
//		String appVersionToken = HttpUtils.getVersionToken(HttpUtils
//				.getUserAgent(headers));
//		if (appVersionToken != null
//				&& !appVersionToken.equals(Constants.versionInfo.getToken())) {
//			String newVersionDownUrl = Constants.DOWN_NEW_APP_BASE_URL
//					+ Constants.versionInfo.getSha();
//			newAppVersion = newVersionDownUrl + ">;" + "sha=\""
//					+ Constants.versionInfo.getSha() + "\"";
//		}
//		return newAppVersion;
//	}
	
	public static void handleRateLimitError(ChannelHandlerContext ctx, String source) {
		ClientErrorResponse errorResponse = new ClientErrorResponse();
		errorResponse.setMessage("API rate limit exceeded for "+source);
		HttpUtils.sendError(ctx, FORBIDDEN, errorResponse);
	}
}
