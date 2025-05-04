import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Paths;

public class Printer extends Device implements Serializable {

    boolean failure = false;

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
}
