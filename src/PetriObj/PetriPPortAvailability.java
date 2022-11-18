package PetriObj;

public class PetriPPortAvailability extends PetriP {
    private double previousTime = 0;

    private double sumTime = 0;
    private double n = 0;
    private double maxTime = 0;
    private double minTime = Double.POSITIVE_INFINITY;
    private double currentTime = 0;

    public PetriPPortAvailability(String n, int m) {
        super(n, m);
    }

    @Override
    public void increaseMark(int a) {
        super.increaseMark(a);

        double time = currentTime - previousTime;
        n++;
        sumTime += time;
        maxTime = Double.max(maxTime, time);
        minTime = Double.min(minTime, time);
    }

    @Override
    public void decreaseMark(int a) {
        super.decreaseMark(a);
        previousTime = currentTime;
    }

    public double getMaxTime() {
        return maxTime;
    }

    public double getMinTime() {
        return minTime;
    }

    public double getAverageTime() {
        return sumTime / n;
    }

    public double getSumTime() {
        return sumTime;
    }

    public double getN() {
        return n;
    }

    public void setCurrentTime(double currentTime) {
        this.currentTime = currentTime;
    }
}
