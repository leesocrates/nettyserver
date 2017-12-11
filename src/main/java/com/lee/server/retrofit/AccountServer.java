package com.lee.server.retrofit;

import java.net.InetSocketAddress;

import handler.AccountRecordListHandler;
import handler.AddAccountRecordHandler;
import handler.FileDownloadHandler;
import handler.GetAccountInfoHandler;
import handler.GetFileHandle;
import handler.GetHtmlHandler;
import handler.GetImageHandler;
import handler.GetJsonHandler;
import handler.LoginHandler;
import handler.RegisterHandler;
import handler.UploadImageHandler;
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

public class AccountServer {

	private static final Router router = new Router().POST("/register", RegisterHandler.class)
			.POST("/login", LoginHandler.class);
	Handler handler = new Handler(router);

	public static void main(String[] args) throws Exception {
		int port = 8081;
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
