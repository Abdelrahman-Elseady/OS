# 🧠 Java Operating System Simulations

This repository features **three Java-based simulations** focused on key Operating System concepts. From CPU scheduling algorithms and a shell-like command interpreter to a realistic multithreaded parking system — each project demonstrates core OS behaviors using Java multithreading, synchronization, GUI (Swing), and file I/O.

---


### 🚦 1. CPU Scheduler Simulation (GUI)

**Description**:  
A graphical simulator for multiple CPU scheduling algorithms. Allows users to input processes with parameters and visualize how each scheduling strategy works.

#### ✅ Implemented Algorithms:
- **Non-Preemptive Priority Scheduling** (with context switching)
- **Non-Preemptive Shortest Job First (SJF)** — starvation-free
- **Shortest Remaining Time First (SRTF)** — starvation-free
- **FCAI Scheduling** — a custom dynamic algorithm that considers arrival time, priority, and remaining burst time

#### ✨ Features:
- Java **Swing GUI** for interactive input/output
- **Gantt chart visualization**
- Assign **custom colors** to processes
- Output: Waiting Time, Turnaround Time, Averages
- Handles starvation, idle CPU time, and context switching

#### 📥 Inputs:
- Number of Processes
- Context Switch Duration
- Round Robin Quantum (for FCAI)
- For each process: Name, Color, Arrival Time, Burst Time, Priority

#### 📤 Outputs:
- Execution Order (Gantt chart)
- Individual Waiting/Turnaround Times
- Average Metrics

---

### 🧾 2. Command Line Interpreter (Shell Simulator)

**Description**:  
A Java-based shell environment that mimics basic UNIX/Linux command behavior using custom classes and parsing logic.

#### ✅ Supported Commands:
- `cd`, `mkdir`, `rmdir`, `ls`, `touch`, `cp`, `rm`, `clear`, `pwd`, `echo`, `exit`

#### ✨ Features:
- Command tokenization and parsing
- Simulated **file system structure**
- Easily extendable via command interface


---


### 🚗 3. Multithreaded Parking System Simulation

This project simulates a smart parking system using Java multithreading and semaphores. It demonstrates how concurrency mechanisms can be used to manage shared resources such as parking spots and entry gates.

---

## 📋 Overview

Each car is represented as a thread that:
- Arrives at a specified time
- Enters through one of three gates (synchronized via semaphores)
- Parks for a specific duration (if a spot is available)
- Leaves the parking lot after parking

Semaphores are used to control:
- Number of available parking spots
- Access to each gate (only one car at a time per gate)
