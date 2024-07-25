package com.sample.io.nio;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

public class NIOServer {
    private static Selector selector;
    private static ServerSocketChannel serverSocketChannel;
    private static ByteBuffer bf = ByteBuffer.allocate(1024);
    public static void main(String[] args) throws Exception{
        init();
        while(true){
            selector.select();
            Iterator<SelectionKey> it = selector.selectedKeys().iterator();
            while(it.hasNext()){
                SelectionKey key = it.next();
                if(key.isAcceptable()){
                    System.out.println("连接准备就绪");
                    ServerSocketChannel server = (ServerSocketChannel)key.channel();
                    System.out.println("等待客户端连接中........................");
                    SocketChannel channel = server.accept();
                    channel.configureBlocking(false);
                    channel.register(selector,SelectionKey.OP_READ);
                }
                else if(key.isReadable()){
                    System.out.println("读准备就绪，开始读.......................");
                    SocketChannel channel = (SocketChannel)key.channel();
                    System.out.println("客户端的数据如下：");
                    
                    int readLen = 0;
                    bf.clear();
                    StringBuffer sb = new StringBuffer();
                    while((readLen=channel.read(bf))>0){
                        bf.flip();
                        byte [] temp = new byte[readLen];
                        bf.get(temp,0,readLen);
                        sb.append(new String(temp));
                        bf.clear();
                    }
                    if(-1==readLen){
                        channel.close();
                    }
                    System.out.println(sb.toString());
                    channel.write(ByteBuffer.wrap(("客户端，你传过来的数据是："+sb.toString()).getBytes()));
                }
                it.remove();
            }
        }
    }
    private static void init() throws Exception{
        selector = Selector.open();
        serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.configureBlocking(false);
        serverSocketChannel.socket().bind(new InetSocketAddress(8080));
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
    }
}