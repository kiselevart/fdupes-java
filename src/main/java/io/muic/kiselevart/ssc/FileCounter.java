package io.muic.kiselevart.ssc;

import java.io.*;
import java.nio.file.*;
import java.nio.file.attribute.*;

public class FileCounter {
    public static void countFiles(Path path) {
        final int[] fileCount = new int[1];
        final int[] directoryCount = new int[1];
        final long[] totalSize = new long[1];

        try {
            Files.walkFileTree(path, new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    fileCount[0]++;
                    totalSize[0] += Files.size(file); 
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                    directoryCount[0]++;
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
                    System.err.println("Failed to access: " + file.toString() + " due to " + exc.getMessage());
                    return FileVisitResult.CONTINUE;
                }
            });
        } catch (IOException e) {
            System.err.println("An error occurred: " + e.getMessage());
            e.printStackTrace();
        }

        System.out.println("Total number of files is: " + ValueFormatter.formattedValue(fileCount[0]));
        System.out.println("Total number of directories is: " + ValueFormatter.formattedValue(directoryCount[0]));
        System.out.println("Total size of all files is: " + ValueFormatter.formattedValue(totalSize[0]) + " bytes");
    }
}
