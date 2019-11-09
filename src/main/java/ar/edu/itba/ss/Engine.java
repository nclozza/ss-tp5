package ar.edu.itba.ss;

import ar.edu.itba.ss.model.ForceCalculator;
import ar.edu.itba.ss.model.Particle;
import ar.edu.itba.ss.model.Vector;

import java.util.*;
import java.util.stream.Collectors;

public class Engine {
    SystemConfiguration systemConfiguration;

    public Engine(SystemConfiguration systemConfiguration) {
        this.systemConfiguration = systemConfiguration;
    }

    public void run() {
        double time = 0;
        double auxTime = 0;
        Map<Integer, Vector> previousAccelerations = new HashMap<>();
        Set<Particle> particles = generateParticles();
        List<Set<Particle>> particlesToPrint = new ArrayList<>();

        Neighbour neighbour = new Neighbour(systemConfiguration, 0);

        System.out.println("Starting stuff: " + time);
        Output.writeParticles(systemConfiguration, particles);

        while (time < systemConfiguration.totalTime) {
            particles = integrate(neighbour, previousAccelerations, particles);
            particles = removeAndReAddFallenParticles(time, particles);

            double kineticEnergy = particles.stream().collect(Collectors.summingDouble(particle -> particle.kineticEnergy()));

            if (auxTime >= systemConfiguration.dt2) {
                auxTime = 0;
                particlesToPrint.add(particles);
//                Output.writeParticles(systemConfiguration, particles);
//                Output.writeEnergy(time, kineticEnergy);
                System.out.println("Time: " + time);
//                System.out.println(kineticEnergy);
            } else {
                auxTime += systemConfiguration.dt;
            }

//            getEnergy(energyPrinter);

            time += systemConfiguration.dt;
        }

        Output.writeManyParticles(systemConfiguration, particlesToPrint);
    }


    public Set<Particle> integrate(Neighbour neighbour, Map<Integer, Vector> previousAccelerations, Set<Particle> allParticles) {
        Map<Integer, Set<Particle>> allNeighbours = neighbour.getNeighbours(allParticles);
        Set<Particle> newParticles = new HashSet<>();
        ForceCalculator forceCalculator = new ForceCalculator(systemConfiguration);

        for (Particle particle : allParticles) {
            Set<Particle> neighboursForParticle = allNeighbours.get(particle.id);
            Vector previousAcceleration = previousAccelerations.getOrDefault(particle.id, new Vector(0, 0));

            Particle newParticle = particle.integrationStep(forceCalculator, systemConfiguration.dt, previousAcceleration, neighboursForParticle);
            newParticles.add(newParticle);

            previousAccelerations.put(particle.id, forceCalculator.calculate(particle, neighboursForParticle).second().dividedScalar(particle.mass));
        }

        return newParticles;
    }

    private Set<Particle> generateParticles() {
        Set<Particle> particles = new HashSet<>();

        int attempts = 0;

        while (attempts < systemConfiguration.addParticlesAttempts) {
            Particle newParticle = addParticle(particles);

            if (newParticle != null) {
                particles.add(newParticle);
            } else {
                attempts++;
            }
        }
        System.out.println(particles.size() + " particles added.");

        return particles;
    }

    private Particle addParticle(Set<Particle> particles) {
        double radius = Helper.randomDouble(systemConfiguration.minRadius, systemConfiguration.maxRadius);
        double x = Helper.randomDouble(2 * radius, systemConfiguration.W - 2 * radius);
        double y = Helper.randomDouble(systemConfiguration.curvedWallY + 2 * radius, systemConfiguration.L - 2 * radius);

        Particle particle = new Particle(particles.size(), x, y, 0, 0, radius, systemConfiguration.mass);

        if (isOverlappingOtherParticle(particle, particles)) {
            return null;
        } else {
            return particle;
        }

    }

    private Particle relocateFallenParticle(Particle oldParticle, Set<Particle> newParticles) {
        double radius = oldParticle.radius;
        double x;
        double y;
        Particle newParticle;

        do {
            x = Helper.randomDouble(2 * radius, systemConfiguration.W - 2 * radius);
            y = Helper.randomDouble(systemConfiguration.L * 3.0 / 4 + radius, systemConfiguration.L - 2 * radius);

            newParticle = new Particle(oldParticle.id, x, y, 0, 0, radius, systemConfiguration.mass);
        } while (isOverlappingOtherParticle(newParticle, newParticles));

        return newParticle;
    }

    private boolean isOverlappingOtherParticle(Particle particle, Set<Particle> newParticles) {
        Particle obstacleParticle = Particle.of(-5, 1, systemConfiguration.obstacleRadius, systemConfiguration.obstacleCenter, new Vector(0, 0));

        for (Particle other : newParticles) {
            if (particle.overlaps(other)) {
                return true;
            }
        }

        // Check for overlap with obstacle
        if (particle.overlaps(obstacleParticle)) {
            return true;
        }

        return false;
    }

    private Set<Particle> removeAndReAddFallenParticles(double time, Set<Particle> particles) {
        Set<Particle> newParticles = new HashSet<>();

        Set<Particle> fallenParticles = new HashSet<>();

        // First, re-add all particles that did not fall
        for (Particle particle : particles) {
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
