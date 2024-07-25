package com.sample.io.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

/**
 * 使用nio实现客户端连接任何服务端
 */
public class SocketChannelTest {
    public static void main(String[] args) throws IOException {
        SocketChannel socketChannel = SocketChannel.open();
        socketChannel.connect(new InetSocketAddress(7777));
        ByteBuffer byteBuffer = ByteBuffer.wrap("你好啊".getBytes());
        socketChannel.write(byteBuffer);
        socketChannel.close();
    }
}
