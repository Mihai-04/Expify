import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
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
            long rcaDateLong = redudantMethods.daysLeft(rcaDate);
            long insuranceDateLong = redudantMethods.daysLeft(insuranceDate);

            ps.setString(1, carPlate);
            ps.setString(2, vinNumber);
            ps.setString(3, rcaDate.toString());
            ps.setString(4, insuranceDate.toString());
            ps.setString(5, String.valueOf(Math.min(rcaDateLong, insuranceDateLong)));
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
                String rcaDate = rs.getString("RCA DATE");
                long rcaDays = redudantMethods.daysLeft(LocalDate.parse(rcaDate));
                String insuranceDate = rs.getString("Insurance date");
                long insuranceDays = redudantMethods.daysLeft(LocalDate.parse(insuranceDate));

                JPanel panel = new JPanel();
                redudantMethods.setPanelDesign(panel, new Dimension(400, 50), new Color(45, 45, 45), new FlowLayout(FlowLayout.LEFT), null);
                panel.add(Box.createHorizontalStrut(10));

                JTextField carPlate = new JTextField(plate + ":");
                redudantMethods.setTextFieldDesign(carPlate, new Dimension(90, 45), null, new Color(150, 150, 150),
                        new Font("Arial", Font.BOLD, 14), new Color(45, 45, 45), false);
                panel.add(carPlate);

                JTextField expiry = new JTextField("Closest expiration date: " + daysLeft);
                redudantMethods.setTextFieldDesign(expiry, new Dimension(210, 45), null, new Color(150, 150, 150),
                        new Font("Arial", Font.BOLD, 14), new Color(45, 45, 45), false);
                panel.add(expiry);

                JButton viewMoreButton = new JButton("MORE");
                redudantMethods.setButtonDesign(viewMoreButton, new Dimension(50, 30), null, new Color(150, 150, 150), new Font("Arial", Font.BOLD, 12));
                viewMoreButton.setBorder(BorderFactory.createMatteBorder(0 , 0, 2, 0, new Color(150, 150, 150)));
                panel.add(viewMoreButton);

                viewMoreButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        mainPanel.removeAll();
                        JTextField plateTextField = new JTextField(plate + ":");
                        plateTextField.setHorizontalAlignment(SwingConstants.CENTER);
                        redudantMethods.setTextFieldDesign(plateTextField, new Dimension(150, 30), null, new Color(150, 150, 150),
                                new Font("Arial", Font.BOLD, 16), null, false);
                        plateTextField.setBorder(BorderFactory.createMatteBorder(0, 0, 3, 0, new Color(150, 150, 150)));
                        mainPanel.add(plateTextField);

                        mainPanel.add(Box.createVerticalStrut(10));

                        JTextField vinTextField = new JTextField("VIN number: " + vinNumber);
                        redudantMethods.setTextFieldDesign(vinTextField, new Dimension(450, 30), null, new Color(150, 150, 150),
                                new Font("Arial", Font.BOLD, 16), null, false);
                        mainPanel.add(vinTextField);

                        JTextField rcaDateTextField = new JTextField("RCA expiration date: " + rcaDate + " (" + rcaDays + " days left)");
                        redudantMethods.setTextFieldDesign(rcaDateTextField,  new Dimension(450, 30), null, new Color(150, 150, 150),
                                new Font("Arial", Font.BOLD, 16), null, false);
                        mainPanel.add(rcaDateTextField);

                        JTextField insuranceDateTextField = new JTextField("Insurance expiration date: " + insuranceDate + " (" + insuranceDays + " days left)");
                        redudantMethods.setTextFieldDesign(insuranceDateTextField,  new Dimension(450, 30), null, new Color(150, 150, 150),
                                new Font("Arial", Font.BOLD, 16), null, false);
                        mainPanel.add(insuranceDateTextField);

                        JTextArea notesField = new JTextArea("Notes:");
                        notesField.setMinimumSize(new Dimension(450, 250));
                        notesField.setPreferredSize(new Dimension(450, 250));
                        notesField.setMaximumSize(new Dimension(450, 250));
                        notesField.setLineWrap(true);
                        notesField.setWrapStyleWord(true);
                        notesField.setBackground(new Color(45, 45, 45));
                        notesField.setForeground(new Color(150, 150, 150));
                        notesField.setBorder(new EmptyBorder(10, 10, 10, 10));
                        notesField.addFocusListener(new FocusListener() {
                            @Override
                            public void focusGained(FocusEvent e) {
                                if(notesField.getText().trim().isEmpty()) {
                                    notesField.setText("Notes:");
                                } else if(notesField.getText().equals("Notes:")) {
                                    notesField.setText("");
                                }
                            }
                            @Override
                            public void focusLost(FocusEvent e) {
                                if(notesField.getText().trim().isEmpty()) {
                                    notesField.setText("Notes:");
                                }
                            }
                        });
                        mainPanel.add(notesField);

                        mainPanel.revalidate();
                        mainPanel.repaint();
                    }
                });

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
           String select = "SELECT [RCA DATE], [Insurance date] FROM CARS";
           stmt = c.createStatement();
           rs = stmt.executeQuery(select);

           String updateSQL = "UPDATE CARS SET [Days left] = ?";
           PreparedStatement ps = c.prepareStatement(updateSQL);
           DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

           while(rs.next()) {
               String rcaDate = rs.getString("RCA DATE");
               String insuranceDate = rs.getString("Insurance date");
               LocalDate localDate = LocalDate.parse(rcaDate, formatter);
               LocalDate localDate2 = LocalDate.parse(insuranceDate, formatter);
               long daysLeft = redudantMethods.daysLeft(localDate);
               long daysLeft2 = redudantMethods.daysLeft(localDate2);

               ps.setString(1, String.valueOf(Math.min(daysLeft, daysLeft2)));
               ps.executeUpdate();
           }
           rs.close();
           stmt.close();
           ps.close();
           c.commit();
           c.close();
       } catch(Exception e) {
           throw new RuntimeException(e);
       }
   }

   public void closestDate() {
        try {
            c = DriverManager.getConnection(url);
            c.setAutoCommit(false);
            stmt = c.createStatement();
            rs = stmt.executeQuery("SELECT [RCA DATE], [Insurance date] FROM CARS");
            PreparedStatement ps = c.prepareStatement("UPDATE CARS SET [Days left] = ?" + " WHERE [RCA DATE] = ? AND [Insurance date] = ?");

            while(rs.next()) {
                String rcaDate = rs.getString("RCA DATE");
                String insuranceDate = rs.getString("Insurance date");
                if(rcaDate != null && insuranceDate != null) {
                    long rcaDays = redudantMethods.daysLeft(LocalDate.parse(rcaDate, DateTimeFormatter.ofPattern("yyyy-MM-dd")));
                    long insDays = redudantMethods.daysLeft(LocalDate.parse(insuranceDate, DateTimeFormatter.ofPattern("yyyy-MM-dd")));
                    ps.setLong(1, Math.min(rcaDays, insDays));
                    ps.setString(2, rcaDate);
                    ps.setString(3, insuranceDate);
                    ps.executeUpdate();
                }
            }
            rs.close();
            stmt.close();
            ps.close();
            c.commit();
            c.close();
        } catch(Exception e) {
            throw new RuntimeException(e);
        }
   }
}
