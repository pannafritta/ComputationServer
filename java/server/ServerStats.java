package server;

import java.util.ArrayList;
import java.util.List;

public class ServerStats {

    private double avg = 0;
    private double max = 0;
    private final List<Double> respTimes = new ArrayList<>();

    public ServerStats() {
    }

    public void addTime(Double responseTime) {
        respTimes.add(responseTime);
        if (responseTime > max) {
            max = responseTime;
        }
        avg = avg - (avg - responseTime)/respTimes.size();
    }

    public double getAvgOkResponseTime() {
        return avg;
    }

    public double getMaxOkResponseTime() {
        return max;
    }

    public int getNumOfOkResponses() {
        return respTimes.size();
    }
}
