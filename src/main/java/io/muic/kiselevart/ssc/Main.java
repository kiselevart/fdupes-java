package io.muic.kiselevart.ssc;

import java.io.*;
import java.util.*;
import java.nio.file.*;
import java.nio.file.attribute.*;
import io.muzoo.ssc.assignment.tracker.SscAssignment;
import org.apache.commons.cli.*;
import java.util.function.Function;
import java.security.*;

public class Main extends SscAssignment {
    public static void main(String[] args) {
        Options options = new Options();
        options.addOption("c", "count-duplicates", false, "prints the total count of duplicate files");
        options.addOption("a", "algorithm", true, "specifies the algorithm used");
        options.addOption("p", "print", false, "prints relative paths of all duplicates grouped together");
        options.addOption("f", true, "specifies path to folder, must be provided.");

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = null;

        try {
            cmd = parser.parse(options, args);
        } catch (ParseException e) {
            System.err.println("Error passing arguments " + e.getMessage());
        }

        String path = cmd.getOptionValue("p");
        String algorithm = Optional.ofNullable(cmd.getOptionValue("a")).orElse("sha256");

        ChecksumService checksumService = new ChecksumService();
        FileSystemService fileSystemService = new FileSystemService();

        try {
            Map<String, List<Path>> duplicates = fileSystemService.findDuplicates(Paths.get(path), algorithm);
            duplicates.forEach((checksum, paths) -> {
                System.out.println("Checksum: " + checksum);
                paths.forEach(System.out::println);
                System.out.println();
            });
        } catch (IOException e) {
            System.err.println("Error processing files: " + e.getMessage());
        }
    }
}