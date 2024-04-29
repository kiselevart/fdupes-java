package io.muic.kiselevart.ssc;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.*;
import io.muzoo.ssc.assignment.tracker.SscAssignment;
import org.apache.commons.cli.*;


public class Main extends SscAssignment {
    public static void main(String[] args) {
        Options options = new Options();
        options.addOption("c", "count-duplicates", false, "prints the total count of duplicate files");
        options.addOption("a", "algorithm", true, "specifies the algorithm used");
        options.addOption("p", "print", false, "prints relative paths of all duplicates grouped together");
        options.addOption("f", true, "specifies path to folder, must be provided.");

        CommandLineParser parser = new DefaultParser();
        try {
            CommandLine cmd = parser.parse(options, args);
            if (cmd.hasOption("f")) {
                System.out.println("good");
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

        String pathArg = args[0];
        Path path = Paths.get(pathArg);
        final int[] fileCount = {0};

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
        
        System.out.println(fileCount[0]);
    }
}