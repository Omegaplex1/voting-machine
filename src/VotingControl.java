import BallotPackage.Ballot;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class VotingControl {
    private Monitor monitor;
    private AdminManager admin;
    private VotingManager vote;
    private CardHolder card;
    private BlockingQueue messages;

    public VotingControl(Monitor mon, AdminManager admin, VotingManager vot, CardHolder card){

        // these all need to be on their own threads (monitor, Admin manager, votingManager)
        this.monitor = mon;
        // set the reference to voting control in here
        monitor.setRefToVotingControl(this);


        this.admin = admin;
        this.vote = vot;
        this.card = card;
        this.messages = new LinkedBlockingQueue();

        new Thread(() -> {
            try {
                readMessages();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }).start();

//        mainHandlingLoop();

    }

//    private void mainHandlingLoop(){
//        while (true){
//            if (card.cardDetected())
//        }
//    }

    /**
     * Method to constantly read in messages
     * @throws InterruptedException ..
     */
    private void readMessages() throws InterruptedException {
        while (true){
            String currentMessage  = (String) messages.take();
            handleMessage(currentMessage);
        }
    }

    /**
     * Method to handle a message from monitor specifically
     * @param message ..
     */
    private void handleMessage(String message){
        System.out.println("This is the current message: " + message);

        switch (message){
            case "Failure":
                System.out.println("This is the failure message");
                admin.handleFailure("Device Failure");
                break;
            case "Success":
                System.out.println("Successfully voted");
                break;
        }
    }

    /**
     * Method to put a message in the inbox of the Voting Controller
     * @param message ..
     */
    public void sendMessage(String message) {
        messages.add(message);
    }



    public static void main(String[] args) throws IOException {

        Latch latch = new Latch();
        Screen screen = new Screen();
        Printer printer = new Printer();
        Ballot ballot = new Ballot(new File("resources/markUpExample1.txt"));

        // init all the sd cards
        SDCardPort sdCardPort1 = new SDCardPort(1, SDMode.WRITE_ONLY);
        SDCardPort sdCardPort2 = new SDCardPort(2, SDMode.WRITE_ONLY);
        SDCardPort sdCardPort3 = new SDCardPort(3, SDMode.WRITE_ONLY);

        // init the id card reader and the cardholder
        IDCardReader idCardReader = new IDCardReader("localhost", 5001);
        CardHolder cardHolder = new CardHolder(idCardReader);

        //init the vote process
        VotingProcess votingProcess = new VotingProcess(screen, ballot);

        VoteRecording voteRecording = new VoteRecording(sdCardPort3, sdCardPort2, printer);

        Battery battery = new Battery();


        AdminManager adminManager = new AdminManager(latch, screen);
        Monitor monitor = new Monitor(latch, sdCardPort1, sdCardPort2, sdCardPort3, screen, printer, battery);
        VotingManager votingManager = new VotingManager(ballot, voteRecording, votingProcess);


        VotingControl vc = new VotingControl(monitor, adminManager ,votingManager ,cardHolder);
    }
}
