import java.io.IOException;
import java.util.ArrayList;

public class VotingManager {
    private VoteRecording voteRecording;
    private VotingControl votingControl;
    private Ballot activeBallot;
    private VotingProcess votingProcess;
    private boolean isVotingInProgress = false;
    private Ballot emptyBallot;


    public VotingManager(Ballot ballot, VoteRecording voteRecording, VotingProcess votingProcess){
        this.activeBallot = ballot;
        this.voteRecording = voteRecording;
        this.votingControl = votingControl;
        this.votingProcess = votingProcess;
    }


    public VotingProcess getVotingProcess() {
        return votingProcess;
    }
    /**
     * Setter for the voting control
     * @param votingControl
     */
    public void setVotingControl(VotingControl votingControl){
        this.votingControl = votingControl;

    }

    /**
     * Getter for if the voting is in progress
     * @return
     */
    public boolean isVotingInProgress(){
        return isVotingInProgress;
    }

    public void setVotingInProgress(boolean isVotingInProgress){
        this.isVotingInProgress = isVotingInProgress;
    }

    /**
     * Method to return an empty ballot
     * @return empty b
     */
    public Ballot getEmptyBallot(){
        this.emptyBallot = activeBallot.createNewBallotCopy();
        return emptyBallot;
    }

    /**
     * Method to start the voting process we get an empty ballot, set voting process to true and then return the ballot
     * @return empty ballot
     */
    public void startVoting() throws IOException, ClassNotFoundException {
        // set the boolean to true
        isVotingInProgress = true;
        // create a blank ballot copy
        emptyBallot = getEmptyBallot();

        ArrayList<Template> newTemps = votingProcess.giveTemplate(emptyBallot);

        voteRecording.setBallot(votingProcess.getEmptyBallot(newTemps));
        voteRecording.printBallot();

        isVotingInProgress = false;
        // We need a blank ballot to eventually process in Voting process

        // We need to start after that to process the ballot into the screen BALLOT -> [Template] GUI

        // After the GUI is done we need to Record the vote

    }

    public void getBallotFromSDC1(){

    }

//    public static void main(String[] args) {
//
//    }
}
