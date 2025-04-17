package Priority;


import Priority.ProcessInputListener;


import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;




class PriorityGUI implements ActionListener {

    JFrame frame;
    JPanel panel = new JPanel();
    JButton bot = new JButton("Enter");
    JTextField text = new JTextField();
    JLabel label = new JLabel("Enter the number of processes:");
    private final ProcessInputListener listener;
    private final ArrayList<Process> processes;

    public PriorityGUI(ProcessInputListener listener) {
        this.listener = listener;
        this.processes = new ArrayList<>();

        frame = new JFrame("Number of Processes");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(null);
        frame.setSize(420, 200);
        frame.setLocationRelativeTo(null);

        panel.setLayout(null);
        panel.setSize(420, 200);

        label.setBounds(40, 20, 200, 30);
        text.setBounds(40, 60, 100, 30);
        bot.setBounds(40, 100, 100, 30);

        bot.addActionListener(this);

        panel.add(label);
        panel.add(text);
        panel.add(bot);

        frame.add(panel);
        frame.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        int num = Integer.parseInt(text.getText());
        frame.dispose();

        for (int i = 0; i < num; i++) {
            new PriorityInput(processes, num, listener, i);
        }
    }
}

