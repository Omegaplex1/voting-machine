import BallotPackage.Ballot;

public class VotingManager {
    private VoteRecording voteRecording;
    private VotingControl votingControl;
    private Ballot ballot;
    private VotingProcess votingProcess;


    public VotingManager(Ballot ballot,VoteRecording voteRecording, VotingProcess votingProcess ){
        this.ballot = ballot;
        this.voteRecording = voteRecording;
        this.votingControl = votingControl;
        this.votingProcess = votingProcess;

    }

    public void setVotingControl(VotingControl votingControl){
        this.votingControl = votingControl;

    }
}
