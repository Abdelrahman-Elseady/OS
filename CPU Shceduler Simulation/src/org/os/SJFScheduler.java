package org.os;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

class Process {
    String name;
    String color;
    int arrivalTime;
    int burstTime;
    int startTime;
    int completionTime;
    int waitingTime;
    int priorityNumber;

    Process(String name, String color, int arrivalTime, int burstTime, int priorityNumber) {
        this.name = name;
        this.color = color;
        this.arrivalTime = arrivalTime;
        this.burstTime = burstTime;
        this.waitingTime = 0;
        this.priorityNumber = priorityNumber;
        this.startTime = -1; // Indicates it hasn't started yet
    }
}

public class SJFScheduler {
    public static void main(String[] args) {
        final int[] agingThreshold = new int[1]; // To allow modification inside EventQueue

        EventQueue.invokeLater(() -> {
            JTextField textField = new JTextField(10);
            JPanel contentPane = new JPanel();
            contentPane.add(new JLabel("Enter the aging threshold: "));
            contentPane.add(textField);

            JOptionPane.showMessageDialog(null, contentPane, "Aging Threshold", JOptionPane.PLAIN_MESSAGE);

            agingThreshold[0] = Integer.parseInt(textField.getText());
        });

        SJFScheduler scheduler = new SJFScheduler();
        scheduler.start(agingThreshold[0]);
    }

    public void start(int agingThreshold) {
        new SJFGUI(processes -> scheduleProcesses(processes, agingThreshold));
    }

    public static void scheduleProcesses(ArrayList<Process> processes, int agingThreshold) {
        int currentTime = 0;
        int completed = 0;

        while (completed < processes.size()) {
            Process currentProcess = null;
            int shortestTime = Integer.MAX_VALUE;

            List<Process> availableProcesses = getProcesses(currentTime, processes, agingThreshold);
            for (Process p : availableProcesses) {
                if (p.burstTime < shortestTime) {
                    currentProcess = p;
                    shortestTime = p.burstTime;
                }
            }

            if (currentProcess == null) {
                currentTime++;
                continue;
            }

            if (currentProcess.startTime == -1) {
                currentProcess.startTime = currentTime;
            }

            currentTime += currentProcess.burstTime;
            currentProcess.completionTime = currentTime;
            completed++;
        }

        int totalBurstTime = 0;
        float sumWaiting = 0;
        float sumTurnaround = 0;
        for (Process p : processes) {
            totalBurstTime += p.burstTime;
            int turnaroundTime = p.completionTime - p.arrivalTime;
            int waitingTime = turnaroundTime - p.burstTime;
            p.waitingTime = waitingTime;
            sumWaiting += waitingTime;
            sumTurnaround += turnaroundTime;
        }

        float avgWaitingTime = sumWaiting / processes.size();
        float avgTurnaroundTime = sumTurnaround / processes.size();

        new SJFActionDetail(avgTurnaroundTime, avgWaitingTime, totalBurstTime, processes);
    }

    public static List<Process> getProcesses(int time, ArrayList<Process> processes, int agingThreshold) {
        List<Process> ret = new ArrayList<>();
        for (Process p : processes) {
            if (p.arrivalTime <= time && p.completionTime == 0) {
                ret.add(p);
            } else if (p.completionTime == 0) {
                p.waitingTime++;

                // Apply aging logic
                if (p.waitingTime % agingThreshold == 0) {
                    p.burstTime = Math.max(1, p.burstTime - 1); // Ensure burst time doesn't drop below 1
                }
            }
        }
        return ret;
    }
}
