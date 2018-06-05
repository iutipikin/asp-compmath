package xyz.galera.compmath.integration;

import xyz.galera.compmath.utils.CSVGen;

public class TrapezoidalRule {
//    Trapezoidal rule integrator
    private static Func f;

    private Float integrate(Float a, Float b, Integer N) {
        Float h = (b - a) / N;
        Float sum = 0.5f * (f.evaluate(a) + f.evaluate(b));
        for (int i = 1; i < N; i++) {
            Float x = a + h * i;
            sum = sum + f.evaluate(x);
        }
        return sum * h;
    }

    public TrapezoidalRule (Float g, Float m ,Float p) {
        f = new Func(g, m, p);
    }

    public Float evaluate (Integer grid) {
        return integrate(f.getBoundaries().getKey(), f.getBoundaries().getValue(), grid);
    }

    public static void main(String[] args) {
        Float g=8f, m=-14f, p=1f;
        TrapezoidalRule tr = new TrapezoidalRule(g,m,p);
        String result = "";
        for (int i = 10; i < 300; i+=10) {
            System.out.println(String.format("g=%.2f, m=%.2f, p=%.2f, func=%s, int=%.6f", g, m, p, f.toString(), tr.evaluate(i)));
            result = result.concat(String.format("%s, %.6f\n", i, tr.evaluate(i)));
        }
        CSVGen.toCSV("trapezoidal.csv", result.getBytes());
//        f.toCSV("trapezoidal.csv", 200);
    }
}
