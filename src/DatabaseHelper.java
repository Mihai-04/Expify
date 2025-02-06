import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DatabaseHelper {
    RedudantMethods redudantMethods = new RedudantMethods();

    Connection c = null;
    Statement stmt = null;
    ResultSet rs = null;
    String url = "jdbc:sqlite:carsDB.db";
    public void insertValue(String carPlate, String vinNumber, LocalDate rcaDate, LocalDate insuranceDate) {
        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection(url);
            c.setAutoCommit(false);
            System.out.println("Opened database successfully");

            String sql = "INSERT INTO CARS ([Num plate], VIN, [RCA DATE], [Insurance date], [Days left]) VALUES (?, ?, ?, ?, ?);";
            PreparedStatement ps = c.prepareStatement(sql);
            ps.setString(1, carPlate);
            ps.setString(2, vinNumber);
            ps.setString(3, rcaDate.toString());
            ps.setString(4, insuranceDate.toString());
            ps.setString(5, String.valueOf(redudantMethods.daysLeft(rcaDate)));
            ps.executeUpdate();

            ps.close();
            c.commit();
            c.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void printCars(JPanel mainPanel) {
        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection(url);

            String sql = "SELECT * FROM CARS";
            stmt = c.createStatement();
            rs = stmt.executeQuery(sql);
            while(rs.next()) {
                String plate = rs.getString("Num plate");
                String vinNumber = rs.getString("VIN");
                String daysLeft = rs.getString("Days left");

                JPanel panel = new JPanel();
                redudantMethods.setPanelDesign(panel, new Dimension(500, 50), new Color(45, 45, 45), new FlowLayout(FlowLayout.LEFT), null);

                JTextField carPlate = new JTextField(plate + ":");
                redudantMethods.setTextFieldDesign(carPlate, new Dimension(90, 45), null, new Color(150, 150, 150),
                        new Font("Arial", Font.BOLD, 14), new Color(45, 45, 45), false);
                panel.add(carPlate);

                JTextField expiry = new JTextField("Days until expiration date: " + daysLeft);
                redudantMethods.setTextFieldDesign(expiry, new Dimension(210, 45), null, new Color(150, 150, 150),
                        new Font("Arial", Font.BOLD, 14), new Color(45, 45, 45), false);
                panel.add(expiry);

                JButton viewMoreButton = new JButton("MORE");
                redudantMethods.setButtonDesign(viewMoreButton, new Dimension(50, 30), Color.black, Color.white, new Font("Arial", Font.BOLD, 12));
                panel.add(viewMoreButton);

                mainPanel.add(Box.createRigidArea(new Dimension(10, 30)));
                mainPanel.add(panel);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

   public boolean isDatabaseEmpty() {
       try {
           Class.forName("org.sqlite.JDBC");
           c = DriverManager.getConnection(url);
           String sql = "SELECT COUNT(*) FROM CARS";
           stmt = c.createStatement();
           rs = stmt.executeQuery(sql);
           if(rs.next()) {
               return rs.getInt(1) == 0;
           }
       } catch (Exception e) {
           throw new RuntimeException(e);
       } finally {
           try {
               if (rs != null) rs.close();
               if (stmt != null) stmt.close();
               if (c != null) c.close();
           } catch (SQLException e) {
               e.printStackTrace();
           }
       }
       return false;
   }

   public boolean isDataUnique(String numPlate, String vinNumber) {
        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection(url);
            c.setAutoCommit(false);
            String sql = "SELECT [Num plate], VIN FROM CARS";
            stmt = c.createStatement();
            rs = stmt.executeQuery(sql);
            while(rs.next()) {
                String plate = rs.getString("Num plate").replaceAll("\\s+","");
                if(plate.equals(numPlate) || rs.getString("VIN").equals(vinNumber)) {
                    return true;
                }
            }
            stmt.close();
            rs.close();
            c.commit();
            c.close();
            return false;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
   }

   public void updateDaysLeft() {
       try {
           Class.forName("org.sqlite.JDBC");
           c = DriverManager.getConnection(url);
           c.setAutoCommit(false);
           String select = "SELECT [Days left], [RCA DATE] FROM CARS";
           stmt = c.createStatement();
           rs = stmt.executeQuery(select);

           String updateSQL = "UPDATE CARS SET [Days left] = ? WHERE [RCA DATE] = ?";
           PreparedStatement ps = c.prepareStatement(updateSQL);
           DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

           while(rs.next()) {
               String chosenDate = rs.getString("RCA DATE");
               LocalDate localDate = LocalDate.parse(chosenDate, formatter);
               long daysLeft = redudantMethods.daysLeft(localDate);
               ps.setLong(1, daysLeft);
               ps.setString(2, chosenDate);
               ps.executeUpdate();
           }
           rs.close();
           stmt.close();
           ps.close();
           c.commit();
           c.close();
       } catch (Exception e) {
           throw new RuntimeException(e);
       }
   }
}
