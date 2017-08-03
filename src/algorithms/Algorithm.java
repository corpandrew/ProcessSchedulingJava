package algorithms;

import objects.Process;

import java.util.List;

/**
 * Abstract class for all of the Algorithms to extend
 */
public abstract class Algorithm {

    /**
     * Start the algorithm
     * @param processList List of processes to pass in (from the table)
     * @return the list of completed processes, for the Gantt Chart
     */
    public abstract List<Process> start(List<Process> processList);

    /**
     * To String function
     * @return the name of the algorithm
     */
    public abstract String toString();

    //variables that they all use
    public double averageWaitTime;
    public double averageTurnAroundTime;
    public int finishTime;

}
