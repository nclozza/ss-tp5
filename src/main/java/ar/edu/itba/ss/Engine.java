package ar.edu.itba.ss;

import ar.edu.itba.ss.model.Force;
import ar.edu.itba.ss.model.Particle;
import ar.edu.itba.ss.model.Vector;

import java.util.*;
import java.util.stream.Collectors;

public class Engine {
    SystemConfiguration systemConfiguration;

    private Set<Particle> particles;
    Map<Integer, Vector> previousAccelerations = new HashMap<>();
    private Neighbour neighbour;


    public Engine(SystemConfiguration systemConfiguration) {
        this.systemConfiguration = systemConfiguration;
        this.particles = generateParticles();
        this.neighbour = new Neighbour(systemConfiguration, 0);
    }

    public void run() {
        double time = 0;
        double auxTime = 0;

        while (time < systemConfiguration.totalTime) {

            this.particles = integrate(particles);

            this.particles = removeAndReAddFallenParticles(time);

            double kineticEnergy = particles.stream().collect(Collectors.summingDouble(particle -> particle.kineticEnergy()));

            if (auxTime >= systemConfiguration.dt2) {
                auxTime = 0;
                Output.writeParticles(systemConfiguration, particles);
                Output.writeEnergy(time, kineticEnergy);
                System.out.println("Time: " + time);
//                System.out.println(kineticEnergy);
            } else {
                auxTime += systemConfiguration.dt;
            }

//            getEnergy(energyPrinter);

            time += systemConfiguration.dt;
        }

    }


    public Set<Particle> integrate(Set<Particle> allParticles) {
        Map<Integer, Set<Particle>> allNeighbours = neighbour.getNeighbours(allParticles);
        Set<Particle> newParticles = new HashSet<>();
        Force force = new Force(systemConfiguration);

        for (Particle particle : allParticles) {
            Set<Particle> neighboursForParticle = allNeighbours.get(particle.id);
            Vector previousAcceleration = previousAccelerations.getOrDefault(particle.id, new Vector(0, 0));

            Particle newParticle = particle.integrationStep(force, systemConfiguration.dt, previousAcceleration, neighboursForParticle);
            newParticles.add(newParticle);

            previousAccelerations.put(particle.id, force.calculate(particle, neighboursForParticle).second().dividedScalar(particle.mass));
        }

        return newParticles;
    }

    private Set<Particle> generateParticles() {
        Set<Particle> particles = new HashSet<>();

        int i = 0;
        Particle newParticle;
        while (i < systemConfiguration.addParticlesAttempts) {

            newParticle = addParticle(particles);
            if (newParticle != null) {
                particles.add(newParticle);
            } else {
                i++;
            }
        }
        System.out.println(particles.size() + " particles added.");

        return particles;
    }

    private Particle addParticle(Set<Particle> particles) {
        Random rand = Helper.getRandom();

        double radius = rand.nextDouble() * (systemConfiguration.maxRadius - systemConfiguration.minRadius) + systemConfiguration.minRadius;
        double x = rand.nextDouble() * (systemConfiguration.W - 2 * radius) + radius;
        double y = rand.nextDouble() * (systemConfiguration.L - 2 * radius) + radius;

        Particle particle = new Particle(particles.size(), x, y, 0, 0, radius, systemConfiguration.mass);

        for (Particle other : particles) {
            if (particle.overlaps(other)) {
                return null;
            }
        }

        return particle;
    }

    private Particle relocateFallenParticle(Particle oldParticle, Set<Particle> newParticles) {
        double radius = oldParticle.radius;
        double x;
        double y;
        Particle newParticle;

        do {
            x = Helper.randomDouble(radius, systemConfiguration.W - 2 * radius);
            y = Helper.randomDouble(systemConfiguration.L * 3.0 / 4 + radius, systemConfiguration.L - 2 * radius);
//            y = rand.nextDouble() * (systemConfiguration.L / 4 - 2 * oldParticleRadius) + oldParticleRadius + systemConfiguration.L * 3.0 / 4;

            newParticle = new Particle(oldParticle.id, x, y, 0, 0, radius, systemConfiguration.mass);
        } while (isOverlappingOtherParticle(newParticle, newParticles));

        return newParticle;
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

        Set<Particle> fallenParticles = new HashSet<>();

        // First, re-add all particles that did not fall
        for (Particle particle : this.particles) {
            if (particle.position.y > systemConfiguration.fallenParticlesY) {
                newParticles.add(particle);
            } else {
                fallenParticles.add(particle);
            }
        }

        // Second, re-add all fallen particles at the top
        for (Particle fallenParticle : fallenParticles) {
            Particle newParticle = relocateFallenParticle(fallenParticle, newParticles);
            newParticles.add(newParticle);
            Output.writeFallenParticleTime(time);
        }

        return newParticles;
    }
}
