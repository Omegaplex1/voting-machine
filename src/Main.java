import BallotPackage.Ballot;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

/**
 * Main.java
 * <p>
 * This will run the hardware tests for the demonstration of our voting machine simulation
 */


public class Main {
    public static void main(String[] args) throws IOException {
        Template basicTemplate = new Template("President",
                "This person will run the country!",
                new String[]{"Julian", "Ryan", "Bat", "Sachi", "Ira"},
                new boolean[]{true, true, true});

        Server server = new Server();
        server.startSocket(basicTemplate);

        VotingControl vc = new VotingControl();









































        SDCardPort sdCardPort = new SDCardPort(1, SDMode.WRITE_ONLY);
        // init the reader and printer and battery and latch
        IDCardReader reader = new IDCardReader();
        Printer printer = new Printer();
        Battery battery = new Battery();
        Latch latch = new Latch();

        // init the screen
        Screen screen = new Screen();

        // init all the sd cards
        SDCardPort sdCardPort1 = new SDCardPort(1, SDMode.WRITE_ONLY);
        SDCardPort sdCardPort2 = new SDCardPort(2, SDMode.WRITE_ONLY);
        SDCardPort sdCardPort3 = new SDCardPort(3, SDMode.WRITE_ONLY);

        // init the ballot class
        Ballot ballot = new Ballot(new File("testFile1.txt"));

        // init the vote recording
        VoteRecording voteRecording = new VoteRecording(sdCardPort3, sdCardPort2, printer);

        //init the vote process
        VotingProcess votingProcess = new VotingProcess(screen);

        // init the adminManager
        AdminManager adminManager = new AdminManager(latch, screen);
        Monitor monitor = new Monitor(latch, sdCardPort1, sdCardPort2, sdCardPort3, screen, printer);
        VotingManager votingManager = new VotingManager(ballot, voteRecording, votingProcess);


        // init the id card reader and the cardholder
        IDCardReader idCardReader = new IDCardReader();
        CardHolder cardHolder = new CardHolder(idCardReader);

        // pass Monitor, and Admin Manager, and Voting Manager, and Card Holder
        VotingControl vc = new VotingControl(monitor, adminManager ,votingManager ,cardHolder);


        // Initialize voting machine devices for socket communication
        server.initDevices(printer, battery, screen, reader, latch, sdCardPort);



        //Conduct tests of devices
        System.out.println("Please input a valid card number. \n");
        Scanner in = new Scanner(System.in);
        reader.readCard(in.nextLine());
        System.out.println("Card number: " + reader.getCardNumber());
        System.out.println("Card type: " + reader.getCardType());

        printer.print("Hey guys it's Ryan");
        printer.advance();
        printer.print("Just kidding guys it's Julian");


        //SD Card tests placeholder
        System.out.println("Ballot: " + sdCardPort.readLine());
        SDCardPort voteCard = new SDCardPort(2, SDMode.WRITE_ONLY);
        if (!voteCard.write("999,999,999 Votes for Julian :)\n")) {
            System.err.println("Could not write to SDCard Port");
        }
        if (sdCardPort.ejectSDCard()) System.out.println("Ballot Ejected");
        else System.out.println("Could not eject ballot");
        if (voteCard.ejectSDCard()) System.out.println("Votes Ejected");
        else System.out.println("Could not eject votes");

    }
}