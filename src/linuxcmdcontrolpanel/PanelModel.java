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
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

class PanelModel {

    //string array for selection of commands to choose from
    private final String[] commandsToPickFrom = {"cd", "cd ..", "date", "ls", "lscpu", "ps", "pwd", "whoami"};
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
                /*
                okay get ready, this is a long section here
                this changes the directory in both windows and non-windows systems
                if the directory does not exist, it refuses to do so
                 */
                case "cd":
                    if (System.getProperty("os.name").contains("Windows")) {
                        String oldDirectory = directory;
                        directory = directory + "\\" + parameter + "\\";
                        //replace any of the double slashes that sometimes pop up
                        directory = directory.replace("\\\\", "\\");
                        //Path object parses the literal path from the directory string
                        Path path = Paths.get(directory);
                        //if the new directory the user wants to cd into exists, do it
                        if (Files.exists(path)) {
                            finalOut.add("**Directory changed to " + directory + "**");
                            break;
                            //if the new directory doesnt exist, set it to the old directory and give an error
                        } else {
                            finalOut.add("**Error changing directory to " + directory + "**");
                            directory = oldDirectory;
                            break;
                        }
                    } else {
                        String oldDirectory = directory;
                        directory = directory + "/" + parameter + "/";
                        directory = directory.replace("//", "/");
                        //try to get literal path with Path object from directory string
                        Path path = Paths.get(directory);
                        //if the directory actually exists, go into it
                        if (Files.exists(path)) {
                            finalOut.add("**Directory changed to " + directory + "**");
                            break;
                            //if it doesnt exist, go back to the old directory and give an error
                        } else {
                            finalOut.add("**Error changing directory to " + directory + "**");
                            directory = oldDirectory;
                            break;
                        }
                    }
                /*
                okay get ready, this is a long section here as well
                this changes the directory up in both windows and non-windows systems
                if you're at the root it will refuse to go up further
                 */
                case "cd ..":
                    if (System.getProperty("os.name").contains("Windows")) {
                        //if the current directory isn't the root directory, try to cd up
                        if (!(directory.equals("C:\\"))) {
                            //arraylist to store each bit of the path, separated by slashes
                            ArrayList<String> directoryTemp = new ArrayList<>();
                            //windows uses the backslash, but temporarily change these to the other slash so we can manipualte it easier
                            directory = directory.replace("\\", "/");
                            //split directory based on the slashes
                            String[] split = directory.split("/");
                            //start with a blank slate for directory variable
                            directory = "";
                            //for each item in the array minus last one, add it to the arraylist with a slash
                            for (int i = 0; i < split.length - 1; i++) {
                                directoryTemp.add(split[i] + "/");
                            }
                            //now for each item in the arraylist, append it to the real sdirectory string
                            for (int i = 0; i < directoryTemp.size(); i++) {
                                directory = directory + directoryTemp.get(i);
                            }
                            //remove duplicate slashes
                            directory = directory.replace("//", "/");
                            //return the forward slashes to normal backslashes for windows
                            directory = directory.replace("/", "\\");
                            finalOut.add("**Directory changed to " + directory + "**");
                            break;
                            //if you're already at the root, dont cd up because nothing else exists
                        } else {
                            finalOut.add("**Can't change directory, already at root**");
                            break;
                        }
                    } else {
                        if (!(directory.equals("/"))) {
                            //make arraylist for directory modification
                            ArrayList<String> directoryTemp = new ArrayList<>();
                            //make absic array that splits directory based on "/" delimiter
                            String[] split = directory.split("/");
                            //reset directory to blank
                            directory = "";
                            //for each item in the array minus the last, add to arraylist with a slash
                            for (int i = 0; i < split.length - 1; i++) {
                                directoryTemp.add(split[i] + "/");
                            }
                            //for each item in the arraylist now, append back to the original directory string
                            for (int i = 0; i < directoryTemp.size(); i++) {
                                directory = directory + directoryTemp.get(i);
                            }
                            //remove any duplicate slashes there may be
                            directory = directory.replace("//", "/");
                            finalOut.add("**Directory changed to " + directory + "**");
                            break;
                            //if you're already at the root, then don't do anything because you're already at the top
                        } else {
                            finalOut.add("**Can't change directory, already at root**");
                            break;
                        }
                    }
                //default case for running any other process not specified
                default:
                    //makes new ProcessBuilder object to prepare for process running
                    //this first case here is for translation if you use windows
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
                        } else if (cmd.equals("ps")) {
                            cmd = "tasklist";
                            process.command("CMD", "/C", cmd);
                        }
                        //case for translation if you use mac
                    } else if (System.getProperty("os.name").contains("Mac")) {
                        if (cmd.equals("lscpu")) {
                            process.command("sysctl", "-n", "machdep.cpu.brand_string");
                        } else if (cmd.equals("ps")) {
                            process.command("ps", "-e");
                        }
                        //case for translation if you use linux
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
            finalOut.add("IO Exception While Running Command");
        } finally {
            //finally at the end it returns the output arraylist
            return finalOut;
        }
    }
}
