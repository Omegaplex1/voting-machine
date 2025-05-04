/**
 * Get the failure status of devices or simulate their failures.
 */

public class Failure {

    /**
     * Return the failing state of the device
     * @param device the chosen device to check for failure.
     * @return returns the failure state of the device as a boolean
     */
    public static boolean failure(Device device) {return device.failure(); }

    /**
     * Fail a device for testing.
     * @param device the chosen device to fail.
     */
    public static void simulateFailure(Device device) { device.simulateFailure(); }
}
