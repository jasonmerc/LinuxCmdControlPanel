/*
Jason Mercede
jsm5801

CHANGES:

-Changed logic in viewer to make a separate runCommandButtonPressed method, this is needed to ask for input if "cd" is run, but NOT for anything else
-Changed logic in viewer that displayed message dialog, added a separate method for displaying any sort of generic message with a string as a parameter to pass
-Added method in PanelCntl to call PanelView's display-dialog method so that the model classes can call it too
-Added HELP button that describes what the selected command does
-Added EXPORT button that exports the command history to a text file in the user's home directory
-Added "cd" and "cd .." commands to navigate directories in the system, actually changes the working directory too!
-Imported Files & Paths classes to the PanelModel class, this has to do with exception handling when trying to cd to a directory that doesn't exist

HOW TO TEST:

-Simply open the program, run the commands in the list in any order, and check the command history to see what you ran
-When running any cd-related command, follow up with a "pwd" or "ls" to ensure it changed directories properly
-Press the HELP button for all the commands to ensure proper help is displayed for all of them
-Check the current working directory in the file manager to ensure the ls command works properly, it should be giving real results
-Export the command history to a text file, then check your home directory to make sure it exported properly (for Windows this is C:\Users\[YOU\)

BUGS:
-XLint gives an error when compiling (unsafe practices) but I am not able to determine what it leads to.  Otherwise it doesn't affect the program in any way
 */
package linuxcmdcontrolpanel;

public class LinuxCmdControlPanel {

    public static void main(String[] args) {
        PanelCntl cntl = new PanelCntl();
    }

}
