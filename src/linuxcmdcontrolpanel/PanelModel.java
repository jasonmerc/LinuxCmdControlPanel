/*
Jason Mercede
jsm5801
 */
package linuxcmdcontrolpanel;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

class PanelModel {

    //string array for selection of commands to choose from
    private final String[] commandsToPickFrom = {"date", "ls", "pwd", "whoami"};
    private final ProcessBuilder process;
    PanelModelData history;

    public PanelModel() {
        history = new PanelModelData();
        process = new ProcessBuilder();
    }

    //returns string array of commands that appear for the user to pick from
    public String[] getCommandList() {
        return this.commandsToPickFrom;
    }

    //method that gets output from command and returns it as arraylist of strings
    //currently defaults to user's home directory, may add functionality to change this later
    public ArrayList<String> runProcess(String cmd) throws IOException {
        ArrayList<String> finalOut = new ArrayList<>();
        //add command to history of commands run
        history.addCommand(cmd);
        try {
            switch (cmd) {
                case "whoami":
                    finalOut.add(System.getProperty("user.name"));
                    break;
                case "pwd":
                    finalOut.add(System.getProperty("user.home"));
                    break;
                default:
                    //makes new ProcessBuilder object to prepare for process running
                    //converts 'ls' to 'dir' if user is running Windows
                    if (System.getProperty("os.name").contains("Windows")) {
                        if (cmd.equals("ls")) {
                            cmd = "dir";
                            process.command("CMD", "/C", cmd);
                        } else if (cmd.equals("date")) {
                            cmd = "echo %date%";
                            process.command("CMD", "/C", cmd);
                        }
                    } else {
                        process.command(cmd);
                    }
                    process.directory(new File(System.getProperty("user.home")));
                    //runs command, gets output with bufferedreader and inputstreamreader
                    Process p2 = process.start();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(p2.getInputStream()));
                    String line = "";
                    while ((line = reader.readLine()) != null) {
                        finalOut.add(line + "\n");
                    }
                    reader.close();
                    break;
            }
        } catch (IOException ioexec) {
            finalOut.add("IO Exception While Running Command");
            System.err.println("IO Exception While Running Command");
        } finally {
            //finally at the end it returns the output
            return finalOut;
        }
    }
}
