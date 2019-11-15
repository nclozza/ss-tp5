package ar.edu.itba.ss;

import ar.edu.itba.ss.model.Cell;
import ar.edu.itba.ss.model.Particle;

import java.util.*;
import static java.util.Collections.EMPTY_SET;

class CellIndexMethod {
    
    private double cellWidth;
    private Cell[][] grid;
    private int gridWidth;
    private int gridHeight;
    private Set<Particle> notInteractParticles;

    private static final int[] MOVES_X = {-1, 0, 1, -1, 0, 1, -1, 0, 1};
    private static final int[] MOVES_Y = {-1, -1, -1, 0, 0, 0, 1, 1, 1};

    CellIndexMethod(SystemConfiguration systemConfiguration, final double interactionRadius) {
        this.cellWidth = 2 * systemConfiguration.maxRadius + interactionRadius;
        this.gridWidth = (int) Math.ceil(systemConfiguration.W / cellWidth);
        this.gridHeight = (int) Math.ceil(systemConfiguration.L / cellWidth);
        this.notInteractParticles = new HashSet<>();
        this.grid = new Cell[gridWidth][gridHeight];

        for (int i = 0; i < gridWidth; i++) {
            for (int j = 0; j < gridHeight; j++) {
                grid[i][j] = new Cell(i, j);
            }
        }
    }

    Map<Integer, Set<Particle>> getNearParticles(Set<Particle> allParticles) {
        clearParticles();
        addParticlesToGridIfInteract(allParticles);
        return putNearParticles();
    }

    private void clearParticles(){
        notInteractParticles.clear();

        for (int i = 0; i < gridWidth; i++) {
            for (int j = 0; j < gridHeight; j++) {
                grid[i][j].getParticles().clear();
            }
        }
    }

    private void addParticlesToGridIfInteract(final Set<Particle> particles) {
        for (Particle particle : particles) {
            Double x = particle.position.x / cellWidth;
            Double y = particle.position.y / cellWidth;

            if (isInteract(x, y)) {
                grid[x.intValue()][y.intValue()].getParticles().add(particle);
            } else {
                notInteractParticles.add(particle);
            }
        }
    }

    private boolean isInteract(final double x, final double y){
        return x >= 0 && y >= 0 && x < gridWidth  && y < gridHeight;
    }

    private Map<Integer, Set<Particle>> putNearParticles(){
        Map<Integer, Set<Particle>> nearParticles = new HashMap<>();

        for (int i = 0; i < gridWidth; i++) {
            for (int j = 0; j < gridHeight; j++) {
                Set<Particle> cellNearParticles = getNearParticlesByCell(grid[i][j]);
                Set<Particle> gridParticles = grid[i][j].getParticles();

                for (Particle particle : gridParticles) {
                    nearParticles.put(particle.id, cellNearParticles);
                }
            }
        }

        for (Particle particle : notInteractParticles) {
            nearParticles.put(particle.id, EMPTY_SET);
        }
        return nearParticles;
    }

    private Set<Particle> getNearParticlesByCell(Cell cell) {
        Set<Particle> nearParticlesResult = new HashSet<>();

        for (int i = 0; i < MOVES_X.length ; i++){
            nearParticlesResult.addAll(addParticles(cell.x + MOVES_X[i], cell.y + MOVES_Y[i]));
        }

        return nearParticlesResult;
    }

    private Set<Particle> addParticles(int x, int y) {
        Set<Particle> nearParticles = new HashSet<>();

        if (isInteract(x, y)) {
            Set<Particle> particlesToAdd = grid[x][y].getParticles();

            for (Particle particle : particlesToAdd){
                nearParticles.add(particle);
            }
        }
        return nearParticles;
    }
}
