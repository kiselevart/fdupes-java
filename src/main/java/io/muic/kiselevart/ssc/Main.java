package io.muic.kiselevart.ssc;

import java.nio.file.Files;
import io.muzoo.ssc.assignment.tracker.SscAssignment;

public class Main  extends SscAssignment  {
    public static void main(String[] args) {
        String dir = System.getProperty("user.dir");
        System.out.println(dir);

        System.out.println("Number of arguments: " + args.length);
        System.out.println("Arguments:");
        for (int i = 0; i < args.length; i++) {
            System.out.println((i + 1) + ": " + args[i]);
        }
        //Files.walkFileTree(null, null);
    }
}