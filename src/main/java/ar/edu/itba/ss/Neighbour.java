package ar.edu.itba.ss;

import ar.edu.itba.ss.model.Cell;
import ar.edu.itba.ss.model.Particle;

import java.util.*;

public class Neighbour {
    private double cellWidth;
    private Cell[][] grid;
    private int gridWidth;
    private int gridHeight;
    private Set<Particle> outOfBounds;

    public Neighbour(SystemConfiguration systemConfiguration, final double interactionRadius) {
        this.cellWidth = 2 * systemConfiguration.maxRadius + interactionRadius;
        this.gridWidth = (int) Math.ceil(systemConfiguration.W / cellWidth);
        this.gridHeight = (int) Math.ceil(systemConfiguration.L / cellWidth);
        this.outOfBounds = new HashSet<>();
        this.grid = initializeGrid();
    }

    private Cell[][] initializeGrid() {
        Cell[][] grid = new Cell[gridWidth][gridHeight];

        for (int i = 0; i < gridWidth; i++) {
            for (int j = 0; j < gridHeight; j++) {
                grid[i][j] = new Cell(i, j);
            }
        }

        return grid;
    }

    public Map<Integer, Set<Particle>> getNeighbours(Set<Particle> allParticles) {
        Map<Integer, Set<Particle>> neighbours = new HashMap<>();

        clearGrid();

        addParticlesToGrid(allParticles);

        for (int i = 0; i < gridWidth; i++) {
            for (int j = 0; j < gridHeight; j++) {
                Set<Particle> cellNeighbours = getNearMolecules(grid[i][j]);
                for (Particle p : grid[i][j].getParticles()) {
                    neighbours.put(p.id, cellNeighbours);
                }
            }
        }

        for (Particle particle : outOfBounds) {
            neighbours.put(particle.id, Collections.EMPTY_SET);
        }

        return neighbours;
    }

    private void addParticlesToGrid(Set<Particle> particles) {
        for (Particle p : particles) {
            int x = (int) (p.position.x / cellWidth);
            int y = (int) (p.position.y / cellWidth);

            if (x >= 0 && x < gridWidth && y >= 0 && y < gridHeight) {
                grid[x][y].addParticle(p);
            } else {
                outOfBounds.add(p);
            }

        }
    }

    private Set<Particle> getNearMolecules(Cell field) {
        Set<Particle> nearParticles = new HashSet<>();
        int x = field.getX();
        int y = field.getY();

        addParticles(nearParticles, x - 1, y - 1);
        addParticles(nearParticles, x, y - 1);
        addParticles(nearParticles, x + 1, y - 1);

        addParticles(nearParticles, x - 1, y);
        addParticles(nearParticles, x, y);
        addParticles(nearParticles, x + 1, y);

        addParticles(nearParticles, x - 1, y + 1);
        addParticles(nearParticles, x, y + 1);
        addParticles(nearParticles, x + 1, y + 1);


        return nearParticles;
    }

    public void addParticles(Set<Particle> nearParticles, int x, int y) {
        if (x >= 0 && x < gridWidth && y >= 0 && y < gridHeight) {
            nearParticles.addAll(grid[x][y].getParticles());
        }
    }

    private void clearGrid() {
        outOfBounds.clear();
        for (int i = 0; i < gridWidth; i++) {
            for (int j = 0; j < gridHeight; j++) {
                grid[i][j].clearParticles();
            }
        }
    }
}
