package ar.edu.itba.ss;

import ar.edu.itba.ss.model.Particle;
import ar.edu.itba.ss.model.Vector;

import java.util.Set;

public class Force {

    private double L;
    private double W;
    private double D;

    public Force(final double L, final double W, final double D) {
        this.L = L;
        this.W = W;
        this.D = D;
    }


    public Vector calculate(final Particle particle, final Set<Particle> neighbours) {
        Vector force = new Vector(0, -particle.getMass() * SystemConfiguration.g);

        double totalFn = 0;

        for (Particle other : neighbours) {
            Pair<Double, Vector> f = particleForce(particle, other);
            totalFn += f.first();
            force = force.plusVector(f.second());
        }

        Particle wallPart1 = new Particle(40000, SystemConfiguration.W / 2 - SystemConfiguration.D / 2, 0, 0, 0, SystemConfiguration.minRadius, SystemConfiguration.mass);
        Particle wallPart2 = new Particle(40000, SystemConfiguration.W / 2 + SystemConfiguration.D / 2, 0, 0, 0, SystemConfiguration.minRadius, SystemConfiguration.mass);


        Pair<Double, Vector> forceWalls = getWallForces(particle);
        force = force.plusVector(forceWalls.second());
        totalFn += forceWalls.first();

        Pair<Double, Vector> forceWallPart1 = particleForce(particle, wallPart1);
        force = force.plusVector(forceWallPart1.second());
        totalFn += forceWallPart1.first();

        Pair<Double, Vector> forceWallPart2 = particleForce(particle, wallPart2);
        force = force.plusVector(forceWallPart2.second());
        totalFn += forceWallPart2.first();

        particle.setTotalFn(totalFn);

        return force;
    }

    private Pair<Double, Vector> particleForce(final Particle particle, final Particle other) {
        double fn = 0;
        double ft = 0;
        Vector en = other.getPosition().minusVector(particle.getPosition()).normalize();

        if (!particle.equals(other)) {
            double overlap = overlapping(particle, other);
            if (overlap > 0) {
                fn = getFn(overlap);
                ft = getFt(overlap, relativeSpeed(particle, other));
            }
        }

        return Pair.of(fn, new Vector(fn * en.getX() - ft * en.getY(), fn * en.getY() + ft * en.getX()));
    }

    // N.2
    private double getFn(final double overlapping) {
//        return 0;
        return -SystemConfiguration.k_n * overlapping;
    }

    // T.3
    private double getFt(final double overlapping, final double relativeSpeed) {
//        return 0;
        return -SystemConfiguration.k_t * overlapping * relativeSpeed;
    }


    private double overlapping(final Particle i, final Particle j) {
        double result = i.getRadius() + j.getRadius() - i.getPosition().minusVector(j.getPosition()).norm();


        return result > 0 ? result : 0;
    }

    private double relativeSpeed(final Particle i, final Particle j) {
        Vector direction = j.getPosition().minusVector(i.getPosition()).tangent();
        return i.getSpeed().minusVector(j.getSpeed()).projectedOn(direction);
    }


    private Pair<Double, Vector> getWallForces(final Particle particle) {
        Pair<Double, Vector> right = rightWall(particle);

        Pair<Double, Vector> left = leftWall(particle);

        Pair<Double, Vector> horizontal = bottomWall(particle);

        return Pair.of(right.first() + left.first() + horizontal.first(), right.second().plusVector(left.second()).plusVector(horizontal.second()));
    }

    private Pair<Double, Vector> leftWall(final Particle p) {
        double overlapping = 0;
        double enx = 0;
        double eny = 0;
        double fn;
        double ft;

        if (p.getPosition().getX() - p.getRadius() < 0) {
            overlapping = p.getRadius() - Math.abs(p.getPosition().getX());
            enx = -1;
            eny = 0;
        }

        fn = getFn(overlapping);
        ft = 0;
        return Pair.of(fn, new Vector(fn * enx - ft * eny, fn * eny + ft * enx));
    }

    private Pair<Double, Vector> rightWall(final Particle p) {
        double overlapping = 0;
        double enx = 0;
        double eny = 0;
        double fn;
        double ft;

        if (p.getPosition().getX() + p.getRadius() > W) {
            overlapping = p.getRadius() - (Math.abs(p.getPosition().getX() - W));
            enx = 1;
            eny = 0;
        }

        fn = getFn(overlapping);
        ft = 0;
        return Pair.of(fn, new Vector(fn * enx - ft * eny, fn * eny + ft * enx));
    }

    private Pair<Double, Vector> bottomWall(final Particle p) {
        double overlapping = 0;
        double enx = 0;
        double eny = 0;
        double fn;
        double ft;

        boolean shouldCrashBottom = (p.getPosition().getX() < SystemConfiguration.holeStart.getX() || p.getPosition().getX() > SystemConfiguration.holeEnd.getX()) && p.getPosition().getY() > 0;

        if (shouldCrashBottom && p.getPosition().getY() - p.getRadius() < 0) {
            overlapping = p.getRadius() - p.getPosition().getY();
            enx = 0;
            eny = -1;
        }


        double horizBorderOverlap = 0;
        double boxHoleStartingX = (SystemConfiguration.W - SystemConfiguration.D) / 2;
        double boxHoleEndingX = boxHoleStartingX + SystemConfiguration.D;
        boolean isWithinHole = p.getPosition().getX() > boxHoleStartingX && p.getPosition().getX() < boxHoleEndingX;
        if (!isWithinHole && Math.abs(p.getPosition().getY()) < p.getRadius()) {
            horizBorderOverlap = p.getRadius() - Math.abs(p.getPosition().getY());
        }

        double forceY = SystemConfiguration.k_n * horizBorderOverlap;

        return Pair.<Double, Vector>of(forceY, new Vector(0, forceY));
    }

}
