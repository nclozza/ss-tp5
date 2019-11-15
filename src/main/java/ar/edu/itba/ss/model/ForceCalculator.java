package ar.edu.itba.ss.model;

import ar.edu.itba.ss.Helper;
import ar.edu.itba.ss.SystemConfiguration;

import java.util.Set;

public class ForceCalculator {
    private final SystemConfiguration systemConfiguration;

    public ForceCalculator(SystemConfiguration systemConfiguration) {
        this.systemConfiguration = systemConfiguration;
    }

    public Pair<Double, Vector> calculate(final Particle particle, final Set<Particle> nearParticles) {
        Pair<Double, Vector> forcePair = Pair.of(0d, new Vector(0, -particle.mass * systemConfiguration.g));


        for (Particle other : nearParticles) {
            Pair<Double, Vector> particleForce = particleForce(particle, other);
            forcePair = addPairs(forcePair, particleForce);
        }


        ////// WALLS //////
        // Particles at the ends of the hole
        Particle wallPart1 = new Particle(40000, systemConfiguration.holeStart().x, 0, 0, 0, systemConfiguration.minRadius, systemConfiguration.mass);
        Particle wallPart2 = new Particle(40000, systemConfiguration.holeEnd().x, 0, 0, 0, systemConfiguration.minRadius, systemConfiguration.mass);


        Pair<Double, Vector> forceWalls = getWallForces(particle);
        forcePair = addPairs(forcePair, forceWalls);

        Pair<Double, Vector> forceWallPart1 = particleForce(particle, wallPart1);
        forcePair = addPairs(forcePair, forceWallPart1);

        Pair<Double, Vector> forceWallPart2 = particleForce(particle, wallPart2);
        forcePair = addPairs(forcePair, forceWallPart2);

        ////// OBSTACLE //////
        Pair<Double, Vector> obstacleForce = obstacle(particle);
        forcePair = addPairs(forcePair, obstacleForce);

        ////// CURVED WALLS //////
        Pair<Double, Vector> leftCurvedWallForce = leftCurvedWall(particle);
        forcePair = addPairs(forcePair, leftCurvedWallForce);

        Pair<Double, Vector> rightCurvedWallForce = rightCurvedWall(particle);
        forcePair = addPairs(forcePair, rightCurvedWallForce);

        ////// TOTAL FORCE //////
        return forcePair;
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


    private Pair<Double, Vector> obstacle(final Particle particle) {
        double overlap = particle.radius + systemConfiguration.obstacleRadius - particle.position.minusVector(systemConfiguration.obstacleCenter).norm();

        double fn = 0;
        double ft = 0;
        Vector en = systemConfiguration.obstacleCenter.minusVector(particle.position).normalize();

        if (overlap > 0) {
            Vector direction = systemConfiguration.obstacleCenter.minusVector(particle.position).tangent();

            double relativeSpeed = particle.speed.projectedOn(direction);

            fn = getFn(overlap);
            ft = getFt(overlap, relativeSpeed);
        }

        return Pair.of(Math.abs(fn), new Vector(fn * en.x - ft * en.y, fn * en.y + ft * en.x));
    }


    private Pair<Double, Vector> leftCurvedWall(final Particle particle) {
        Pair<Vector, Vector> topAndBottom = systemConfiguration.leftCurvedWallEndpoints();
        Vector wallParticlePosition = closestPoint(topAndBottom.first(), topAndBottom.second(), particle.position);

        return particleForce(particle, Particle.of(-1, systemConfiguration.mass, systemConfiguration.wallParticleRadius, wallParticlePosition, new Vector(0, 0)));
    }

    private Pair<Double, Vector> rightCurvedWall(final Particle particle) {
        Pair<Vector, Vector> topAndBottom = systemConfiguration.rightCurvedWallEndpoints();
        Vector wallParticlePosition = closestPoint(topAndBottom.first(), topAndBottom.second(), particle.position);

        return particleForce(particle, Particle.of(-1, systemConfiguration.mass, systemConfiguration.wallParticleRadius, wallParticlePosition, new Vector(0, 0)));
    }

    // Finds the closest point over a line segment AB to a point P
    public Vector closestPoint(Vector A, Vector B, Vector P) {
        Vector a_to_p = P.minusVector(A);
        Vector a_to_b = B.minusVector(A);


        double atb2 = Math.pow(a_to_b.norm(), 2);

        double aToPdotAtoB = a_to_p.dot(a_to_b);

        double t = Helper.clamp(aToPdotAtoB / atb2, 0, 1);

        return new Vector(A.x + a_to_b.x * t, A.y + a_to_b.y * t);
    }

    private Pair<Double, Vector> addPairs(Pair<Double, Vector> pair1, Pair<Double, Vector> pair2) {
        return Pair.of(pair1.first() + pair2.first(), pair1.second().plusVector(pair2.second()));
    }
}
