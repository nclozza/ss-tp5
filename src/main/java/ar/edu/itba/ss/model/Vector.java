package ar.edu.itba.ss.model;

public class Vector {

    private final double x;
    private final double y;

    public Vector(final double x, final double y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public String toString() {
        return "(" + x + ", " + y + ")";
    }

    public Vector normalize() {
        if (x == 0 && y == 0) {
            return new Vector(0, 0);
        }

        return this.timesScalar(1 / norm());
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double norm() {
        return Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2));
    }

    public Vector timesScalar(double z) {
        return new Vector(x * z, y * z);
    }

    public Vector plusVector(Vector v) {
        return new Vector(x + v.x, y + v.y);
    }

    public Vector minusVector(Vector v) {
        return this.plusVector(v.timesScalar(-1));
    }

    public double distanceTo(Vector v) {
        return this.minusVector(v).norm();
    }

    public Vector clone() {
        return new Vector(x, y);
    }
}
