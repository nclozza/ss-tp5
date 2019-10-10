package ar.edu.itba.ss;

import ar.edu.itba.ss.model.Particle;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class Engine {

    private double dt;
    private double dt2;
    private double totalTime;
    private double time;
    private double auxTime;
    private Writer xyzWriter;
    private Writer fallenParticlesWriter;

    private Set<Particle> particles;
    private double L;
    private double W;
    private double D;

    private double minRadius;
    private double maxRadius;
    private double mass;


    public Engine(final double L, final double W, final double D, final double dt, final double dt2,
                  final double totalTime, final String xyzWriterPath, final String fallenParticlesWriterPath,
                  final double minRadius, final double maxRadius, final double mass) {
        this.L = L;
        this.W = W;
        this.D = D;
        this.minRadius = minRadius;
        this.maxRadius = maxRadius;
        this.mass = mass;
        this.dt = dt;
        this.dt2 = dt2;
        this.time = 0;
        this.totalTime = totalTime;
        this.xyzWriter = new Writer(xyzWriterPath);
        this.fallenParticlesWriter = new Writer(fallenParticlesWriterPath);

        this.particles = new HashSet<>();
        generateParticles();

    }

    public void run() {

        Beeman beeman = new Beeman(new Force(L, W, D), new Neighbour(L, W, 0, maxRadius), dt, particles);


        while (time < totalTime) {

            this.particles = beeman.integrate(particles);

            this.particles = removeAndReAddFallenParticles(time);


            if (auxTime >= dt2) {
                auxTime = 0;
                Output.writeParticles(xyzWriter, particles);
                System.out.println("Time: " + time);
            } else {
                auxTime += dt;
            }

//            getEnergy(energyPrinter);

            time += dt;
        }

    }

    private void generateParticles() {
        int i = 0;
        Particle newParticle;
        while (i < SystemConfiguration.addParticlesAttempts) {

            newParticle = addParticle();
            if (newParticle != null) {
                particles.add(newParticle);
            } else {
                i++;
            }
        }

        System.out.println(particles.size() + " particles added.");
    }

    private Particle addParticle() {
        Random rand = Helper.getRandom();

        double radius = rand.nextDouble() * (maxRadius - minRadius) + minRadius;
        double x = rand.nextDouble() * (W - 2 * radius) + radius;
        double y = rand.nextDouble() * (L - 2 * radius) + radius;

        Particle particle = new Particle(particles.size(), x, y, 0, 0, radius, mass);

        for (Particle other : particles) {
            if (particle.overlaps(other)) {
                return null;
            }
        }

        return particle;
    }

    private void reAddParticle(Particle oldParticle, Set<Particle> newParticles) {
        Random rand = Helper.getRandom();
        boolean done;
        double oldParticleRadius = oldParticle.getRadius();
        double x;
        double y;
        Particle particle;

        do {
            x = rand.nextDouble() * (W - 2 * oldParticleRadius) + oldParticleRadius;
            y = rand.nextDouble() * (L / 4 - 2 * oldParticleRadius) + oldParticleRadius + L * 3.0 / 4;

            particle = new Particle(oldParticle.getId(), x, y, 0, 0, oldParticleRadius, mass);

            done = !isOverlappingOtherParticle(particle, this.particles);
        } while (!done);

        newParticles.add(particle);
    }

    private boolean isOverlappingOtherParticle(Particle p, Set<Particle> newParticles) {
        for (Particle other : newParticles) {
            if (p.overlaps(other)) {
                return true;
            }
        }
        return false;
    }

    private Set<Particle> removeAndReAddFallenParticles(double time) {
        Set<Particle> newParticles = new HashSet<>();

        for (Particle p : this.particles) {

            if (p.getPosition().getY() > SystemConfiguration.fallenParticlesY) {
                newParticles.add(p);
            } else {
                reAddParticle(p, newParticles);
                fallenParticlesWriter.getWriter().println(time);
            }
        }

        return newParticles;
    }
}
