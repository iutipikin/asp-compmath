package xyz.galera.compmath.diffeq;

import xyz.galera.compmath.utils.CSVGen;

import java.util.function.BiFunction;
import static java.lang.Math.*;

public class RungeKutta {

    private static void runge(BiFunction<Float, Float, Float> y1, BiFunction<Float, Float, Float> y2,
                              float[] x,
                              float[] y1Arr,
                              float[] y2Arr,
                              float h) {
        for (int n = 0; n < x.length - 1; n++) {
//            y1
            float y1k1 = h * y1.apply(x[n], y1Arr[n] + y2Arr[n]);
            float y1k2 = h * y1.apply(x[n] + h / 2.0f, y1Arr[n] + y2Arr[n] + y1k1 / 2.0f);
            float y1k3 = h * y1.apply(x[n] + h / 2.0f, y1Arr[n] + y2Arr[n] + y1k2 / 2.0f);
            float y1k4 = h * y1.apply(x[n] + h, y1Arr[n] + y2Arr[n] + y1k3);
            x[n + 1] = x[n] + h;
            y1Arr[n + 1] = y1Arr[n] + (y1k1 + 2.0f * (y1k2 + y1k3) + y1k4) / 6.0f;
//            y2
            float y2k1 = h * y2.apply(x[n], y1Arr[n] - y2Arr[n]);
            float y2k2 = h * y2.apply(x[n] + h / 2.0f, y1Arr[n] - y2Arr[n] + y2k1 / 2.0f);
            float y2k3 = h * y2.apply(x[n] + h / 2.0f, y1Arr[n] - y2Arr[n] + y2k2 / 2.0f);
            float y2k4 = h * y2.apply(x[n] + h, y1Arr[n] - y2Arr[n] + y2k3);
            x[n + 1] = x[n] + h;
            y2Arr[n + 1] = y2Arr[n] + (y2k1 + 2.0f * (y2k2 + y2k3) + y2k4) / 6.0f;
        }
    }

//    variant #8
    public static void main(String[] args) {
        float a = 1, b = 5;
        int N = 1000;
        float h = (b - a) / N;

        float[] xArr = new float[N + 1];
        float[] y1Arr = new float[N + 1];
        float[] y2Arr = new float[N + 1];

//        init settings
        xArr[0] = a;
        y1Arr[0] = -0.6f;
        y2Arr[0] = 2;

        for (float dx = a; dx < b ; dx += h) {
            runge((x, y1) -> (float) (x * cos(y1)), (x, y2) -> (float) (sin(y2)),
                    xArr,
                    y1Arr,
                    y2Arr,
                    h);
        }
        String rungeY1 = "";
        String rungeY2 = "";
        for (int i = 0; i < xArr.length; i++){
            if (i % 10 == 0)
                System.out.printf("x=%.5f, y1=%.5f, y2=%.5f\n",
                        xArr[i], y1Arr[i], y2Arr[i]);
            rungeY1 = rungeY1.concat(String.format("%.5f, %.5f \n", xArr[i], y1Arr[i]));
            rungeY2 = rungeY2.concat(String.format("%.5f, %.5f \n", xArr[i], y2Arr[i]));
        }
        CSVGen.toCSV("runge1.csv", rungeY1.getBytes());
        CSVGen.toCSV("runge2.csv", rungeY2.getBytes());
    }
}
