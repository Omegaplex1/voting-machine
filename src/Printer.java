import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Paths;


public class Printer extends Device implements Serializable {
    boolean failure = false;
    private Socket socket;
    private ObjectOutputStream out;
    private ObjectInputStream in;

    public Printer() throws IOException {
        this.socket = new Socket("localhost", 5001);
        this.out = new ObjectOutputStream(socket.getOutputStream());
        this.in = new ObjectInputStream(socket.getInputStream());

        out.writeObject("Printer");
        out.flush();

        // start a new thread for listening to messages
        new Thread(() -> {
            try {
                listenForMessages(in);
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }).start();
    }

    // method to listen for messages
    public void listenForMessages(ObjectInputStream in) throws IOException, ClassNotFoundException {
        while(true){
            String input = (String) in.readObject();

            if (input.equals("failure")) {
                System.out.println("(In Printer) Failure initiiated by the USER ");
                failure = true;
            }
        }
    }

    public void print(String text) {

        String filePrefix = "printPaper";
        String fileSuffix = ".txt";
        try {
            String filename = filePrefix + fileSuffix;
            String path = Paths.get("printerFile", filename).toAbsolutePath().toString();
            System.out.println(path);
            FileWriter writer = new FileWriter(path, true);
            writer.write(text);
            writer.close();
        } catch (IOException e) {
            System.err.println(e.getMessage());
            failure = true;
        }
    }

    public void advance() {
        String filePrefix = "printPaper";
        String fileSuffix = ".txt";
        try {
            String filename = filePrefix + fileSuffix;
            String path = Paths.get("printerFile", filename).toAbsolutePath().toString();
            System.out.println(path);
            FileWriter writer = new FileWriter(path, true);
            String blanks = "\n".repeat(5);
            writer.write(blanks);
            writer.close();
        } catch (IOException e) {
            System.err.println(e.getMessage());
            failure = true;
        }
    }


    @Override
    public boolean failure() {
        return failure;
    }

    @Override
    public void simulateFailure() {
        this.failure = true;
        System.err.println("Printer failure.");
    }

    @Override
    public String handleCommand(Command cmd) {
        if (cmd.deviceType() != DeviceType.PRINTER) {
            throw new IllegalArgumentException("Invalid device type command.");
        }

        switch (cmd.command()) {
            case "print": {
                print(cmd.args().getFirst());
                break;
            }
            case "advance": {
                advance();
                break;
            }
            case "simulateFailure": {
                simulateFailure();
                break;
            }
            default: {
                return "Invalid command for " + cmd.deviceType() + ": " + cmd.command() + ".";
            }
        }

        return "OK";

    }

    public static void main(String[] args) throws IOException {
        Printer printer = new Printer();


    }
}
