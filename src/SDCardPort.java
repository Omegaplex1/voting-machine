import java.io.*;
import java.net.Socket;
import java.nio.file.Paths;

/**
 * SDCardPort.java
 * <p>
 * An SD Card slot device simulator that will allow for opening of the SD Card ports
 * simulated by text files.
 */

public class SDCardPort extends Device implements Serializable {

    // The mode of the sd card. Read, write, or both
    private final SDMode mode;
    // Which slot of the machine this slot corresponds to
    private final int slot;
    // Determines whether the SD slot is usable or not
    private boolean isOpened;
    // File streams for writing and or reading
    private PrintWriter writingStream = null;
    private BufferedReader readingStream = null;

    private Socket socket;
    private ObjectOutputStream outputStream;
    private ObjectInputStream inputStream;



    SDCardPort() {
        slot = -1;
        mode = null;
        writingStream = null;
        readingStream = null;
        isOpened = false;
    }

    public SDCardPort(int slot, SDMode mode) throws IOException {
        this.slot = slot;
        this.mode = mode;
        this.isOpened = openSDFile();

        this.socket = new Socket("localhost", 5001);
        this.outputStream = new ObjectOutputStream(socket.getOutputStream());
        this.inputStream = new ObjectInputStream(socket.getInputStream());

        outputStream.writeObject("SDCard" + slot);
        outputStream.flush();

        // start thread to listen for failure messages
        new Thread(() -> {
            try {
                waitForFailureMessages(inputStream);
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }).start();
    }

    /**
     * Method to wait for failure messages
     * @param in ..
     */
    private void waitForFailureMessages(ObjectInputStream in) throws IOException, ClassNotFoundException {
        while (true){
            String failureMessage = (String) in.readObject();
            if (failureMessage.equals("Fail")){
                isOpened = false;
            }
        }
    }

    // Open the file for reading or writing to the sd
    private boolean openSDFile() {
        String sdFilePrefix = "sd_card_";
        String sdFileSuffix = ".txt";
        try {
            String filename = sdFilePrefix + this.slot + sdFileSuffix;
            String path = Paths.get("sd_cards", filename).toAbsolutePath().toString();
            System.out.println(path);
            switch (this.mode) {
                case READ_ONLY:
                    this.readingStream = new BufferedReader(new FileReader(path));
                    break;
                case WRITE_ONLY:
                    this.writingStream = new PrintWriter(path);
                    break;
                case READ_WRITE:
                    this.writingStream = new PrintWriter(path);
                    this.readingStream = new BufferedReader(new FileReader(path));
                    break;
            }
            return true;

        } catch (Exception e) {
            System.err.println("Failed to open SD card file.\n" + e.getMessage());
            return false;
        }
    }

    /**
     * Writes a string of data to the SD Card, will only work if the correct mode is enabled
     * @param bytes: The data to be written to the SD Card.
     * @return boolean for if the data was written successfully or not.
     */
    public boolean write(String bytes) {
        if (failure() || this.mode == SDMode.READ_ONLY) return false;
        this.writingStream.print(bytes);
        return true;
    }

//    public boolean writeBallot2SD(Ballot ballot) {
//        if (failure() || this.mode == SDMode.READ_ONLY) return false;
//
//    }

    /**
     * readLine will read a single line of data until it hits a newline or the end of the file.
     * @return A string of data, or null for failures.
     */
    public String readLine() {
        if (failure() || this.mode == SDMode.WRITE_ONLY) return null;
        try {
            return this.readingStream.readLine();
        } catch (Exception e) {
            System.err.println("Could not read lines from SDCardPort!\n" + e.getMessage());
            return null;
        }
    }

    /**
     * Ejects the sd card and closes the reading and writing streams
     * @return boolean for successful ejection
     */
    public boolean ejectSDCard() {
        if (failure()) return false;
        try {
            if (this.writingStream != null) this.writingStream.close();
            if (this.readingStream != null) this.readingStream.close();
            this.isOpened = false;
            return true;
        } catch (Exception e) {
            this.isOpened = false;
            System.err.println("Could not eject SDCardPort!\n" + e.getMessage());
            return false;
        }
    }

    /**
     * SDCard failure: Determines if the slot is usable or not or if there was an error of any type
     * @return The failure state of the device. True if it has failed, false otherwise.
     */
    @Override
    public boolean failure() {
        return !isOpened;
    }


    /**
     * simulateFailure: Forces the device to fail for testing purposed.
     */
    @Override
    public void simulateFailure() {
        ejectSDCard(); //Ejecting is the same as failing and the files can no longer be modified
        this.isOpened = false;
        System.err.println("SDCardPort failure.");
    }

    @Override
    public String handleCommand(Command cmd) {
        if (cmd.deviceType() != DeviceType.SDCARD) {
            throw new IllegalArgumentException("Invalid device type command.");
        }

        switch (cmd.command()) {
            case "simulateFailure": {
                simulateFailure();
                break;
            }
            case "ejectSDCard": {
                ejectSDCard();
                break;
            }
            case "write": {
                write(cmd.args().getFirst());
                break;
            }
            case "read": {
                readLine();
                break;
            }
            default: {
                return "Invalid command for " + cmd.deviceType() + ": " + cmd.command() + ".";
            }
        }

        return "OK";
    }

//    public static void main(String[] args) throws IOException {
//        SDCardPort sd1 = new SDCardPort(1, SDMode.READ_ONLY);
//        SDCardPort sd2 = new SDCardPort(2, SDMode.WRITE_ONLY);
//
//    }
}

