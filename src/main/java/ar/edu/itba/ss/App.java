package ar.edu.itba.ss;

public class App {
    public static void main(String[] args) {
//        Engine engine = new Engine(SystemConfiguration.defaultConfig(),
//                new Output("energy", "output", "fallen_particles"));
//        engine.run();


        String name = "no static particle";

        for (int i = 0; i < 3; i++) {
            System.out.println("i = " + i);
            Engine engine = new Engine(SystemConfiguration.defaultConfig(),
                    new Output("energy; " + name + ", i = " + i,
                            "output; " + name + ", i = " + i,
                            "fallen_particles " + name + ", i = " + i));
            engine.run();
        }
    }
}

