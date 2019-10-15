package ar.edu.itba.ss;

import ar.edu.itba.ss.model.Particle;
import ar.edu.itba.ss.model.Vector;

import java.io.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

public class Output {

    static PrintWriter writer;

    static {
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
        writer.println(time + "," +energy);
        writer.flush();
    }

    private static List<Particle> wallParticles;

    public static void writeParticles(final Writer writer, final Set<Particle> particles) {
        List<Particle> wallParticles = generateWallParticles();
        writer.getWriter().println(particles.size() + wallParticles.size());
        writer.getWriter().println("id \t radius \t positionX \t positionY \t speedX \t speedY \t pressure");

        // Decorative wall particles
        for (Particle wallParticle : wallParticles) {
            writer.getWriter().println(wallParticle.print(0));
        }


        double maximumPressure = particles.stream().map(p -> p.getPressure()).max(Comparator.comparing(i -> i)).get();

        // Real particles
        for (Particle particle : particles) {
            writer.getWriter().println(particle.print(maximumPressure));
        }
    }

    private static List<Particle> generateWallParticles() {
        if (wallParticles != null) {
            return wallParticles;
        }
        wallParticles = new ArrayList<>();

        double step = 0.005;

        // Vertical walls
        for (double y = SystemConfiguration.fallenParticlesY; y < SystemConfiguration.L; y += step) {
            wallParticles.add(Particle.wallParticle(new Vector(0, y)));
            wallParticles.add(Particle.wallParticle(new Vector(SystemConfiguration.W, y)));
        }

        // Horizontal walls
        for (double x = 0; x < SystemConfiguration.W; x += step) {
            wallParticles.add(Particle.wallParticle(new Vector(x, SystemConfiguration.L)));
            wallParticles.add(Particle.wallParticle(new Vector(x, SystemConfiguration.fallenParticlesY)));
            if (x <= SystemConfiguration.holeStart.getX() || x >= SystemConfiguration.holeEnd.getX()) {
                wallParticles.add(Particle.wallParticle(new Vector(x, 0)));
            }
        }

        return wallParticles;
    }
}
