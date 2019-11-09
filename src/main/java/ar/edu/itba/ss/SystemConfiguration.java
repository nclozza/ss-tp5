package ar.edu.itba.ss;

import ar.edu.itba.ss.model.Pair;
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
    public final double totalTime = 10; // s
    public final int addParticlesAttempts = 400;
    public final double k_n = Math.pow(10, 5); // N/m
    public final double k_t = 400000;
    public final int gamma = 70; // kg/s
    public final double g = 9.8; // m/seg

    public final double dt = 0.1 * Math.sqrt(this.mass / k_n);
    //    public final double dt = 0.00005d; //s
    public final double dt2 = 0.016d; // s


    // OBSTACLE
    public final Vector obstacleCenter = new Vector(W / 2, D + (14 * maxRadius));
    public final double obstacleRadius = D;

    // CURVED WALLS
    public final double curvedWallY = L / 2;

    // Angle in degrees
    public final double curvedWallAngle = 20;

    public final double wallParticleRadius = 0.0045;

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

    public Pair<Vector, Vector> leftCurvedWallEndpoints() {
        Vector top = new Vector(0, curvedWallY);

        double angleRadians = this.curvedWallAngle / 360 * 2 * Math.PI;
        double x = Math.sin(angleRadians) * this.curvedWallY * this.W;
        Vector bottom = new Vector(x, 0);

        return Pair.of(top, bottom);
    }

    public Pair<Vector, Vector> rightCurvedWallEndpoints() {
        Vector top = new Vector(this.W, curvedWallY);

        double angleRadians = this.curvedWallAngle / 360 * 2 * Math.PI;
        double x = Math.sin(angleRadians) * this.curvedWallY * this.W;

        Vector bottom = new Vector(this.W - x, 0);

        return Pair.of(top, bottom);
    }
}
