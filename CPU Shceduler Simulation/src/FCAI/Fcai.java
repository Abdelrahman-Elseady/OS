package FCAI;

import java.util.*;

import static java.lang.Integer.MAX_VALUE;
import static java.lang.Integer.MIN_VALUE;

class Process
{
    String name;
    int arrivalTime;
    int priority;
    int burstTime;
    int timeQuantum;
    int fcaiFactor;
    boolean entered;

    public Process(String name, int arrivalTime, int priority, int burstTime, int timeQuantum)
    {
        this.name = name;
        this.arrivalTime = arrivalTime;
        this.priority = priority;
        this.burstTime = burstTime;
        this.timeQuantum = timeQuantum;
        this.fcaiFactor = 0;
        this.entered = false;
    }

    public void calculateFcaiFactor(int maxArrivalTime, int maxBurstTime)
    {
        int v1 = Math.ceilDiv(arrivalTime, maxBurstTime);
        int v2 = Math.ceilDiv(burstTime, maxBurstTime);
        this.fcaiFactor = (10 - this.priority) + v1 + v2;
    }
}

class Fcai
{
    int maxArrivalTime = MIN_VALUE;
    int maxBurstTime = MIN_VALUE;

    private List<Process> readInput()
    {
        List<Process> processes = new ArrayList<>();

        Scanner input = new Scanner(System.in);
        System.out.println("Enter number of processes:");
        int numProcesses = input.nextInt();
        for (int i = 0; i < numProcesses; i++)
        {
            System.out.println("Enter the process name:");
            String processName = input.next();
            System.out.println("Enter the Arrival Time of the process:");
            int arrivalTime = input.nextInt();
            System.out.println("Enter the process Priority:");
            int processPriority = input.nextInt();
            System.out.println("Enter the burst Time of the process:");
            int burstTime = input.nextInt();
            System.out.println("Enter the time quantum of the process:");
            int timeQuantum = input.nextInt();

            processes.add(new Process(processName, arrivalTime, processPriority, burstTime, timeQuantum));
        }

        for (Process process : processes)
        {
            int scaledArrivalTime = (int) Math.ceil((double) process.arrivalTime / 10);
            int scaledBurstTime = (int) Math.ceil((double) process.burstTime / 10);
            maxArrivalTime = Math.max(maxArrivalTime, scaledArrivalTime);
            maxBurstTime = Math.max(maxBurstTime, scaledBurstTime);
        }

        if (maxBurstTime <= 0 || maxArrivalTime <= 0)
        {
            System.out.println("Invalid input. Burst time and arrival time cannot be zero or negative.");
            System.exit(0);
        }

        processes.sort(Comparator.comparingInt(p -> p.arrivalTime));

        for (Process process : processes)
        {
            process.calculateFcaiFactor(maxArrivalTime, maxBurstTime);
        }

        return processes;
    }


    private void checkAndAddProcesses(List<Process> processes, Queue<Process> inQueue, int currTime)
    {
        Iterator<Process> iterator = processes.iterator();
        while (iterator.hasNext())
        {
            Process process = iterator.next();
            if (process.arrivalTime <= currTime)
            {
                inQueue.add(process);
                iterator.remove();
            } else
            {
                break;
            }
        }
    }


    public void fcaiScheduler()
    {
        int units = 0;
        int currTime = 0;
        List<Process> processes = readInput();
        Queue<Process> inQueue = new LinkedList<>();
        inQueue.add(processes.remove(0));
        Process current = inQueue.peek();
        inQueue.remove(current);

        while (!processes.isEmpty() || !inQueue.isEmpty())
        {
            units = 0;


            if (current == null)
            {
                System.out.println("no process");
                break;
            }
            if (current.entered)
            {
                System.out.println(current.name + ", completes execution, ");
            } else
            {
                System.out.println(current.name + ", starts execution, ");
            }
            int quantum = current.timeQuantum;
            while ((double) units / quantum < 0.40 && current.burstTime != 0)
            {
                current.entered = true;
                current.burstTime--;
                current.timeQuantum--;
                units++;
                currTime++;
                checkAndAddProcesses(processes, inQueue, currTime);
            }

            if (current.burstTime == 0)
            {
                System.out.println(current.name + " runs for " + units + " and terminated");
                if (inQueue.size() == 0)
                {
                    System.out.println("Finished executing......");
                    System.exit(0);
                }
                current = inQueue.peek();
                inQueue.remove(current);
                continue;
            }

            current.calculateFcaiFactor(maxArrivalTime, maxBurstTime);

            Process potential = null;
            int minFactor = MAX_VALUE;
            for (Process process : inQueue)
            {
                if (process.fcaiFactor < minFactor)
                {
                    minFactor = process.fcaiFactor;
                    potential = process;
                }
            }

            if (potential != null && potential.fcaiFactor < current.fcaiFactor)
            {
                current.timeQuantum = (units + current.timeQuantum) + current.timeQuantum;
                System.out.println(current.name + " runs for  " + units);
                System.out.println("remain burst for  " + current.burstTime);
                System.out.println(potential.name + " preempted " + current.name);
                inQueue.add(current);
                inQueue.remove(potential);
                current = potential;
                continue;
            }

            while (current.burstTime > 0 && current.timeQuantum > 0)
            {
                current.calculateFcaiFactor(maxArrivalTime, maxBurstTime);

                minFactor = MAX_VALUE;
                for (Process process : inQueue)
                {
                    if (process.fcaiFactor < minFactor)
                    {
                        minFactor = process.fcaiFactor;
                        potential = process;
                    }
                }

                if (potential != null && potential.fcaiFactor < current.fcaiFactor)
                {
                    current.timeQuantum = (current.timeQuantum + units) + current.timeQuantum;
                    System.out.println(current.name + " runs for  " + units);
                    System.out.println("remain burst for  " + current.burstTime);
                    System.out.println(potential.name + " preempted " + current.name);
                    inQueue.add(current);
                    inQueue.remove(potential);
                    current = potential;
                    checkAndAddProcesses(processes, inQueue, currTime);
                    break;
                }

                current.burstTime--;
                current.timeQuantum--;
                units++;
                currTime++;
                checkAndAddProcesses(processes, inQueue, currTime);
            }

            if (current.burstTime <= 0)
            {
                System.out.println(current.name + " runs for " + units + " and terminated");
                if (inQueue.size() == 0)
                {
                    System.out.println("Finished executing......");
                    System.exit(0);
                }
                current = inQueue.peek();
                inQueue.remove(current);
            } else if (current.timeQuantum <= 0)
            {
                System.out.println(current.name + " runs for " + units + " and finished its quantum");
                current.timeQuantum = units + 2; // Replenish quantum
                inQueue.add(current);
                current = inQueue.peek();
                inQueue.remove(current);
            }
        }

        units = 0;

        while (current.burstTime != 0)
        {
            current.burstTime--;
            current.timeQuantum--;
            units++;
            currTime++;
        }
        System.out.println(current.name + " runs for  " + units);
        System.out.println("Finished executing......");
        System.exit(0);
    }
}