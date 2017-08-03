package objects;

import javafx.beans.property.SimpleIntegerProperty;

import java.util.Comparator;

/**
 * Process Class, for storing variables that have to do with the Process
 */
public class Process {

    private SimpleIntegerProperty pid;
    private SimpleIntegerProperty arrivalTime;
    private SimpleIntegerProperty burstTime;
    private SimpleIntegerProperty priority;
    private SimpleIntegerProperty timeLeft;

    private SimpleIntegerProperty waitTime;
    private SimpleIntegerProperty turnAroundTime;

    private SimpleIntegerProperty startTime;
    private SimpleIntegerProperty endTime;

    public Process(int pid, int arrivalTime, int burstTime, int priority) {
        this.pid = new SimpleIntegerProperty(pid);
        this.arrivalTime = new SimpleIntegerProperty(arrivalTime);
        this.burstTime = new SimpleIntegerProperty(burstTime);
        this.priority = new SimpleIntegerProperty(priority);
        timeLeft = new SimpleIntegerProperty(burstTime);
        waitTime = new SimpleIntegerProperty(0);
        turnAroundTime = new SimpleIntegerProperty(0);
        startTime = new SimpleIntegerProperty(-1);
        endTime = new SimpleIntegerProperty(0);
    }

    public int getPid() {
        return pid.get();
    }

    public SimpleIntegerProperty pidProperty() {
        return pid;
    }

    public void setPid(int pid) {
        this.pid.set(pid);
    }

    public int getArrivalTime() {
        return arrivalTime.get();
    }

    public SimpleIntegerProperty arrivalTimeProperty() {
        return arrivalTime;
    }

    public void setArrivalTime(String arrivalTime) {
        this.arrivalTime.set(Integer.parseInt(arrivalTime));
    }

    public void setArrivalTime(int arrivalTime) {
        this.arrivalTime.set(arrivalTime);
    }

    public int getBurstTime() {
        return burstTime.get();
    }

    public SimpleIntegerProperty burstTimeProperty() {
        return burstTime;
    }

    public void setBurstTime(int burstTime) {
        this.burstTime.set(burstTime);
    }

    public int getPriority() {
        return priority.get();
    }

    public SimpleIntegerProperty priorityProperty() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority.set(priority);
    }

    public int getTimeLeft() {
        return timeLeft.get();
    }

    public SimpleIntegerProperty timeLeftProperty() {
        return timeLeft;
    }

    public void setTimeLeft(int timeLeft) {
        this.timeLeft.set(timeLeft);
    }

    public int getWaitTime() {
        return waitTime.get();
    }

    public SimpleIntegerProperty waitTimeProperty() {
        return waitTime;
    }

    public void setWaitTime(int waitTime) {
        this.waitTime.set(waitTime);
    }

    public int getTurnAroundTime() {
        return turnAroundTime.get();
    }

    public SimpleIntegerProperty turnAroundTimeProperty() {
        return turnAroundTime;
    }

    public void setTurnAroundTime(int turnAroundTime) {
        this.turnAroundTime.set(turnAroundTime);
    }

    public int getStartTime() {
        return startTime.get();
    }

    public SimpleIntegerProperty startTimeProperty() {
        return startTime;
    }

    public void setStartTime(int startTime) {
        this.startTime.set(startTime);
    }

    public int getEndTime() {
        return endTime.get();
    }

    public SimpleIntegerProperty endTimeProperty() {
        return endTime;
    }

    public void setEndTime(int endTime) {
        this.endTime.set(endTime);
    }

    //for all of them
    public static Comparator<Process> arrivalTimeComparator() {
        return Comparator.comparingInt(Process::getArrivalTime);
    }

    //for SJF
    public static Comparator<Process> burstTimeComparator() {
        return Comparator.comparingInt(Process::getBurstTime);
    }

    //for Priority
    public static Comparator<Process> priorityComparator() {
        return Comparator.comparingInt(Process::getPriority);
    }

    //for SRT
    public static Comparator<Process> timeLeftComparator() {
        return Comparator.comparingInt(Process::getTimeLeft);
    }

    @Override
    public String toString() {
        return "Process{" +
                "name='" + pid + '\'' +
                ", arrivalTime=" + arrivalTime +
                ", burstTime=" + burstTime +
                ", priority=" + priority +
                ", timeLeft=" + timeLeft +
                ", waitTime=" + waitTime +
                ", turnAroundTime=" + turnAroundTime +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                '}';
    }

    @Override
    public Process clone() {
        Process p = new Process(pid.get(), arrivalTime.get(), burstTime.get(), priority.get());
        timeLeft = new SimpleIntegerProperty(burstTime.get());
        waitTime = new SimpleIntegerProperty(0);
        turnAroundTime = new SimpleIntegerProperty(0);
        startTime = new SimpleIntegerProperty(-1);
        endTime = new SimpleIntegerProperty(0);
        return p;
    }
}
