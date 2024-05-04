package io.muic.kiselevart.ssc;

import java.io.*;
import java.nio.file.*;
import java.security.*;

public class ChecksumService {
    private static final int BUFFER_SIZE = 1024;

    public String calculateChecksum(Path file, String algorithm) throws NoSuchAlgorithmException, IOException {
        MessageDigest md = MessageDigest.getInstance(algorithm);
        try (InputStream is = Files.newInputStream(file);
            BufferedInputStream bis = new BufferedInputStream(is)) {
            byte[] buffer = new byte[BUFFER_SIZE];
            int read = 0;
            while ((read = bis.read(buffer)) != -1) {
                md.update(buffer, 0, read);
            }
        }
        byte[] digest = md.digest();
        return bytesToHex(digest);
    }

    private String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }
}
