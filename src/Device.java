public abstract class Device {

    /**
     * All devices have a failure method that can be called by
     * the failure object
     * @return The failure state of the device
     */
    public abstract boolean failure();

    /**
     * This will simulate a failure of the device
     */
    public abstract void simulateFailure();


    /**
     * This will respond to commands sent over the socket
     * @param cmd a command type class that contains the device type and the command with its arguments
     * @return a response string
     */
    public abstract String handleCommand(Command cmd);

}

