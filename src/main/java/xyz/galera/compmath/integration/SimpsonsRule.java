package xyz.galera.compmath.integration;

import xyz.galera.compmath.utils.CSVGen;

public class SimpsonsRule {
    private static Func f;
    public Float integrate(Float a, Float b, int N) {
//        step
        Float h = (b - a) / (N - 1);

//        1/3
        Float sum = 1.0f / 3.0f * (f.evaluate(a) + f.evaluate(b));

//        4/3
        for (int i = 1; i < N - 1; i += 2) {
            Float x = a + h * i;
            sum += 4.0f / 3.0f * f.evaluate(x);
        }

//        2/3
        for (int i = 2; i < N - 1; i += 2) {
            Float x = a + h * i;
            sum += 2.0f / 3.0f * f.evaluate(x);
        }
        return sum * h;
    }

    public SimpsonsRule (Float g, Float m ,Float p) {
        f = new Func(g, m, p);
    }

    public Float evaluate (Integer grid) {
        return integrate(f.getBoundaries().getKey(), f.getBoundaries().getValue(), grid);
    }

    public static void main(String[] args) {
        Float g=8f, m=-14f, p=1f;
        SimpsonsRule tr = new SimpsonsRule(g,m,p);
        String result = "";
        for (int i = 10; i < 700; i+=10) {
            System.out.println(String.format("g=%.2f, m=%.2f, p=%.2f, func=%s, int=%.6f", g, m, p, f.toString(), tr.evaluate(i)));
            result = result.concat(String.format("%s, %.6f\n", i, tr.evaluate(i)));
        }
        CSVGen.toCSV("simpsons.csv", result.getBytes());
//        System.out.println(String.format("g=%.2f, m=%.2f, p=%.2f, func=%s, int=%.10f", g, m, p, f.toString(), tr.evaluate(10000)));
//        f.toCSV("simpsons.csv", 200);
    }
}
