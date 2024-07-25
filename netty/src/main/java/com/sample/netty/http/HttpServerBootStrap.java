package com.sample.netty.http;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;

/**
 * BootStrap是引导的意思
 * 自定义http服务端
 *
 * @author JiYunFei
 */
public class HttpServerBootStrap {
    public static void main(String[] args) {
        NioEventLoopGroup boss = new NioEventLoopGroup();
        NioEventLoopGroup worker = new NioEventLoopGroup();

        ServerBootstrap bootstrap = new ServerBootstrap();

        // group用来设置主从reactor
        // channel用来设置一个服务器端的通道实现
        // handler用来添加到boss（主reactor）的处理器
        // childHandler用来添加到worker对读写处理的handler
        bootstrap.group(boss, worker)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        socketChannel.pipeline().addLast("codec", new HttpServerCodec());
                        socketChannel.pipeline().addLast("process", new SimpleChannelInboundHandler<HttpObject>() {
                            @Override
                            protected void channelRead0(ChannelHandlerContext channelHandlerContext, HttpObject httpObject) throws Exception {
                                if (httpObject instanceof HttpRequest) {
                                    HttpRequest request = (HttpRequest) httpObject;
                                    System.out.println("receive:\n" + request.toString());

                                    ByteBuf content = Unpooled.copiedBuffer("hello, 您的uri是：" + request.uri(), CharsetUtil.UTF_8);
                                    FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, content);

                                    // 告诉浏览器编码格式
                                    response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/plain;charset=utf-8");
                                    // 告诉body长度，必须带，要不然浏览器一直转圈圈
                                    response.headers().set(HttpHeaderNames.CONTENT_LENGTH, content.readableBytes());

                                    channelHandlerContext.writeAndFlush(response);
                                }
                            }
                        });
                    }
                });

        try {
            // 绑定端口返回一个异步对象，可以给这个对象添加监听，监听到了会执行lister内方法
            ChannelFuture future = bootstrap.bind(8002).sync();
            future.addListener(new GenericFutureListener<Future<? super Void>>() {
                public void operationComplete(Future<? super Void> future) throws Exception {
                    if (future.isSuccess()) {
                        System.out.println("bind success");
                    } else {
                        System.out.println("bind fail");
                    }
                }
            });
            // 最好带上，监听关闭事件
            future.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            boss.shutdownGracefully();
            worker.shutdownGracefully();
        }

    }
}
