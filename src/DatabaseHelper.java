import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.io.*;
import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.List;

public class DatabaseHelper {
    RedundantMethods redundantMethods = new RedundantMethods();

    Connection c = null;
    Statement stmt = null;
    ResultSet rs = null;
    String url = "jdbc:sqlite:carsDB.db";

    public Connection getConnection() throws ClassNotFoundException, SQLException {
        Class.forName("org.sqlite.JDBC");
        return DriverManager.getConnection(url);
    }

    public void insertValue(String carPlate, String vinNumber, LocalDate rcaDate, LocalDate insuranceDate) {
        try(Connection c = getConnection()) {
            c.setAutoCommit(false);
            System.out.println("Opened database successfully");

            String sql = "INSERT INTO CARS ([Num plate], VIN, [RCA DATE], [Insurance date], [Days left]) VALUES (?, ?, ?, ?, ?);";
            PreparedStatement ps = c.prepareStatement(sql);
            String minDays = String.valueOf(redundantMethods.getMinDays(String.valueOf(rcaDate), String.valueOf(insuranceDate)));

            ps.setString(1, carPlate);
            ps.setString(2, vinNumber);
            ps.setString(3, rcaDate.toString());
            ps.setString(4, insuranceDate.toString());
            ps.setString(5, minDays);
            ps.executeUpdate();

            ps.close();
            c.commit();
            c.close();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public boolean getValue(String value) {
        //value.replaceAll(" +", "");
        System.out.println("value: " + value);
        try(Connection c = getConnection()) {
            c.setAutoCommit(false);

            String sql = "SELECT [Num plate] FROM CARS WHERE [Num plate] LIKE ?";
            PreparedStatement ps = c.prepareStatement(sql);
            ps.setString(1, "%" + value + "%");
            rs = ps.executeQuery();
            if(rs.next()) {
                return true;
            }
            rs.close();
            ps.close();
            c.commit();
            c.close();

            return false;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void removeValue(String carPlate) {
        try(Connection c = getConnection()) {
            c.setAutoCommit(false);

            String sql = "DELETE FROM CARS WHERE [Num plate] = ?";
            PreparedStatement ps = c.prepareStatement(sql);
            ps.setString(1, carPlate);
            ps.executeUpdate();
            ps.close();
            c.commit();
            c.close();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public void printCars(JPanel mainPanel, JPanel bottomPanel, String searchValue) {
        try(Connection c = getConnection()) {
            mainPanel.removeAll();
            bottomPanel.setVisible(false);

            String sql = "SELECT * FROM CARS";
            PreparedStatement ps = c.prepareStatement(sql);
            if(!searchValue.isEmpty() && searchValue.contains(searchValue)) {
                sql = "SELECT * FROM CARS WHERE [Num plate] LIKE ?";
                ps = c.prepareStatement(sql);
                ps.setString(1, "%" + searchValue + "%");
                rs = ps.executeQuery();
            } else {
                ps = c.prepareStatement(sql);
                rs = ps.executeQuery();
            }

            while(rs.next()) {
                String plate = rs.getString("Num plate");
                String vinNumber = rs.getString("VIN");
                String daysLeft = rs.getString("Days left");
                String rcaDate = rs.getString("RCA DATE");
                long rcaDays = redundantMethods.daysLeft(LocalDate.parse(rcaDate));
                String insuranceDate = rs.getString("Insurance date");
                long insuranceDays = redundantMethods.daysLeft(LocalDate.parse(insuranceDate));

                redundantMethods.createCarPanel(mainPanel, bottomPanel, plate, vinNumber, daysLeft, rcaDate, rcaDays, insuranceDate, insuranceDays);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean isDatabaseEmpty() {
       try(Connection c = getConnection()) {
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

    public String isDataUnique(String numPlate, String vinNumber) {
        String badValue = "";
        try(Connection c = getConnection()) {
            c.setAutoCommit(false);
            String sql = "SELECT [Num plate], VIN FROM CARS";
            stmt = c.createStatement();
            rs = stmt.executeQuery(sql);
            while(rs.next()) {
                String plate = rs.getString("Num plate").replaceAll("\\s+","");
                if(plate.equals(numPlate) || vinNumber.equals(rs.getString("VIN"))) {
                    if(plate.equals(numPlate)) badValue = plate;
                     else badValue = vinNumber;
                }
            }
            stmt.close();
            rs.close();
            c.commit();
            c.close();
            return badValue;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void readNotesFromFile(String carPlate, JTextArea notesField) {
       notesField.setText("");
       File file = new File(redundantMethods.createCarDocument(carPlate) + File.separator + "Notes.txt");
       try(BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while((line = br.readLine()) != null) {
                notesField.append(line + "\r\n");
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Map<String, List<Integer>> getAllDays() {
        Map<String, List<Integer>> carDaysMap = new HashMap<>();
        try(Connection c = getConnection()) {
            stmt = c.createStatement();
            rs = stmt.executeQuery("SELECT [Num plate], [RCA DATE], [Insurance date] FROM CARS");
            while(rs.next()) {
                String plate = rs.getString("Num plate");
                String rcaDate = rs.getString("RCA DATE");
                String insuranceDate = rs.getString("Insurance date");
                long rcaDays = redundantMethods.daysLeft(LocalDate.parse(rcaDate));
                long insuranceDays = redundantMethods.daysLeft(LocalDate.parse(insuranceDate));
                carDaysMap.computeIfAbsent(plate, k -> new ArrayList<>()).add((int) rcaDays);
                carDaysMap.computeIfAbsent(plate, k -> new ArrayList<>()).add((int) insuranceDays);
            }
            System.out.println("all days: " + carDaysMap);
            return carDaysMap;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public long closestDate() {
       long minDays = Integer.MAX_VALUE;
        try(Connection c = getConnection()) {
            c.setAutoCommit(false);
            stmt = c.createStatement();
            rs = stmt.executeQuery("SELECT [RCA DATE], [Insurance date] FROM CARS");
            PreparedStatement ps = c.prepareStatement("UPDATE CARS SET [Days left] = ?" + " WHERE [RCA DATE] = ? AND [Insurance date] = ?");

            while(rs.next()) {
                String rcaDate = rs.getString("RCA DATE");
                String insuranceDate = rs.getString("Insurance date");
                if(rcaDate != null && insuranceDate != null) {
                    minDays = redundantMethods.getMinDays(rcaDate, insuranceDate);

                    ps.setLong(1, minDays);
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
            return minDays;
        } catch(Exception e) {
            throw new RuntimeException(e);
        }
    }
}
