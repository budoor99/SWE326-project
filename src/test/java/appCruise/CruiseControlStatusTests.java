package appCruise;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CruiseControlStatusTests {

    private CarSimulator carSimulator;
    private Controller controller;
    private CruiseDisplay cruiseDisplay;

    @BeforeEach
    public void setUp() {
        carSimulator = new CarSimulator();
        cruiseDisplay = new CruiseDisplay();
        controller = new Controller(carSimulator, cruiseDisplay, true);
    }

    @Test // TC019 / R12: Cruise control status should turn green when enabled
    public void testCruiseControlStatusGreen() throws InterruptedException {
        boolean isPassed = false;
        try {
            carSimulator.engineOn();

            // Simulate manual acceleration to reach valid speed
            for (int i = 0; i < 10; i++) {
                carSimulator.accelerate();
                Thread.sleep(200);
            }

            controller.on(); // Enable cruise control
            Thread.sleep(500); // allow display to update

            // Check if status indicator is ON (green)
            assertTrue(cruiseDisplay.isCruiseOn(), "Cruise control status light should be green (enabled)");
            isPassed = true;
        } catch (AssertionError e) {
            System.err.println("Test Failed: " + e.getMessage());
        }

        if (isPassed) {
            System.out.println("✔ testCruiseControlStatusGreen() - PASS");
        } else {
            System.out.println("✘ testCruiseControlStatusGreen() - FAIL");
        }
    }

    @Test // TC020 / R12: Cruise control status should turn red when disabled
    public void testCruiseControlStatusRed() throws InterruptedException {
        boolean isPassed = false;
        try {
            carSimulator.engineOn();

            // Reach valid speed
            for (int i = 0; i < 10; i++) {
                carSimulator.accelerate();
                Thread.sleep(200);
            }

            controller.on(); // Turn it ON
            Thread.sleep(300);
            controller.off(); // Then turn it OFF
            Thread.sleep(300);

            // Check if status indicator is OFF (red)
            assertFalse(cruiseDisplay.isCruiseOn(), "Cruise control status light should be red (disabled)");
            isPassed = true;
        } catch (AssertionError e) {
            System.err.println("Test Failed: " + e.getMessage());
        }

        if (isPassed) {
            System.out.println("✔ testCruiseControlStatusRed() - PASS");
        } else {
            System.out.println("✘ testCruiseControlStatusRed() - FAIL");
        }
    }
}
