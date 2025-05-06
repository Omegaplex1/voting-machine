import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class VotingProcess {
    private Screen screen;


    public VotingProcess(Screen screen) {
        this.screen = screen;
    }

    public Screen getScreen() {
        return screen;
    }

    private ArrayList<Template> getTemplate(Ballot ballot){
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
            options.put(p.getOptions().getOptions().get(i),false);
        }
        return options;
    }

    public Ballot getEmptyBallot(ArrayList<Template> templates){
        ArrayList<Proposition> props = new ArrayList<>();

        for(Template template : templates){
            Proposition prop = new Proposition(
                    template.getTitle(),
                    template.getDescription(),
                    template.getOptionsList(),
                    template.getSelections());
            props.add(prop);
        }
        Ballot ballot = new Ballot(props);
        return ballot;
    }

    public ArrayList<Template>giveTemplate(Ballot ballot) throws IOException, ClassNotFoundException {
        ArrayList<Template> temps = getTemplate(ballot);
        ArrayList<Template> newTemps = screen.presentTemplate(temps);

        return newTemps;
    }

    public static void main(String args[]) throws IOException, ClassNotFoundException {
        VotingProcess main = new VotingProcess(new Screen());
        main.giveTemplate(new Ballot());

    }
}
