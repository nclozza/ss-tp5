package ar.edu.itba.ss;

import java.util.Random;

public class Helper {
    //    private static final long seed = 1570750146758l; //System.currentTimeMillis();
    private static final long seed = System.currentTimeMillis();
    private static Random random = new Random(seed);

    public static double randomDouble(double min, double max) {
        return (random.nextDouble() * (max - min)) + min;
    }

    public static double clamp(double value, double min, double max) {
        return value > max ? max : value < min ? min : value;
    }
}
