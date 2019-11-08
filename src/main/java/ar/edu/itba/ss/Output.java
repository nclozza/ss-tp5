package ar.edu.itba.ss;

import ar.edu.itba.ss.model.Particle;
import ar.edu.itba.ss.model.Vector;

import java.io.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

public class Output {
    private static PrintWriter writer;
    private static PrintWriter xyzWriter;
    private static PrintWriter fallenParticlesWriter;
    static {
        try {
            fallenParticlesWriter = new PrintWriter("fallen_particles.txt", "UTF-8");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        try {
            xyzWriter = new PrintWriter("output.xyz", "UTF-8");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        try {
            writer = new PrintWriter("energy.txt", "UTF-8");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    ;

    public static void writeEnergy(double time, double energy) {
        writer.println(time + "," + energy);
        writer.flush();
    }

    private static List<Particle> wallParticles;

    public static void writeParticles(SystemConfiguration systemConfiguration, final Set<Particle> particles) {
        List<Particle> wallParticles = generateWallParticles(systemConfiguration);
        xyzWriter.println(particles.size() + wallParticles.size());
        xyzWriter.println("id \t radius \t positionX \t positionY \t speedX \t speedY \t pressure");

        // Decorative wall particles
        for (Particle wallParticle : wallParticles) {
            xyzWriter.println(wallParticle.print(0));
        }


        double maximumPressure = particles.stream().map(p -> p.pressure()).max(Comparator.comparing(i -> i)).orElse(1d);

        // Real particles
        for (Particle particle : particles) {
            xyzWriter.println(particle.print(maximumPressure));
        }
    }

    private static List<Particle> generateWallParticles(SystemConfiguration systemConfiguration) {
        if (wallParticles != null) {
            return wallParticles;
        }
        wallParticles = new ArrayList<>();

        double step = 0.005;

        // Vertical walls
        for (double y = systemConfiguration.fallenParticlesY; y < systemConfiguration.L; y += step) {
            wallParticles.add(Particle.wallParticle(new Vector(0, y)));
            wallParticles.add(Particle.wallParticle(new Vector(systemConfiguration.W, y)));
        }

        // Horizontal walls
        for (double x = 0; x < systemConfiguration.W; x += step) {
            wallParticles.add(Particle.wallParticle(new Vector(x, systemConfiguration.L)));
//            wallParticles.add(Particle.wallParticle(new Vector(x, systemConfiguration.fallenParticlesY)));

            // Hole
            if (x <= systemConfiguration.holeStart().x || x >= systemConfiguration.holeEnd().x) {
                wallParticles.add(Particle.wallParticle(new Vector(x, 0)));
            }
        }

        return wallParticles;
    }

    public static void writeFallenParticleTime(double time) {
        fallenParticlesWriter.println(time);
    }


}

