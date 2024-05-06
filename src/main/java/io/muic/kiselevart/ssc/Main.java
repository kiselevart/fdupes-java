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
        try {
            CommandLine cmd = parser.parse(options, args);
            String pathArg = cmd.getOptionValue("f");
            boolean printPaths = false;
            boolean printCount = false;

            if (pathArg != null) {
                Path path = Paths.get(pathArg);
                String algorithm = cmd.getOptionValue("a");
                if (algorithm == null) {algorithm = "sha256";}

                if (cmd.hasOption("c")) {
                    printCount = true;
                }
                if (cmd.hasOption("p")) {
                    printPaths = true;
                }

                FileCounter.countFiles(path);
                ChecksumCalculator checksumCalculator = new ChecksumCalculator(algorithm);
                DuplicateFinder duplicateFinder = new DuplicateFinder(checksumCalculator, printCount, printPaths);
                duplicateFinder.countDuplicates(path);

            }
            else {
                throw new ParseException("File path -f is required.");
            }

        } catch (ParseException e) {
            System.err.println("Error: " + e.getMessage());
            HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp("dirwalker", options);
        }
    }
}