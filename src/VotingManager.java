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

    public void setVotingControl(VotingControl votingControl){
        this.votingControl = votingControl;

    }


    public void startVoting(){
        // set the boolean to true
        isVotingInProgress = true;
        // create a blank ballot copy
        emptyBallot = activeBallot.createNewBallotCopy();
        // We need a blank ballot to eventually process in Voting process

        // We need to start after that to process the ballot into the screen BALLOT -> [Template] GUI

        // After the GUI is done we need to Record the vote
    }

    public void getBallotFromSDC1(){

    }

    public static void main(String[] args) {

    }
}
