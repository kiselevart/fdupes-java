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

    public DuplicateFinder(ChecksumCalculator checksumCalculator) {
        this.checksumCalculator = checksumCalculator;
    }

    public void countDuplicates(Path path, boolean printCount, boolean printPaths) {
        if (!printCount && !printPaths) {
            return;
        }

        Map<Long, List<Path>> duplicateSizes = pruneBySize(path);
        Map<String, List<Path>> checksumMap = countDuplicatesChecksum(path, duplicateSizes);

        if (printPaths) {
            PrinterFunction.printDuplicates(checksumMap);
        }
    
        if (printCount) {
            PrinterFunction.printCount(checksumMap);
        }
    }

    private Map<Long, List<Path>> pruneBySize(Path path) {
        Map<Long, List<Path>> duplicateSizes = new HashMap<>();
        try {
            Files.walkFileTree(path, new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    duplicateSizes.computeIfAbsent(attrs.size(), k -> new ArrayList<>()).add(file);
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

        duplicateSizes.entrySet().removeIf(entry -> entry.getValue().size() == 1);
        return duplicateSizes;
    }

    private Map<String, List<Path>> countDuplicatesChecksum(Path path, Map<Long, List<Path>> duplicateSizes) {
        Map<String, List<Path>> checksumMap = new HashMap<>();

        for (List<Path> paths : duplicateSizes.values()) {
            for (Path filePath : paths) {
                try {
                    String checksum = checksumCalculator.calculateChecksum(filePath);
                    List<Path> checksumPaths = checksumMap.getOrDefault(checksum, new ArrayList<>());
                    checksumPaths.add(filePath);
                    checksumMap.put(checksum, checksumPaths);
                } catch (Exception e) {
                    System.err.println("Error calculating checksum: " + e.getMessage());
                    e.printStackTrace();
                }
            }
        }

        checksumMap.entrySet().removeIf(entry -> entry.getValue().size() == 1);
        return checksumMap;
    } 
}
