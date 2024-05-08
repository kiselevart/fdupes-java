package io.muic.kiselevart.ssc;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DuplicateFinder {
    private ChecksumCalculator checksumCalculator;
    private boolean printCount = false;
    private boolean printPaths = false;

    public DuplicateFinder(ChecksumCalculator checksumCalculator, boolean printCount, boolean printPaths) {
        this.checksumCalculator = checksumCalculator;
        this.printCount = printCount;
        this.printPaths = printPaths;
    }

    private Map<List<Path>> pruneBySize(Path path)
    public void countDuplicates(Path path) {
        if (!printCount && !printPaths) {
            return;
        }
        
        Map<String, Integer> checksumMap = new HashMap<>();
        Map<String, List<String>> duplicatePathsMap = new HashMap<>();
    
        try {
            Files.walkFileTree(path, new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    try {
                        String checksum = checksumCalculator.calculateChecksum(file);                    
                        checksumMap.put(checksum, checksumMap.getOrDefault(checksum, 0) + 1);
                        if (printPaths) {
                            List<String> paths = duplicatePathsMap.computeIfAbsent(checksum, k -> new ArrayList<>());
                            paths.add(file.toString());
                        }
                    } catch (Exception e) {
                        System.err.println("Error calculating checksum: " + e.getMessage());
                        e.printStackTrace();
                    } 
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
    
        if (printPaths) {
            PrinterFunction.printDuplicates(duplicatePathsMap);
        }
    
        if (printCount) {
            PrinterFunction.printCount(checksumMap);
        }
    } 
}
