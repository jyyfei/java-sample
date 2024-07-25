package com.sample.io.nio;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.StandardOpenOption;

/**
 * @author JiYunFei
 */
public class TextRead {
    public static void main(String[] args) throws IOException {
        String path = "file/hello.txt";
        File file = new File(path);

        FileChannel fileChannel = FileChannel.open(file.toPath(), StandardOpenOption.READ);
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        int read = fileChannel.read(byteBuffer);
        fileChannel.close();

        // 切记，一定要记得转
        byteBuffer.flip();
        byte[] content = new byte[1024];
        byteBuffer.get(content, 0, read);

        System.out.println(new String(content, 0, read));

        // 如果不用get，用byteBuffer.array()就不用byteBuffer.flip();
    }
}
