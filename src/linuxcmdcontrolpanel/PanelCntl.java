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
    public void runCommand(String cmd) throws IOException {
        view.clearOutputField();
        ArrayList<String> outputToPrint = model.runProcess(cmd);
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
}
