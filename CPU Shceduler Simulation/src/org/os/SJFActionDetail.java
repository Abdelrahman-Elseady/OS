package org.os;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.Comparator;

public class SJFActionDetail extends JComponent {
    ArrayList<Process> processes;
    JFrame frame;
    int maxWidth = 1200;
    int totalBurstTime;

    public SJFActionDetail(float avgTurnaroundTime, float avgWaitingTime,int totalBurstTime, ArrayList<Process> processes) {
        this.processes = processes;
        this.totalBurstTime = totalBurstTime;


        int chartWidth = Math.min(maxWidth, totalBurstTime * 20);
        int chartHeight = 100;
        int frameWidth = chartWidth + 50;
        int frameHeight = 200 + (processes.size() * 30) + chartHeight; //as the frame will contain the chart ,some data and the table


        frame = new JFrame();
        frame.setLayout(null);
        frame.setSize(frameWidth, frameHeight);
        frame.setTitle("SJF Scheduling Details");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);


        frame.getContentPane().setBackground(new Color(0, 102, 102));


        JLabel turnaroundLabel = new JLabel("Avg. Turnaround Time: " + avgTurnaroundTime);
        JLabel waitingLabel = new JLabel("Avg. Waiting Time: " + avgWaitingTime);

        turnaroundLabel.setBounds(10, 10, frameWidth - 20, 30);
        waitingLabel.setBounds(10, 40, frameWidth - 20, 30);


        turnaroundLabel.setForeground(Color.WHITE);
        waitingLabel.setForeground(Color.WHITE);

        frame.add(turnaroundLabel);
        frame.add(waitingLabel);

        // this refers to the jcomponent which is the gant chart
        this.setBounds(10, 80, chartWidth, chartHeight);
        this.setBackground(new Color(0, 102, 102));
        this.setOpaque(true);
        frame.add(this);


        JTable actionTable = new JTable();
        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("Time");
        model.addColumn("Process Name");
        model.addColumn("Executed Time");
        model.addColumn("Action Details");

        for (Process p : processes) {
            model.addRow(new Object[]{
                    p.startTime + " -> " + p.completionTime,
                    p.name,
                    p.burstTime,
                    p.name + " completes execution."
            });
        }

        actionTable.setModel(model);
        JScrollPane scrollPane = new JScrollPane(actionTable);
        scrollPane.setBounds(10, 100 + chartHeight, chartWidth, processes.size() * 25);


        actionTable.setBackground(new Color(0, 102, 102));
        actionTable.setForeground(Color.WHITE);
        actionTable.setGridColor(Color.LIGHT_GRAY);
        actionTable.getTableHeader().setBackground(new Color(0, 102, 102));
        actionTable.getTableHeader().setForeground(Color.WHITE);

        frame.add(scrollPane);


        frame.setVisible(true);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        int x = 0;
        int y = 10;
        int height = 50;


        processes.sort(Comparator.comparingInt(p -> p.startTime));



        int chartWidth = Math.min(maxWidth, totalBurstTime * 20);
        double scaleFactor = (double) chartWidth / totalBurstTime;


        for (Process process : processes) {
            Color color;
            try {
                color = Color.decode(process.color); //  color is in hex format
            } catch (NumberFormatException e) {
                color = Color.GRAY;
            }

            int width = (int) (process.burstTime * scaleFactor);


            g2d.setColor(color);
            g2d.fillRect(x, y, width, height);


            g2d.setColor(Color.BLACK);
            g2d.drawRect(x, y, width, height);


            g2d.drawString(process.name, x + 5, y + height / 2);


            x += width;
        }

        // timeline
        x = 0; // Reset x for the timeline
        for (int i = 1; i <= totalBurstTime+1; i++) {

            int tickX = (int) (i * scaleFactor);
            g2d.drawString(String.valueOf(i), tickX, y + height + 20);
        }


}

}
