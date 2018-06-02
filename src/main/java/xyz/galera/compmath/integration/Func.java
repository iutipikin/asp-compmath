package xyz.galera.compmath.integration;

import javafx.util.Pair;
import xyz.galera.compmath.utils.CSVGen;

import java.util.Comparator;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Stream;

public class Func {

    private static Float g, p, m;

    public Func (Float _g, Float _m, Float _p) {
        g = _g;
        p = _p;
        m = _m;
    }

    public Float evaluate (Float x) {
        return (float) Math.cos( Math.pow(x, 2) + (g -p) * x + (m -p));
    }

    public Pair<Float, Float> getBoundaries () {
        AtomicReference<Pair<Float, Float>> boundaries = new AtomicReference<>(new Pair<>(0f, 0f));
        Optional<Float> max = Stream.of((g-p),(m-p)).max(Comparator.naturalOrder());
        Optional<Float> min = Stream.of((g-p),(m-p)).min(Comparator.naturalOrder());
        max.ifPresent(m1 -> min.ifPresent(m2 -> boundaries.set(new Pair<>(m2, m1))));
        return boundaries.get();
    }

    public String toString() {
        return "cos(x^2 + (g-p)x + (m-p))";
    }

    public void toCSV (String filename, Integer grid) {
        String result = "";
        for (float i = this.getBoundaries().getKey(); i < this.getBoundaries().getValue(); i+= (this.getBoundaries().getValue() - this.getBoundaries().getKey()) /grid) {
            result = result.concat(String.format("%.3f, %.6f\n", i, this.evaluate(i)));
        }
        CSVGen.toCSV(filename, result.getBytes());
    }
}
