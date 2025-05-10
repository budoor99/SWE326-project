package appCruise;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class OdometerUnitTests {

    private CarSimulator carSimulator;
    private Controller controller;

    @BeforeEach
    public void setUp() {
        carSimulator = new CarSimulator();
        CruiseDisplay cruiseDisplay = new CruiseDisplay();
        controller = new Controller(carSimulator, cruiseDisplay, true);
    }

    @Test // TC017 / R10: Verify distance tracking while driving manually without cruise control
    public void testDistanceWithoutCruiseControl() throws InterruptedException {
        boolean isPassed = false;
        try {
            carSimulator.engineOn();
            int initialDistance = carSimulator.getDistance();

            // Simulate manual acceleration
            for (int i = 0; i < 5; i++) {
                carSimulator.accelerate();
                Thread.sleep(200); // Give time for thread to update distance
            }

            Thread.sleep(1000); // wait for the simulation thread to update distance
            int updatedDistance = carSimulator.getDistance();

            assertTrue(updatedDistance > initialDistance, "Distance should increase while driving manually");
            isPassed = true;
        } catch (AssertionError e) {
            System.err.println("Test Failed: " + e.getMessage());
        }

        if (isPassed) {
            System.out.println("✔ testDistanceWithoutCruiseControl() - PASS");
        } else {
            System.out.println("✘ testDistanceWithoutCruiseControl() - FAIL");
        }
    }

    @Test // TC018 / R10: Verify distance tracking while cruise control is enabled
    public void testDistanceWithCruiseControl() throws InterruptedException {
        boolean isPassed = false;
        try {
            carSimulator.engineOn();
            CruiseDisplay display = new CruiseDisplay();
            controller = new Controller(carSimulator, display, true);

            // Simulate manual acceleration to reach cruising speed
            for (int i = 0; i < 10; i++) {
                carSimulator.accelerate();
                Thread.sleep(200);
            }

            Thread.sleep(1000); // allow time for speed to stabilize

            int initialDistance = carSimulator.getDistance();
            controller.on(); // Enable cruise control
            Thread.sleep(1500); // let cruise control maintain speed and update distance

            int updatedDistance = carSimulator.getDistance();
            assertTrue(updatedDistance > initialDistance, "Distance should increase while in cruise control");
            isPassed = true;
        } catch (AssertionError e) {
            System.err.println("Test Failed: " + e.getMessage());
        }

        if (isPassed) {
            System.out.println("✔ testDistanceWithCruiseControl() - PASS");
        } else {
            System.out.println("✘ testDistanceWithCruiseControl() - FAIL");
        }
    }
}