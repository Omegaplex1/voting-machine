import java.io.File;
import java.io.IOException;

/**
 * Main.java
 * <p>
 * This will run the hardware tests for the demonstration of our voting machine simulation
 */


public class Main {
    public static void main(String[] args) throws IOException {

        // init the terminal window server
//        TerminalWindowServer terminalWindowServer = new TerminalWindowServer();
//        ClientGUI clientGUI = new ClientGUI();



        // init the reader and printer and battery and latch
        IDCardReader idCardReader = new IDCardReader();
        Printer printer = new Printer();
        Battery battery = new Battery();
        Latch latch = new Latch();
        Screen screen = new Screen();

        // init all the sd cards
        SDCardPort sdCardPort1 = new SDCardPort(1, SDMode.WRITE_ONLY);


        SDCardPort sdCardPort2 = new SDCardPort(2, SDMode.WRITE_ONLY);
        SDCardPort sdCardPort3 = new SDCardPort(3, SDMode.WRITE_ONLY);

        // init the ballot class
        Ballot ballot = new Ballot(new File("resources/markUpExample1.txt"), sdCardPort1);

        // empty ballot
        Ballot emptyBallot = ballot.createNewBallotCopy();

        // write onto sdcard 1
        sdCardPort1.write(emptyBallot.toString());


        // init the vote recording
        VoteRecording voteRecording = new VoteRecording(sdCardPort3, sdCardPort2, printer);

        //init the vote process
        VotingProcess votingProcess = new VotingProcess(screen, ballot);

        // init the adminManager
        AdminManager adminManager = new AdminManager(latch, screen);
        Monitor monitor = new Monitor(latch, sdCardPort1, sdCardPort2, sdCardPort3, screen, printer, battery);
        VotingManager votingManager = new VotingManager(ballot, voteRecording, votingProcess);


        CardHolder cardHolder = new CardHolder(idCardReader);

        // pass Monitor, and Admin Manager, and Voting Manager, and Card Holder
        VotingControl vc = new VotingControl(monitor, adminManager ,votingManager ,cardHolder);


    }
}