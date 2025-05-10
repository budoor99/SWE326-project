package appCruise;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CruiseControlAllTests {

    private CarSimulator carSimulator;
    private Controller controller;
    private CruiseDisplay cruiseDisplay;

    @BeforeEach
    public void setUp() {
        carSimulator = new CarSimulator();
        cruiseDisplay = new CruiseDisplay();
        controller = new Controller(carSimulator, cruiseDisplay, true);
    }

    @Test // TC001 / R1
    public void testEngineOn() {
        carSimulator.engineOn();
        assertTrue(carSimulator.isEngineRunning());
        assertEquals(0, carSimulator.getSpeed());
    }

    @Test // TC002 / R5
    public void testEngineOff() {
        carSimulator.engineOn();
        carSimulator.engineOff();
        assertFalse(carSimulator.isEngineRunning());
        assertEquals(0, carSimulator.getSpeed());
    }

    @Test // TC003 / R1, R2
    public void testCruiseOnAtValidSpeed() throws InterruptedException {
        carSimulator.engineOn();
        carSimulator.setThrottle(6.0);
        Thread.sleep(1000);
        controller.on();
        Thread.sleep(300);
        assertEquals(Controller.CRUISING, controller.getState());
    }

    @Test // TC004 / R15
    public void testCruiseFailsAtLowSpeed() throws InterruptedException {
        carSimulator.engineOn();
        carSimulator.setThrottle(1.0);
        Thread.sleep(1000);
        controller.on();
        Thread.sleep(300);
        assertNotEquals(Controller.CRUISING, controller.getState());
    }

    @Test // TC005 / R4
    public void testCruiseOffWhenBrake() throws InterruptedException {
        carSimulator.engineOn();
        carSimulator.setThrottle(6.0);
        Thread.sleep(1000);
        controller.on();
        Thread.sleep(300);
        controller.brake();
        Thread.sleep(300);
        assertFalse(cruiseDisplay.isCruiseOn());
    }

    @Test // TC006 / R3
    public void testCruiseOffWhenAccelerator() throws InterruptedException {
        carSimulator.engineOn();
        carSimulator.setThrottle(6.0);
        Thread.sleep(1000);
        controller.on();
        Thread.sleep(300);
        controller.accelerator();
        Thread.sleep(300);
        assertFalse(cruiseDisplay.isCruiseOn());
    }

    @Test // TC007 / R7
    public void testResumeFunction() throws InterruptedException {
        carSimulator.engineOn();
        carSimulator.setThrottle(5.0);
        Thread.sleep(500);
        controller.on();
        controller.brake();
        controller.resume();
        Thread.sleep(300);
        assertTrue(cruiseDisplay.isCruiseOn());
    }

    @Test // TC008 / R8
    public void testCruiseDefaultOffOnEngineStart() {
        carSimulator.engineOn();
        assertFalse(cruiseDisplay.isCruiseOn());
    }

    @Test // TC009 / R9
    public void testEngineOffTurnsOffCruise() throws InterruptedException {
        carSimulator.engineOn();
        carSimulator.setThrottle(5.0);
        Thread.sleep(500);
        controller.on();
        Thread.sleep(300);
        carSimulator.engineOff();
        controller.engineOff();
        Thread.sleep(300);
        assertFalse(cruiseDisplay.isCruiseOn());
    }

    @Test // TC010 / R17
    public void testCruiseStaysOffAfterEngineRestart() throws InterruptedException {
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
        assertFalse(cruiseDisplay.isCruiseOn());
    }

    @Test // TC011 / R11
    public void testActivateCruiseWithEngineOff() {
        carSimulator.engineOff();
        controller.on();
        assertFalse(cruiseDisplay.isCruiseOn());
    }

    @Test // TC012 / R13
    public void testPressOnWhenAlreadyOn() throws InterruptedException {
        carSimulator.engineOn();
        carSimulator.setThrottle(5.0);
        Thread.sleep(400);
        controller.on();
        boolean first = cruiseDisplay.isCruiseOn();
        controller.on();
        boolean second = cruiseDisplay.isCruiseOn();
        assertTrue(first && second);
    }

    @Test // TC013 / R14
    public void testPressOffWhenAlreadyOff() {
        carSimulator.engineOn();
        controller.off();
        assertFalse(cruiseDisplay.isCruiseOn());
    }

    @Test // TC014 / R16
    public void testRapidOnOff() throws InterruptedException {
        carSimulator.engineOn();
        carSimulator.setThrottle(5.0);
        Thread.sleep(500);
        controller.on();
        controller.off();
        controller.on();
        controller.off();
        assertFalse(cruiseDisplay.isCruiseOn());
    }

    @Test // TC015 / R6
    public void testDashboardSpeedOnCruise() throws InterruptedException {
        carSimulator.engineOn();
        carSimulator.setThrottle(6.0);
        Thread.sleep(600);
        controller.on();
        assertEquals(carSimulator.getSpeed(), cruiseDisplay.getRecordedSpeed());
    }

    @Test // TC016 / R7
    public void testDashboardSpeedOnResume() throws InterruptedException {
        carSimulator.engineOn();
        carSimulator.setThrottle(6.0);
        Thread.sleep(600);
        controller.on();
        controller.brake();
        controller.resume();
        Thread.sleep(300);
        assertEquals(carSimulator.getSpeed(), cruiseDisplay.getRecordedSpeed());
    }

    @Test // TC017 / R10
    public void testDistanceWithoutCruiseControl() throws InterruptedException {
        carSimulator.engineOn();
        int initialDistance = carSimulator.getDistance();
        for (int i = 0; i < 5; i++) {
            carSimulator.accelerate();
            Thread.sleep(200);
        }
        Thread.sleep(1000);
        int updatedDistance = carSimulator.getDistance();
        assertTrue(updatedDistance > initialDistance);
    }

    @Test // TC018 / R10
    public void testDistanceWithCruiseControl() throws InterruptedException {
        carSimulator.engineOn();
        for (int i = 0; i < 10; i++) {
            carSimulator.accelerate();
            Thread.sleep(200);
        }
        Thread.sleep(1000);
        int initialDistance = carSimulator.getDistance();
        controller.on();
        Thread.sleep(1500);
        int updatedDistance = carSimulator.getDistance();
        assertTrue(updatedDistance > initialDistance);
    }

    @Test // TC019 / R12
    public void testCruiseControlStatusGreen() throws InterruptedException {
        carSimulator.engineOn();
        for (int i = 0; i < 10; i++) {
            carSimulator.accelerate();
            Thread.sleep(200);
        }
        controller.on();
        Thread.sleep(500);
        assertTrue(cruiseDisplay.isCruiseOn());
    }

    @Test // TC020 / R12
    public void testCruiseControlStatusRed() throws InterruptedException {
        carSimulator.engineOn();
        for (int i = 0; i < 10; i++) {
            carSimulator.accelerate();
            Thread.sleep(200);
        }
        controller.on();
        Thread.sleep(300);
        controller.off();
        Thread.sleep(300);
        assertFalse(cruiseDisplay.isCruiseOn());
    }
}
