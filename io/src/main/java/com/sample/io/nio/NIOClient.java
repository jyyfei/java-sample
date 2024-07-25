package com.sample.io.nio;

import java.io.ByteArrayOutputStream;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

public class NIOClient {
    private static Selector selector;
    public static void main(String[]args) throws Exception{
        selector = Selector.open();
        SocketChannel sc = SocketChannel.open();
        sc.configureBlocking(false);
        sc.connect(new InetSocketAddress("127.0.0.1",8080));
        sc.register(selector,SelectionKey.OP_READ);
        
        ByteBuffer bf = ByteBuffer.allocate(1024);
        bf.put("Hi,server,i'm client".getBytes());
        
        
        if(sc.finishConnect()){
            bf.flip();
            while(bf.hasRemaining()){
                sc.write(bf);
            }
            
            while(true){
                selector.select();
                Iterator<SelectionKey> it = selector.selectedKeys().iterator();
                while(it.hasNext()){
                    SelectionKey key = it.next();
                    
                    
                    if(key.isReadable()){
                        ByteArrayOutputStream bos = new ByteArrayOutputStream();
                        bf.clear();
                        SocketChannel othersc  = (SocketChannel)key.channel();
                        while(othersc.read(bf)>0){
                            bf.flip();
                            while(bf.hasRemaining()){
                                bos.write(bf.get());
                            }
                            bf.clear();
                        };
                        System.out.println("服务端返回的数据："+bos.toString());
                    }
                }
                selector.selectedKeys().clear();
            }
        }
    }
}