package xyz.galera.compmath.utils;

import java.io.IOException;
import java.nio.file.*;

public class CSVGen {
    public static void toCSV (String fileName, byte[] data) {
        try {
            Path newFilePath;
            if (!Files.exists(Paths.get("figures/data/".concat(fileName)))) {
                newFilePath = Files.createFile(Paths.get("figures/data/".concat(fileName)));
            } else {
                newFilePath = Paths.get("figures/data/".concat(fileName));
            }
            Files.write(newFilePath, data, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
