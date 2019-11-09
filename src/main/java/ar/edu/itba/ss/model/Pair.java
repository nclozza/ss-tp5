package ar.edu.itba.ss.model;

public class Pair <A,B> {
    public final A a;
    public final B b;

    private Pair(A a, B b) {
        this.a = a;
        this.b = b;
    }

    public static <A,B> Pair<A,B> of(A a, B b) {
        return new Pair<>(a,b);
    }

    public A first() {
        return a;
    }

    public B second() {
        return b;
    }
}
