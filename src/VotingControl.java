import java.io.File;
import java.io.IOException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class VotingControl {
    private Monitor monitor;
    private AdminManager admin;
    private VotingManager votingManager;
    private CardHolder card;
    private BlockingQueue messages;

    public VotingControl(Monitor mon, AdminManager admin, VotingManager vot, CardHolder card){

        // these all need to be on their own threads (monitor, Admin manager, votingManager)
        this.monitor = mon;
        // set the reference to voting control in here
        monitor.setRefToVotingControl(this);


        this.admin = admin;
        this.votingManager = vot;
        this.card = card;
        this.messages = new LinkedBlockingQueue();

        new Thread(() -> {
            try {
                readMessages();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }).start();


        new Thread(() -> {
            try {
                mainHandlingLoop();
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }).start();

    }

    /**
     * Method to constantly check if a card has been inserted.
     */
    private void mainHandlingLoop() throws IOException, ClassNotFoundException {
        while (true){
            if (card.checkCard() && !votingManager.isVotingInProgress()){
                String cardT = card.getCardType();
                long cardNum = card.getCardNumber();

                System.out.println("Card Detected: " + cardT + " (" + cardNum + ") ");
                // 1st admin
                if (cardT.equals("A")){
                    System.out.println("Here SFJNUGFIBNGUIBNGNUGNJUGOIKNGOI");
                    this.votingManager.getVotingProcess().getScreen().setFailureScreen();
                    break;
                }
                // 2nd "Voter"
                else {
                    // start the voting process
                    votingManager.startVoting();
//                    System.out.println("Starting the voting process");
                    card.ejectCard();
                    System.out.println("CARD IS EJECTED");
                }
            }
        }
    }

    /**
     * Method to constantly read in messages
     * @throws InterruptedException ..
     */
    private void readMessages() throws InterruptedException, IOException {
        while (true){
            String currentMessage  = (String) messages.take();
            handleMessage(currentMessage);
        }
    }

    /**
     * Method to handle a message from monitor specifically
     * @param message ..
     */
    private void handleMessage(String message) throws IOException {
        System.out.println("This is the current message: " + message);

        switch (message){
            case "Failure":
                if (votingManager.isVotingInProgress()){
                    votingManager.setVotingInProgress(false);
                }
                System.out.println("This is the failure message");
                admin.handleFailure("Device Failure");

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



//    public static void main(String[] args) throws IOException {
//
//        Latch latch = new Latch();
//        Screen screen = new Screen();
//        Printer printer = new Printer();
//        Ballot ballot = new Ballot(new File("resources/markUpExample1.txt"));
//
//        // init all the sd cards
//        SDCardPort sdCardPort1 = new SDCardPort(1, SDMode.WRITE_ONLY);
//        SDCardPort sdCardPort2 = new SDCardPort(2, SDMode.WRITE_ONLY);
//        SDCardPort sdCardPort3 = new SDCardPort(3, SDMode.WRITE_ONLY);
//
//        // init the id card reader and the cardholder
//        IDCardReader idCardReader = new IDCardReader();
//        CardHolder cardHolder = new CardHolder(idCardReader);
//
//        //init the vote process
//        VotingProcess votingProcess = new VotingProcess(screen, ballot);
//
//        VoteRecording voteRecording = new VoteRecording(sdCardPort3, sdCardPort2, printer);
//
//        Battery battery = new Battery();
//
//
//        AdminManager adminManager = new AdminManager(latch, screen);
//        Monitor monitor = new Monitor(latch, sdCardPort1, sdCardPort2, sdCardPort3, screen, printer, battery);
//        VotingManager votingManager = new VotingManager(ballot, voteRecording, votingProcess);
//
//
//        VotingControl vc = new VotingControl(monitor, adminManager ,votingManager ,cardHolder);
//    }
}
