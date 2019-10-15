package ar.edu.itba.ss;

import ar.edu.itba.ss.model.Vector;

public class SystemConfiguration {

    // Box
    public static final double L = 1.5; // [1, 1.5] m
    public static final double W = 0.4; // [0.3, 0.4] m
//    public static final double D = 0.15; // [0.15, 0.25] m
    public static final double D = 0; // [0.15, 0.25] m
    public static final double fallenParticlesY = -L / 10; // m

    // Particle
    public static final double minRadius = 0.02/2d; // m
    public static final double maxRadius = 0.03/2d; //m
    public static final double mass = 0.01; // kg

    // Hole
    public static final Vector holeStart = new Vector(W / 2 - D / 2, 0);
    public static final Vector holeEnd = new Vector(W / 2 + D / 2, 0);

    // General
    public static final double totalTime = 6; // s
    public static final int addParticlesAttempts = 50;
    public static final double k_n = Math.pow(10, 5); // N/m
    public static final double k_t = 400000;
    public static final int gamma = 70; // kg/s
    public static final double g = 9.8; // m/seg

//    public static final double dt = 0.1 * Math.sqrt(mass / k_n);
    public static final double dt = 3.16e-5; //s
    public static final double dt2 = 0.001d; // s

    public static final String xyzWriterPath = "output.xyz";
    public static final String fallenParticlesWriterPath = "fallen_particles.txt";
}
