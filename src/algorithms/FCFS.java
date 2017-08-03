package algorithms;

import objects.Process;
import sample.Main;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * First Come First Serve Algorithm
 */
public class FCFS extends Algorithm {

    @Override
    public List<Process> start(List<Process> processList) {
        Main.outputArea.appendText("\n" + "----------------------------------------");
        Main.outputArea.appendText("\n" + toString());

        //initialize variables

        ArrayList<Process> processArrayList = new ArrayList<>(processList);
        List<Process> finishedProcessList = new ArrayList<>();

        //sort by arrival time
        processArrayList.sort(Process.arrivalTimeComparator());
        int currentTime = 0;
        int totalWaitTime = 0;
        int totalTurnAroundTime = 0;
        double processAmount = processArrayList.size();

        Process p;
        while (!processArrayList.isEmpty()) {
            p = processArrayList.remove(0);

            //run till the process is ready to run
            while(p.getArrivalTime() > currentTime){
                currentTime++;
            }

            p.setStartTime(currentTime);
            //start the process
            //run till its out of time
            while (p.getTimeLeft() > 0) {
                currentTime++;
                p.setTimeLeft(p.getTimeLeft() - 1);
            }
            p.setWaitTime(p.getStartTime() - p.getArrivalTime());
            //process is finished
            p.setEndTime(currentTime);
            p.setTurnAroundTime(p.getEndTime() - p.getArrivalTime());

            //calculation
            totalWaitTime += p.getWaitTime();
            totalTurnAroundTime += p.getTurnAroundTime();

            Main.outputArea.appendText("\nP" + p.getPid() + " finished at " + p.getEndTime());
            finishedProcessList.add(p);
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
        return "FCFS";
    }
}