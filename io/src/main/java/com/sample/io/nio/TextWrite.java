package com.sample.io.nio;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.StandardOpenOption;

/**
 * @author JiYunFei
 */
public class TextWrite {
    public static void main(String[] args) throws IOException {
        // 坑爹的，FileOutputStream能创建文件，但不能创建多级目录下的文件
        // FileOutputStream outputStream = new FileOutputStream("file/hello.txt", true);

        String path = "file/hello.txt";
        File file = new File(path);
        if (!file.exists()) {
            String dir = file.getPath().replace(file.getName(), "");
            File dirFile = new File(dir);
            if (!dirFile.exists()) {
                dirFile.mkdirs();
            }

            file.createNewFile();
        }

        FileChannel fileChannel = FileChannel.open(file.toPath(), StandardOpenOption.WRITE);
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        byteBuffer.put("你好".getBytes());
        // 写模式切换成读模式
        byteBuffer.flip();
        fileChannel.write(byteBuffer);
        fileChannel.close();
    }
}
