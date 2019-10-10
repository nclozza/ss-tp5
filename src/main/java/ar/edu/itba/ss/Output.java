package ar.edu.itba.ss;

import ar.edu.itba.ss.model.Particle;
import ar.edu.itba.ss.model.Vector;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class Output {

    private static List<Particle> wallParticles;

    public static void writeParticles(final Writer writer, final Set<Particle> particles) {
        List<Particle> wallParticles = generateWallParticles();
        writer.getWriter().println(particles.size() + wallParticles.size());
        writer.getWriter().println("id \t radius \t positionX \t positionY \t speedX \t speedY \t pression");

        // Decorative wall particles
        for (Particle wallParticle : wallParticles) {
            writer.getWriter().println(wallParticle.print());
        }

        // Real particles
        for (Particle particle : particles) {
            writer.getWriter().println(particle.print());
        }
    }

    private static List<Particle> generateWallParticles() {
        if (wallParticles != null) {
            return wallParticles;
        }
        wallParticles = new ArrayList<>();

        double step = 0.0005;

        // Vertical walls
        for (double y = 0; y < SystemConfiguration.L; y += step) {
            wallParticles.add(Particle.wallParticle(new Vector(0, y)));
            wallParticles.add(Particle.wallParticle(new Vector(SystemConfiguration.W, y)));
        }

        // Horizontal walls
        for (double x = 0; x < SystemConfiguration.W; x += step) {
            if (x <= SystemConfiguration.holeStart.getX() || x >= SystemConfiguration.holeEnd.getX()) {
                wallParticles.add(Particle.wallParticle(new Vector(x, 0)));
            }
        }

        return wallParticles;
    }
}
