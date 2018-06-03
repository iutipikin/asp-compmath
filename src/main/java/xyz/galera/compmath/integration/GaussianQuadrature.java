package xyz.galera.compmath.integration;

import java.util.function.Function;

import static java.lang.Math.*;

public class GaussianQuadrature {
    
    private static Integer N;

    private static float[] lroots;
    private static float[] weight;
    private static float[][] lcoef;

    public GaussianQuadrature (Integer degree) {
        N = degree;
        lroots = new float[N];
        weight = new float[N];
        lcoef = new float[N + 1][N + 1];
        legendrePolyCoef();
        calculateLegendreRoots();
    }

    private static void legendrePolyCoef() {
        lcoef[0][0] = lcoef[1][1] = 1f;

        for (int n = 2; n <= N; n++) {

            lcoef[n][0] = -(n - 1) * lcoef[n - 2][0] / n;

            for (int i = 1; i <= n; i++) {
                lcoef[n][i] = ((2 * n - 1) * lcoef[n - 1][i - 1]
                        - (n - 1) * lcoef[n - 2][i]) / n;
            }
        }
    }

    private static Float evaluateLegendrePoly(int n, float x) {
        float s = lcoef[n][n];
        for (int i = n; i > 0; i--)
            s = s * x + lcoef[n][i - 1];
        return s;
    }

    private static Float evaluateLegendreDerivative(int n, float x) {
        return n * (x * evaluateLegendrePoly(n, x) - evaluateLegendrePoly(n - 1, x)) / (x * x - 1);
    }

    private static void calculateLegendreRoots() {
        Float xPrev, xCur;
        for (int i = 1; i <= N; i++) {
            xPrev = - (float) cos(PI * (i - 0.25) / (N + 0.5));
            xCur = xPrev;
            for(int j=0; i<j; j++){
                xCur = xPrev - evaluateLegendrePoly(N, xPrev) / evaluateLegendreDerivative(N, xPrev);
                xPrev = xCur;
            }
            lroots[i - 1] = xCur;
            weight[i - 1] = 2 / ((1 - xCur * xCur) * (float) pow(evaluateLegendreDerivative(N, xCur), 2));
        }
    }

    @SuppressWarnings("WeakerAccess")
    public float integrate(Function<Float, Float> f, float a, float b) {
        float c1 = (b - a) / 2, c2 = (b + a) / 2, sum = 0;

        for (int i = 0; i < N; i++)
            sum += weight[i] * f.apply(c1 * lroots[i] + c2);
        return c1 * sum;
    }

    public String toString() {
        String summary = "";
//        roots
        summary = summary.concat("Roots: ");
        for (int i = 0; i < N; i++)
            summary = summary.concat(String.format(" %f", lroots[i]));
        summary = summary.concat("\n");
//        weights
        summary = summary.concat("Weights: ");
        for (int i = 0; i < N; i++)
        summary = summary.concat(String.format(" %f", weight[i]));
        return summary;
    }

    public static void main(String[] args) {

        for (int i=2; i<=9; i++){
            GaussianQuadrature q = new GaussianQuadrature(i);
            System.out.println(q.toString());
            Func f = new Func(2f,1f,1f);
            System.out.println(f.toString());
            System.out.println(String.format("N=%s, Result: %.7f at [%.2f, %.2f]\n\n",i, q.integrate(f::evaluate, f.getBoundaries().getKey(), f.getBoundaries().getValue()),
                    f.getBoundaries().getKey(), f.getBoundaries().getValue()));

        }
    }

}
