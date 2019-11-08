package ar.edu.itba.ss.model;

public class Vector {
    public final double x;
    public final double y;

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

        return this.dividedScalar(norm());
    }

    public double norm() {
        return Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2));
    }

    public Vector timesScalar(final double z) {
        return new Vector(x * z, y * z);
    }

    public Vector plusVector(final Vector v) {
        return new Vector(x + v.x, y + v.y);
    }

    public Vector minusVector(final Vector v) {
        return this.plusVector(v.timesScalar(-1));
    }

    public double distanceTo(final Vector v) {
        return this.minusVector(v).norm();
    }

    public Vector dividedScalar(final double value) {
        return this.timesScalar(1 / value);
    }

    public double dot(Vector other) {
        return x * other.x + y * other.y;
    }

    public double projectedOn(Vector other) {
        return new Vector(x, y).dot(other.normalize());
    }

    public Vector tangent() {
        return new Vector(-y, x);
    }
}
