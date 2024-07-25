package com.sample.io.bio;

import java.io.IOException;
import java.net.Socket;
import java.nio.ByteBuffer;

public class BIOClient {
    public static void main(String[] args) throws IOException {
        Socket socket = new Socket("127.0.0.1", 8679);
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        byte[] bytes = new byte[1024];
        int read = 0;
        while ((read = socket.getInputStream().read(bytes)) > 0) {
            byteBuffer.put(bytes, 0, read);
            byteBuffer.flip();
            String rs = new String(byteBuffer.array());
            byteBuffer.clear();
            System.out.println(rs);
            socket.getOutputStream().write("reply".getBytes());
        }
    }
}
