package Gui.control;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Vector;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import Gui.view.MainFrame;

// The controller is the class that connects the front end with the back end 
// for a smoother and safer functionality of the program.
public class Controller extends WindowAdapter{
    MainFrame view;

    JFrame error = new JFrame("Error");
    JFrame success = new JFrame("Success");

    public Controller() {

    }

    public void start() throws SQLException, ClassNotFoundException{
        try {
        this.view = new MainFrame(this);
        this.view.addWindowListener(this);
        this.view.setVisible(true);
        dropdownClientListFromVortexDatabase();
        }
        catch(SQLException e) {
            JOptionPane.showMessageDialog(error, "Make sure that you are connected to the SQL Server!");
            quit();
        }
    }

    @Override
    public void windowClosing(WindowEvent event) {
        quit();
    }

    public void quit() {
        System.out.println("Exiting...");
        System.exit(1);
    }

    public Statement connectToPylon() throws SQLException, ClassNotFoundException{
        String connectionUrlPylon = "jdbc:sqlserver://yourServerName;"
                        + "databaseName=yourPylonDatabaseName;"
                        + "user=yourUsername;"
                        + "password=yourPassword;"
                        + "encrypt=false;"
                        + "characterEncoding=UTF-8;";
                
        Connection pConnection = DriverManager.getConnection(connectionUrlPylon);
        Statement statement = pConnection.createStatement();

        return statement;
    }

    public Statement connectToVortex() throws SQLException, ClassNotFoundException{
        String connectionUrlPylon = "jdbc:sqlserver://yourServerName;"
                        + "databaseName=yourVortexDatabaseName;"
                        + "user=yourUsername;"
                        + "password=yourPassword;"
                        + "encrypt=false;"
                        + "characterEncoding=UTF-8;";
                
        Connection pConnection = DriverManager.getConnection(connectionUrlPylon);
        Statement statement = pConnection.createStatement();
        
        return statement;
    }

    // Querying all of the clients from the Vortex database
    public Vector<String> dropdownClientListFromVortexDatabase() throws SQLException, ClassNotFoundException{
        Statement statement = connectToVortex();

        String connStr = "";
        Vector<String> ddList = new Vector<>();

        String sqlStr = "SELECT DISTINCT Company FROM Customers ORDER BY Company";

        ResultSet resultSet = statement.executeQuery(sqlStr);

        while (resultSet.next()) {
            connStr += resultSet.getString(1) + "\n";
        }
        
        String[] strArray = connStr.split("\n");
        for (int i=0; i<strArray.length; i++) {
            ddList.add(strArray[i]);
        }
        
        return ddList;
    }

    // Querying all of the clients's locations from the Vortex database
    public Vector<String> dropdownLocationListFromVortexDatabase(String clientName) throws SQLException, ClassNotFoundException {
        Statement statement = connectToVortex();

        String connStr = "";
        Vector<String> ddList = new Vector<>();

        String customerId = "";
        String cIdQuery = "SELECT CustomerId FROM Customers WHERE Company COLLATE GREEK_CI_AI = N'"+ clientName +"'";
        ResultSet resSet = statement.executeQuery(cIdQuery);
        while (resSet.next()) {
            customerId += resSet.getString(1);
        }

        String sqlStr = "SELECT ProjectDescription FROM Projects WHERE CustomerId=" + customerId;
        ResultSet resultSet = statement.executeQuery(sqlStr);

        while (resultSet.next()) {
            connStr += resultSet.getString(1) + "\n";
        }

        String[] strArray = connStr.split("\n");
        for (int i=0; i<strArray.length; i++) {
            ddList.add(strArray[i]);
        }

        return ddList;
    }

    // Querying all of the order codes from the Anysma database
    public Vector<String> dropdownOrderListFromPylonDatabase() throws SQLException, ClassNotFoundException{
        Statement statement = connectToPylon();

        String connStr = "";
        Vector<String> ddList = new Vector<>();

        String sqlStr = "SELECT DISTINCT HEDOCCODE FROM HEDOCENTRIES WHERE HEDOCCODE LIKE 'ΠΑΡ%'";
        ResultSet resultSet = statement.executeQuery(sqlStr);

        while (resultSet.next()) {
            connStr += resultSet.getString(1) + "\n";
        }

        String[] strArray = connStr.split("\n");
        for (int i=0; i<strArray.length; i++) {
            ddList.add(strArray[i]);
        }

        return ddList;
    }

