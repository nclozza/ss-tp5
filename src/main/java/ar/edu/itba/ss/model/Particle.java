package ar.edu.itba.ss.model;

import ar.edu.itba.ss.SystemConfiguration;

public class Particle {
    private static int idCounter = 0;

    private int id;
    private Vector position;
    private Vector speed;
    private double mass;
    private double radius;

    public Particle(final double positionX, final double positionY, final double speedX, final double speedY, final double mass, final double radius) {
        this.id = idCounter++;
        this.position = new Vector(positionX, positionY);
        this.speed = new Vector(speedX, speedY);
        this.mass = mass;
        this.radius = radius;
    }

    public Particle(final int id, final Vector position, final Vector speed, final double mass, final double radius) {
        this.id = id;
        this.position = position;
        this.speed = speed;
        this.mass = mass;
        this.radius = radius;
    }

    public Particle(final int id, final double positionX, final double positionY, final double speedX, final double speedY, final double mass, final double radius) {
        this.id = id;
        this.position = new Vector(positionX, positionY);
        this.speed = new Vector(speedX, speedY);
        this.mass = mass;
        this.radius = radius;
    }

    public String print() {
        return id + "\t" + radius + "\t" + position.getX() + "\t" + position.getY() + "\t" + speed.getX() + "\t" + speed.getY();
    }

    public int getId() {
        return id;
    }

    public void setId(final int id) {
        this.id = id;
    }

    public Vector getPosition() {
        return position;
    }

    public void setPosition(final Vector position) {
        this.position = position;
    }

    public Vector getSpeed() {
        return speed;
    }

    public void setSpeed(final Vector speed) {
        this.speed = speed;
    }

    public double getMass() {
        return mass;
    }

    public void setMass(final double mass) {
        this.mass = mass;
    }

    public double getRadius() {
        return radius;
    }

    public void setRadius(final double radius) {
        this.radius = radius;
    }

    public boolean overlaps(final Particle other) {
        return this.position.distanceTo(other.position) < radius + other.radius;
    }

    private double calculateFn(final double overlap) {
        return -SystemConfiguration.k_n * overlap;
    }

    //    private double calculateFt(final double overlap) {
//        return -SystemConfiguration.k_t * overlap
//    }
//
//    private double force() {
//        return -SystemConfiguration.k_n * position - SystemConfiguration.gamma * velocity;
//    }
//
    public double stepBeeman(final double dt) {
//        double currentAcceleration = this.acceleration();
//
//        double previousAcceleration = this.stepEuler(-dt).acceleration();
//
//        double nextPosition = position + velocity * dt + ((2.0 / 3) * currentAcceleration - (1.0 / 6) * previousAcceleration) * Math.pow(dt, 2);
//
//        double nextPredictedVelocity = velocity + (3.0 / 2) * currentAcceleration * dt - 0.5 * previousAcceleration * dt;
//
//        double nextAcceleration = new OscillatorParticle(nextPosition, nextPredictedVelocity, mass).acceleration();
//
//        double nextVelocity = velocity + (1.0 / 3) * nextAcceleration * dt + (5.0 / 6) * currentAcceleration * dt - (1.0 / 6) * previousAcceleration * dt;
//
//        return new OscillatorParticle(nextPosition, nextVelocity, mass);

        return 0;
    }
}
