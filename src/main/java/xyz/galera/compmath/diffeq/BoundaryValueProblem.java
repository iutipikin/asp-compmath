package xyz.galera.compmath.diffeq;

import xyz.galera.compmath.utils.CSVGen;

import java.util.function.Function;

import static java.lang.Math.*;

public class BoundaryValueProblem {

    private Function<Double, Double> p, q, f;

    public double[] solve(double c1, double c2, double c,
                          double d1, double d2, double d,
                          double a, double b, int N) {
//        init columns of the solver
        double u[] = new double[N + 1];
        double v[] = new double[N + 1];
        double y[] = new double[N + 1];

//        initial conditions
        double h = (b - a) / N;
        u[0] = (h * c) / (c1 * h - c2);
        v[0] = -c2 / (c1 * h - c2);
        double xi = a;

//        forward run, u[i] & v[i] i = 1 to N
        for (int i = 1; i < N; i++) {
            xi += h;
            double ai = 1 - (1f / 2f) * p.apply(xi) * h;
            double bi = q.apply(xi) * pow(h, 2) - 2;
            double gmi = 1 + (1f / 2f) * p.apply(xi) * h;
            double phi = f.apply(xi) * pow(h, 2);
            // 1/cos(x) hack
            if (abs(phi) > abs(2f*f.apply(xi - h) * pow(h, 2))){
                phi = (f.apply(xi-h) * pow(h, 2)) + (f.apply(xi+h) * pow(h, 2))/2f;
            }
            v[i] = -gmi / (bi + ai * v[i - 1]);
            u[i] = (phi - ai * u[i - 1]) / (bi + ai * v[i]);
//            System.out.println(String.format("x[%s]=%.4f, a=%.9f, b=%.9f, gm=%.9f, phi=%.9f", i, xi, ai, bi, gmi, phi));
//            System.out.println(String.format("v[%s]=%.9f, u[%s]=%.9f", i, v[i], i, u[i]));
        }

        double an = -d2;
        double bn = h * d1 + d2;
        double phin = h * d;
        v[N] = 0;
        u[N] = (phin - an * u[N - 1]) / bn;
        y[N] = u[N];

        for (int i = N - 1; i >= 0; i--) {
            y[i] = u[i] + v[i] * y[i + 1];
        }

        return y;
    }

    public BoundaryValueProblem(Function<Double, Double> _p, Function<Double, Double> _q, Function<Double, Double> _f) {
        p = _p;
        q = _q;
        f = _f;
    }

    //    variant #5
//    c1= -1, c2=0, c=1;
//    d1=0.74, d2=0.45, d=0;
//    a=0, b=PI;
    public static void main(String[] args) {
        BoundaryValueProblem bvp = new BoundaryValueProblem((x) -> pow(x, 2), (x) -> pow(x, 3), (x) -> 1f/cos(x));
        double a = 0, b = PI;
        int N = 100;
        double y[] = bvp.solve(-1f, 0, 1, 0.74f, 0.45f, 0, a, b, N);
        double h = (b - a) / N;
        String result = "";
        for (int i = 0; i < y.length; i++) {
            result = result.concat(String.format("%.6f, %.6f\n", a + h * i, y[i]));
            System.out.println(String.format("x=%.6f, y=%.6f", a + h * i, y[i]));
        }
        CSVGen.toCSV("bvp1.csv",result.getBytes());
    }
}
