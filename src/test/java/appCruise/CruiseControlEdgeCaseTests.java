package appCruise;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CruiseControlEdgeCaseTests {

    private CarSimulator carSimulator;
    private Controller controller;
    private CruiseDisplay cruiseDisplay;

    @BeforeEach
    public void setUp() {
        carSimulator = new CarSimulator();
        cruiseDisplay = new CruiseDisplay();
        controller = new Controller(carSimulator, cruiseDisplay, true);
    }

    @Test // TC011 / R11: Ensure cruise control does not activate when engine is off
    public void testActivateCruiseWithEngineOff() {
        boolean isPassed = false;
        try {
            carSimulator.engineOff(); // ensure engine is off
            controller.on(); // try activating cruise
            assertFalse(cruiseDisplay.isCruiseOn(), "Cruise control should not activate with engine off");
            isPassed = true;
        } catch (AssertionError e) {
            System.err.println("Test Failed: " + e.getMessage());
        }
        System.out.println(isPassed ? "✔ testActivateCruiseWithEngineOff() - PASS" : "✘ testActivateCruiseWithEngineOff() - FAIL");
    }

    @Test // TC012 / R13: Pressing ON when already ON should have no effect
    public void testPressOnWhenAlreadyOn() throws InterruptedException {
        boolean isPassed = false;
        try {
            carSimulator.engineOn();
            carSimulator.setThrottle(5.0);
            Thread.sleep(400); // let speed build up
            controller.on(); // first ON
            boolean statusAfterFirstOn = cruiseDisplay.isCruiseOn();

            controller.on(); // second ON
            boolean statusAfterSecondOn = cruiseDisplay.isCruiseOn();

            assertTrue(statusAfterFirstOn && statusAfterSecondOn, "Cruise control should stay on if ON is pressed again");
            isPassed = true;
        } catch (AssertionError e) {
            System.err.println("Test Failed: " + e.getMessage());
        }
        System.out.println(isPassed ? "✔ testPressOnWhenAlreadyOn() - PASS" : "✘ testPressOnWhenAlreadyOn() - FAIL");
    }


}
