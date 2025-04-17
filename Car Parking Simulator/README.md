# 🚗 Parking Lot Simulation in Java

This project simulates a multi-gate parking lot system using **Java multithreading** and **semaphores**. Cars arrive at gates, pass through them, and occupy parking spots for a specified duration.

## 📦 Features

- Simulates cars arriving at 3 different gates.
- Parking lot with **4 total spots** managed using a counting semaphore.
- Each gate is protected by a **binary semaphore** to ensure one car passes at a time.
- Cars read from an external input file (`input.txt`) defining:
  - Gate number
  - Car number
  - Arrival time (seconds)
  - Parking duration (seconds)