package ar.edu.itba.ss;

import java.util.Random;

public class Helper {

    private static Random random;
    private static final long seed = System.currentTimeMillis();

    public static Random getRandom() {
        if (random == null) {
            random = new Random(seed);
        }

        return random;
    }
}