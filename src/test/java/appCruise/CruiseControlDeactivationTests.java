package appCruise;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CruiseControlDeactivationTests {

    private CarSimulator carSimulator;
    private Controller controller;
    private CruiseDisplay cruiseDisplay;

    @BeforeEach
    public void setUp() {
        carSimulator = new CarSimulator();
        cruiseDisplay = new CruiseDisplay();
        controller = new Controller(carSimulator, cruiseDisplay, true);
    }

    @Test // TC005 / R4: Cruise control turns off when brake is pressed
    public void testCruiseOffWhenBrake() throws InterruptedException {
        carSimulator.engineOn();
        carSimulator.setThrottle(6.0); // accelerate to valid speed
        Thread.sleep(1000); // allow speed to increase

        controller.on(); // enable cruise control
        Thread.sleep(300);
        controller.brake(); // apply brake
        Thread.sleep(300);

        assertFalse(cruiseDisplay.isCruiseOn(), "Cruise control should deactivate when braking");
        System.out.println("✔ testCruiseOffWhenBrake() - PASS");
    }

    @Test // TC006 / R3: Cruise control turns off when accelerator is pressed
    public void testCruiseOffWhenAccelerator() throws InterruptedException {
        carSimulator.engineOn();
        carSimulator.setThrottle(6.0); // accelerate to valid speed
        Thread.sleep(1000); // allow speed to increase

        controller.on(); // enable cruise control
        Thread.sleep(300);
        controller.accelerator(); // simulate manual acceleration
        Thread.sleep(300);

        assertFalse(cruiseDisplay.isCruiseOn(), "Cruise control should deactivate when accelerating");
        System.out.println("✔ testCruiseOffWhenAccelerator() - PASS");
    }
}
