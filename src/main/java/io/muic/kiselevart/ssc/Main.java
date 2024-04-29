package io.muic.kiselevart.ssc;

import java.io.IOException;
import java.util.*;
import java.nio.file.*;
import java.nio.file.attribute.*;
import io.muzoo.ssc.assignment.tracker.SscAssignment;
import org.apache.commons.cli.*;
import org.apache.commons.io.IOUtils;

import java.util.function.Function;

public class Main extends SscAssignment {
    public static void main(String[] args) {
        Options options = new Options();
        options.addOption("c", "count-duplicates", false, "prints the total count of duplicate files");
        options.addOption("a", "algorithm", true, "specifies the algorithm used");
        options.addOption("p", "print", false, "prints relative paths of all duplicates grouped together");
        options.addOption("f", true, "specifies path to folder, must be provided.");

        //String.format("%,d", 2000000) for future reference, this is how to comma format numbers

        CommandLineParser parser = new DefaultParser();
        try {
            CommandLine cmd = parser.parse(options, args);
            String pathArg = cmd.getOptionValue("f");
            if (pathArg != null) {
                Path path = Paths.get(pathArg);
                final int[] fileCount = {0};

                countFiles(path, fileCount);
                if (cmd.hasOption("c")) {
                    countDuplicates(path, ___);    
                }
            }
            else {
                throw new ParseException("File path -f is required.");
            }

        }
        catch (ParseException e) {
            System.err.println("Error: " + e.getMessage());
            HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp("myapp", options);
        }
    }

    private static void countFiles(Path path, final int[] fileCount) {
        try {
            Files.walkFileTree(path, new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    System.out.println(file.toAbsolutePath());
                    fileCount[0]++;
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

        System.out.println("Total number of files is: " + fileCount[0]);
    }

    private static void countDuplicates(Path path, Function<Path, String> checksumAlgorithm) {
        Map<String, Integer> checksumMap = new HashMap<>();

        try {
            Files.walkFileTree(path, new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    String checksum = checksumAlgorithm.apply(file);
                    checksumMap.put(checksum, checksumMap.getOrDefault(checksum, 0) + 1);
                    return FileVisitResult.CONTINUE;
                }

                @Override 
                public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
                    System.err.println("Failed to access: " + file.toString() + " due to " + exc.getMessage());
                    return FileVisitResult.CONTINUE;
                }
            });
        }
        catch (IOException e) {
            System.err.println("An error occurred: " + e.getMessage());
            e.printStackTrace();
        }

        int totalDuplicates = 0;
        for (int count : checksumMap.values()) {
            if (count > 1) {
                totalDuplicates += count-1;
            }
        }

        System.out.println("The total number of duplicate files is: " + totalDuplicates);
    }
}