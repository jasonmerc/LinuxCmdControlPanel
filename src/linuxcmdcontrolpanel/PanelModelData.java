/*
Jason Mercede
jsm5801
 */
package linuxcmdcontrolpanel;

import java.util.HashMap;
import java.util.Iterator;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

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

    //method to write history file to text file in user's home directory
    public String exportHistory() {
        String output = "";
        try {
            FileWriter fwriter = new FileWriter(System.getProperty("user.home") + "/CommandHistory.txt", false);
            PrintWriter pwriter = new PrintWriter(fwriter);
            String data = "";
            if (history.isEmpty()) {
                data = "No command history yet!";
            } else {
                data = getHistory(history);
            }
            pwriter.write(data);
            pwriter.close();
            fwriter.close();
            output = "Exported history to text file in " + System.getProperty("user.name") + "'s home direcotry";
        } catch (IOException ioexechistory) {
            output = "Error writing history file to " + System.getProperty("user.name") + "'s home directory!";
        } finally {
            return output;
        }
    }
}
