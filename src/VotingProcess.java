import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class VotingProcess {
    private Screen screen;
    private Ballot ballot;


    public VotingProcess(Screen screen, Ballot ballot) {
        this.screen = screen;
        this.ballot = ballot;
    }
    private ArrayList<Template> getTemplate(){
        ArrayList<Template> temps = new ArrayList<>();
        for(int i = 0; i < ballot.getSize();i++){
            Proposition temp = ballot.getPropositionAtIndex(i);
            HashMap<String,Boolean> options = getHashMap(temp);
            temps.add(new Template(temp.getTitle(),temp.getDescription(),temp.getNumberOfSelections(),options));
        }
        return temps;
    }
    private HashMap<String,Boolean> getHashMap(Proposition p){
        HashMap<String,Boolean> options = new HashMap<>();
        for(int i = 0; i < p.getOptions().getOptions().size();i++){
            options.put(p.getOptions().getOptions().get(i),true);
        }
        return options;
    }
    public void giveTemplate() throws IOException {
        ArrayList<Template> temps = getTemplate();
        screen.sendTempArrays(temps);
    }

    public static void main(String args[]) throws IOException {
        VotingProcess main = new VotingProcess(new Screen(),new Ballot());
        main.giveTemplate();

    }
}
