import java.util.ArrayList;
import java.util.concurrent.Semaphore;

public class Main {
    public static void main(String[] args) {
        Semaphore parkingSemaphore = new Semaphore(4); // Parking lot with 4 spots

        // Semaphores for gates
        Semaphore gate1Semaphore = new Semaphore(1);
        Semaphore gate2Semaphore = new Semaphore(1);
        Semaphore gate3Semaphore = new Semaphore(1);

        ArrayList<Car> cars = new ArrayList<>();
        ArrayList<String[]> parsedData = InputParser.parseFile("input.txt");

        int[] carsServedByGate = new int[3]; // To track cars served by each gate

        // Create Car objects
        for (String[] data : parsedData) {
            int gateNumber = Integer.parseInt(data[0].replace("Gate ", "").trim());
            int carNumber = Integer.parseInt(data[1].replace("Car ", "").trim());
            int arrivalTime = Integer.parseInt(data[2].replace("Arrive ", "").trim());
            int parkingDuration = Integer.parseInt(data[3].replace("Parks ", "").trim());

            Semaphore gateSemaphore = switch (gateNumber) {
                case 1 -> gate1Semaphore;
                case 2 -> gate2Semaphore;
                case 3 -> gate3Semaphore;
                default -> throw new IllegalArgumentException("Invalid gate number");
            };

            carsServedByGate[gateNumber - 1]++; // Increment count for the gate
            Car car = new Car(gateNumber, carNumber, arrivalTime, parkingDuration, parkingSemaphore, gateSemaphore);
            cars.add(car);
        }

        // Start all car threads
        for (Car car : cars) {
            car.start();
        }

        // Wait for all threads to complete
        for (Car car : cars) {
            try {
                car.join();
            } catch (InterruptedException e) {
                System.out.println("Error joining threads: " + e.getMessage());
            }
        }

        // Display summary
        System.out.println("Simulation complete.");
        System.out.println("Total Cars Served: " + parsedData.size());
        System.out.println("Current Cars in Parking: 0"); // All cars have left at the end
        System.out.println("Details:");
        for (int i = 0; i < carsServedByGate.length; i++) {
            System.out.println("- Gate " + (i + 1) + " served " + carsServedByGate[i] + " cars.");
        }
    }
}