    // Enabling the client's locations field after a client has been chosen.
    public void enableLocations() {
        this.view.getMainAreaPanel().getComboBoxLocations().setEnabled(true);
        try {
            if (this.view.getMainAreaPanel().getComboBoxClients().getSelectedItem().toString() != null)
                    this.view.getMainAreaPanel().getComboBoxLocations().setModel(new DefaultComboBoxModel<>(this.dropdownLocationListFromVortexDatabase(this.view.getMainAreaPanel().getComboBoxClients().getSelectedItem().toString())));
        } catch (ClassNotFoundException | SQLException e) {
                e.printStackTrace();
        }
    }

    // Enabling the order code field after a client's location has been chosen.
    public void enableOrders() {
        this.view.getMainAreaPanel().getComboBoxOrders().setEnabled(true);
        try {
            this.view.getMainAreaPanel().getComboBoxOrders().setModel(new DefaultComboBoxModel<>(this.dropdownOrderListFromPylonDatabase()));

        }
        catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    // Inserting the items of an order to a specific client's location.
    public void pushOrderToClientLocation(String clientName, String locationName, String orderCode) throws SQLException, ClassNotFoundException{
        Statement pstatement = connectToPylon();

        String connectionUrlVortex = "jdbc:sqlserver://yourServerName;"
                        + "databaseName=yourVortexDatabaseName;"
                        + "user=yourUsername;"
                        + "password=yourPassword;"
                        + "encrypt=false;"
                        + "characterEncoding=UTF-8;";
        Connection vConnection = DriverManager.getConnection(connectionUrlVortex);
        Statement vstatement = vConnection.createStatement();

        String customerId = "";
        String customerIdQuery = "SELECT CustomerId FROM Customers WHERE Company COLLATE GREEK_CI_AI = N'"+ clientName +"'";        
        ResultSet cIdSet = vstatement.executeQuery(customerIdQuery);
        while (cIdSet.next()) {
            customerId += cIdSet.getString(1);
        }

        String projectId = "";
        String projectIdQuery = "SELECT ProjectId FROM Projects WHERE CustomerId =" + customerId + " AND ProjectDescription COLLATE Greek_CI_AI = N'" + locationName + "'";
        ResultSet pIdSet = vstatement.executeQuery(projectIdQuery);
        while (pIdSet.next()) {
            projectId += pIdSet.getString(1);
        }
        System.out.println("Customer Id: " + customerId);
        System.out.println("Project Id: " + projectId);

        String heId = "";
        String itemDescriptions = "";
        String heIdQuery = "SELECT HEID FROM HEDOCENTRIES WHERE HEDOCCODE='" + orderCode + "'";    
        
        ResultSet heIdSet = pstatement.executeQuery(heIdQuery);
        while (heIdSet.next()) {
            heId += heIdSet.getString(1);
        }
        System.out.println("HEID: " + heId);

        String codesDescriptionsQuantitiesQuery = "SELECT HEITEMCODE FROM HECENTLINES WHERE HEDENTID = '" + heId + "'";
        ResultSet codeSet = pstatement.executeQuery(codesDescriptionsQuantitiesQuery);
        while (codeSet.next()) {
            itemDescriptions += codeSet.getString(1);
            itemDescriptions += ",";
        }
        
        String[] codeArray = itemDescriptions.split(",");
        for (int i=0; i<codeArray.length; i++) {
            System.out.println("Product #" + (i+1) + " code: " + codeArray[i]); 
        }
        ArrayList<String> prodIdArray = new ArrayList<>();
        int rowsAffected = 0;
        
        for (int i=0; i<codeArray.length; i++) {
            String prodIdQuery = "SELECT ProductId FROM Products WHERE ProductCode = N'" + codeArray[i] + "'";
            ResultSet rs = vstatement.executeQuery(prodIdQuery);
            while (rs.next()) {
                prodIdArray.add(rs.getString(1));
                System.out.println("Product #" + (i+1) + " Id: " + rs.getString(1));
            }
        }

        try {
            int updateCount=0;
            for (int i=0; i<prodIdArray.size(); i++) {
                String insertToVortexQuery = "INSERT INTO ProjectProducts (ProjectId, ProductId, Installed, Removed, IsGroup, DateTimeCreated, DateTimeModified, CompanyId, RowGuid) VALUES(" + projectId + "," + prodIdArray.get(i) + ", 0, NULL, NULL, GETDATE(), GETDATE(), 1, NEWID())";
                PreparedStatement vPreparedStatement = vConnection.prepareStatement(insertToVortexQuery);
                rowsAffected = vPreparedStatement.executeUpdate();
                updateCount++;
            }
            JOptionPane.showMessageDialog(success, "Order insertion was completed succesfully!" + "\n" + rowsAffected*updateCount + " products were inserted.");
            System.out.println(rowsAffected*updateCount + " row(s) inserted successfully.");
        }
        catch (Exception e) {
            JOptionPane.showMessageDialog(error, "An error occured while pushing the items of an order to the selected clients's location");
            quit();
        }
    }
}
