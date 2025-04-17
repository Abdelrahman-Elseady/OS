package SRTF;



import SRTF.ProcessInputListener;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;


class SRTFInput implements ActionListener {
    private final ArrayList<Process> processes;
    private final ProcessInputListener listener;
    private final int totalProcesses;
    private int currentProcessIndex;


    JFrame frame = new JFrame("Process Information");
    JPanel panel = new JPanel();
    JButton bot = new JButton("Enter");

    JTextField name = new JTextField();
    JTextField arrivalTime = new JTextField();
    JTextField burstTime = new JTextField();
    JTextField color = new JTextField();

    JLabel nameLabel = new JLabel("Name:");
    JLabel arrivalTimeLabel = new JLabel("Arrival Time:");
    JLabel burstTimeLabel = new JLabel("Burst Time:");
    JLabel colorLabel = new JLabel("Color in hexa:");

    public SRTFInput(ArrayList<Process> processes, int totalProcesses, ProcessInputListener listener, int currentProcessIndex) {
        this.processes = processes;
        this.listener = listener;
        this.totalProcesses = totalProcesses;
        this.currentProcessIndex = currentProcessIndex;

        bot.setBounds(100, 300, 200, 40);
        bot.setFocusable(false);
        bot.addActionListener(this);

        frame.setLayout(null);
        frame.setSize(400, 400);
        frame.setLocationRelativeTo(null);

        panel.setLayout(null);
        panel.setSize(400, 400);

        name.setBounds(150, 50, 200, 30);
        arrivalTime.setBounds(150, 100, 200, 30);
        burstTime.setBounds(150, 150, 200, 30);
        color.setBounds(150, 200, 200, 30);

        nameLabel.setBounds(30, 50, 100, 30);
        arrivalTimeLabel.setBounds(30, 100, 100, 30);
        burstTimeLabel.setBounds(30, 150, 100, 30);
        colorLabel.setBounds(30, 200, 100, 30);

        panel.add(nameLabel);
        panel.add(arrivalTimeLabel);
        panel.add(burstTimeLabel);
        panel.add(colorLabel);

        panel.add(name);
        panel.add(arrivalTime);
        panel.add(burstTime);
        panel.add(color);
        panel.add(bot);

        frame.add(panel);
        frame.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == bot) {
            String Name = name.getText();
            String Color = color.getText();
            int ArrivalTime = Integer.parseInt(arrivalTime.getText());
            int BurstTime = Integer.parseInt(burstTime.getText());

            processes.add(new Process(Name,Color, ArrivalTime, BurstTime));
            frame.dispose();

            if (processes.size() == totalProcesses) {
                listener.onInputComplete(processes);
            }
        }
    }
}

