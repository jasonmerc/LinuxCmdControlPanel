/*
Jason Mercede
jsm5801

CHANGES:

-Ensured all method names were NOT Mission BBQ related, as per Cole's suggestion
-Changed Map to HashMap for PanelModelData (command history), having just a Map would mess up the order of commands when returning history
-Changed runCommand() method in PanelModel to finally implement ProcessBuilder, pipes command output to arraylist
-Added history button with appropriate methods to PanelView, shows command history in the order that you ran them
-Decided to make history button appear below output box instead of next to the run button, I like it in this location more
-Added "date" command to list of commands to choose from
-Changed window title code in the PanelView to reflect the operating system that you're using

HOW TO TEST:

-Simply open the program, run the commands in the list in any order, and check the command history to see what you ran
-Check your user's home directory in the file manager to ensure the ls command works properly, it should be giving real results

BUGS:

-Nothing that actually affects the functionality, however during compilation Xlint might or might not specify that something is unsafe.  I am unable to find out why this is
 */
package linuxcmdcontrolpanel;

public class LinuxCmdControlPanel {

    public static void main(String[] args) {
        PanelCntl cntl = new PanelCntl();
    }

}
