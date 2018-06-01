package xyz.galera.compmath.interpolation;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Stream;

public class HermitInterpolation {
    private Map<Float, Float[]> initialMatrix;
    private Float[] z;
    private Map<Integer, Float[]> factorMatrix;

    public HermitInterpolation() {
        initialMatrix = new HashMap<>();
    }

    private void loadMatrix() {
        //        read init matrix from file, tab separated
        try (Stream<String> stream =
                     Files.lines(
                             Paths.get(Objects.requireNonNull(Thread.currentThread().getContextClassLoader().getResource("hermite_init.txt")).toURI()))) {
//            init factorMatrix with null values
            factorMatrix = new HashMap<>();
            stream.forEach(line -> {
                String[] values = line.split("\\t", -1);
                initialMatrix.put(Float.parseFloat(values[0]), new Float[values.length - 1]);
                for (int i = 1; i < values.length; i++) {
                    initialMatrix.get(Float.parseFloat(values[0]))[i - 1] = Float.parseFloat(values[i]);
                }
            });
            ArrayList<Float> tmpZ = new ArrayList<>();
            for (Map.Entry<Float, Float[]> x : initialMatrix.entrySet()) {
                for (int i = 0; i < x.getValue().length; i++) {
                    tmpZ.add(x.getKey());
                }
            }

//            init z array as repeated xi vector
            z = tmpZ.toArray(new Float[0]);
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }
    }

    private void ddFunc(Integer step) {
        if (step == 0) {
            factorMatrix.put(step, new Float[z.length]);
            for (int i = 0; i < z.length; i++) {
                factorMatrix.get(step)[i] = initialMatrix.get(z[i])[0];
            }
//            System.out.println(Arrays.deepToString(factorMatrix.get(step)));
        } else {
            factorMatrix.put(step, new Float[z.length]);
            for (int i = 0; i + step < z.length; i++) {
                if (z[i + step] - z[i] == 0) {
                    factorMatrix.get(step)[i] = initialMatrix.get(z[i])[step];
                } else {
//                    System.out.println(String.format("i=%s, step=%s, fM=%s", i, step, Arrays.deepToString(factorMatrix.get(step -1))));
                    factorMatrix.get(step)[i] = (factorMatrix.get(step - 1)[i + 1] - factorMatrix.get(step - 1)[i]) / (z[i + step] - z[i]);
                }
            }
        }
    }

    private void calculateFactorMatrix() {
        for (int i = 0; i < z.length; i++) {
            ddFunc(i);
        }
        factorMatrix.forEach((k, v) -> System.out.println(String.format("dd degree: %s, <factors>=%s", k, Arrays.deepToString(v))));
    }

    private Float evaluate(Float x) {
        Float f = factorMatrix.get(0)[0];
        Float difference = 1.0f;

        for (int i = 1; i < factorMatrix.keySet().size(); i++) {
            for (int j = 0; j < i; j++) {
                difference *= (x - z[j]);
            }
            f += factorMatrix.get(i)[0] * difference;
            difference = 1.0f;
        }
        return f;
    }

    public static void main(String[] args) {
        HermitInterpolation hermit = new HermitInterpolation();
        hermit.loadMatrix();
        hermit.calculateFactorMatrix();
        Map<Float, Float> plot = new TreeMap<>();
//        evaluation
        for (float i = 0.5f; i <= 4.5f; i+=0.01) {
            plot.put((float) i, hermit.evaluate((float) i));
            System.out.println(String.format("%.2f, %.10f", i, plot.get(i)));
        }

//        plot.put((float) 10, hermit.evaluate((float) 10));
//        plot.keySet().forEach(k -> {
//            System.out.print(String.format("%.10f,", k));
//        });
//        System.out.print("\n");
//        plot.values().forEach(v -> {
//            System.out.print(String.format("%.10f,", v));
//        });

    }
}
