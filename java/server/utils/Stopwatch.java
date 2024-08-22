package server.utils;

public class Stopwatch {
    private double startTime;

    public void start() {
        startTime = (double) System.nanoTime();
    }

    public double stop() {
        return (System.nanoTime() - startTime) / 1000000000;
    }
}
