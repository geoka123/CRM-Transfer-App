package Gui.view;

import java.awt.Container;
import java.awt.Dimension;
import java.sql.SQLException;
import java.awt.BorderLayout;

import javax.swing.JFrame;

import Gui.control.Controller;

// The main frame of the application contains:
// 1) A top panel (The heading of the frame)
// 2) A Main Area Panel, where the main logic of the program exists
public class MainFrame extends JFrame {
	private TopPanel topPanel;
	private MainAreaPanel mainAreaPanel;
    static public final int WIDTH = 900;
	static public final int HEIGHT = 450;
	Controller controller; // Initiating the controller

	public MainFrame(Controller controller) throws SQLException, ClassNotFoundException{
		Container c = this.getContentPane();
		this.controller = controller;
		this.setTitle("Order Insertion");

        c.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        
		topPanel = new TopPanel(controller);
		this.add(topPanel, BorderLayout.PAGE_START);

		mainAreaPanel = new MainAreaPanel(controller);
		this.add(mainAreaPanel, BorderLayout.PAGE_END);

        this.pack();
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	}

	public MainAreaPanel getMainAreaPanel() {
		return mainAreaPanel;
	}

	public void setMainAreaPanel(MainAreaPanel mainAreaPanel) {
		this.mainAreaPanel = mainAreaPanel;
	}
}