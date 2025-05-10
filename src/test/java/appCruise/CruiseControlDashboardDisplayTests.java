package appCruise;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CruiseControlDashboardDisplayTests {

    private CarSimulator carSimulator;
    private Controller controller;
    private CruiseDisplay cruiseDisplay;

    @BeforeEach
    public void setUp() {
        carSimulator = new CarSimulator();
        cruiseDisplay = new CruiseDisplay();
        controller = new Controller(carSimulator, cruiseDisplay, true);
    }

    @Test // TC015 / R6: Dashboard should show correct speed when cruise control is on
    public void testDashboardSpeedOnCruise() throws InterruptedException {
        carSimulator.engineOn();
        carSimulator.setThrottle(6.0);
        Thread.sleep(600); // allow speed to increase
        controller.on();

        int actualSpeed = carSimulator.getSpeed();
        int displayedCruiseSpeed = cruiseDisplay.getRecordedSpeed();

        assertEquals(actualSpeed, displayedCruiseSpeed,
                "Dashboard should match cruise speed");
        System.out.println("✔ testDashboardSpeedOnCruise() - PASS");
    }

    @Test // TC016 / R7: Dashboard should show resumed cruise speed after resume
    public void testDashboardSpeedOnResume() throws InterruptedException {
        carSimulator.engineOn();
        carSimulator.setThrottle(6.0);
        Thread.sleep(600); // allow speed to increase
        controller.on();
        controller.brake(); // standby
        controller.resume();
        Thread.sleep(300); // allow resumption to take effect

        int actualSpeed = carSimulator.getSpeed();
        int displayedCruiseSpeed = cruiseDisplay.getRecordedSpeed();

        assertEquals(actualSpeed, displayedCruiseSpeed,
                "Dashboard should match resumed cruise speed");
        System.out.println("✔ testDashboardSpeedOnResume() - PASS");
    }
}

