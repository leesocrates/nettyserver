package com.lee.server.retrofit;

import java.net.InetSocketAddress;

import javax.swing.plaf.basic.BasicBorders.MarginBorder;

import handler.*;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import io.netty.handler.codec.http.router.Handler;
import io.netty.handler.codec.http.router.Router;
import socket.SocketHandler;

public class RetrofitServer {

	private static final Router router = new Router().POST("/register", RegisterHandler.class)
			.POST("/login", LoginHandler.class).POST("addAccountRecord", AddAccountRecordHandler.class)
			.GET("accountRecordList", AccountRecordListHandler.class)
			.POST("/upload/image", UploadImageHandler.class)
			.GET("/getAccount", GetAccountInfoHandler.class)
			.GET("/getHtml/:path", GetHtmlHandler.class).GET("/getFile/:path", GetFileHandle.class)
			.GET("/getJson/:path", GetJsonHandler.class).GET("/getFileDir/:path", FileDownloadHandler.class)
			.GET("/getImage/:path", GetImageHandler.class).GET("getJs/:path", GetJsHandler.class)
			.POST("/submitevaluation", SubmitEvaluationHandler.class);
	Handler handler = new Handler(router);

	public static void main(String[] args) throws Exception {
		int port = 8080;
		if (args.length > 0) {
			try {
				port = Integer.parseInt(args[0]);
			} catch (NumberFormatException e) {
				e.printStackTrace();
			}
		}
		
		new RetrofitServer().run(port);
	}

	public void run(final int port) throws Exception {
		EventLoopGroup bossGroup = new NioEventLoopGroup();
		EventLoopGroup workerGroup = new NioEventLoopGroup();
		try {
			ServerBootstrap b = new ServerBootstrap();
			b.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class)
					.option(ChannelOption.SO_RCVBUF, 160 * 1024).childHandler(new ChannelInitializer<SocketChannel>() {
						@Override
						protected void initChannel(SocketChannel ch) throws Exception {
							// ch.pipeline().addLast(new SocketHandler());
							ch.pipeline().addLast(new HttpRequestDecoder());
							ch.pipeline().addLast(new HttpResponseEncoder());
							ch.pipeline().addLast(handler.name(), handler);
						}
					});
			ChannelFuture future = b.bind(new InetSocketAddress(port)).sync();
			System.out.println("retrofit server start at : " + "http://localhost:" + port);
			future.channel().closeFuture().sync();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			bossGroup.shutdownGracefully();
			workerGroup.shutdownGracefully();
		}
	}

}
