import java.io.IOException;
import java.util.ArrayList;

public class VoteRecording {
    private SDCardPort sdCardPort3;
    private SDCardPort sdCardPort2;
    private Printer printer;
    private Ballot ballot;

    public VoteRecording(SDCardPort sdCardPort2, SDCardPort sdCardPort3, Printer printer) {
        this.sdCardPort3 = sdCardPort3;
        this.sdCardPort2 = sdCardPort2;
        this.printer = printer;
    }

    public void setBallot(Ballot ballot) {
        this.ballot = ballot;
    }

    public void printBallot(){
        for(int i = 0; i < ballot.getSize(); i++){
            printProposition(ballot.getPropositionAtIndex(i));
        }
    }

    public void printProposition(Proposition p){
        String title = p.getTitle();
        String desc = p.getDescription();
        ArrayList<String> votedFor = p.getSelected();
        ArrayList<String> cands = p.getOptions().options;


        printer.print(title + "\n" + desc + "\nOptions:\n");
        for(int i = 0; i < cands.size(); i++){
            printer.print(cands.get(i) + "\n");
        }

        printer.print("\nYou voted for:\n");
        for(int i = 0; i < votedFor.size(); i++){
            printer.print(votedFor.get(i) + "\n");
        }
        printer.print("\n\n\n");
    }

    public void writeToCards(Proposition p){
        String title = p.getTitle();
        String desc = p.getDescription();
        ArrayList<String> votedFor = p.getSelected();
        ArrayList<String> cands = p.getOptions().options;

        String result = (title + "\n" + desc + "\nOptions:\n");
        for(int i = 0; i < cands.size(); i++){
            result += (cands.get(i) + "\n");
        }

        result += "\nYou voted for:\n";
        for(int i = 0; i < votedFor.size(); i++){
            result += (votedFor.get(i) + "\n");
        }
        result += ("\n\n\n");

        sdCardPort2.write(result);
        sdCardPort3.write(result);
    }

    public static void main(String[] args) throws IOException {
        SDCardPort p2 = new SDCardPort();
        SDCardPort p3 = new SDCardPort();
        Printer printer = new Printer();
        VoteRecording vr = new VoteRecording(p2, p3, printer);

        Ballot b = new Ballot();
        vr.setBallot(b);
        vr.printBallot();
    }

}
