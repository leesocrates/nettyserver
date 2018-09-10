package com.lee.wx;

import java.net.InetSocketAddress;

import com.lee.retrofit.handler.GetHtmlHandler;
import com.lee.wx.handler.GetWxHtmlHandler;
import com.lee.wx.handler.WxAuthHandler;
import com.lee.wx.handler.WxMessageHandler;
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

public class WxServer {

    private static final Router router = new Router().GET("/wx", WxAuthHandler.class)
            .POST("/wx", WxMessageHandler.class)
            .GET("/wxPage", GetWxHtmlHandler.class);
    private static Handler handler = new Handler(router);

    public static void main(String[] args) throws Exception {
        int port = 80;
        if (args.length > 0) {
            try {
                port = Integer.parseInt(args[0]);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
        run(port);
    }

    public static void run(final int port) throws Exception {
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
