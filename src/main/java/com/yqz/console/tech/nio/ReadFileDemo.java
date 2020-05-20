package com.yqz.console.tech.nio;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class ReadFileDemo {

    public static void main(String[] args) throws Exception {
        readFile();
    }

    public static void readFile() throws FileNotFoundException, IOException {
        RandomAccessFile aFile = new RandomAccessFile("d:/wiki.txt", "rw");
        FileChannel inChannel = aFile.getChannel();
// 分配一个48字节大小的缓冲区
        ByteBuffer buf = ByteBuffer.allocate(48);
        int bytesRead = inChannel.read(buf); // 读取数据到缓冲区
        while (bytesRead != -1) {
            buf.flip();  // 将position重置为0
            while (buf.hasRemaining()) {
                System.out.print((char) buf.get()); // 读取数据并输出到控制台
            }
            buf.clear(); // 清理缓冲区
            bytesRead = inChannel.read(buf);
        }
        aFile.close();
    }
}
