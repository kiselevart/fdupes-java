package kiselevart;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
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

    /**
     * Runs pruneBySize and countDuplicatesChecksum after to find duplicate files
     * @param path The path to search through
     * @param printCount Flag indicating whether to print the number of duplicates
     * @param printPaths Flag indicating whether to print the duplicate paths
     */
    public void countDuplicates(Path path, boolean printCount, boolean printPaths) {
        if (!printCount && !printPaths) {
            return;
        }

        Map<Long, List<Path>> duplicateSizes = pruneBySize(path);
        Map<String, List<Path>> checksumMap = countDuplicatesChecksum(duplicateSizes);

        if (printPaths) {
            PrinterFunction.printDuplicates(checksumMap);
        }
    
        if (printCount) {
            PrinterFunction.printCount(checksumMap);
        }
    }

    /**
     * Uses walkFileTree to traverse path and find files with duplicate file sizes
     * @param path The path to search through 
     * @return A map with file size as the key, and a list of the files with that size as the value
     */
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

    /**
     * Finds duplicates in given list using the configured checksum algorithm
     * @param duplicateSizes Map of file paths keyed by their size
     * @return A map of file paths keyed by their hashcode
     */
    private Map<String, List<Path>> countDuplicatesChecksum(Map<Long, List<Path>> duplicateSizes) {
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
