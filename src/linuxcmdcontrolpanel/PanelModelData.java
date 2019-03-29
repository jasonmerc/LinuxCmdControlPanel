/*
Jason Mercede
jsm5801
 */
package linuxcmdcontrolpanel;

import java.util.HashMap;
import java.util.Iterator;

//class used to store history of commands run
public class PanelModelData {

    //storing commands in form of hashmap
    HashMap<Integer, String> history;
    //counter for storing history of commands
    private int counter = 1;

    public PanelModelData() {
        history = new HashMap();
    }

    //method to add command to history, with order they are run
    public void addCommand(String cmd) {
        history.put(counter, cmd);
        counter++;
    }

    //method to return entire hashmap of commands as string, in the order they were run
    public String getHistory(HashMap list) {
        String output = "";
        //iterator to go through all the items in the hashmap
        Iterator iterator = list.entrySet().iterator();
        while (iterator.hasNext()) {
            //store the pair of keys and values in a pair variable
            HashMap.Entry pair = (HashMap.Entry) iterator.next();
            //append the pair as "key value" to the output as a string
            output = output + pair.getKey().toString() + " " + pair.getValue().toString() + "\n";
        }
        return output;
    }
}
