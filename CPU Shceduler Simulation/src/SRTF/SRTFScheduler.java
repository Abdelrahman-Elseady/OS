package SRTF;

import java.util.ArrayList;

class Process {
    String name;
    String color;
    int arrivalTime;
    int burstTime;
    int remainingTime;
    int completionTime;
    int startTime;

    Process(String name, String color, int arrivalTime, int burstTime) {
        this.name = name;
        this.color = color;
        this.arrivalTime = arrivalTime;
        this.burstTime = burstTime;
        this.remainingTime = burstTime;
        this.startTime = -1;
    }
}
//to keep each process detail so i can make the gui for it
class ResultRecord {
    String timeInterval;
    String processName;
    String color;
    int executedTime;
    String actionDetails;

    ResultRecord(String timeInterval, String processName,String color, int executedTime, String actionDetails) {
        this.timeInterval = timeInterval;
        this.processName = processName;
        this.color=color;
        this.executedTime = executedTime;
        this.actionDetails = actionDetails;
    }
}

public class SRTFScheduler {
    public static void main(String[] args) {
        SRTFScheduler scheduler = new SRTFScheduler();
        scheduler.start();
    }
     //the srtf gui implement a functional interface that wait for the input to be taken from the gui component then start to implement its logic
    private void start() {
        new SRTFGUI(processes -> {
            scheduleProcesses(processes);
        });
    }

    public static void scheduleProcesses(ArrayList<Process> processes) {
        int currentTime = 0;
        int completed = 0;
        //this is the array list that will be passed for the gui compnent to produce the output form it insted of the string builder
        ArrayList<ResultRecord> result = new ArrayList<>();

        while (completed < processes.size()) {
            Process currentProcess = null;
            int shortestTime = Integer.MAX_VALUE;

            // Find the process with the shortest remaining time
            for (Process p : processes) {
                if (p.arrivalTime <= currentTime && p.remainingTime > 0 && p.remainingTime < shortestTime) {
                    currentProcess = p;
                    shortestTime = p.remainingTime;
                }
            }

            if (currentProcess == null) {
                currentTime++;
                continue;
            }

            if (currentProcess.startTime == -1) {
                currentProcess.startTime = currentTime;
            }

            int startTime = currentTime;
            int executedTime = 0;

            // Execute the process until it's interrupted or completes
            while (true) {
                currentProcess.remainingTime--;
                executedTime++;
                currentTime++;

                boolean shorterProcessAvailable = false;
                for (Process p : processes) {
                    if (p.arrivalTime <= currentTime && p.remainingTime > 0 && p.remainingTime < currentProcess.remainingTime) {
                        shorterProcessAvailable = true;
                        break;
                    }
                }

                if (currentProcess.remainingTime == 0 || shorterProcessAvailable) {
                    break;
                }
            }

            String actionDetails;
            if (currentProcess.remainingTime == 0) {
                currentProcess.completionTime = currentTime;
                completed++;
                actionDetails = String.format("%s completes execution.", currentProcess.name);
            } else {
                actionDetails = String.format("%s runs for %d units, remaining burst = %d.",
                        currentProcess.name, executedTime, currentProcess.remainingTime);
            }

            result.add(new ResultRecord(
                    startTime + " -> " + currentTime,
                    currentProcess.name,
                    currentProcess.color,
                    executedTime,
                    actionDetails
            ));
        }

        // Calculate average turnaround and waiting time
        float totalTurnaround = 0;
        float totalWaiting = 0;
        for (Process p : processes) {
            int turnaroundTime = p.completionTime - p.arrivalTime;
            int waitingTime = turnaroundTime - p.burstTime;
            totalTurnaround += turnaroundTime;
            totalWaiting += waitingTime;
        }
        float avgTurnaroundTime = totalTurnaround / processes.size();
        float avgWaitingTime = totalWaiting / processes.size();

        // Pass the result to the GUI and the currentTime is simply indicate the total burst time
        new SRTFOutput(avgTurnaroundTime, avgWaitingTime, currentTime, result);
    }
}
