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
 * Priority Algorithm Class
 */
public class Priority extends Algorithm {
    private int currentTime;

    @Override
    public List<Process> start(List<Process> processList) {
        Main.outputArea.appendText("\n" + "----------------------------------------");
        Main.outputArea.appendText("\n" + toString());

        //init variables
        ArrayList<Process> processArrayList = new ArrayList<>(processList);

        List<Process> finishedProcessList = new ArrayList<>();
        //sort by priority
        Queue<Process> currentProcessQueue = new PriorityQueue<>(Process.priorityComparator());

        processArrayList.sort(Process.arrivalTimeComparator());
        currentTime = 0;
        int totalWaitTime = 0;
        int totalTurnAroundTime = 0;
        double processAmount = processArrayList.size();
        boolean continueBoolean = false;

        Process p;

        while (true) {

            //this gets all the processes that are available at the currentTime
            //and then puts them into the processQueue, to be sorted and picked.
            //new Java 1.8 stuff
            processArrayList.stream().
                    filter(a -> a.getArrivalTime() <= currentTime).
                    collect(Collectors.toList()).
                    forEach(a ->
                    {
                        currentProcessQueue.add(a);
                        processArrayList.remove(a);
                    });

            p = currentProcessQueue.poll();

            //if there is no process in the queue, increment time and continue the loop from the top
            if (p == null) {
                currentTime++;
                continue;
            }

            //if its the first time getting here set the time
            if (p.getStartTime() == -1)
                p.setStartTime(currentTime);

            while (p.getTimeLeft() > 0) {
                currentTime++;

                for (Process proc : currentProcessQueue) {
                    proc.setWaitTime(proc.getWaitTime() + 1);
                }

                p.setTimeLeft(p.getTimeLeft() - 1);
                if (!processArrayList.isEmpty()) {//manage preemption
                    if (processArrayList.get(0).getArrivalTime() == currentTime && processArrayList.get(0).getBurstTime() < p.getTimeLeft()) {
                        currentProcessQueue.add(p);//add the current back to the queue
                        continueBoolean = true;//to continue the while after this break
                        break;
                    }
                }
            }
            //if the process got added back into the queue, it didnt finish, run again.
            if (continueBoolean) {
                continueBoolean = false;
                continue;
            }

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
        return "Priority";
    }
}
