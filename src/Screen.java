
/*
    Screen.java
    The screen simulates a touchscreen interface using JavaFX. It displays the ballot and captures voter selections.
 */

//sends to socket

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;


public class Screen extends Device implements Serializable {
    private Template currentTemplate = null;
    private boolean templateReady = false;
    private boolean failure = false;
    private boolean isOn;
    private boolean exitReady;

    private Socket guiSocket;
    private ObjectOutputStream guiOutput;
    private ObjectInputStream guiInput;

    private Socket terminalSocket;
    private ObjectOutputStream terminalOutput;
    private ObjectInputStream terminalInput;



    private PrintWriter out;

    public Screen() throws IOException {
        // set up the gui out/ in
        this.guiSocket = new Socket("localhost", 12345);
        this.guiOutput = new ObjectOutputStream(guiSocket.getOutputStream());
        this.guiInput = new ObjectInputStream(guiSocket.getInputStream());

        System.out.println("Here ");

        // set up the terminal window out/in
        this.terminalSocket = new Socket("localhost", 5001);
        this.terminalOutput = new ObjectOutputStream(terminalSocket.getOutputStream());
        this.terminalInput = new ObjectInputStream(terminalSocket.getInputStream());

        System.out.println("Here 2 ");
        // send an initial message
        terminalOutput.writeObject("Screen");
        terminalOutput.flush();

        // start thread to listen for messages from the terminal window
        new Thread(() -> {
            try {
                waitForFailureMessage(terminalInput);
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }).start();

    }


    public void waitForFailureMessage(ObjectInputStream in) throws IOException, ClassNotFoundException {
        String line;
        while ((line = (String) in.readObject()) != null){
            if (line.equals("failure")){
                failure = true;
            }
        }
    }

    //TODO Implement the methods from Dr. Roman's API

    /**  public static void screenOn()
     *   public static void screenOff()
     *   public static void presentTemplate(Template t)
     *   public static Boolean exitReady()
     *   public static Template returnTemplate()
     */


    public void screenOn(){isOn = true;}

    public void screenOff(){isOn = false;}

    public void presentTemplate(Template template){
        //TODO Have template be sent over the socket
        this.currentTemplate = template;
    }

    public boolean exitReady(){
        return this.exitReady;
    }

    public Template returnTemplate(){
        return this.currentTemplate;
    }

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

    /**
     * Method to set the failure screen from the admin manager
     */
    public void setFailureScreen(){
        System.out.println("(In Admin Manager) Setting the failure screen.");
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

    public void sendTempArrays(ArrayList<Template> temps) throws IOException {
        guiOutput.writeObject(temps);
        guiOutput.flush();

    }

//    public static void main(String[] args) throws IOException {
//        Screen screen = new Screen();
//    }
}
