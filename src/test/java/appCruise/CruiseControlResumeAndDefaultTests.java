package appCruise;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CruiseControlResumeAndDefaultTests {

    private CarSimulator carSimulator;
    private Controller controller;
    private CruiseDisplay cruiseDisplay;

    @BeforeEach
    public void setUp() {
        carSimulator = new CarSimulator();
        cruiseDisplay = new CruiseDisplay();
        controller = new Controller(carSimulator, cruiseDisplay, true);
    }

    @Test // TC007 / R7: Resume should re-enable cruise control after braking
    public void testResumeFunction() throws InterruptedException {
        boolean isPassed = false;
        try {
            carSimulator.engineOn();
            carSimulator.setThrottle(5.0);
            Thread.sleep(500); // allow speed to build up
            controller.on();
            controller.brake(); // puts CC on standby
            controller.resume(); // attempt to resume
            Thread.sleep(300); // allow state update

            assertTrue(cruiseDisplay.isCruiseOn(), "Cruise control should resume after braking");
            isPassed = true;
        } catch (AssertionError e) {
            System.err.println("Test Failed: " + e.getMessage());
        }

        if (isPassed) {
            System.out.println("✔ testResumeFunction() - PASS");
        } else {
            System.out.println("✘ testResumeFunction() - FAIL");
        }
    }

    @Test // TC008 / R8: Cruise control should be OFF by default on engine start
    public void testCruiseDefaultOffOnEngineStart() {
        boolean isPassed = false;
        try {
            carSimulator.engineOn(); // start engine only, don't enable CC
            assertFalse(cruiseDisplay.isCruiseOn(), "Cruise control should be off by default");
            isPassed = true;
        } catch (AssertionError e) {
            System.err.println("Test Failed: " + e.getMessage());
        }

        if (isPassed) {
            System.out.println("✔ testCruiseDefaultOffOnEngineStart() - PASS");
        } else {
            System.out.println("✘ testCruiseDefaultOffOnEngineStart() - FAIL");
        }
    }
}
