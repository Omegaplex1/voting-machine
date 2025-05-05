import BallotPackage.Ballot;
import BallotPackage.Proposition;

import java.time.temporal.UnsupportedTemporalTypeException;
import java.util.HashMap;

public class VotingProcess {
    private Screen screen;
    private Ballot ballot;
    private boolean isValid = true;
    private int votingIndex = -1;

    public VotingProcess(Screen screen, Ballot ballot){
        this.screen = screen;
        this.ballot = ballot;
    }


}
