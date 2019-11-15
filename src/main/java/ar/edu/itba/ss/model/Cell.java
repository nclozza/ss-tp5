package ar.edu.itba.ss.model;

import java.util.HashSet;
import java.util.Set;

public class Cell {

    public int x;
    public int y;
    private Set<Particle> particles;

    public Cell(int x, int y) {
        this.x = x;
        this.y = y;
        this.particles = new HashSet<>();
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public Set<Particle> getParticles() {
        return particles;
    }

    public void setParticles(Set<Particle> particles) {
        this.particles = particles;
    }
}
