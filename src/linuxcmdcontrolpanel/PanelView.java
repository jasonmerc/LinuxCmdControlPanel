/*
Jason Mercede
jsm5801
 */
package linuxcmdcontrolpanel;

import java.awt.BorderLayout;
import java.io.IOException;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JComboBox;
import javax.swing.JScrollPane;
import javax.swing.JOptionPane;

class PanelView extends JFrame {

    private final JComboBox listOfCommands;
    private final JButton runButton, histButton, exportButton, helpButton;
    private final JTextArea outputArea;
    private final JLabel commandListLabel, outputAreaLabel, blankSpace;
    private final PanelCntl cntl;
    private final JScrollPane scrollPane;
    private JPanel instrumentPanel, cmdSelectPanel, outputPanel, bottomButtonPanel, runHelpPanel;

    public PanelView(PanelCntl cntl) {
        this.cntl = cntl;
        this.listOfCommands = new JComboBox(cntl.getCommandList());
        this.runButton = new JButton();
        this.histButton = new JButton();
        this.exportButton = new JButton();
        this.helpButton = new JButton();
        this.outputArea = new JTextArea(20, 10);
        this.blankSpace = new JLabel("   ");
        this.commandListLabel = new JLabel();
        this.outputAreaLabel = new JLabel();
        this.scrollPane = new JScrollPane(outputArea, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        initComponents();
    }

    private void initComponents() {
        //window title will change based on operating system
        setTitle(System.getProperty("os.name") + " Command Control Panel");
        setSize(640, 480);
        setLocationRelativeTo(null);  // center the frame
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        outputArea.setLineWrap(true);
        outputArea.setRows(20);
        outputArea.setColumns(1);
        scrollPane.setSize(200, 200);

        runButton.setText("RUN");
        histButton.setText("SHOW HISTORY");
        exportButton.setText("EXPORT HISTORY");
        helpButton.setText("HELP");
        outputAreaLabel.setText("OUTPUT:");
        commandListLabel.setText("COMMANDS TO PICK FROM:");

        outputArea.setEditable(false);
        outputArea.setRows(30);
        outputArea.setColumns(30);
        outputArea.setLineWrap(true);

        runButton.addActionListener(event -> runCommandButtonPressed());
        histButton.addActionListener(event -> showMessageDialog(cntl.getCommandHistory()));
        helpButton.addActionListener(event -> cntl.getHelp(listOfCommands.getSelectedItem().toString()));
        exportButton.addActionListener(event -> cntl.exportHistory());

        runHelpPanel = new JPanel(new BorderLayout());
        runHelpPanel.add(helpButton, BorderLayout.WEST);
        runHelpPanel.add(runButton, BorderLayout.EAST);

        cmdSelectPanel = new JPanel(new BorderLayout());
        cmdSelectPanel.add(commandListLabel, BorderLayout.NORTH);
        cmdSelectPanel.add(listOfCommands, BorderLayout.CENTER);
        cmdSelectPanel.add(runHelpPanel, BorderLayout.EAST);
        cmdSelectPanel.add(blankSpace, BorderLayout.SOUTH);

        bottomButtonPanel = new JPanel(new BorderLayout());
        bottomButtonPanel.add(histButton, BorderLayout.EAST);
        bottomButtonPanel.add(exportButton, BorderLayout.WEST);

        //i'm moving the history button down below the output window, not next to "RUN"
        outputPanel = new JPanel(new BorderLayout());
        outputPanel.add(outputAreaLabel, BorderLayout.NORTH);
        outputPanel.add(scrollPane, BorderLayout.CENTER);
        outputPanel.add(bottomButtonPanel, BorderLayout.SOUTH);

        instrumentPanel = new JPanel(new BorderLayout());
        instrumentPanel.add(cmdSelectPanel, BorderLayout.NORTH);
        instrumentPanel.add(outputPanel, BorderLayout.CENTER);

        this.add(instrumentPanel);

        setVisible(true);
    }

    //separate method to run the command selected
    //this "bit of logic" needs to be in the viewer to properly transfer the additional parameter for "cd"
    public void runCommandButtonPressed() {
        try {
            //if the command is the "cd" command specifically which needs additional input...
            if (listOfCommands.getSelectedItem().toString().equals("cd")) {
                //ask user to input a folder (relative path) in working directory
                String newDirectory = JOptionPane.showInputDialog(this, "Please input the folder in the current working directory to move to");
                //if the input isn't empty or cancel button not pressed, try to go into that directory
                if (!(newDirectory == null || newDirectory.isEmpty())) {
                    cntl.runCommand(listOfCommands.getSelectedItem().toString(), newDirectory);
                    //else just abort the operation and do nothing
                } else {
                    showMessageDialog("Operation aborted");
                }
                //if the command isn't "cd" that needs input, just run the command
            } else {
                cntl.runCommand(listOfCommands.getSelectedItem().toString(), null);
            }
            //if something goes wrong, show a message that it's broken
        } catch (IOException ex) {
            showMessageDialog("IO Exception From Viewer While Running Command");
        }
    }

    //automatically appends new line of text followed by a line break
    //does not print any null lines
    public void appendToOutputField(String text) {
        outputArea.append(text);
    }

    //clears output field, currently used before each new cmd is run
    public void clearOutputField() {
        outputArea.setText("");
    }

    //generic method to display a message dialog with some text
    public void showMessageDialog(String message) {
        JOptionPane.showMessageDialog(this, message);
    }
}
