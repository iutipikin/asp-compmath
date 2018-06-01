package xyz.galera.compmath.utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class CSVGen {
    public static void toCSV (String fileName, byte[] data) {
        try {
            Path newFilePath = Files.createFile(Paths.get("figures/data/".concat(fileName)));
            Files.write(newFilePath, data);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
