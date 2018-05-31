package xyz.galera.compmath.interpolation;

import lombok.NoArgsConstructor;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Stream;

public class HermitInterpolation {
    private Map<Float, List<Float>> initialMatrix;

    public HermitInterpolation() {
        initialMatrix = new HashMap<>();
    }

    private void loadMatrix () {
        //        read init matrix from file
        try (Stream<String> stream =
                     Files.lines(
                             Paths.get(Objects.requireNonNull(Thread.currentThread().getContextClassLoader().getResource("hermite_init.txt")).toURI()))) {
            stream.forEach(line -> {
                String[] values =  line.split("\\t", -1);
                initialMatrix.put(Float.parseFloat(values[0]), new ArrayList<>());
                for (int i = 1; i < values.length; i++) {
                    initialMatrix.get(Float.parseFloat(values[0])).add(Float.parseFloat(values[i]));
                }
            });
            System.out.println(initialMatrix.toString());

        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        HermitInterpolation hermit = new HermitInterpolation();
        hermit.loadMatrix();
    }
}
