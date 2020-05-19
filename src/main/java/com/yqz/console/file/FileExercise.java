package com.yqz.console.file;

import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;

public class FileExercise {

    public static void main(String[] args) throws Exception {
        String targetDirectory = "d:/dd";
        String url = "https://replaylive.autohome.com.cn/fragments/z1.athmlive.9523/1571727668610-1571727675820.ts";


        deleteDirectoryForce(targetDirectory);

        String fileName = downloadFile(url, targetDirectory);
        moveFile(Paths.get(fileName), Paths.get(targetDirectory, "temp"));


        //        url=java.net.URLEncoder.encode(url, "utf-8");
//
//        writeFileIfNotExists("many experts to regard him as the greatest clay");
//        writeFileIfNotExists("hello world");
    }

    public static void moveFile(Path filePath, Path targetDirectory) {
        Path targetFile =targetDirectory.resolveSibling(filePath.getFileName());
        try {
            if (!targetFile.toFile().exists())
                Files.move(filePath, targetFile, StandardCopyOption.REPLACE_EXISTING);
            System.out.printf("succeed to move file from %s to %s ", filePath, targetFile);
        } catch (Exception e) {
            System.out.printf("fail to move file error, from %s to %s ,cause: %s", filePath, targetFile, e.toString());
            e.printStackTrace();
        }
        System.out.println();
    }

    public static void writeFileIfNotExists(String sentence) {
        Path path = Paths.get("D:/", "wiki.txt");
        try {
            byte[] bytes = sentence.getBytes("UTF-8");
            Files.write(path, bytes, StandardOpenOption.CREATE_NEW);
        } catch (IOException e) {
            System.err.println(e);
        }
    }

    public static boolean createDirectory(String directory) {
        if (Files.notExists(Paths.get(directory))) {
            try {
                Files.createDirectories(Paths.get(directory));
            } catch (IOException e) {
                System.out.println(  "write file error: ".concat(e.toString()));
                return false;
            }
        }
        return true;
    }

    public static String downloadFile(String url, String targetDirectory) throws IOException {

        if (!Files.exists(Paths.get(targetDirectory)))
            Files.createDirectories(Paths.get(targetDirectory));

        URL uri = new URL(url);//URI.create(url).toURL();
        String fileName = uri.getPath().substring(uri.getPath().lastIndexOf("/") + 1);
        fileName = Paths.get(targetDirectory, fileName).toString();

        Path filePath=Paths.get(fileName);
        createDirectory(filePath.getParent().toString());

        ReadableByteChannel readableByteChannel = Channels.newChannel(uri.openStream());
        try(FileOutputStream fileOutputStream = new FileOutputStream(fileName)){
            fileOutputStream.getChannel().transferFrom(readableByteChannel, 0, Long.MAX_VALUE);
        }


//        System.out.println(Paths.get(targetDirectory).resolve("1.txt"));
        System.out.printf("文件%s下载成功，size为%s Byte "+System.lineSeparator(), fileName, Files.size(Paths.get(fileName)));
        return fileName;
    }

    public static void deleteDirectoryForce(String targetDirectory)
            throws IOException {

        Path pathToBeDeleted = Paths.get(targetDirectory);
        if (!pathToBeDeleted.toFile().isDirectory() || !Files.exists(pathToBeDeleted)) {
            System.out.printf(targetDirectory + " is not directory or doesn't exists \\n");
            return;
        }


        Files.walkFileTree(pathToBeDeleted,
                new SimpleFileVisitor<Path>() {
                    @Override
                    public FileVisitResult postVisitDirectory(
                            Path dir, IOException exc) throws IOException {
                        Files.delete(dir);
                        System.out.println("delete dir:".concat(dir.toString()));
                        return FileVisitResult.CONTINUE;
                    }

                    @Override
                    public FileVisitResult visitFile(
                            Path file, BasicFileAttributes attrs)
                            throws IOException {
                        Files.delete(file);
                        System.out.println("delete file:".concat(file.toString()));
                        return FileVisitResult.CONTINUE;
                    }
                });

        System.out.println("succeed to delete directory: "+ targetDirectory);
    }

}
