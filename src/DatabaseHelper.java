import javax.swing.*;
import java.io.*;
import java.sql.*;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
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

    public void insertValue(String carPlate, String vinNumber, LocalDate rcaDate, LocalDate insuranceDate, int dueDate, String totalLeasing, String monthlyPay) {
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

            String leasingSQL = "INSERT INTO LEASING ([Num plate], [Leasing due date], [Total leasing], [Monthly pay]) VALUES (?, ?, ?, ?);)";
            PreparedStatement leasingPs = c.prepareStatement(leasingSQL);
            leasingPs.setString(1, carPlate);
            leasingPs.setInt(2, dueDate);
            leasingPs.setInt(3, Integer.parseInt(totalLeasing));
            leasingPs.setInt(4, Integer.parseInt(monthlyPay));

            leasingPs.executeUpdate();
            c.commit();
            c.close();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public void insertReminder(String plate, String description, LocalDate reminderLocalDate) {
        try(Connection c = getConnection()) {
            c.setAutoCommit(false);
            String insertSQL = "INSERT INTO reminders ([Num plate], Description, Date, [Is done]) VALUES (?, ?, ?, ?);";
            PreparedStatement ps = c.prepareStatement(insertSQL);
            ps.setString(1, plate);
            ps.setString(2, description);
            ps.setString(3, reminderLocalDate.toString());
            ps.setInt(4, 0);
            ps.executeUpdate();

            ps.close();
            c.commit();
            c.close();
        } catch(Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void updateReminder(String plate, String description, LocalDate reminderLocalDate, int value) {
        try(Connection c = getConnection()) {
            c.setAutoCommit(false);
            String updateSQL = "UPDATE Reminders SET [Is done] = ? WHERE [Num plate] = ? AND [Description] = ? AND Date = ?";
            PreparedStatement ps = c.prepareStatement(updateSQL);
            System.out.println("plate: " + plate + " description: " + description + " reminderLocalDate: " + reminderLocalDate + " value: " + value);
            ps.setInt(1, value);
            ps.setString(2, plate);
            ps.setString(3, description);
            ps.setString(4, reminderLocalDate.toString());
            ps.executeUpdate();

            ps.close();
            c.commit();
            c.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void removeReminder(String plate, String description, LocalDate reminderLocalDate) {
        try(Connection c = getConnection()) {
            c.setAutoCommit(false);
            String deleteSQL = "DELETE FROM Reminders WHERE [Num plate] = ? AND [Description] = ? AND Date = ?";
            PreparedStatement ps = c.prepareStatement(deleteSQL);
            ps.setString(1, plate);
            ps.setString(2, description);
            ps.setString(3, reminderLocalDate.toString());
            ps.executeUpdate();

            ps.close();
            c.commit();
            c.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Map<String, List<String>> reminderMap(String plate) {
        try(Connection c = getConnection()) {
            Map<String, List<String>> reminderMap = new HashMap<>();
            String selectSQL = "SELECT [Num plate], Date, Description FROM reminders WHERE [Num plate] = ?";
            PreparedStatement ps = c.prepareStatement(selectSQL);
            ps.setString(1, plate);
            rs = ps.executeQuery();
            while(rs.next()) {
                reminderMap.computeIfAbsent(rs.getString(2), k -> new ArrayList<>()).add(rs.getString(3));
            }
            return reminderMap;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Map<String, List<String>> getReminderMap() {
        try(Connection c = getConnection()){
            Map<String, List<String>> reminderMap = new HashMap<>();
            String selectSQL = "SELECT [Num plate], Date, Description, [Is done] FROM reminders";
            PreparedStatement ps = c.prepareStatement(selectSQL);
            rs = ps.executeQuery();
            while(rs.next()) {
                long days = redundantMethods.daysLeft(LocalDate.parse(rs.getString(2)));
                int isDone = rs.getInt(4);
                if(days >= 0 && days <= 7 && isDone == 0) {
                    reminderMap.computeIfAbsent(rs.getString(1), k -> new ArrayList<>()).add(rs.getString(2) + " " + rs.getString(3));
                }
            }
            return reminderMap;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public int getReminderValue(String plate, String description, LocalDate reminderLocalDate) {
        try(Connection c = getConnection()) {
            String selectSQL = "SELECT [Is done] FROM Reminders WHERE [Num plate] = ? AND [Description] = ? AND Date = ?";
            PreparedStatement ps = c.prepareStatement(selectSQL);
            ps.setString(1, plate);
            ps.setString(2, description);
            ps.setString(3, reminderLocalDate.toString());
            rs = ps.executeQuery();
            if(rs.next()) {
                return rs.getInt(1);
            }
            return -1;
        } catch (Exception e) {
            throw new RuntimeException(e);
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

            String leasingSQL = "DELETE FROM LEASING WHERE [Num plate] = ?";
            PreparedStatement leasingPs = c.prepareStatement(leasingSQL);
            leasingPs.setString(1, carPlate);
            leasingPs.executeUpdate();
            leasingPs.close();

            String reminderSQL = "DELETE FROM reminders WHERE [Num plate] = ?";
            PreparedStatement reminderPs = c.prepareStatement(reminderSQL);
            reminderPs.setString(1, carPlate);
            reminderPs.executeUpdate();
            reminderPs.close();

            c.commit();
            c.close();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public void printCars(JPanel mainPanel, JPanel bottomPanel, String searchValue) {
        try (Connection c = getConnection()) {
            mainPanel.removeAll();
            bottomPanel.setVisible(false);

            String sql = "SELECT C.[Num plate], C.VIN, C.[Days left], C.[RCA DATE], C.[Insurance date], " +
                    "L.[Leasing due date], L.[Total leasing], L.[Monthly pay] " +
                    "FROM CARS C " +
                    "LEFT JOIN LEASING L ON C.[Num plate] = L.[Num plate] " +
                    "WHERE (? = '' OR C.[Num plate] LIKE ?)";

            PreparedStatement ps = c.prepareStatement(sql);
            ps.setString(1, searchValue);
            ps.setString(2, "%" + searchValue + "%");
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                String plate = rs.getString("Num plate");
                String vinNumber = rs.getString("VIN");
                String daysLeft = rs.getString("Days left");
                String rcaDate = rs.getString("RCA DATE");
                long rcaDays = redundantMethods.daysLeft(LocalDate.parse(rcaDate));
                String insuranceDate = rs.getString("Insurance date");
                long insuranceDays = redundantMethods.daysLeft(LocalDate.parse(insuranceDate));

                String dueDate = rs.getString("Leasing due date") != null ? rs.getString("Leasing due date") : "0";
                String totalLeasing = rs.getString("Total leasing") != null ? rs.getString("Total leasing") : "0";
                String monthlyPay = rs.getString("Monthly pay") != null ? rs.getString("Monthly pay") : "0";

                redundantMethods.createCarPanel(mainPanel, bottomPanel, plate, vinNumber, daysLeft, rcaDate, rcaDays, insuranceDate, insuranceDays, dueDate, totalLeasing, monthlyPay);
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
                notesField.append(line + "\n");
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

    public Map<String, List<Integer>> getDueDateMap() {
        Map<String, List<Integer>> dueDateMap = new HashMap<>();
        try(Connection c = getConnection()) {
            String selectQuery = "SELECT [Num plate], [Leasing due date] FROM leasing";
            PreparedStatement pstmt = c.prepareStatement(selectQuery);
            rs = pstmt.executeQuery();
            while(rs.next()) {
                String plate = rs.getString("Num plate");
                int dueDate = Integer.parseInt(rs.getString("Leasing due date"));
                if(dueDate != 0) {
                    LocalDate today = LocalDate.now();
                    LocalDate dueLocalDate = LocalDate.of(today.getYear(), today.getMonthValue(), dueDate);
                    /*if(dueDate < today.getDayOfMonth()) {
                        dueLocalDate.plusMonths(1);
                    } */
                    long daysBetween = ChronoUnit.DAYS.between(today, dueLocalDate);
                    if(daysBetween >= 0 && daysBetween <= 7) {
                        dueDateMap.computeIfAbsent(plate, k -> new ArrayList<>()).add((int) dueDate - LocalDate.now().getDayOfMonth());
                    }
                }
            }

            return dueDateMap;
        } catch(Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void updateCalendar(String numPlate, String editedRcaDate, String editedInsDate) {
        try(Connection c = getConnection()) {
            c.setAutoCommit(false);
            stmt = c.createStatement();
            String selectQuery = "SELECT [Num plate], [RCA DATE], [Insurance Date] FROM CARS WHERE [Num plate] = ?";
            PreparedStatement psSelect = c.prepareStatement(selectQuery);
            psSelect.setString(1, numPlate);
            ResultSet rs = psSelect.executeQuery();
            if(rs.next()) {
                String updateQuery = "UPDATE CARS SET [RCA DATE] = ?, [Insurance Date] = ? WHERE [Num plate] = ?";
                try (PreparedStatement psUpdate = c.prepareStatement(updateQuery)) {
                    psUpdate.setString(1, editedRcaDate);
                    psUpdate.setString(2, editedInsDate);
                    psUpdate.setString(3, numPlate);
                    psUpdate.executeUpdate();
                }
            }
            stmt.close();
            c.commit();
            c.close();
        } catch(Exception e) {
            throw new RuntimeException(e);
        }
    }

    public long updateClosestDate() {
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
