import javax.swing.JFrame;
import javax.swing.JOptionPane;

import Gui.control.Controller;

// ---------------------------------------------- MADE BY "GK SOFTWARE SOLUTIONS" ----------------------------------------------

public class App {
    public static void main(String[] args) throws Exception {
        // Frame to be shown if an error occurs while the program is starting
        JFrame error = new JFrame("Error");

        // Program starts as soon as the controller is initiated
        try {
            Controller c = new Controller();
            c.start();
        }
        catch (Exception e) {
            JOptionPane.showMessageDialog(error, "An error occured during the program's launch!");
        }
    }
}
