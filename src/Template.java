
import java.io.Serializable;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;


public class Template implements Serializable {
    private String title;
    private String description;
    private int selections;
    private HashMap<String, Boolean> options = new HashMap<>();
    private String who;

    public Template(String who,String title, String description, int selections,HashMap<String,Boolean> options) {
        this.who = who;
        this.title = title;
        this.description = description;
        this.selections = selections;
        this.options = options;

    }
    public Template(String device){
        this.title = "Failure occured with " + device;
        this.description = "Please sellect and option below";
        this.selections = 0;
        this.options = new HashMap<>();
        options.put("Power Down",false);
        options.put("Suspend",false);
    }

    public String getTitle() {
        return title;
    }
    public String getDescription() {
        return description;
    }
    public int getSelections() {
        return selections;
    }
    public HashMap<String,Boolean> getOptions() {
        return options;
    }


}