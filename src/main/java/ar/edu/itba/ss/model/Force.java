package ar.edu.itba.ss.model;

import ar.edu.itba.ss.SystemConfiguration;
import ar.edu.itba.ss.model.Pair;
import ar.edu.itba.ss.model.Particle;
import ar.edu.itba.ss.model.Vector;

import java.util.Set;

public class Force {
    private final SystemConfiguration systemConfiguration;

    public Force(SystemConfiguration systemConfiguration) {
        this.systemConfiguration = systemConfiguration;
    }

    public Pair<Double, Vector> calculate(final Particle particle, final Set<Particle> neighbours) {
        Vector force = new Vector(0, -particle.mass * systemConfiguration.g);

        double totalFn = 0;

        for (Particle other : neighbours) {
            Pair<Double, Vector> f = particleForce(particle, other);
            totalFn += f.first();
            force = force.plusVector(f.second());
        }


        // Particles at the ends of the hole
        Particle wallPart1 = new Particle(40000, systemConfiguration.holeStart().x, 0, 0, 0, systemConfiguration.minRadius, systemConfiguration.mass);
        Particle wallPart2 = new Particle(40000, systemConfiguration.holeEnd().x, 0, 0, 0, systemConfiguration.minRadius, systemConfiguration.mass);


        Pair<Double, Vector> forceWalls = getWallForces(particle);
        force = force.plusVector(forceWalls.second());
        totalFn += forceWalls.first();

        Pair<Double, Vector> forceWallPart1 = particleForce(particle, wallPart1);
        force = force.plusVector(forceWallPart1.second());
        totalFn += forceWallPart1.first();

        Pair<Double, Vector> forceWallPart2 = particleForce(particle, wallPart2);
        force = force.plusVector(forceWallPart2.second());
        totalFn += forceWallPart2.first();

        return Pair.of(totalFn, force);
    }

    private Pair<Double, Vector> particleForce(final Particle particle, final Particle other) {
        double fn = 0;
        double ft = 0;
        Vector en = other.position.minusVector(particle.position).normalize();

        if (!particle.equals(other)) {
            double overlap = overlapping(particle, other);
            if (overlap > 0) {
                fn = getFn(overlap);
                ft = getFt(overlap, relativeSpeed(particle, other));
            }
        }

        return Pair.of(Math.abs(fn), new Vector(fn * en.x - ft * en.y, fn * en.y + ft * en.x));
    }

    // N.2
    private double getFn(final double overlapping) {
        return -systemConfiguration.k_n * overlapping;
    }

    // T.3
    private double getFt(final double overlapping, final double relativeSpeed) {
//        return 0;
        return -systemConfiguration.k_t * overlapping * relativeSpeed;
    }


    /**
     * @param i
     * @param j
     * @return The amount of overlap measured in meters
     */
    private double overlapping(final Particle i, final Particle j) {
        double result = i.radius + j.radius - i.position.minusVector(j.position).norm();

        return result > 0 ? result : 0;
    }

    private double relativeSpeed(final Particle i, final Particle j) {
        Vector direction = j.position.minusVector(i.position).tangent();

        return i.speed.minusVector(j.speed).projectedOn(direction);
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

        if (p.position.x - p.radius < 0) {
            overlapping = p.radius - Math.abs(p.position.x);
            enx = -1;
            eny = 0;
        }

        fn = getFn(overlapping);
        ft = 0;
        return Pair.of(Math.abs(fn), new Vector(fn * enx - ft * eny, fn * eny + ft * enx));
    }

    private Pair<Double, Vector> rightWall(final Particle p) {
        double overlapping = 0;
        double enx = 0;
        double eny = 0;
        double fn;
        double ft;

        if (p.position.x + p.radius > systemConfiguration.W) {
            overlapping = p.radius - (Math.abs(p.position.x - systemConfiguration.W));
            enx = 1;
            eny = 0;
        }

        fn = getFn(overlapping);
        ft = 0;

        return Pair.of(Math.abs(fn), new Vector(fn * enx - ft * eny, fn * eny + ft * enx));
    }

    private Pair<Double, Vector> bottomWall(final Particle p) {
        double overlapping = 0;
        double enx = 0;
        double eny = 0;
        double fn;
        double ft;

        boolean shouldCrashBottom = (p.position.x < systemConfiguration.holeStart().x || p.position.x > systemConfiguration.holeEnd().x) && p.position.y > 0;

        if (shouldCrashBottom && p.position.y - p.radius < 0) {
            overlapping = p.radius - p.position.y;
            enx = 0;
            eny = -1;
        }

        double horizBorderOverlap = 0;
        boolean isWithinHole = p.position.x > systemConfiguration.holeStart().x && p.position.x < systemConfiguration.holeEnd().x;

        if (!isWithinHole && Math.abs(p.position.y) < p.radius) {
            horizBorderOverlap = p.radius - Math.abs(p.position.y);
        }

        double forceY = systemConfiguration.k_n * horizBorderOverlap;

        return Pair.of(Math.abs(forceY), new Vector(0, forceY));
    }
}
