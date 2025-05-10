package appCruise;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CruiseControlActivationTests {

    private CarSimulator carSimulator;
    private Controller controller;
    private CruiseDisplay cruiseDisplay;

    @BeforeEach
    public void setUp() {
        carSimulator = new CarSimulator();
        cruiseDisplay = new CruiseDisplay();
        controller = new Controller(carSimulator, cruiseDisplay, true);
    }

    @Test // TC003 / R1, R2: Activate cruise control at valid speed
    public void testCruiseOnAtValidSpeed() throws InterruptedException {
        carSimulator.engineOn();
        carSimulator.setThrottle(6.0); // Build speed
        Thread.sleep(1000); // Give time for run() to update speed

        controller.on(); // Try to activate cruise control
        Thread.sleep(300);

        assertEquals(Controller.CRUISING, controller.getState(), "Cruise control should activate at valid speed");
        System.out.println("✔ testCruiseOnAtValidSpeed() - PASS");
    }

    @Test // TC004 / R15: Should not activate cruise control at low speed
    public void testCruiseFailsAtLowSpeed() throws InterruptedException {
        carSimulator.engineOn();
        carSimulator.setThrottle(1.0); // Low speed throttle
        Thread.sleep(1000); // Give time for run() to update speed

        controller.on(); // Try to activate cruise control
        Thread.sleep(300);

        assertNotEquals(Controller.CRUISING, controller.getState(), "Cruise control should NOT activate at low speed");
        System.out.println("✔ testCruiseFailsAtLowSpeed() - PASS");
    }
}
