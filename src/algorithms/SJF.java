package algorithms;

import objects.Process;
import sample.Main;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.stream.Collectors;

/**
 * Shortest Job First Algorithm
 */
public class SJF extends Algorithm {
    private int currentTime;

    @Override
    public List<Process> start(List<Process> processList) {
        Main.outputArea.appendText("\n" + "----------------------------------------");
        Main.outputArea.appendText("\n" + toString());

        ArrayList<Process> processArrayList = new ArrayList<>(processList);
        List<Process> finishedProcessList = new ArrayList<>();
        //sort by burst time
        Queue<Process> currentProcessQueue = new PriorityQueue<>(Process.burstTimeComparator());

        processArrayList.sort(Process.arrivalTimeComparator());
        currentTime = 0;
        int totalWaitTime = 0;
        int totalTurnAroundTime = 0;
        double processAmount = processArrayList.size();

        Process p;

        while (true) {

            //this gets all the processes that are available at the currentTime
            //and then puts them into the processQueue, to be sorted and picked.
            processArrayList.stream().
                    filter(a -> a.getArrivalTime() <= currentTime).
                    collect(Collectors.toList()).
                    forEach(a ->
                    {
                        currentProcessQueue.add(a);
                        processArrayList.remove(a);
                    });

            p = currentProcessQueue.poll();

            if (p == null) {
                currentTime++;
                continue;
            }

            p.setStartTime(currentTime);

            while (p.getTimeLeft() > 0) {
                currentTime++;
                p.setTimeLeft(p.getTimeLeft() - 1);
            }

            p.setWaitTime(p.getStartTime() - p.getArrivalTime());
            //process is finished
            p.setEndTime(currentTime);
            p.setTurnAroundTime(p.getEndTime() - p.getArrivalTime());

            totalWaitTime += p.getWaitTime();
            totalTurnAroundTime += p.getTurnAroundTime();

            Main.outputArea.appendText("\nP" + p.getPid() + " finished at " + p.getEndTime());
            finishedProcessList.add(p);

            if (processArrayList.isEmpty() && currentProcessQueue.isEmpty())
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
        return "SJF";
    }

}
