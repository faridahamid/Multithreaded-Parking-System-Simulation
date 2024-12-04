import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

class parking_slot {
    private static final int total_spots = 4;
    private static final semaphore parking_spots = new semaphore(total_spots);
    private static AtomicInteger cars_Parking = new AtomicInteger(0);
    private static AtomicInteger total_cars_served = new AtomicInteger(0);
    private static AtomicInteger[] gate_served = {new AtomicInteger(0), new AtomicInteger(0), new AtomicInteger(0)};

    static class semaphore {
        private final int permits;
        private volatile int availablePermits;

        public semaphore(int permits) {
            this.permits = permits;
            this.availablePermits = permits;
        }

        public synchronized void acquire() throws InterruptedException {
            while (availablePermits == 0) {
                wait();
            }
            availablePermits--;
        }

        public synchronized boolean tryAcquire() {
            if (availablePermits > 0) {
                availablePermits--;
                return true;
            }
            return false;
        }

        public synchronized void release() {
            availablePermits++;
            notify();
        }
    }

    static class Car implements Runnable {
        private final String gate;
        private final int car_id;
        private final int arrival_time;
        private final int parking_duration;

        public Car(String gate, int carId, int arrivalTime, int parkingDuration) {
            this.gate = gate;
            this.car_id = carId;
            this.arrival_time = arrivalTime;
            this.parking_duration = parkingDuration;
        }

        @Override
        public void run() {
            try {
                Thread.sleep(arrival_time * 1000);
                System.out.println("Car " + car_id + " from " + gate + " arrived at time " + arrival_time);

                long waitStart = System.currentTimeMillis();
                if (!parking_spots.tryAcquire()) {
                    System.out.println("Car " + car_id + " from " + gate + " waiting for a spot.");
                    parking_spots.acquire();
                }
                long wait_time = (System.currentTimeMillis() - waitStart) / 1000;

                synchronized (parking_slot.class) {
                    cars_Parking.incrementAndGet();
                    total_cars_served.incrementAndGet();
                    gate_served[Integer.parseInt(gate.split(" ")[1]) - 1].incrementAndGet();
                    if (wait_time > 0) {
                        System.out.println("Car " + car_id + " from " + gate + " parked after waiting for " + wait_time +
                                " units of time. (Parking Status: " + cars_Parking.get() + " spots occupied)");
                    } else {
                        System.out.println("Car " + car_id + " from " + gate + " parked. (Parking Status: " + cars_Parking.get() + " spots occupied)");
                    }
                }

                Thread.sleep(parking_duration * 1000);

                synchronized (parking_slot.class) {
                    cars_Parking.decrementAndGet();
                    System.out.println("Car " + car_id + " from " + gate + " left after " + parking_duration +
                            " units of time. (Parking Status: " + cars_Parking.get() + " spots occupied)");
                }

                parking_spots.release();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        try (BufferedReader reader = new BufferedReader(new FileReader("input.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(", ");
                String gate = parts[0];
                int carId = Integer.parseInt(parts[1].split(" ")[1]);
                int arrivalTime = Integer.parseInt(parts[2].split(" ")[1]);
                int parkingDuration = Integer.parseInt(parts[3].split(" ")[1]);
                new Thread(new Car(gate, carId, arrivalTime, parkingDuration)).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            Thread.sleep(60000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        System.out.println("\nTotal Cars Served: " + total_cars_served.get());
        System.out.println("Current Cars in Parking: " + cars_Parking.get());
        System.out.println("Details:");
        System.out.println("- Gate 1 served " + gate_served[0].get() + " cars.");
        System.out.println("- Gate 2 served " + gate_served[1].get() + " cars.");
        System.out.println("- Gate 3 served " + gate_served[2].get() + " cars.");
    }
}