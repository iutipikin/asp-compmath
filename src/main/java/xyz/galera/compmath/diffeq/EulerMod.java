package xyz.galera.compmath.diffeq;

import xyz.galera.compmath.utils.CSVGen;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import static java.lang.Math.cos;
import static java.lang.Math.sin;

public class EulerMod {

    private float[] xArr;
    private float[][] yArr;
    private List<Function<Float[], Float>> fArr;
    private int deg;

    public EulerMod (int nodes, int deg) {
        xArr = new float[nodes + 1];
        yArr = new float[nodes+1][deg];
        fArr = new ArrayList<>(deg);
        this.deg = deg;
    }

    public void calculateByEuler (float h) {
        for (int i = 0; i < xArr.length - 1; i++) {
            Float args[] = new Float[deg + 1];
            args[0] = 0f;
            for (int j=1; j < deg + 1; j++) {
                args[j] = yArr[i][j - 1];
            }
//            System.out.println(String.format("INIT ARGS y1%s=%.4f y2%s=%.4f",i, args[1], i, args[2]));
            for (int j = 0; j < deg; j++) {
//                forecast for y_j
                args[j+1] = yArr[i][j] + h * fArr.get(j).apply(args);
//                yArr[i+1][j] = yArr[i][j] + (h / 2) * ( fArr.get(j).apply(args) + fArr.get(j).apply(args));
            }
            for (int j = 0; j < deg; j++) {
//                forecast for y_j
//                args[j+1] = yArr[i][j] + h * fArr.get(j).apply(args);
                yArr[i+1][j] = yArr[i][j] + (h / 2) * ( fArr.get(j).apply(args) + fArr.get(j).apply(args));
            }
//            System.out.println(String.format("CALCULATED ARGS y1%s=%.4f y2%s=%.4f",i, args[1], i, args[2]));
//            System.out.println(String.format("x= %.2f y1= %.4f y2= %.4f",1 + h*i, yArr[i][0], yArr[i][1]));
        }
    }

    public void initDiffArr (Float... floats) {
        for (int i = 0; i < floats.length; i++) {
            yArr[0][i] = floats[i];
        }
    }

    public boolean addFunction(Function<Float[], Float> f){
        return fArr.add(f);
    }

    public String getResults2 (Integer steps) {
        return String.format("%s, %.7f, %s, %.7f\n", steps, yArr[steps + 1][0], steps, yArr[steps + 1][1]);
    }

//    variant #6
    public static void main (String args1[]) {
        int n[] = {10, 20, 30, 40, 50, 60, 70, 80, 90, 100};
        String result = "";
        for (int N : n) {
            float a=1,b=3;
            EulerMod euler = new EulerMod(N + 1,2);
//        args = [3] = {x,y1,y2}
            euler.addFunction((args) -> (float) sin(args[2]));
            euler.addFunction((args) -> (float) cos(args[1]));
            euler.initDiffArr(0.5f, -0.5f);
            euler.calculateByEuler((b-a)/N);
            result = result.concat(euler.getResults2(N));
        }
        CSVGen.toCSV("euler.csv", result.getBytes());
        System.out.println(result);
    }
}
