package com.sample.io.aio;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;

public class AIOServer {
    public static void main(String[] args) throws Exception {
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        AsynchronousServerSocketChannel serverSocketChannel = AsynchronousServerSocketChannel.open().bind(new InetSocketAddress(8097));
        serverSocketChannel.accept(null, new CompletionHandler<AsynchronousSocketChannel, Object>() {

            @Override
            public void completed(AsynchronousSocketChannel asc, Object attachment) {
                byteBuffer.clear();
                byteBuffer.put("hello".getBytes());
                byteBuffer.flip();

                asc.write(byteBuffer, null, new CompletionHandler<Integer, Object>() {

                    @Override
                    public void completed(Integer result, Object attachment) {
                        byteBuffer.clear();
                    }

                    @Override
                    public void failed(Throwable exc, Object attachment) {

                    }
                });

                asc.read(byteBuffer, null, new CompletionHandler<Integer, Object>() {

                    @Override
                    public void completed(Integer result, Object attachment) {

                        byteBuffer.flip();
                        String rs = new String(byteBuffer.array());
                        System.out.println(rs);
                    }

                    @Override
                    public void failed(Throwable exc, Object attachment) {

                    }
                });

            }

            @Override
            public void failed(Throwable exc, Object attachment) {

            }
        });
        Thread.sleep(1000 * 30);
    }
}
