package com.sample.io.bio;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;

public class BIOServer {
    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(8679);
        while (true) {
            Socket socket = serverSocket.accept();
            socket.getOutputStream().write("hello".getBytes());
            try {
                ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
                byte[] bytes = new byte[1024];
                int read = 0;
                while ((read = socket.getInputStream().read(bytes)) > 0) {
                    byteBuffer.put(bytes, 0, read);
                    byteBuffer.flip();
                    String rs = new String(byteBuffer.array());
                    System.out.println(rs);
                    byteBuffer.clear();
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (!socket.isClosed()) {
                    try {
                        socket.close();
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }

                }
            }
        }
    }
}
