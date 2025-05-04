
/*
    Screen.java
    The screen simulates a touchscreen interface using JavaFX. It displays the ballot and captures voter selections.
 */

//sends to socket

import java.io.PrintWriter;
import java.io.Serializable;
import java.net.Socket;


public class Screen extends Device implements Serializable {
//    private Template currentTemplate = null;
    private boolean templateReady = false;
    private boolean failure = false;

    private Socket socket;
    private PrintWriter out;

    //TODO Implement the methods from Dr. Roman's API

    /**  public static void screenOn()
     *   public static void screenOff()
     *   public static void presentTemplate(Template t)
     *   public static Boolean exitReady()
     *   public static Template returnTemplate()
     */


    //API methods:
    public void displayTemplate(Template template) {
        this.currentTemplate = template;
        this.templateReady = false;
    }

    public Template getCurrentTemplate() {
        return templateReady ? currentTemplate : null;
    }

    public boolean templateReady() {
        return templateReady;
    }

    public boolean failure() {
        return failure;
    }

    @Override
    public void simulateFailure() {
        this.failure = true;
        System.err.println("Screen failure.");
    }

    @Override
    public String handleCommand(Command cmd) {
        if (cmd.deviceType() != DeviceType.SCREEN) {
            throw new IllegalArgumentException("Invalid device type command.");
        }

        switch (cmd.command()) {
            //TODO: add way to send back and forth templates
            case "displayTemplate":
                break;
            case "getCurrentTemplate":
                break;
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
