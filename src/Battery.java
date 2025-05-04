import java.io.Serializable;

/**
 * Battery.java
 * <p>
 * A battery device simulator that shows whether the battery is on.
 */
public class Battery extends Device implements Serializable {
    // Determines whether there is battery life left
    private boolean isOn = true;

    /**
     * Battery failure: Determines if there is battery life left or if there was an error of any type
     * @return the failure state of the battery. True if it has failed, false otherwise.
     */
    @Override
    public boolean failure() { return !isOn; }

    @Override
    public void simulateFailure() {
        this.isOn = false;
        System.err.println("Power failure.");
    }

    @Override
    public String handleCommand(Command cmd) {
        if (cmd.deviceType() != DeviceType.BATTERY) {
            throw new IllegalArgumentException("Invalid device type command.");
        }

        switch (cmd.command()) {
            case "simulateFailure": {
                this.simulateFailure();
                break;
            }
            default: {
                return "Invalid command for " + cmd.deviceType() + ": " + cmd.command() + ".";
            }
        }

        return "OK";
    }
}
