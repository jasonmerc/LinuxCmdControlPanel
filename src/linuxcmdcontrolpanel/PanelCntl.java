/*
Jason Mercede
jsm5801
 */
package linuxcmdcontrolpanel;

import java.io.IOException;
import java.util.ArrayList;

class PanelCntl {

    PanelModel model;
    PanelView view;

    public PanelCntl() {
        model = new PanelModel();
        view = new PanelView(this);
    }

    //returns list of commands from model to put into the viewer
    public String[] getCommandList() {
        return model.getCommandList();
    }

    //runs the command specified in the view AND pipes output back to the view
    public void runCommand(String cmd, String parameter) throws IOException {
        view.clearOutputField();
        ArrayList<String> outputToPrint = model.runProcess(cmd, parameter);
        for (int i = 0; i < outputToPrint.size(); i++) {
            view.appendToOutputField(outputToPrint.get(i));
        }
    }

    //passes hashmap of command history to the viewer
    public String getCommandHistory() {
        if (!(model.history.history.isEmpty())) {
            return model.history.getHistory(model.history.history);
        } else {
            return "No history to show yet!";
        }
    }

    //calls model to export history to text file
    public void exportHistory() {
        makeViewerShowMessage(this.model.history.exportHistory());
    }

    //method to tell viewer to display calculated help message for the selected command
    public void getHelp(String cmd) {
        makeViewerShowMessage(this.model.getHelp(cmd));
    }

    //method to make the viewer display some sort of message as a dialog
    public void makeViewerShowMessage(String message) {
        this.view.showMessageDialog(message);
    }
}
