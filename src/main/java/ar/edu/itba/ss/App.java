package ar.edu.itba.ss;

public class App {

    public static void main(String[] args) {
        Engine engine = new Engine(SystemConfiguration.L, SystemConfiguration.W, SystemConfiguration.D,
                SystemConfiguration.dt, SystemConfiguration.dt2, SystemConfiguration.totalTime,
                SystemConfiguration.xyzWriterPath, SystemConfiguration.fallenParticlesWriterPath,
                SystemConfiguration.minRadius, SystemConfiguration.maxRadius, SystemConfiguration.mass);
        engine.run();
    }
}
