public class VotingControl {
    Monitor monitor;
    AdminManager admin;
    VotingManager vote;
    CardHolder card;
    public VotingControl(Monitor mon,AdminManager admin, VotingManager vot, CardHolder card){

        // these all need to be on their own threads (monitor, Admin manager, votingManager)
        this.monitor = mon;
        this.admin = admin;
        this.vote = vot;
        this.card = card;

        // not on thread (NOT ACTIVE )
        CardHolder cardHolder = new CardHolder();
    }

}
