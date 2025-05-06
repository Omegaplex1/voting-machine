import java.util.ArrayList;

/*
 * Will contain the candidates, a boolean to confirm the selection and the index for the selection*/
public class OptionsList{
    public ArrayList<String> options;
    private int maxSelections;
    private int currentSelections;
    private ArrayList<Integer> indexSelected;
    public ArrayList<Boolean> selected;


    /**
     * Constructor to init the Options list
     * @param options the options ie List (Option1 , Option2, Option3)
     * @param numberOfSelections the number of max selections for the current proposition
     */
    public OptionsList(ArrayList<String> options, int numberOfSelections){
//        System.out.println("This is the number of selections " + numberOfSelections);
        this.maxSelections = numberOfSelections;
        this.currentSelections = 0;
        this.indexSelected = new ArrayList<>();
        this.options = options;
        this.selected = new ArrayList<>();
        fillBooleansList();
    }

    public OptionsList(){
        this.options = new ArrayList<>();
        options.add("Greg");
        options.add("Martin");
        options.add("Robbie");
        options.add("Jesus");
        maxSelections = 1;
        currentSelections = 1;
        indexSelected = new ArrayList<>();
        indexSelected.add(0);
        selected = new ArrayList<>();
        selected.add(false);
        selected.add(false);
        selected.add(false);
        selected.add(false);
    }

    /**
     * Simple getter for the options list
     * @return list of options
     */
    public ArrayList<String> getOptions(){
        return this.options;
    }
    public ArrayList<Boolean> getSelected(){return this.selected;}

    /**
     * Simple getter for the indexes selected
     * @return the indexes selected
     */
    public ArrayList<Integer> getIndexSelected(){
        return this.indexSelected;
    }
    public String getSelectedOption(int index){
        return this.options.get(index);
    }

    /**
     * Method to fill the booleans list with all false
     */
    private void fillBooleansList(){
        for (String option : options){
            selected.add(false);
        }
    }
    /**
     * Method to select an option
     * @param selectionIndex the selected index
     * @return ..
     */
    public boolean selectOptions(int selectionIndex){
        if((currentSelections < maxSelections) && (options.size() > selectionIndex)){
            selected.set(selectionIndex, true); // add the index to the array of selected indexes
            System.out.println("Option selected " + getSelectedOption(selectionIndex));
            currentSelections++; // update the current selections
        }else if (currentSelections >= maxSelections){
            System.out.println("Cannot add anymore options");
        }else{
            System.out.println("Invalid, selected index is out of bounds");
        }
        return (currentSelections < maxSelections + 1);
    }

    /**
     * Method to clear the options to make space for new selections
     */
    public void clearOptions(){
        indexSelected.clear();
    }

    /**
     * Method to replace a selected value for a new option should the
     * voter decide to change their mind
     * */
    public void replace(int replacedI, int replacement){
        indexSelected.remove(replacedI);
        indexSelected.add(replacement);
    }

    public void fillboolean(int index, boolean value){
        selected.set(index, value);
    }
}

