package io.muic.kiselevart.ssc;

import java.util.*;

public class PrinterFunction {
    public static void printDuplicates(Map<String, List<String>> duplicatePaths) {
        System.out.println("Duplicate Paths:");
        for (Map.Entry<String, List<String>> entry : duplicatePaths.entrySet()) {
            List<String> paths = entry.getValue();
            if (paths.size() > 1) {
                for (String duplicatePath : paths) {
                    System.out.println(duplicatePath);
                }
                System.out.println();
            }
        }
    }

    public static void printCount(Map<String, Integer> checksumMap) {
        int totalDuplicates = 0;
        for (int count : checksumMap.values()) {
            if (count > 1) {
                totalDuplicates += count - 1;
            }
        }
        System.out.println("Total number of duplicate files: " + ValueFormatter.formattedValue(totalDuplicates));
    }

    public static void printFileInfo(int[] fileCount, int[] directoryCount, long[] totalSize) {
        System.out.println("Total number of files is: " + ValueFormatter.formattedValue(fileCount[0]));
        System.out.println("Total number of directories is: " + ValueFormatter.formattedValue(directoryCount[0]));
        System.out.println("Total size of all files is: " + ValueFormatter.formattedValue(totalSize[0]) + " bytes");
    }
}
