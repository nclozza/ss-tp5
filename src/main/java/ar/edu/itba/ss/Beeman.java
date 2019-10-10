package ar.edu.itba.ss;

import ar.edu.itba.ss.model.Particle;
import ar.edu.itba.ss.model.Vector;

import java.util.*;

public class Beeman {

    private Force force;
    private double dt;
    private Neighbour neighbour;
    private Map<Particle, Set<Particle>> neighbours;

    public Beeman(Force force, Neighbour neighbour, double dt, Set<Particle> allparticles) {
        this.force = force;
        this.dt = dt;
        this.neighbour = neighbour;
        initializeNeighbours(allparticles);
    }

    private void initializeNeighbours(Set<Particle> allParticles) {
        neighbours = new HashMap<>();
        for (Particle particle : allParticles) {
            neighbours.put(particle, Collections.emptySet());
        }
    }

    public Set<Particle> integrate(Set<Particle> allParticles) {

        calculateAcceleration(allParticles);

        calculateNextPosition(allParticles);

        calculateNextSpeedPredicted(allParticles);

        calculateNextAcceleration(allParticles);

        calculateNextSpeedCorrected(allParticles);

        return getUpdatedParticles(allParticles);
    }

    private void calculateAcceleration(Set<Particle> allParticles) {
        for (Particle p : allParticles) {
            Vector acceleration = force.calculate(p, neighbours.get(p)).dividedScalar(p.getMass());
            p.setAcceleration(acceleration);
        }
    }

    private void calculateNextPosition(Set<Particle> allParticles) {
        for (Particle p : allParticles) {
            Vector pos = p.getPosition();
            Vector sp = p.getSpeed();
            Vector ac = p.getAcceleration();
            Vector prAc = p.getPreviousAcc();

            double nextPx = pos.getX() + sp.getX() * dt + 2.0 / 3.0 * ac.getX() * Math.pow(dt, 2) - 1.0 / 6.0 * prAc.getX() * Math.pow(dt, 2);
            double nextPy = pos.getY() + sp.getY() * dt + 2.0 / 3.0 * ac.getY() * Math.pow(dt, 2) - 1.0 / 6.0 * prAc.getY() * Math.pow(dt, 2);

            p.setNextPosition(new Vector(nextPx, nextPy));
        }
    }

    private void calculateNextSpeedPredicted(Set<Particle> allParticles) {
        for (Particle p : allParticles) {
            Vector sp = p.getSpeed();
            Vector ac = p.getAcceleration();
            Vector prAc = p.getPreviousAcc();

            double nextVx = sp.getX() + 3.0 / 2.0 * ac.getX() * dt - 1.0 / 2.0 * prAc.getX() * dt;
            double nextVy = sp.getY() + 3.0 / 2.0 * ac.getY() * dt - 1.0 / 2.0 * prAc.getY() * dt;


            p.setNextSpeedPredicted(new Vector(nextVx, nextVy));
        }

    }

    private void calculateNextAcceleration(Set<Particle> allParticles) {
        neighbours = neighbour.getNeighbours(allParticles);
        for (Particle p : allParticles) {
            Vector acceleration = force.calculate(p, neighbours.get(p)).dividedScalar(p.getMass());
            p.setNextAcceleration(acceleration);
        }
    }


    private void calculateNextSpeedCorrected(Set<Particle> allParticles) {
        for (Particle p : allParticles) {
            Vector sp = p.getSpeed();
            Vector ac = p.getAcceleration();
            Vector prAc = p.getPreviousAcc();
            Vector neAc = p.getNextAcceleration();

            double nextVx = sp.getX() + 1.0 / 3.0 * neAc.getX() * dt + 5.0 / 6.0 * ac.getX() * dt - 1.0 / 6.0 * prAc.getX() * dt;
            double nextVy = sp.getY() + 1.0 / 3.0 * neAc.getY() * dt + 5.0 / 6.0 * ac.getY() * dt - 1.0 / 6.0 * prAc.getY() * dt;


            p.setNextSpeedCorrected(new Vector(nextVx, nextVy));
        }

    }


    private Set<Particle> getUpdatedParticles(Set<Particle> allParticles) {
        Set<Particle> updatedParticles = new HashSet<>();

        for (Particle p : allParticles) {
            Particle newP = Particle.of(p, p.getNextPosition(), p.getNextSpeedCorrected(), p.getAcceleration());
            newP.setTotalFn(p.getTotalFn());
            updatedParticles.add(newP);

        }
        return updatedParticles;
    }


}