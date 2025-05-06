import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class Ballot{
    ArrayList<Proposition> propositions;
    Boolean locked;
    private SDCardPort sdCardPort1;

    public Ballot(ArrayList<Proposition> propositions){
        this.propositions = propositions;
        locked = false;
    }

    /**
     * Constructor to init the ballots propositions into a List (Proposition 1, Proposition 2, Proposition 3)
     * @param markUpFile the markup file
     * @throws FileNotFoundException ..
     */
    public Ballot(File markUpFile, SDCardPort sdCardPort1) throws FileNotFoundException {
        // Method to read in the file and parse it into propositions
        locked=false;
        this.propositions = extractInfo(markUpFile);
        this.sdCardPort1 = sdCardPort1;

    }

    /**
     * Constructor to init the ballots propositions into a List (Proposition 1, Proposition 2, Proposition 3)
     * @param markUpFile the markup file
     * @throws FileNotFoundException ..
     */
    public Ballot(File markUpFile) throws FileNotFoundException {
        // Method to read in the file and parse it into propositions
        locked=false;
        this.propositions = extractInfo(markUpFile);
        this.sdCardPort1 = sdCardPort1;

    }

    public Ballot(){
        locked=false;
        propositions = new ArrayList<Proposition>();
        propositions.add(new Proposition());
        propositions.add(new Proposition());
        propositions.add(new Proposition());
        propositions.add(new Proposition());
        propositions.add(new Proposition());
        propositions.add(new Proposition());
    }

    public Ballot createNewBallotCopy(){
        ArrayList<Proposition> ps = new ArrayList<Proposition>();
        for(int i = 0; i < propositions.size(); i++){
            ps.add(propositions.get(i).makeCopy());
        }
        return new Ballot(ps);
    }

    public int getSize(){
        return propositions.size();
    }

    public SDCardPort getSdCardPort1(){
        return this.sdCardPort1;
    }

    /**
     * This method is a lock to prevent tampering once the ballot is completed
     */
    public void lock(){locked=true;}

    /**
     * Method to parse in the file. It will return a list of propositions
     * @param markUpFile the markup file.
     * @return List of propositions
     * @throws FileNotFoundException ..
     */
    private ArrayList<Proposition> extractInfo(File markUpFile) throws FileNotFoundException {
        Scanner myReader = new Scanner(markUpFile);
        ArrayList<Proposition> propositions = new ArrayList<>();

        //Scanning through the lines to search for the tokens and assign values accordingly
        while (myReader.hasNextLine()) {
            String line = myReader.nextLine();

            if(line.equals("/b")){
                propositions = parseBallot(myReader);
            }

        }
        return propositions;
    }

    /**
     * Method to parse the ballot ie /b (What is contained here) //b
     * @param myReader the scanner
     * @return ArrayList of propositions
     */
    private ArrayList<Proposition> parseBallot(Scanner myReader){
//        System.out.println("Start of ballot");
        ArrayList<Proposition> propositions = new ArrayList<>();

        while (myReader.hasNextLine()){
            String nextLine = myReader.nextLine();

            if (nextLine.equals("//b")){
//                System.out.println("End of ballot");
                return propositions;
            }
            if (nextLine.equals("/p")){
                Proposition p = parseProposition(myReader);
                propositions.add(p);
            }

        }
        return propositions;
    }

    /**
     * Method to parse the propositions ie /p (Whatever is in here) //p
     * @param myReader the scanner
     * @return Proposition
     */
    private Proposition parseProposition(Scanner myReader){
//        System.out.println("Start of proposition");

        String title = "";
        String description = "";
        ArrayList<String> options = new ArrayList<>();
        int numberOfSelections = 0;

        while (myReader.hasNextLine()) {
            String nextLine2 = myReader.nextLine();

            if (nextLine2.equals("//p")) {
//                System.out.println("Title for proposition: " + title.toString());
//                System.out.println("Description for proposition: " + description.toString());
//                System.out.println("Options for proposition: " + options.get(0).toString());
//                System.out.println("End of proposition");

//                System.out.println();
                return new Proposition(
                        title,
                        description,
                        new OptionsList(options, numberOfSelections),
                        numberOfSelections
                );
            }

            if (nextLine2.equals("/t")) {
                title = parseTitle(myReader);
            }
            if (nextLine2.equals("/d")){
                description = parseDescription(myReader);
            }
            if (nextLine2.equals("/n")){
                numberOfSelections = parseMaxNumOfSelections(myReader);
            }
            if (nextLine2.equals("/o")){
                options.add(parseOption(myReader));
            }
        }
        return new Proposition(null,null,null,0);
    }

    /**
     * Method to parse the options ie /o (Whatever is in here) //o
     * @param myReader the scanner
     * @return String of the option "OPTION1"
     */
    private String parseOption(Scanner myReader){

//            System.out.println("Start of option");
            String option = "";
            while (myReader.hasNext()){
                String nextLine6 = myReader.nextLine();
                if(nextLine6.equals("//o")){
//                    System.out.println("HERE 2" + option.toString());
                    return option;
                }
                else {
                    option = nextLine6;
                }
            }
            return option;
    }

    /**
     * Method to parse the max number of selections ie /n (Whatever is in here) //n
     * @param myReader the scanner
     * @return an int representing the max number of selections for a given proposition
     */
    private int parseMaxNumOfSelections(Scanner myReader){
        int numberOfSelections = 0;
//        System.out.println("Start of number of options");
        while (myReader.hasNext()){
            String nextLine5 = myReader.nextLine();
            if(nextLine5.equals("//n")){
//                System.out.println("End of number of options");
//                System.out.println();
                return numberOfSelections;
            }
            else {
                numberOfSelections = Integer.parseInt(nextLine5);
//                System.out.println(numberOfSelections);
            }
        }
        return numberOfSelections;
    }

    /**
     * Method to parse the description ie /d (Whatever is in here) //d
     * @param myReader the scanner
     * @return String representation of the description "DESCRIPTION"
     */
    private String parseDescription(Scanner myReader){
//        System.out.println("Start of description");
        StringBuilder description =  new StringBuilder();

        while (myReader.hasNext()){
            String nextLine4 = myReader.nextLine();
            if(nextLine4.equals("//d")){
//                System.out.println(description.toString());
//                System.out.println("End of description");
//                System.out.println();
                return description.toString();
            }
            else {
                description.append(nextLine4);

            }
        }
        return description.toString();
    }

    /**
     * Method to parse the title ie /t (Whatever is in here) //t
     * @param myReader the scanner
     * @return String representation of the title "TITLE"
     */
    private String parseTitle(Scanner myReader){
//        System.out.println("Start of title");
        StringBuilder title = new StringBuilder();

        while (myReader.hasNext()){
            String nextLine3 = myReader.nextLine();

            if(nextLine3.equals("//t")){
//                System.out.println(title.toString());
//                System.out.println("End of title");
//                System.out.println();
                return title.toString();

            }
            else {
                title.append(nextLine3);
            }
        }
//        System.out.println("HERE" + title.toString());
        return title.toString();
    }

    /**
     * Method to select certain option based upon certain proposition index
     * @param indexOfProposition the index of the proposition
     * @param indexOfOption the index of the option
     */
    public void selectCertainOption(int indexOfProposition, int indexOfOption){
        if(!locked) {
            propositions.get(indexOfProposition).getOptions().selectOptions(indexOfOption);
        }
    }

    /**
     * Method to print out the ballot to make sure it is working (For Testing)
     * @return null (Just for printing)
     */
    public String toString(){
        System.out.println("The ballot is as follows: ");
        System.out.println();
        int j = 1;
        for (Proposition prop : propositions){
            System.out.println("Proposition " + j);
            System.out.println("Title: " + prop.getTitle());
            System.out.println("Description: " + prop.getDescription());
            System.out.println("Max Number of Selections: " + prop.getNumberOfSelections());
            int i = 1;
            for (String option : prop.getOptions().getOptions()){
                System.out.println("Option " + i + ": " + option);
                i++;
            }
            System.out.println("End Proposition");
            System.out.println();
            j++;
        }
        System.out.println("End Ballot");
        System.out.println();
        return null;
    }

    public Proposition getPropositionAtIndex(int i) {
        return propositions.get(i);
    }
}
