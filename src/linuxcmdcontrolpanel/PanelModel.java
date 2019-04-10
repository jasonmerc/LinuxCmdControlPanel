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
    private final String[] commandsToPickFrom = {"date", "ls", "lscpu", "ps", "pwd", "whoami"};
    private final ProcessBuilder process;
    //string to store current working directory, not final because it can change
    private String directory;
    PanelModelData history;

    public PanelModel() {
        history = new PanelModelData();
        process = new ProcessBuilder();
        directory = System.getProperty("user.home");
    }

    //returns string array of commands that appear for the user to pick from
    public String[] getCommandList() {
        return this.commandsToPickFrom;
    }

    //method to return a string describing what selected command in viewer does
    public String getHelp(String cmd) {
        switch (cmd) {
            case "cd":
                return cmd + ": Changes directory to specified directory";
            case "cd ..":
                return cmd + ": Goes up a directory";
            case "date":
                return cmd + ": Shows the date and time on the machine";
            case "ls":
                return cmd + ": Lists contents of current directory";
            case "lscpu":
                return cmd + ": Displays CPU information of the machine";
            case "ps":
                return cmd + ": Lists running processes on the machine";
            case "pwd":
                return cmd + ": Prints the current working directory";
            case "whoami":
                return cmd + ": Prints user currently logged on";
            default:
                return "Error, command " + cmd + " not found in help database!";
        }
    }

    //method that gets output from command and returns it as arraylist of strings
    //currently defaults to user's home directory, may add functionality to change this later
    public ArrayList<String> runProcess(String cmd, String parameter) throws IOException {
        ArrayList<String> finalOut = new ArrayList<>();
        //add command to history of commands run
        history.addCommand(cmd);
        //switch statement to try and return output in "simple" manner
        try {
            switch (cmd) {
                case "whoami":
                    finalOut.add(System.getProperty("user.name"));
                    break;
                case "pwd":
                    finalOut.add(directory);
                    break;
                //default case is for actually running a process
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
                        } else if (cmd.equals("lscpu")) {
                            cmd = "wmic cpu list brief";
                            process.command("CMD", "/C", cmd);
                        } else if (System.getProperty("os.name").contains("Mac")) {
                            if (cmd.equals("lscpu")) {
                                process.command("sysctl", "-n", "machdep.cpu.brand_string");
                            } else if (cmd.equals("ps")) {
                                process.command("ps", "-e");
                            }
                        }
                        //"default" case for if you use linux
                    } else {
                        if (cmd.equals("ps")) {
                            process.command("ps", "-e");
                        } else {
                            process.command(cmd);
                        }
                    }
                    process.directory(new File(directory));
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
            System.err.println("IO Exception While Running Command");
        } finally {
            //finally at the end it returns the output
            return finalOut;
        }
    }
}
