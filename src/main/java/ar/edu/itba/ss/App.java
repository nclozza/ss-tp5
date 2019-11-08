package ar.edu.itba.ss;

public class App {

    public static void main(String[] args) {
        Engine engine = new Engine(SystemConfiguration.defaultConfig());

        engine.run();
    }
}
