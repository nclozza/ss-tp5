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
        double overlapping;

        double totalFn = 0;

        for (Particle other : neighbours) {
            if (!particle.equals(other)) {
                overlapping = overlapping(particle, other);
                if (overlapping > 0) {
                    double fn = getFn(overlapping);
                    double ft = getFt(overlapping, relativeSpeed(particle, other));

                    totalFn += fn;

                    Vector en = other.getPosition().minusVector(particle.getPosition())
                            .dividedScalar(other.getPosition().minusVector(particle.getPosition()).norm());

                    force = force.plusVector(new Vector(fn * en.getX() - ft * en.getY(), fn * en.getY() + ft * en.getX()));
                }

            }


        }
        particle.setTotalFn(totalFn);

        force = force.plusVector(getWallForces(particle));

        return force;
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


    private Vector getWallForces(final Particle particle) {

        Vector right = rightWall(particle);

        Vector left = leftWall(particle);

        Vector horizontal = bottomWall(particle);

        return right.plusVector(left).plusVector(horizontal);
    }

    private Vector leftWall(final Particle p) {
        double overlapping = 0;
        double enx = 0;
        double eny = 0;
        double fn;
        double ft;

        if (p.getPosition().getX() - p.getRadius() < 0) {
            overlapping = p.getRadius() - p.getPosition().getX();
            enx = -1;
            eny = 0;
        }

        fn = getFn(overlapping);
        ft = getFt(overlapping, p.getSpeed().projectedOn(new Vector(0, 1)));
        return new Vector(fn * enx - ft * eny, fn * eny + ft * enx);
    }

    private Vector rightWall(final Particle p) {
        double overlapping = 0;
        double enx = 0;
        double eny = 0;
        double fn;
        double ft;

        if (p.getPosition().getX() + p.getRadius() > W) {
            overlapping = p.getPosition().getX() + p.getRadius() - W;
            enx = 1;
            eny = 0;
        }

        fn = getFn(overlapping);
        ft = getFt(overlapping, p.getSpeed().projectedOn(new Vector(0, 1)));
        return new Vector(fn * enx - ft * eny, fn * eny + ft * enx);
    }

    private Vector bottomWall(final Particle p) {
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

        fn = getFn(overlapping);
        ft = getFt(overlapping, p.getSpeed().projectedOn(new Vector(1, 0)));
        return new Vector(fn * enx - ft * eny, fn * eny + ft * enx);
    }

}
