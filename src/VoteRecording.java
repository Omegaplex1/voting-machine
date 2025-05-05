import BallotPackage.Ballot;

public class VoteRecording {
    private SDCardPort sdCardPort3;
    private SDCardPort sdCardPort2;
    private Printer printer;
    private Ballot ballot;

    public VoteRecording(SDCardPort sdCardPort3, SDCardPort sdCardPort2, Printer printer) {
        this.sdCardPort3 = sdCardPort3;
        this.sdCardPort2 = sdCardPort2;
        this.printer = printer;
    }

    public void setBallot(Ballot ballot) {
        this.ballot = ballot;
    }

//    public printBallot(){
//
//    }
}
