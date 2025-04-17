# CPU Scheduling Simulator (Java GUI)

This is a Java-based GUI application that simulates multiple CPU scheduling algorithms. It provides both visual and analytical representations of process scheduling behavior, context switching, and performance metrics.

---

## 📋 Features

This simulator implements the following scheduling algorithms:

- ✅ **Non-preemptive Priority Scheduling** (with context switching)
- ✅ **Non-preemptive Shortest Job First (SJF)**  
  - Starvation problem is handled
- ✅ **Shortest Remaining Time First (SRTF)**  
  - Starvation problem is handled
- ✅ **FCAI Scheduling Algorithm**  
  - A custom algorithm that adapts based on process priority, arrival time, and remaining burst time using a calculated "FCAI Factor"

---

## 🖥️ GUI Interface

Built with Java Swing for a user-friendly interface.

**Using the GUI, you can:**
- Enter the number of processes and context switching time
- Set Round Robin Time Quantum
- Input for each process:
  - 🏷 Name
  - 🎨 Color (for graphical display)
  - ⏱ Arrival Time
  - 🔁 Burst Time
  - 🔢 Priority
- View:
  - Gantt chart-style process execution
  - Waiting time, turnaround time for each process
  - Averages of both metrics