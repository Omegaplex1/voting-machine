
import java.io.Serializable;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.OptionalLong;
import java.util.stream.Collectors;


public class Template implements Serializable {
    private String title;
    private String description;
    private int selections;
    private HashMap<String, Boolean> options = new HashMap<>();
    private int index;

    public Template(String title, String description, int selections,HashMap<String,Boolean> options) {
        this.title = title;
        this.description = description;
        this.selections = selections;
        this.options = options;

    }
    public Template(String device){
        this.title = "Failure occured with " + device;
        this.description = "Please sellect and option below";
        this.selections = 0;
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
    public OptionsList getOptionsList() {
        ArrayList<String> list = new ArrayList<>(options.keySet());
        int howMany = selections;
        OptionsList temp = new OptionsList(list,selections);
        for(int i = 0;i < options.size();i++){
            temp.fillboolean(i,options.get(list.get(i)));
        }
        return temp;

   }
    public int getIndex() {
        return index;
    }


}