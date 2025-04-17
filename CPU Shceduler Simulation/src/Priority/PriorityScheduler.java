package Priority;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

class Process {
    String name;
    String color;
    int arrivalTime;
    int burstTime;
    int priority;
    int waitingTime;
    int turnAroundTime;

    Process(String name, String color, int arrivalTime, int burstTime, int priority) {
        this.name = name;
        this.color = color;
        this.arrivalTime = arrivalTime;
        this.burstTime = burstTime;
        this.priority = priority;
        this.waitingTime = 0;
        this.turnAroundTime = 0;
    }
}

public class PriorityScheduler {
    public static void main(String[] args) {
        final int[] contextSwitchTime = new int[1]; // To allow modification inside EventQueue

        EventQueue.invokeLater(() -> {
            JTextField textField = new JTextField(10);
            JPanel contentPane = new JPanel();
            contentPane.add(new JLabel("Enter the context switch time: "));
            contentPane.add(textField);

            JOptionPane.showMessageDialog(null, contentPane, "Context Switch", JOptionPane.PLAIN_MESSAGE);

            contextSwitchTime[0] = Integer.parseInt(textField.getText());
        });

        PriorityScheduler scheduler = new PriorityScheduler();
        scheduler.start(contextSwitchTime[0]);
    }

    public void start(int contextSwitchTime) {
        new PriorityGUI(processes -> scheduleProcesses(processes, contextSwitchTime));
    }

    public static void scheduleProcesses(ArrayList<Process> processes, int contextSwitchTime) {
        processes.sort(Comparator.comparingInt((Process p) -> p.arrivalTime).thenComparingInt(p -> p.priority));

        int currentTime = 0;
        int totalWaitingTime = 0;
        int totalTurnAroundTime = 0;
        List<Process> processExecutionOrder = new ArrayList<>();

        for (int i = 0; i < processes.size(); i++) {
            Process p = processes.get(i);

            if (currentTime < p.arrivalTime) {
                processExecutionOrder.add(null); // Idle time is represented as a null process
                currentTime = p.arrivalTime;
            }

            // Simulate context switching if not the first process
            if (i > 0) {
                processExecutionOrder.add(null); // Context switching time is represented as a null process
                currentTime += contextSwitchTime;
            }

            p.waitingTime = currentTime - p.arrivalTime;
            p.turnAroundTime = p.waitingTime + p.burstTime;
            totalWaitingTime += p.waitingTime;
            totalTurnAroundTime += p.turnAroundTime;

            processExecutionOrder.add(p);
            currentTime += p.burstTime;
        }

        double avgWaitingTime = (double) totalWaitingTime / processes.size();
        double avgTurnAroundTime = (double) totalTurnAroundTime / processes.size();

        new PriorityActionDetail(avgTurnAroundTime, avgWaitingTime, processExecutionOrder, processes);
    }
}