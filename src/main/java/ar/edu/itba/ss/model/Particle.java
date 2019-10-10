package ar.edu.itba.ss.model;

import java.util.Objects;
import java.util.Optional;

public class Particle {
    private final int id;
    private final double radius;
    private final double mass;

    private final Vector previousAcc;

    private final Vector position;
    private final Vector speed;

    private Optional<Vector> acceleration;

    private Optional<Vector> nextPosition;
    private Optional<Vector> nextSpeedPredicted;

    private Optional<Vector> nextAcceleration;

    private Optional<Vector> nextSpeedCorrected;

    private double totalFn;


    private Particle(final Particle particle, final Vector position, final Vector speed, final Vector previousAcc) {
        this.id = particle.id;
        this.mass = particle.mass;
        this.radius = particle.radius;

        this.previousAcc = previousAcc;
        this.speed = speed;
        this.position = position;

        this.nextPosition = Optional.empty();
        this.nextSpeedPredicted = Optional.empty();
        this.nextSpeedCorrected = Optional.empty();
        this.acceleration = Optional.empty();
        this.nextAcceleration = Optional.empty();
        this.totalFn = 0;
    }

    public Particle(final int id, final double x, final double y, final double vx, final double vy,
                    final double radius, final double mass) {
        this.id = id;
        this.radius = radius;
        this.mass = mass;

        this.previousAcc = new Vector(0, 0);
        this.position = new Vector(x, y);
        this.speed = new Vector(vx, vy);
        this.nextPosition = Optional.empty();
        this.nextSpeedPredicted = Optional.empty();
        this.nextSpeedCorrected = Optional.empty();
        this.acceleration = Optional.empty();
        this.nextAcceleration = Optional.empty();

    }


    public static Particle of(Particle p, Vector position, Vector speed, Vector previousAcc) {
        return new Particle(p, position, speed, previousAcc);
    }

    public int getId() {
        return id;
    }

    public Vector getPosition() {
        return position;
    }

    public Vector getSpeed() {
        return speed;
    }

    public double getRadius() {
        return radius;
    }

    public double getMass() {
        return mass;
    }

    public Vector getPreviousAcc() {
        return previousAcc;
    }

    public Vector getAcceleration() {
        return acceleration.orElseThrow(IllegalStateException::new);
    }

    public Vector getNextAcceleration() {
        return nextAcceleration.orElseThrow(IllegalStateException::new);
    }

    public Vector getNextPosition() {
        return nextPosition.orElseThrow(IllegalStateException::new);
    }

    public Vector getNextSpeedPredicted() {
        return nextSpeedPredicted.orElseThrow(IllegalStateException::new);
    }

    public Vector getNextSpeedCorrected() {
        return nextSpeedCorrected.orElseThrow(IllegalStateException::new);
    }


    public void setNextPosition(final Vector nextPosition) {
        if (this.nextPosition.isPresent()) {
            throw new IllegalStateException();
        } else {
            this.nextPosition = Optional.of(nextPosition);
        }
    }

    public void setNextSpeedCorrected(final Vector nextVelocity) {
        if (this.nextSpeedCorrected.isPresent()) {
            throw new IllegalStateException();
        } else {
            this.nextSpeedCorrected = Optional.of(nextVelocity);
        }
    }

    public void setNextSpeedPredicted(final Vector nextVelocity) {
        if (this.nextSpeedPredicted.isPresent()) {
            throw new IllegalStateException();
        } else {
            this.nextSpeedPredicted = Optional.of(nextVelocity);
        }
    }

    public void setNextAcceleration(final Vector nextAcceleration) {
        if (this.nextAcceleration.isPresent()) {
            throw new IllegalStateException();
        } else {
            this.nextAcceleration = Optional.of(nextAcceleration);
        }
    }

    public boolean overlaps(final Particle other) {
        double distance = this.position.distanceTo(other.position);

        return distance < radius + other.radius;
    }

    public void setAcceleration(final Vector acceleration) {
        if (this.acceleration.isPresent()) {
            throw new IllegalStateException();
        } else {
            this.acceleration = Optional.of(acceleration);
        }
    }

    public double getTotalFn() {
        return totalFn;
    }

    public void setTotalFn(double totalFn) {
        if (this.totalFn == 0) {
            this.totalFn = totalFn;
        }
    }

    public void clearFn() {
        this.totalFn = 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (!(o instanceof Particle)) {
            return false;
        }

        Particle particle = (Particle) o;

        if (getId() != particle.getId()) {
            return false;
        }

        if (Double.compare(particle.getRadius(), getRadius()) != 0) {
            return false;
        }

        return Double.compare(particle.getMass(), getMass()) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, radius, mass);
    }

    public String print() {
        return id + "\t" + radius + "\t" + position.getX() + "\t" + position.getY() + "\t" + speed.getX() + "\t" + speed.getY() + "\t" + getPression();
    }

    public static Particle wallParticle(Vector position) {
        return new Particle(-1, position.getX(), position.getY(), 0, 0, 0.00045, 0);
    }

    public double getPerimeter() {
        return 2 * Math.PI * radius;
    }

    public double getPression() {
        return getTotalFn() * getPerimeter();
    }

}
