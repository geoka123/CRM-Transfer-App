package Gui.view;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.sql.SQLException;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import Gui.control.ControlledPanel;
import Gui.control.Controller;

// This is the panel where the user has got certain options:
// 1) Select the client to whom he wants to insert the orders
// 2) Select the location of the client
// 3) Select the order code which contains the items that the user wants to get inseted 
// 4) Submit and fulfill the process
// 5) Clear all the fields if he has got any of them wrong
public class MainAreaPanel extends ControlledPanel{
    String[] emptyStrArray = new String[1];

    JComboBox<String> comboBoxOrders;
    JComboBox<String> comboBoxClients;
    JComboBox<String> comboBoxLocations;
    JFrame success = new JFrame("Success");
    JFrame error = new JFrame("Error");

    public MainAreaPanel(Controller c) throws SQLException, ClassNotFoundException{
        super(c);
        this.setLayout(null);
        this.setPreferredSize(new Dimension(900, 350));

        comboBoxOrders = new JComboBox<>(emptyStrArray);
		comboBoxOrders.setBounds(613, 127, 191, 21);
        comboBoxOrders.setEnabled(false);
        add(comboBoxOrders);

        comboBoxClients = new JComboBox<>(c.dropdownClientListFromVortexDatabase());
		comboBoxClients.setBounds(66, 127, 177, 21);
        comboBoxClients.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED)
                    c.enableLocations();
            }
        });
        add(comboBoxClients);

        comboBoxLocations = new JComboBox<>(emptyStrArray);
		comboBoxLocations.setBounds(334, 127, 191, 21);
        comboBoxLocations.setEnabled(false);
        comboBoxLocations.addActionListener((e) -> {
            if (e.getSource() == comboBoxLocations)
                c.enableOrders(); 
        });      
        add(comboBoxLocations);

        JLabel clientLabel = new JLabel("Client");
		clientLabel.setFont(new Font("Tahoma", Font.PLAIN, 18));
		clientLabel.setBounds(109, 89, 134, 28);
		add(clientLabel);

        JLabel locationLabel = new JLabel("Location");
		locationLabel.setFont(new Font("Tahoma", Font.PLAIN, 18));
		locationLabel.setBounds(390, 89, 134, 28);
		add(locationLabel);

        JLabel codeLabel = new JLabel("Order Code");
		codeLabel.setFont(new Font("Tahoma", Font.PLAIN, 18));
		codeLabel.setBounds(613, 89, 204, 28);
		add(codeLabel);

        // Either if errors may occur during the process of order insertion or the process is succesful,
        // a pop-up dialog box is being thrown.
        JButton submitBtn = new JButton("Submit");
		submitBtn.setBounds(200, 225, 124, 38);
        submitBtn.addActionListener((e) -> {
            try {
                c.pushOrderToClientLocation(comboBoxClients.getSelectedItem().toString(), comboBoxLocations.getSelectedItem().toString(), comboBoxOrders.getSelectedItem().toString());
            } catch (ClassNotFoundException e1) {
                e1.printStackTrace();
                JOptionPane.showMessageDialog(error, "An error occured while inserting the selected order into Vortex CRM!" + "\n" + "Error log: " + e1.getMessage());
            } catch (SQLException e1) {
                e1.printStackTrace();
                JOptionPane.showMessageDialog(error, "An error occured while inserting the selected order into Vortex CRM!" + "\n" + "Error log: " + e1.getMessage());
            }
            catch (Exception e2) {
                JOptionPane.showMessageDialog(error, "An error occured while inserting the selected order into Vortex CRM!" + "\n" + "Make sure that you have got all the fields selected!");
            }
        });
        add(submitBtn);

        JButton clearBtn = new JButton("Clear");
        clearBtn.setBounds(511, 225, 124, 38);
        clearBtn.addActionListener((e) -> {
            comboBoxClients.setSelectedIndex(-1);
            comboBoxLocations.setSelectedIndex(-1);
            comboBoxLocations.setEnabled(false);
            comboBoxOrders.setSelectedIndex(-1);
            comboBoxOrders.setEnabled(false);
        });
        add(clearBtn);

        JLabel watermark = new JLabel(new ImageIcon("src\\Gui\\images\\GK_LOGO__1_-removebg-preview.png"));
		watermark.setBounds(760, 278, 80, 50);
		add(watermark);
    }

    public JComboBox<String> getComboBoxOrders() {
        return comboBoxOrders;
    }

    public void setComboBoxOrders(JComboBox<String> comboBoxOrders) {
        this.comboBoxOrders = comboBoxOrders;
    }

    public JComboBox<String> getComboBoxClients() {
        return comboBoxClients;
    }

    public void setComboBoxClients(JComboBox<String> comboBoxClients) {
        this.comboBoxClients = comboBoxClients;
    }

    public JComboBox<String> getComboBoxLocations() {
        return comboBoxLocations;
    }

    public void setComboBoxLocations(JComboBox<String> comboBoxLocations) {
        this.comboBoxLocations = comboBoxLocations;
    }
}
