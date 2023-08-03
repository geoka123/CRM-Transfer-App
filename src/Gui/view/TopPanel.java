// ---------- Heading of the main frame ----------
package Gui.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;

import Gui.control.ControlledPanel;
import Gui.control.Controller;

public class TopPanel extends ControlledPanel{
    public TopPanel(Controller c) {   
        super(c);
        this.setLayout(new BorderLayout());
        this.setPreferredSize(new Dimension(900, 100));
    }   

    @Override
	public void paint(Graphics g) {
		int x = this.getWidth() / 2 - 200;
		int y = this.getHeight()/2;
        g.setFont(getFont().deriveFont(Font.BOLD));
        Font largeFontSize = g.getFont().deriveFont(15f);
        g.setFont(largeFontSize);
		g.drawString("Automatic order insertion into Vortex CRM", x, y);
	}
}
