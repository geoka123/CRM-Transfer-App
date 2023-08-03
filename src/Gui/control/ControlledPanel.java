package Gui.control;

import javax.swing.JPanel;

// This is a panel that implements the controller class. Both TopPanel and MainAreaPanel classes
// inherit from this class.
public class ControlledPanel extends JPanel{
    protected Controller c;

    public ControlledPanel(Controller c) {
        this.c = c;
    }

    public Controller getC() {
        return c;
    }

    public void setC(Controller c) {
        this.c = c;
    }
}
