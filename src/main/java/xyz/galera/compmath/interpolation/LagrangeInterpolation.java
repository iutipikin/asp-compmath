package xyz.galera.compmath.interpolation;

import javafx.util.Pair;
import xyz.galera.compmath.utils.CSVGen;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Stream;

public class LagrangeInterpolation {

    private List<Pair<Float, Float>> initFunction;

    private void loadFunction() {
        //        read init function values (z(x)) from file, tab separated
        try (Stream<String> stream =
                     Files.lines(
                             Paths.get(Objects.requireNonNull(Thread.currentThread().getContextClassLoader().getResource("lagrange_init.txt")).toURI()))) {
            stream.forEach(line -> {
                String[] values = line.split("\\t", -1);
                initFunction.add(new Pair<>(Float.parseFloat(values[0]), Float.parseFloat(values[1])));
            });
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }
    }

    private Float lagrangeFunc(Float x) {
        Float sum = 0.0f;
        for (int j = 0; j < initFunction.size(); j++) {
            Float lx = 1.0f;
            for (int i = 0; i < initFunction.size(); i++){
                if (j != i)
                    lx *= (x - initFunction.get(i).getKey()) / (initFunction.get(j).getKey() - initFunction.get(i).getKey());
            }
            sum += initFunction.get(j).getValue() * lx;
        }
        return sum;
    }


    public LagrangeInterpolation() {
        initFunction = new ArrayList<>();
    }

    public static void main(String[] args) {
        LagrangeInterpolation lagrange = new LagrangeInterpolation();
        lagrange.loadFunction();
        String result = "";
        for (float i =0; i < 10; i+=0.01){
            Float f = lagrange.lagrangeFunc(i);
            result = result.concat(String.format("%.2f, %.5f\n", i, f));
            System.out.println(String.format("%.2f, %.5f", i, f));
        }
        CSVGen.toCSV("lagrange.csv", result.getBytes());
        System.out.println(lagrange.initFunction.toString());
    }

}
