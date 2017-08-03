package algorithms;

import objects.Process;
import sample.Main;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.stream.Collectors;

/**
 * Round Robin Fixed
 */
public class RoundRobinFixed extends Algorithm {
    private int currentTime;

    @Override
    public List<Process> start(List<Process> processList) {
        Main.outputArea.appendText("\n" + "----------------------------------------");
        Main.outputArea.appendText("\n" + toString());

        ArrayList<Process> processArrayList = new ArrayList<>(processList);
        List<Process> finishedProcessList = new ArrayList<>();
        Queue<Process> roundRobinQueue = new LinkedList<>();

        processArrayList.sort(Process.arrivalTimeComparator());

        currentTime = 0;
        int totalWaitTime = 0;
        int totalTurnAroundTime = 0;
        double processAmount = processArrayList.size();
        boolean continueBoolean = false;

        int fixedQuantum = (Integer) Main.quantumSpinner.valueProperty().get();

        int quantumCounter = 0;
        Process p;
        while (true) {
            //this gets all the processes that are available at the currentTime
            //and then puts them into the processQueue, to be sorted and picked.
            processArrayList.stream().
                    filter(a -> a.getArrivalTime() <= currentTime).
                    collect(Collectors.toList()).
                    forEach(a ->
                    {
                        roundRobinQueue.add(a);
                        processArrayList.remove(a);
                    });

            p = roundRobinQueue.poll();

            if (p == null) {
                currentTime++;
                continue;
            }

            if (p.getStartTime() == -1)
                p.setStartTime(currentTime);

            //loop till they are equal
            while (quantumCounter <= fixedQuantum) {
                currentTime++;

                //incrmemnt all waitTimes in queue
                for (Process proc : roundRobinQueue) {
                    proc.setWaitTime(proc.getWaitTime() + 1);
                }

                p.setTimeLeft(p.getTimeLeft() - 1);

                quantumCounter++;
            }

            quantumCounter = 0;

            if(p.getTimeLeft() > 0){
                roundRobinQueue.add(p);
                continue;
            }

            p.setWaitTime(p.getStartTime() - p.getArrivalTime());
            //process is finished
            p.setEndTime(currentTime);
            p.setTurnAroundTime(p.getEndTime() - p.getArrivalTime());

            totalWaitTime += p.getWaitTime();
            totalTurnAroundTime += p.getTurnAroundTime();

            Main.outputArea.appendText("\nP" + p.getPid() + " finished at " + p.getEndTime());
            finishedProcessList.add(p);

            if (processArrayList.isEmpty() && roundRobinQueue.isEmpty())
                break;
        }

        averageWaitTime = totalWaitTime / processAmount;
        averageTurnAroundTime = totalTurnAroundTime / processAmount;
        finishTime = currentTime;

        DecimalFormat decimalFormat = new DecimalFormat("#,###.###");

        Main.outputArea.appendText("\nAverage Wait Time: " + decimalFormat.format(averageWaitTime));
        Main.outputArea.appendText("\nAverage Turn Around Time: " + decimalFormat.format(averageTurnAroundTime));
        return finishedProcessList;
    }

    @Override
    public String toString() {
        return "RR Fixed";
    }
}
