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
    private Set<Particle> particleSet;
    private Writer xyzWriter;
    private Writer fallenParticlesWriter;

    public Engine(final double dt, final double dt2, final double totalTime, final String xyzWriterPath, final String fallenParticlesWriterPath) {
        this.particleSet = generateParticles();
        this.dt = dt;
        this.dt2 = dt2;
        this.totalTime = totalTime;
        this.time = 0;
        this.auxTime = 0;
        this.xyzWriter = new Writer(xyzWriterPath);
        this.fallenParticlesWriter = new Writer(fallenParticlesWriterPath);
    }

    public void run() {

        while (time < totalTime) {
            particleSet = integrate();
            particleSet = removeFallenParticles();

            if (auxTime >= dt2) {
                auxTime = 0;
                printParticlesToXyzWriter();
                System.out.println("Time: " + time);
            } else {
                auxTime += dt;
            }

            time += dt;
        }

    }

    private void printParticlesToXyzWriter() {
        xyzWriter.getWriter().println(particleSet.size());
        xyzWriter.getWriter().println("id \t radius \t positionX \t positionY \t speedX \t speedY");

        for (Particle particle : particleSet) {
            xyzWriter.getWriter().println(particle.print());
        }
    }

    private Set<Particle> generateParticles() {

        Set<Particle> particles = new HashSet<>();

        int i = 0;
        while (i < SystemConfiguration.addParticlesAttempts) {
            Particle particle = generateNewParticle();

            if (particle != null) {
                particles.add(particle);
            } else {
                i++;
            }
        }

        System.out.println(particles.size() + " particles generated.");
        return particles;
    }

    private Particle generateNewParticle() {
        Random random = Helper.getRandom();

        double radius = random.nextDouble() * (SystemConfiguration.maxRadius - SystemConfiguration.minRadius) + SystemConfiguration.minRadius;
        double x = random.nextDouble() * (SystemConfiguration.W - 2 * radius) + radius;
        double y = random.nextDouble() * (SystemConfiguration.L - 2 * radius) + radius;

        Particle particle = new Particle(x, y, 0, 0, SystemConfiguration.mass, radius);

        for (Particle other : particleSet) {
            if (particle.overlaps(other)) {
                return null;
            }
        }

        return particle;
    }

    private Set<Particle> integrate() {
        // Calcular fuerzas, posiciones, etc. para todas las particulas con el metodo de integracion elegido

        return particleSet;
    }

    private Set<Particle> removeFallenParticles() {
        Set<Particle> newParticles = new HashSet<>();

        for (Particle particle : particleSet) {
            if (particle.getPosition().getY() > SystemConfiguration.fallenParticlesY) {
                newParticles.add(particle);
            } else {
                fallenParticlesWriter.getWriter().println(time);
                reinsertParticle(particle, newParticles);
            }
        }

        return newParticles;
    }

    private void reinsertParticle(final Particle oldParticle, final Set<Particle> newParticles) {

        int id = oldParticle.getId();
        double radius = oldParticle.getRadius();
        double y = SystemConfiguration.L;
        double x;
        Particle particle;

        Random random = Helper.getRandom();

        do {
            x = random.nextDouble() * (SystemConfiguration.W - 2 * radius) + radius;
            particle = new Particle(id, x, y, 0, 0, SystemConfiguration.mass, radius);
        } while (checkOverlap(particle, newParticles));

        newParticles.add(particle);
    }

    private boolean checkOverlap(final Particle particle, final Set<Particle> particles) {
        for (Particle other : particles) {
            if (!particle.equals(other) && particle.overlaps(other)) {
                return false;
            }
        }

        return true;
    }
}
