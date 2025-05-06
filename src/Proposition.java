import java.util.ArrayList;

/*
 * This will hold the details of each individual proposition including a list of the options*/
public class Proposition{
    private String title, description;
    private OptionsList options;
    private int numberOfSelections;
    private int numberOfOptions;

    public Proposition(String title, String description, OptionsList options, int numberOfSelections) {
        this.title = title;
        this.description = description;
        this.options = options;
        this.numberOfOptions = options.getOptions().size();
        this.numberOfSelections = numberOfSelections;
    }

    public Proposition(){
        this.title = "Best Manager";
        this.description = "Which team member has done the best at managing the others?";
        this.options = new OptionsList();
        this.numberOfSelections = 1;
        this.numberOfOptions = 2;
    }


    public Proposition makeCopy(){
        return new Proposition(this.title, this.description, this.options, this.numberOfSelections);
    }
    /**
     * Retrieval methods to obtain the current values
     */
    public String getDescription() {
        return description;
    }
    public String getTitle() {
        return title;
    }
    public OptionsList getOptions() {
        return this.options;
    }
    public int getNumberOfSelections() {return numberOfSelections;}
    public ArrayList<String> getSelected() {
        ArrayList<String> temp = new ArrayList<>();
        for (int i = 0; i < options.getOptions().size(); i++){
            if(options.getSelected().get(i)){
                temp.add(options.getOptions().get(i));
            }
        }
        return temp;
    }

//    public boolean getOptionSelected(int index){return options.selectOptions(index);}
}