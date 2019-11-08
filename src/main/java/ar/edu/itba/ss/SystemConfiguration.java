package ar.edu.itba.ss;

import ar.edu.itba.ss.model.Vector;

public class SystemConfiguration {
    // Box
    public final double L = 1.5; // [1, 1.5] m
    public final double W = 0.4; // [0.3, 0.4] m
    //    public static final double D = 0.15; // [0.15, 0.25] m
    public final double D = 0.10; // [0.15, 0.25] m
    public final double fallenParticlesY = -L / 10; // m

    // Particle
    public final double minRadius = 0.02 / 2d; // m
    public final double maxRadius = 0.03 / 2d; //m
    public final double mass = 0.01; // kg

    // Hole

    // General
    public final double totalTime = 2; // s
    public final int addParticlesAttempts = 70;
    public final double k_n = Math.pow(10, 5); // N/m
    public final double k_t = 400000;
    public final int gamma = 70; // kg/s
    public final double g = 9.8; // m/seg

    //    public static final double dt = 0.1 * Math.sqrt(mass / k_n);
    public final double dt = 0.00001d; //s
    public final double dt2 = 0.016d; // s

    private SystemConfiguration() {
    }

    static SystemConfiguration defaultConfig() {
        return new SystemConfiguration();
    }

    public Vector holeStart() {
        return new Vector(W / 2 - D / 2, 0);
    }

    public Vector holeEnd() {
        return new Vector(W / 2 + D / 2, 0);
    }
}
