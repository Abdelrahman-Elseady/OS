import java.util.concurrent.Semaphore;

public class Car extends Thread {
    private final int gateNumber;
    private final int carNumber;
    private final int arrivalTime;
    private final int parkingDuration;
    private final Semaphore parkingSemaphore;
    private final Semaphore gateSemaphore;

    public Car(int gateNumber, int carNumber, int arrivalTime, int parkingDuration, Semaphore parkingSemaphore, Semaphore gateSemaphore) {
        this.gateNumber = gateNumber;
        this.carNumber = carNumber;
        this.arrivalTime = arrivalTime;
        this.parkingDuration = parkingDuration;
        this.parkingSemaphore = parkingSemaphore;
        this.gateSemaphore = gateSemaphore;
    }

    @Override
    public void run() {
        try {
            // Simulate arrival delay
            Thread.sleep(arrivalTime * 1000);

            // Acquire gate semaphore
            gateSemaphore.acquire();
            System.out.println("Car " + carNumber + " is passing through Gate " + gateNumber);
            gateSemaphore.release();

            // Acquire parking spot
            parkingSemaphore.acquire();
            System.out.println("Car " + carNumber + " is parking for " + parkingDuration + " seconds.");
            Thread.sleep(parkingDuration * 1000);
            System.out.println("Car " + carNumber + " is leaving.");

            // Release parking spot
            parkingSemaphore.release();
        } catch (InterruptedException e) {
            System.out.println("Car " + carNumber + " interrupted: " + e.getMessage());
        }
    }
}
