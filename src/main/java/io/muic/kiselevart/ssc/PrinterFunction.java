package io.muic.kiselevart.ssc;

import java.util.*;
import java.nio.file.*;

public class PrinterFunction {
    public static void printDuplicates(Map<String, List<Path>> checksumMap) {
        System.out.println("Duplicate Paths:");
        for (List<Path> paths : checksumMap.values()) {
            for (Path path : paths) {
                System.out.println(path);
            }
            System.out.println();
        }
    }

    public static void printCount(Map<String, List<Path>> checksumMap) {
        int totalDuplicates = 0;
        for (List<Path> paths : checksumMap.values()) {
            totalDuplicates += paths.size() -1;
        }
        System.out.println("Total number of duplicate files: " + ValueFormatter.formattedValue(totalDuplicates));
    }

    public static void printFileInfo(int[] fileCount, int[] directoryCount, long[] totalSize) {
        System.out.println("Total number of files is: " + ValueFormatter.formattedValue(fileCount[0]));
        System.out.println("Total number of directories is: " + ValueFormatter.formattedValue(directoryCount[0]));
        System.out.println("Total size of all files is: " + ValueFormatter.formattedValue(totalSize[0]) + " bytes");
    }
}
