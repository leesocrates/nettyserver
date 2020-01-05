package com.lee.retrofit.handler;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.URLDecoder;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.activation.MimetypesFileTypeMap;

import com.lee.utils.Constants;
import com.lee.utils.FileUtils;
import com.lee.utils.HttpUtils;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.handler.codec.http.*;
import io.netty.handler.codec.http.router.Routed;
import io.netty.handler.ssl.SslHandler;
import io.netty.handler.stream.ChunkedFile;
import io.netty.util.CharsetUtil;
import io.netty.util.internal.SystemPropertyUtil;

import static io.netty.handler.codec.http.HttpHeaderUtil.isKeepAlive;
import static io.netty.handler.codec.http.HttpResponseStatus.NOT_FOUND;
import static io.netty.handler.codec.http.HttpResponseStatus.OK;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_0;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

public class FileDownloadHandler extends SimpleChannelInboundHandler<FullHttpRequest> {
	private static final String CRLF = "\r\n";
	private String localDir = "src/main/java/com/lee/server/retrofit/";

	private static SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss");

	public static final HttpVersion HTTP_1_1 = new HttpVersion("HTTP", 1, 1, true);

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		if (msg instanceof Routed) {
			HttpRequest httpRequest = ((Routed) msg).request();
			HttpHeaders headers = httpRequest.headers();
			System.out.println("http request header is : " + headers.toString());
			final String uri = URLDecoder.decode(httpRequest.uri(), "utf-8");
			System.out.println("http request uri is : " + uri);

			if (httpRequest.method().compareTo(HttpMethod.GET) != 0) {
				sendErrorToClient(ctx, HttpResponseStatus.METHOD_NOT_ALLOWED);
				return;
			}
			System.out.println("SystemPropertyUtil.get(\"user.dir\")  is : "+SystemPropertyUtil.get("user.dir"));
			String filePath = getFilePath(uri);
			sendDownloadFile(ctx, filePath, httpRequest);
//			filePath = SystemPropertyUtil.get("user.dir");
//			File file = new File(filePath);
//			// 如果文件不存在
//			if (!file.exists()) {
//				sendErrorToClient(ctx, HttpResponseStatus.NOT_FOUND);
//				return;
//			}
//			// 如果是目录，则显示子目录
//			if (file.isDirectory()) {
//				sendDirListToClient(ctx, file, uri);
//				return;
//			}
//			// 如果是文件，则将文件流写到客户端
//			if (file.isFile()) {
//				sendFileToClient(ctx, file, uri);
//				return;
//			}
//			ctx.close();
		}
	}

	@Override
	public void messageReceived(ChannelHandlerContext ctx, FullHttpRequest req) throws Exception {
		// 解码不成功
		if (!req.decoderResult().isSuccess()) {
			sendErrorToClient(ctx, HttpResponseStatus.BAD_REQUEST);
			return;
		}
		if (req.method().compareTo(HttpMethod.GET) != 0) {
			sendErrorToClient(ctx, HttpResponseStatus.METHOD_NOT_ALLOWED);
			return;
		}
		String uri = req.uri();
		uri = URLDecoder.decode(uri, "utf-8");
		String filePath = getFilePath(uri);
		File file = new File(filePath);
		// 如果文件不存在
		if (!file.exists()) {
			sendErrorToClient(ctx, HttpResponseStatus.NOT_FOUND);
			return;
		}
		// 如果是目录，则显示子目录
		if (file.isDirectory()) {
			sendDirListToClient(ctx, file, uri);
			return;
		}
		// 如果是文件，则将文件流写到客户端
		if (file.isFile()) {
			sendFileToClient(ctx, file, uri);
			return;
		}
		ctx.close();
	}

	public String getFilePath(String uri) throws Exception {
		return uri.substring("/getFileDir/".length());
	}

	private void sendErrorToClient(ChannelHandlerContext ctx, HttpResponseStatus status) throws Exception {
		ByteBuf buffer = Unpooled.copiedBuffer(("系统服务出错,可能请求了错误的文件：" + status.toString() + CRLF).getBytes("utf-8"));
		FullHttpResponse resp = new DefaultFullHttpResponse(HTTP_1_1, status, buffer);
		resp.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/html;charset=utf-8");
		ctx.writeAndFlush(resp).addListener(ChannelFutureListener.CLOSE);
	}

	private void sendDirListToClient(ChannelHandlerContext ctx, File dir, String uri) throws Exception {
		StringBuffer sb = new StringBuffer("");
		String dirpath = dir.getPath();
		sb.append("<!DOCTYPE HTML>" + CRLF);
		sb.append("<html><head><title>");
		sb.append(dirpath);
		sb.append("目录：");
		sb.append("</title></head><body>" + CRLF);
		sb.append("<h3>");
		sb.append("当前目录:" + dirpath);
		sb.append("</h3>");
		sb.append("<table>");
		sb.append("<tr><td colspan='3'>上一级:<a href=\"../\">..</a>  </td></tr>");
		if (uri.equals("/")) {
			uri = "";
		} else {
			if (uri.charAt(0) == '/') {
				uri = uri.substring(0);
			}
			uri += "/";
		}

		String fnameShow;
		for (File f : dir.listFiles()) {
			if (f.isHidden() || !f.canRead()) {
				continue;
			}
			String fname = f.getName();
			Calendar cal = Calendar.getInstance();
			cal.setTimeInMillis(f.lastModified());
			String lastModified = sdf.format(cal.getTime());
			sb.append("<tr>");
			if (f.isFile()) {
				fnameShow = "<font color='green'>" + fname + "</font>";
			} else {
				fnameShow = "<font color='red'>" + fname + "</font>";
			}
			sb.append("<td style='width:200px'> " + lastModified + "</td><td style='width:100px'>"
					+ Files.size(f.toPath()) + "</td><td><a href=\"" + uri + fname + "\">" + fnameShow + "</a></td>");
			sb.append("</tr>");

		}
		sb.append("</table>");
		ByteBuf buffer = Unpooled.copiedBuffer(sb.toString(), CharsetUtil.UTF_8);
		FullHttpResponse resp = new DefaultFullHttpResponse(HTTP_1_1, HttpResponseStatus.OK, buffer);
		resp.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/html;charset=utf-8");
		ctx.writeAndFlush(resp).addListener(ChannelFutureListener.CLOSE);
	}

	private void sendFileToClient(ChannelHandlerContext ctx, File file, String uri) throws Exception {
		ByteBuf buffer = Unpooled.copiedBuffer(Files.readAllBytes(file.toPath()));
		FullHttpResponse resp = new DefaultFullHttpResponse(HTTP_1_1, HttpResponseStatus.OK, buffer);
		MimetypesFileTypeMap mimeTypeMap = new MimetypesFileTypeMap();
		resp.headers().set(HttpHeaderNames.CONTENT_TYPE, mimeTypeMap.getContentType(file));
		ctx.writeAndFlush(resp).addListener(ChannelFutureListener.CLOSE);
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		cause.printStackTrace();
		ctx.close();
	}

	private void sendDownloadFile(ChannelHandlerContext ctx, String fileName, HttpRequest request){
		try{

			final boolean keepAlive = false;

			File file = new File(SystemPropertyUtil.get("user.dir")+File.separator+"src"+File.separator+"main"+File.separator+"resources"+File.separator+"file"+File.separator+fileName);

			RandomAccessFile raf;
			try {
				raf = new RandomAccessFile(file, "r");
			} catch (FileNotFoundException ignore) {
				sendErrorToClient(ctx, HttpResponseStatus.NOT_FOUND);
				return;
			}
			long fileLength = raf.length();

			HttpResponse response = new DefaultHttpResponse(HTTP_1_1, OK);
			response.headers().set(HttpHeaderNames.CONTENT_LENGTH, fileLength+"");
			setContentTypeHeader(response, file);

			if (!keepAlive) {
				response.headers().set(HttpHeaderNames.CONNECTION, HttpHeaderValues.CLOSE);
			} else if (request.protocolVersion().equals(HTTP_1_0)) {
				response.headers().set(HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE);
			}

			// Write the initial line and the header.
			ctx.write(response);

			// Write the content.
			ChannelFuture sendFileFuture;
			ChannelFuture lastContentFuture;
			if (ctx.pipeline().get(SslHandler.class) == null) {
				sendFileFuture =
						ctx.write(new DefaultFileRegion(raf.getChannel(), 0, fileLength), ctx.newProgressivePromise());
				// Write the end marker.
				lastContentFuture = ctx.writeAndFlush(LastHttpContent.EMPTY_LAST_CONTENT);
			} else {
				sendFileFuture =
						ctx.writeAndFlush(new HttpChunkedInput(new ChunkedFile(raf, 0, fileLength, 8192)),
								ctx.newProgressivePromise());
				// HttpChunkedInput will write the end marker (LastHttpContent) for us.
				lastContentFuture = sendFileFuture;
			}

			sendFileFuture.addListener(new ChannelProgressiveFutureListener() {
				@Override
				public void operationProgressed(ChannelProgressiveFuture future, long progress, long total) {
					if (total < 0) { // total unknown
						System.err.println(future.channel() + " Transfer progress: " + progress);
					} else {
						System.err.println(future.channel() + " Transfer progress: " + progress + " / " + total);
					}
				}

				@Override
				public void operationComplete(ChannelProgressiveFuture future) {
					System.err.println(future.channel() + " Transfer complete.");
				}
			});

			// Decide whether to close the connection or not.
			if (!keepAlive) {
				// Close the connection when the whole content is written out.
				lastContentFuture.addListener(ChannelFutureListener.CLOSE);
			}
		} catch(Exception e){
			e.printStackTrace();
		}
	}

	private static void setContentTypeHeader(HttpResponse response, File file) {
		MimetypesFileTypeMap mimeTypesMap = new MimetypesFileTypeMap();
		response.headers().set(HttpHeaderNames.CONTENT_TYPE, mimeTypesMap.getContentType(file.getPath()));
	}
}
