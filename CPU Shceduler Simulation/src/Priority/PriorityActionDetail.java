package Priority;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

class PriorityActionDetail extends JComponent {
    List<Process> processExecutionOrder;
    List<Process> processes;
    JFrame frame;
    int maxWidth = 1200;

    public PriorityActionDetail(double avgTurnAroundTime, double avgWaitingTime, List<Process> processExecutionOrder, List<Process> processes) {
        this.processExecutionOrder = processExecutionOrder;
        this.processes = processes;

        int totalBurstTime = processExecutionOrder.stream().filter(p -> p != null).mapToInt(p -> p.burstTime).sum();
        int chartWidth = Math.min(maxWidth, totalBurstTime * 50); // Adjust scaling factor for better visibility
        int chartHeight = 50;
        int frameWidth = chartWidth + 100;
        int frameHeight = 300 + chartHeight + processes.size() * 25;

        frame = new JFrame();
        frame.setLayout(null);
        frame.setSize(frameWidth, frameHeight);
        frame.setTitle("Priority Scheduling Details");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.getContentPane().setBackground(new Color(0, 102, 102));

        JLabel turnaroundLabel = new JLabel("Avg. Turnaround Time: " + avgTurnAroundTime);
        JLabel waitingLabel = new JLabel("Avg. Waiting Time: " + avgWaitingTime);

        turnaroundLabel.setBounds(10, 10, frameWidth - 20, 30);
        waitingLabel.setBounds(10, 40, frameWidth - 20, 30);
        turnaroundLabel.setForeground(Color.WHITE);
        waitingLabel.setForeground(Color.WHITE);

        frame.add(turnaroundLabel);
        frame.add(waitingLabel);

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

        int currentTime = 0;
        for (Process p : processExecutionOrder) {
            if (p == null) {
                model.addRow(new Object[]{currentTime + " -> " + (currentTime + 1), "Idle", 1, "CPU is idle"});
                currentTime++;
            } else {
                model.addRow(new Object[]{currentTime + " -> " + (currentTime + p.burstTime), p.name, p.burstTime, p.name + " executes for " + p.burstTime});
                currentTime += p.burstTime;
            }
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
        int height = 40;
        int totalTime = processExecutionOrder.stream().filter(p -> p != null).mapToInt(p -> p.burstTime).sum();
        double scaleFactor = (double) getWidth() / totalTime;

        int currentTime = 0;
        for (Process p : processExecutionOrder) {
            Color color;
            if (p == null) {
                color = Color.GRAY; // Idle time
            } else {
                try {
                    color = Color.decode(p.color);
                } catch (NumberFormatException e) {
                    color = Color.LIGHT_GRAY;
                }
            }

            int width = (int) (scaleFactor * (p == null ? 1 : p.burstTime));

            g2d.setColor(color);
            g2d.fillRect(x, y, width, height);
            g2d.setColor(Color.BLACK);
            g2d.drawRect(x, y, width, height);

            if (p != null) {
                g2d.drawString(p.name, x + 5, y + height / 2);
            }

            g2d.drawString(String.valueOf(currentTime), x, y + height + 15);
            currentTime += (p == null ? 1 : p.burstTime);
            x += width;
        }
        g2d.drawString(String.valueOf(currentTime), x, y + height + 15);


        x = 0;
        currentTime = 0;
        for (Process p : processExecutionOrder) {
            int width = (int) (scaleFactor * (p == null ? 1 : p.burstTime));

            g2d.drawString(String.valueOf(currentTime), x, y + height + 35);
            currentTime += (p == null ? 1 : p.burstTime);
            x += width;
        }
        g2d.drawString(String.valueOf(currentTime), x, y + height + 35);
    }
}
