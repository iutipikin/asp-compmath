package xyz.galera.compmath.integration;

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
        System.out.println(String.format("g=%.2f, m=%.2f, p=%.2f, func=%s, int=%.6f", g, m, p, f.toString(), tr.evaluate(200)));
        f.toCSV("trapezoidal.csv", 200);
    }
}
