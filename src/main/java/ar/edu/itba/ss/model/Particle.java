package ar.edu.itba.ss.model;

import ar.edu.itba.ss.SystemConfiguration;

import java.util.Objects;
import java.util.Optional;
import java.util.Set;

public class Particle {
    public final int id;
    public final double radius;
    public final double mass;
    public final Vector position;
    public final Vector speed;
    public double totalFn;


    private Particle(final Particle particle, final Vector position, final Vector speed) {
        this.id = particle.id;
        this.mass = particle.mass;
        this.radius = particle.radius;
        this.speed = speed;
        this.position = position;
        this.totalFn = 0;
    }

    public Particle(final int id, final double x, final double y, final double vx, final double vy,
                    final double radius, final double mass) {
        this.id = id;
        this.radius = radius;
        this.mass = mass;
        this.position = new Vector(x, y);
        this.speed = new Vector(vx, vy);
        this.totalFn = 0;
    }


    private Particle(int id, double mass, double radius, final Vector position, final Vector speed) {
        this.id = id;
        this.mass = mass;
        this.radius = radius;
        this.speed = speed;
        this.position = position;
        this.totalFn = 0;
    }


    public static Particle of(Particle p, Vector position, Vector speed) {
        return new Particle(p, position, speed);
    }


    public static Particle of(int id, double mass, double radius, Vector position, Vector speed) {
        return new Particle(id, mass, radius, position, speed);
    }

    public boolean overlaps(final Particle other) {
        double distance = this.position.distanceTo(other.position);

        return distance < radius + other.radius;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Particle)) {
            return false;
        }

        Particle particle = (Particle) o;

        if (this.id != particle.id) {
            return false;
        }

        if (radius != particle.radius) {
            return false;
        }

        return this.mass == particle.mass;
    }

    public static Particle wallParticle(Vector position) {
        return new Particle(-1, position.x, position.y, 0, 0, 0.0045, 0);
    }

    public double perimeter() {
        return 2 * Math.PI * radius;
    }

    public double pressure() {
        return this.totalFn * perimeter();
    }

    public double kineticEnergy() {
        return 1d / 2d * mass * Math.pow(speed.norm(), 2);
    }

    public String print(double maxPressure) {
        double green = id == -1 ? 0.8 : 0;
        double blue = id == -1 ? 0.2 : 0;
        double actualMaxPressure = Math.min(3, Math.abs(maxPressure));
        double red = id == -1 ? 0 : pressure() / (actualMaxPressure != 0 ? actualMaxPressure : 1);

//        return id + "\t" + radius + "\t" + position.x + "\t" + position.y + "\t" + speed.x + "\t" + speed.y + "\t" + red + "\t" + green + "\t" + blue;
        return id + "\t" + radius + "\t" + position.x + "\t" + position.y + "\t" + speed.x + "\t" + speed.y + "\t" + pressure() / actualMaxPressure;
    }

    public int hashCode() {
        return position.hashCode() + speed.hashCode();
    }

    public Particle integrationStep(Force force, double dt, Vector previousAcceleration, Set<Particle> neighbours) {

        // Using the implementation from the "Predictor-corrector modifications" section of
        // https://en.wikipedia.org/wiki/Beeman%27s_algorithm

        Pair<Double, Vector> totalFnAndForce =force.calculate(this, neighbours);
        Vector currentAcceleration = totalFnAndForce.second().dividedScalar(this.mass);

        /////// POSITION ///////
        // x(t + dt) = x(t) + v(t)*dt + 2/3 * a(t) dt^2 - 1/6 a(t-dt) dt^2

        // accelerationTermForPosition = 2/3 * a(t) dt^2 - 1/6 a(t - dt) dt^2
        Vector accelerationTermForPosition = currentAcceleration.timesScalar(2d / 3d).timesScalar(Math.pow(dt, 2)).minusVector(previousAcceleration.timesScalar(1d / 6d).timesScalar(Math.pow(dt, 2)));

        // newPosition: x(t + dt) = x(t) + v(t)*dt + (2/3 * a(t) dt^2 - 1/6 a(t-dt) dt^2)
        Vector newPosition = position.plusVector(speed.timesScalar(dt)).plusVector(accelerationTermForPosition);

        /////// SPEED ///////

        // v(t + dt) (predicted) = v(t) + 3/2 a(t) dt - 1/2 a(t-dt) dt

        // v(t + dt) (corrected) = v(t) + 5/12 a(t+dt) dt + 2/3 a(t) dt - 1/12 a(t-dt) dt;

        // newSpeedPredicted: v(t+dt) (predicted) = v(t) + 3/2 a(t) dt - 1/2 a(t-dt) dt
        Vector newSpeedPredicted = speed.plusVector(currentAcceleration.timesScalar(3d / 2d).timesScalar(dt)).minusVector(previousAcceleration.timesScalar(1 / 2).timesScalar(dt));

        Particle predictedParticle = new Particle(this.id, this.mass, this.radius, newPosition, newSpeedPredicted);

        // a(t + dt)
        Vector newAcceleration = force.calculate(predictedParticle, neighbours).second();

        // v(t + dt) (corrected) = v(t) + 5/12 a(t+dt) dt + 2/3 a(t) dt - 1/12 a(t-dt) dt;
        Vector newSpeedCorrected = speed.plusVector(newAcceleration.timesScalar(5d / 12d).timesScalar(dt)).plusVector(currentAcceleration.timesScalar(2d / 3d).timesScalar(dt)).minusVector(previousAcceleration.timesScalar(1d / 12d).timesScalar(dt));


        // Set totalFn for pressure
        Particle newParticle = new Particle(this.id, this.mass, this.radius, newPosition, newSpeedCorrected);
        newParticle.totalFn = totalFnAndForce.first();

        return newParticle;
    }
}
