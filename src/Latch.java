import java.io.Serializable;

/**
 * Latch.java
 * <p>
 * A latch device simulator that secures access to internal components
 * like SD cards and the printer.
 */
public class Latch extends Device implements Serializable {
    // Determines whether the latch is locked
    private boolean isLocked = false;
    // Determines whether the latch failed
    private boolean failed = false;

    // Activates the latch
    void lock() {
        if(!this.isLocked) { this.isLocked = true; }
    }

    // Deactivates the latch
    void unlock() {
        if(this.isLocked) {this.isLocked = false; }
    }

    /**
     * Latch failure: Determines if the latch is working
     * @return the failure state of the battery. True if it has failed, false otherwise.
     */
    @Override
    public boolean failure() { return this.failed; }



    @Override
    public void simulateFailure() {
        this.failed = true;
        System.err.println("Latch failure.");
    }

    @Override
    public String handleCommand(Command cmd) {
        if (cmd.deviceType() != DeviceType.LATCH) {
            throw new IllegalArgumentException("Invalid device type command.");
        }

        switch (cmd.command()) {
            case "lock": {
                this.lock();
                break;
            }
            case "unlock": {
                this.unlock();
                break;
            }
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
