package io.muic.kiselevart.ssc;

import java.io.*;
import java.nio.file.*;
import java.security.*;

public class ChecksumCalculator {
    public String algorithm;

    public ChecksumCalculator(String algorithm) {
        this.algorithm = algorithm;
    }

    public String calculateChecksum(Path file) throws NoSuchAlgorithmException, IOException {
        String algo = algorithm.toLowerCase();
        if (algo.equals("bbb")) {
            return calculateChecksumByteByByte(file);
        } else if (algo.equals("md5") || algo.equals("sha256")) {
            return calculateChecksumUsingBuffer(file);
        } else {
            throw new NoSuchAlgorithmException("Invalid Algorithm");
        }
    }

    private String calculateChecksumUsingBuffer(Path file) throws NoSuchAlgorithmException, IOException {
        MessageDigest md = MessageDigest.getInstance(algorithm);
        try (InputStream is = Files.newInputStream(file);
             BufferedInputStream bis = new BufferedInputStream(is)) {
            byte[] buffer = new byte[1024];
            int read;
            while ((read = bis.read(buffer)) != -1) {
                md.update(buffer, 0, read);
            }
        }
        byte[] digest = md.digest();
        return bytesToHex(digest);
    }

    private String calculateChecksumByteByByte(Path file) throws NoSuchAlgorithmException, IOException {
        try {
            byte[] content = Files.readAllBytes(file);
            return bytesToHex(content);
        } catch (IOException e) {
            System.err.println("Error reading file: " + file.toString() + ". " + e.getMessage());
            throw new RuntimeException("Error reading file: " + file.toString(), e);
        }
    }

    private String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }
}
