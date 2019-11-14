package ar.edu.itba.ss;

import ar.edu.itba.ss.model.Pair;
import ar.edu.itba.ss.model.Particle;
import ar.edu.itba.ss.model.Vector;

import java.io.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

public class Output {
    private PrintWriter energyWriter;
    private PrintWriter xyzWriter;
    private PrintWriter fallenParticlesWriter;

    private static List<Particle> wallParticles;
    private static List<Particle> obstacleParticles;

    public Output(final String energyWriter, final String xyzWriter, final String fallenParticlesWriter) {
        try {

            this.energyWriter = new PrintWriter(energyWriter + ".txt", "UTF-8");
            this.xyzWriter = new PrintWriter(xyzWriter + ".xyz", "UTF-8");
            this.fallenParticlesWriter = new PrintWriter(fallenParticlesWriter + ".txt", "UTF-8");
        } catch (FileNotFoundException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public void writeEnergy(double time, double energy) {
        energyWriter.println(time + "," + energy);
        energyWriter.flush();
    }

    public void writeManyParticles(SystemConfiguration systemConfiguration, final List<Set<Particle>> allParticles) {
        allParticles.forEach(particles -> writeParticles(systemConfiguration, particles));
        xyzWriter.flush();
        xyzWriter.close();
    }

    public void writeManyEnergy(final List<Pair<Double, Double>> allEnergy) {
        allEnergy.forEach(energy -> writeEnergy(energy.a, energy.b));
        energyWriter.flush();
        energyWriter.close();
    }

    public void writeManyFallenParticles(final List<Double> allFallenParticles) {
        allFallenParticles.forEach(this::writeFallenParticleTime);
        fallenParticlesWriter.flush();
        fallenParticlesWriter.close();
    }

    public void writeParticles(SystemConfiguration systemConfiguration, final Set<Particle> particles) {
        List<Particle> wallParticles = generateWallParticles(systemConfiguration);
        List<Particle> obstacleParticles = generateObstacleParticles(systemConfiguration);

        xyzWriter.println(particles.size() + wallParticles.size() + obstacleParticles.size());
        xyzWriter.println("id \t radius \t positionX \t positionY \t speedX \t speedY \t pressure");

        // Decorative wall particles
        for (Particle wallParticle : wallParticles) {
            xyzWriter.println(wallParticle.print(0));
        }

        // Decorative obstacle particles
        for (Particle obstacleParticle : obstacleParticles) {
            xyzWriter.println(obstacleParticle.print(0));
        }


        double maximumPressure = particles.stream().map(p -> p.pressure()).max(Comparator.comparing(i -> i)).orElse(1d);

        // Real particles
        for (Particle particle : particles) {
            xyzWriter.println(particle.print(maximumPressure != 0 ? maximumPressure : 1));
        }

        xyzWriter.flush();
    }

    private List<Particle> generateWallParticles(SystemConfiguration systemConfiguration) {
        if (wallParticles != null) {
            return wallParticles;
        }
        wallParticles = new ArrayList<>();

        double step = 0.005;
        double radius = systemConfiguration.wallParticleRadius;


        // Vertical walls
        for (double y = systemConfiguration.L; y >= systemConfiguration.curvedWallY; y -= step) {
            wallParticles.add(Particle.wallParticle(new Vector(0, y), radius));
            wallParticles.add(Particle.wallParticle(new Vector(systemConfiguration.W, y), radius));
        }

        // Horizontal walls
        double angleRadians = systemConfiguration.curvedWallAngle / 360 * 2 * Math.PI;
        double firstX = Math.sin(angleRadians) * systemConfiguration.curvedWallY * systemConfiguration.W;
        double lastX = systemConfiguration.W - firstX;

        for (double x = 0; x < systemConfiguration.W; x += step) {
            // Top wall
            wallParticles.add(Particle.wallParticle(new Vector(x, systemConfiguration.L), radius));

            // Bottom part where particles get reset
            wallParticles.add(Particle.wallParticle(new Vector(x, systemConfiguration.fallenParticlesY), radius));

            // Hole
            if ((x <= systemConfiguration.holeStart().x || x >= systemConfiguration.holeEnd().x) && x >= firstX && x <= lastX) {
                wallParticles.add(Particle.wallParticle(new Vector(x, 0), radius));
            }
        }

        // Curved walls
        for (double y = systemConfiguration.curvedWallY; y >= 0; y -= step) {
            double x = Math.sin(-angleRadians) * y * systemConfiguration.W;

            // Left wall
            wallParticles.add(Particle.wallParticle(new Vector(firstX + x, y), radius));

            // Right wall
            wallParticles.add(Particle.wallParticle(new Vector(systemConfiguration.W - (firstX + x), y), radius));
        }

        return wallParticles;
    }

    private List<Particle> generateObstacleParticles(SystemConfiguration systemConfiguration) {
        if (obstacleParticles != null) {
            return obstacleParticles;
        }
        obstacleParticles = new ArrayList<>();

        double step = 1;

        for (double angleDegrees = 0; angleDegrees < 360; angleDegrees += step) {
            double angleRadians = angleDegrees / 360 * 2 * Math.PI;
            Vector position = new Vector(Math.cos(angleRadians), Math.sin(angleRadians)).timesScalar(systemConfiguration.obstacleRadius).plusVector(systemConfiguration.obstacleCenter);

            obstacleParticles.add(Particle.wallParticle(position, 0.0045));
        }

        return obstacleParticles;
    }

    public void writeFallenParticleTime(double time) {
        fallenParticlesWriter.println(time);
        fallenParticlesWriter.flush();
    }


}

