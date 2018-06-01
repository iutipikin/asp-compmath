package xyz.galera.compmath.integration;

public class TrapezoidRule {
//    Trapezoid rule integrator


    public static void main(String[] args) {
        Func f = new Func(8.0f, 4.0f, 1.0f);
        for (float i = f.getBoundaries().getKey(); i < f.getBoundaries().getValue(); i += 0.1) {
            System.out.println(String.format("%.5f, %.5f", i, f.evaluate(i)));
        }
    }
}
