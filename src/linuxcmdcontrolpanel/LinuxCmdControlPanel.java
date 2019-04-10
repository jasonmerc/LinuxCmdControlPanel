/*
Jason Mercede
jsm5801

CHANGES:

-Changed logic in viewer to make a separate runCommandButtonPressed method, this is needed to ask for input if "cd" is run, but NOT for anything else
-Changed logic in viewer that displayed message dialog, added a separate method for displaying any sort of generic message with a string as a parameter to pass
-Added method in PanelCntl to call PanelView's display-dialog method so that the model classes can call it too
-Added HELP button that describes what the selected command does
-Added EXPORT button that exports the command history to a text file in the user's home directory
-

HOW TO TEST:

-Simply open the program, run the commands in the list in any order, and check the command history to see what you ran
-When running any cd-related command, follow up with a "pwd" or "ls" to ensure it changed directories properly
-Press the HELP button for all the commands to ensure proper help is displayed for all of them
-Check the current working directory in the file manager to ensure the ls command works properly, it should be giving real results
-Export the command history to a text file, then check your home directory to make sure it exported properly

BUGS:

 */
package linuxcmdcontrolpanel;

public class LinuxCmdControlPanel {

    public static void main(String[] args) {
        PanelCntl cntl = new PanelCntl();
    }

}
