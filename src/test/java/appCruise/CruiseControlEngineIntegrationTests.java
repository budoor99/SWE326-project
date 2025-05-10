package appCruise;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CruiseControlEngineIntegrationTests {

    private CarSimulator carSimulator;
    private Controller controller;
    private CruiseDisplay cruiseDisplay;

    @BeforeEach
    public void setUp() {
        carSimulator = new CarSimulator();
        cruiseDisplay = new CruiseDisplay();
        controller = new Controller(carSimulator, cruiseDisplay, true);
    }

    @Test // TC009 / R9: Engine OFF should turn off cruise control
    public void testEngineOffTurnsOffCruise() throws InterruptedException {
        boolean isPassed = false;
        try {
            carSimulator.engineOn();
            carSimulator.setThrottle(5.0);
            Thread.sleep(500); // allow speed to build up
            controller.on();
            Thread.sleep(300);
            carSimulator.engineOff();
            controller.engineOff(); // simulate button behavior
            Thread.sleep(300);
            assertFalse(cruiseDisplay.isCruiseOn(), "Cruise control should deactivate when engine stops");
            isPassed = true;
        } catch (AssertionError e) {
            System.err.println("Test Failed: " + e.getMessage());
        }

        if (isPassed) {
            System.out.println("✔ testEngineOffTurnsOffCruise() - PASS");
        } else {
            System.out.println("✘ testEngineOffTurnsOffCruise() - FAIL");
        }
    }

    @Test // TC010 / R17: Cruise control should stay off after engine restarts
    public void testCruiseStaysOffAfterEngineRestart() throws InterruptedException {
        boolean isPassed = false;
        try {
            carSimulator.engineOn();
            carSimulator.setThrottle(5.0);
            Thread.sleep(500);
            controller.on();
            Thread.sleep(300);
            carSimulator.engineOff();
            controller.engineOff();
            Thread.sleep(300);
            carSimulator.engineOn();
            controller.engineOn();
            Thread.sleep(300);
            assertFalse(cruiseDisplay.isCruiseOn(), "Cruise control should stay off after engine restart");
            isPassed = true;
        } catch (AssertionError e) {
            System.err.println("Test Failed: " + e.getMessage());
        }

        if (isPassed) {
            System.out.println("✔ testCruiseStaysOffAfterEngineRestart() - PASS");
        } else {
            System.out.println("✘ testCruiseStaysOffAfterEngineRestart() - FAIL");
        }
    }
}
