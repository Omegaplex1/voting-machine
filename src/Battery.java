import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;

/**
 * Battery.java
 * <p>
 * A battery device simulator that shows whether the battery is on.
 */
public class Battery extends Device implements Serializable {

    private Socket socket;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    // Determines whether there is battery life left
    private boolean isOn = true;

    public Battery() throws IOException {
        this.socket = new Socket("localhost", 5001);
        this.out = new ObjectOutputStream(socket.getOutputStream());
        this.in = new ObjectInputStream(socket.getInputStream());

        out.writeObject("Battery");
        out.flush();

        // start thread to listen for failure messages
        new Thread(() -> {
            try {
                waitForFailureMessages(in);
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }).start();
    }

    /**
     * Method to wait for failure messages
     * @param in
     */
    private void waitForFailureMessages(ObjectInputStream in) throws IOException, ClassNotFoundException {
        while (true){
            String failureMessage = (String) in.readObject();
            if (failureMessage.equals("Failure")){
                isOn = false;
            }
        }
    }


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

    public static void main(String[] args) throws IOException {
        Battery battery = new Battery();


    }
}
