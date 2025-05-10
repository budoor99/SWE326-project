package appCruise;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CruiseControlUnitTests {

    private CarSimulator carSimulator;
    private Controller controller;

    @BeforeEach
    public void setUp() {
        carSimulator = new CarSimulator();
        CruiseDisplay cruiseDisplay = new CruiseDisplay();
        controller = new Controller(carSimulator, cruiseDisplay, true);
    }

    @Test // TC001 / R1: Test if the engine can be successfully started when the "Engine ON" button is pressed.
    public void testEngineOn() {
        boolean isPassed = false;  // flag
        try {
            carSimulator.engineOn();
            assertTrue(carSimulator.isEngineRunning(), "Engine should be running");
            assertEquals(0, carSimulator.getSpeed(), "Engine should start with speed 0");
            isPassed = true;
        } catch (AssertionError e) {
            System.err.println("Test Failed: " + e.getMessage());
        }
        // print message
        if (isPassed) {
            System.out.println("✔ testEngineOn() - PASS");
        } else {
            System.out.println("✘ testEngineOn() - FAIL");
        }
    }

    @Test // TC002 / R5: Check if the engine stops when the "Engine OFF" button is pressed.
    public void testEngineOff() {
        boolean isPassed = false;  // flag
        try {
            carSimulator.engineOn(); // start the engine
            carSimulator.engineOff(); // stop the engine

            // check engine state
            assertFalse(carSimulator.isEngineRunning(), "Engine should be stopped");
            assertEquals(0, carSimulator.getSpeed(), "Speed should be 0 after engine off");
            isPassed = true;
        } catch (AssertionError e) {
            System.err.println("Test Failed: " + e.getMessage());
        }
        // print message
        if (isPassed) {
            System.out.println("✔ testEngineOff() - PASS");
        } else {
            System.out.println("✘ testEngineOff() - FAIL");
        }
    }

}
