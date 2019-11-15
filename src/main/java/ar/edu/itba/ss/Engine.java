package ar.edu.itba.ss;

import ar.edu.itba.ss.model.ForceCalculator;
import ar.edu.itba.ss.model.Pair;
import ar.edu.itba.ss.model.Particle;
import ar.edu.itba.ss.model.Vector;

import java.util.*;
import java.util.stream.Collectors;

public class Engine {
    SystemConfiguration systemConfiguration;
    Output output;

    public Engine(final SystemConfiguration systemConfiguration, final Output output) {
        this.systemConfiguration = systemConfiguration;
        this.output = output;
    }

    public void run() {
        double time = 0;
        double auxTime = 0;
        Map<Integer, Vector> previousAccelerations = new HashMap<>();
        Set<Particle> particles = generateParticles();
        List<Set<Particle>> particlesToPrint = new ArrayList<>();
        List<Pair<Double, Double>> energyToPrint = new ArrayList<>();
        List<Double> fallenParticlesToPrint = new ArrayList<>();

        CellIndexMethod cellIndexMethod = new CellIndexMethod(systemConfiguration, 0);

        System.out.println("Starting stuff: " + time);
        output.writeParticles(systemConfiguration, particles);

        while (time < systemConfiguration.totalTime) {
            particles = integrate(cellIndexMethod, previousAccelerations, particles);
            particles = removeAndReAddFallenParticles(time, particles, fallenParticlesToPrint);

            double kineticEnergy = particles.stream().collect(Collectors.summingDouble(particle -> particle.kineticEnergy()));

            if (auxTime >= systemConfiguration.dt2) {
                auxTime = 0;
                particlesToPrint.add(particles);
                energyToPrint.add(Pair.of(time, kineticEnergy));
//                Output.writeParticles(systemConfiguration, particles);
//                output.writeEnergy(time, kineticEnergy);
                System.out.println("Time: " + time);
//                System.out.println(kineticEnergy);
            } else {
                auxTime += systemConfiguration.dt;
            }

//            getEnergy(energyPrinter);

            time += systemConfiguration.dt;
        }

        output.writeManyParticles(systemConfiguration, particlesToPrint);
        output.writeManyEnergy(energyToPrint);
        output.writeManyFallenParticles(fallenParticlesToPrint);
    }


    public Set<Particle> integrate(CellIndexMethod cellIndexMethod, Map<Integer, Vector> previousAccelerations, Set<Particle> allParticles) {
        Map<Integer, Set<Particle>> allNearParticles = cellIndexMethod.getNearParticles(allParticles);
        Set<Particle> newParticles = new HashSet<>();
        ForceCalculator forceCalculator = new ForceCalculator(systemConfiguration);

        for (Particle particle : allParticles) {
            Set<Particle> nearParticleForSpecificParticle = allNearParticles.get(particle.id);
            Vector previousAcceleration = previousAccelerations.getOrDefault(particle.id, new Vector(0, 0));

            Particle newParticle = particle.integrationStep(forceCalculator, systemConfiguration.dt, previousAcceleration, nearParticleForSpecificParticle);
            newParticles.add(newParticle);

            previousAccelerations.put(particle.id, forceCalculator.calculate(particle, nearParticleForSpecificParticle).second().dividedScalar(particle.mass));
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

    private Set<Particle> removeAndReAddFallenParticles(final double time, Set<Particle> particles, final List<Double> fallenParticlesToPrint) {
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
            fallenParticlesToPrint.add(time);
        }

        return newParticles;
    }
}
