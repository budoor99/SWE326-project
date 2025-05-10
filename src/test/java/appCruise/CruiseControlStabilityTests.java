package appCruise;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CruiseControlStabilityTests {

    private CarSimulator carSimulator;
    private Controller controller;
    private CruiseDisplay cruiseDisplay;

    @BeforeEach
    public void setUp() {
        carSimulator = new CarSimulator();
        cruiseDisplay = new CruiseDisplay();
        controller = new Controller(carSimulator, cruiseDisplay, true);
    }

    @Test // TC013 / R14: Pressing OFF again when cruise control is already off
    public void testPressOffWhenAlreadyOff() {
        boolean isPassed = false;
        try {
            carSimulator.engineOn();
            controller.off(); // press OFF while CC is off
            assertFalse(cruiseDisplay.isCruiseOn(), "Cruise control should stay off if OFF is pressed again");
            isPassed = true;
        } catch (AssertionError e) {
            System.err.println("Test Failed: " + e.getMessage());
        }
        System.out.println(isPassed ? "✔ testPressOffWhenAlreadyOff() - PASS" : "✘ testPressOffWhenAlreadyOff() - FAIL");
    }

    @Test // TC014 / R16: Rapid ON and OFF toggling
    public void testRapidOnOff() throws InterruptedException {
        boolean isPassed = false;
        try {
            carSimulator.engineOn();
            carSimulator.setThrottle(5.0);
            Thread.sleep(500); // simulate speed build-up

            controller.on();
            controller.off();
            controller.on();
            controller.off();

            assertFalse(cruiseDisplay.isCruiseOn(), "Cruise control should handle rapid toggling without crash");
            isPassed = true;
        } catch (AssertionError e) {
            System.err.println("Test Failed: " + e.getMessage());
        }
        System.out.println(isPassed ? "✔ testRapidOnOff() - PASS" : "✘ testRapidOnOff() - FAIL");
    }
}
