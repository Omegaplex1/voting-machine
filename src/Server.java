/**
 * Server.java
 *
 * This will be the backend processing commands from the client. You may interact with the voting machine's
 * devices through this server.
 */

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.*;

public class Server {

    private ObjectInputStream in;
    private Printer printer;
    private Latch latch;
    private Battery battery;
    private Screen screen;
    private SDCardPort sdCardPort;
    private IDCardReader idCardReader;
    private boolean devicesInitialized = false;

    public void startSocket(Template screen) throws IOException {
        Socket socket;
        try (ServerSocket serverSocket = new ServerSocket(2121)) {
            socket = serverSocket.accept();
        }
        System.out.println("Client connected");
        ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
        this.in = new ObjectInputStream(socket.getInputStream());


        /* Send template to client for display */
        System.out.println("Server started");
        out.writeObject(screen);
        out.flush();

        /* Start the data reading thread */
        new Thread(this::dataReadingThread).start();
    }

    /**
     * Devices in the voting machine to be interacted with by client
     */
    public void initDevices(Printer printer, Battery battery,
            Screen screen, IDCardReader idCardReader,
            Latch latch, SDCardPort sdCardPort) {
        this.printer = printer;
        this.battery = battery;
        this.screen = screen;
        this.idCardReader = idCardReader;
        this.latch = latch;
        this.sdCardPort = sdCardPort;
        devicesInitialized = true;
    }

    private void dataReadingThread() {
        while (true) {
            try {
                Object objData = in.readObject();
                System.out.println(handleObjectData(objData));
            } catch (ClassNotFoundException | IOException e) {
                System.err.println(e.getMessage());
            }
        }
    }

    private String handleObjectData(Object data) {
        switch (data) {
            //Process commands sent across the socket
            case Command cmd -> {
                /* Devices should be initialized before processing commands */
                if (!devicesInitialized) throw new IllegalStateException("Devices not initialized");
                switch (cmd.deviceType()) {
                    case PRINTER -> {
                        return this.printer.handleCommand(cmd);
                    }
                    case SDCARD -> {
                        return this.sdCardPort.handleCommand(cmd);
                    }
                    case LATCH -> {
                        return this.latch.handleCommand(cmd);
                    }
                    case BATTERY -> {
                        return this.battery.handleCommand(cmd);
                    }
                    case SCREEN -> {
                        return this.screen.handleCommand(cmd);
                    }
                    case IDCARD -> {
                        return this.idCardReader.handleCommand(cmd);
                    }
                }
            }
            case Template template -> {
                return template.toString();
            }
            default -> {
                return "Invalid object data received!";
            }
        }

        return "OK";
    }

}
