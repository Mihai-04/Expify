import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RedundantMethods {

    public void createCarPanel(JPanel mainPanel, JPanel bottomPanel, String plate, String vin, String daysLeft, String rcaDate,
                               long rcaDays, String insuranceDate, long insuranceDays) {
        DatabaseHelper databaseHelper = new DatabaseHelper();

        JPanel panel = new JPanel();
        setPanelDesign(panel, new Dimension(450, 50), new Color(45, 45, 45), new FlowLayout(FlowLayout.LEFT), null);
        panel.add(Box.createHorizontalStrut(5));

        JTextField carPlate = new JTextField(plate + ":");
        setComponentDesign(carPlate, new Dimension(100, 40), null, new Color(150, 150, 150),
                new Font("Arial", Font.BOLD, 14));
        carPlate.setEditable(false);
        panel.add(carPlate);

        JTextField expiry = new JTextField("Closest expiration date: " + daysLeft);
        setComponentDesign(expiry, new Dimension(210, 45), null, new Color(150, 150, 150),
                new Font("Arial", Font.BOLD, 14));
        expiry.setEditable(false);
        panel.add(expiry);

        JButton viewMoreButton = new JButton("MORE");
        setComponentDesign(viewMoreButton, new Dimension(50, 30), null, new Color(150, 150, 150), new Font("Arial", Font.BOLD, 12));
        viewMoreButton.setBorder(BorderFactory.createMatteBorder(0 , 0, 2, 0, new Color(150, 150, 150)));
        panel.add(viewMoreButton);

        JButton deleteCar = new JButton("DELETE");
        setComponentDesign(deleteCar, new Dimension(50, 30), null, new Color(150, 150, 150), new Font("Arial", Font.BOLD, 12));
        deleteCar.setBorder(BorderFactory.createMatteBorder(0 , 0, 2, 0, new Color(150, 150, 150)));
        panel.add(deleteCar);

        viewMoreButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainPanel.removeAll();
                JTextField plateTextField = new JTextField(plate + ":");
                plateTextField.setHorizontalAlignment(SwingConstants.CENTER);
                setComponentDesign(plateTextField, new Dimension(150, 30), null, new Color(150, 150, 150),
                        new Font("Arial", Font.BOLD, 16));
                plateTextField.setEditable(false);
                plateTextField.setBorder(BorderFactory.createMatteBorder(0, 0, 3, 0, new Color(150, 150, 150)));
                mainPanel.add(plateTextField);

                mainPanel.add(Box.createVerticalStrut(10));

                JTextField vinTextField = new JTextField("VIN number: " + vin);
                setComponentDesign(vinTextField, new Dimension(450, 30), null, new Color(150, 150, 150),
                        new Font("Arial", Font.BOLD, 16));
                vinTextField.setEditable(false);
                mainPanel.add(vinTextField);

                JTextField rcaDateTextField = new JTextField("RCA expiration date: " + rcaDate + " (" + rcaDays + " days left)");
                setComponentDesign(rcaDateTextField,  new Dimension(450, 30), null, new Color(150, 150, 150),
                        new Font("Arial", Font.BOLD, 16));
                rcaDateTextField.setEditable(false);
                mainPanel.add(rcaDateTextField);

                JTextField insuranceDateTextField = new JTextField("Insurance expiration date: " + insuranceDate + " (" + insuranceDays + " days left)");
                setComponentDesign(insuranceDateTextField,  new Dimension(450, 30), null, new Color(150, 150, 150),
                        new Font("Arial", Font.BOLD, 16));
                insuranceDateTextField.setEditable(false);
                mainPanel.add(insuranceDateTextField);

                JTextArea notesField = new JTextArea("Notes:");
                databaseHelper.readNotesFromFile(plate, notesField);
                if(notesField.getText().trim().isEmpty() || notesField.getText().trim().equals("Notes:")) {
                    notesField.setText("Notes:");
                }
                notesField.setMinimumSize(new Dimension(450, 250));
                notesField.setPreferredSize(new Dimension(450, 250));
                notesField.setMaximumSize(new Dimension(450, 250));
                notesField.setLineWrap(true);
                notesField.setWrapStyleWord(true);
                notesField.setBackground(new Color(45, 45, 45));
                notesField.setForeground(new Color(150, 150, 150));
                notesField.setCaretColor(Color.white);
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

                mainPanel.add(Box.createVerticalStrut(5));

                JButton saveNotesButton = new JButton("Save notes");
                setComponentDesign(saveNotesButton, new Dimension(100, 30), null, new Color(150, 150, 150),
                        new Font("Arial", Font.BOLD, 16));
                saveNotesButton.setBorder(BorderFactory.createMatteBorder(0 , 0, 2, 0, new Color(150, 150, 150)));
                saveNotesButton.setAlignmentX(Component.CENTER_ALIGNMENT);
                saveNotesButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        String directoryPath = createCarDocument(plate);
                        String notesFileName = "Notes.txt";
                        try(FileWriter writer = new FileWriter(directoryPath + File.separator + notesFileName)) {
                            writer.write(notesField.getText());
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
                    }
                });
                mainPanel.add(saveNotesButton);

                mainPanel.revalidate();
                mainPanel.repaint();
            }
        });

        deleteCar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                databaseHelper.removeValue(plate);
                String path = createCarDocument(plate);
                File file = new File(path);
                File[] files = file.listFiles();
                if(files != null) {
                    for(File f : files) {
                        f.delete();
                    }
                    file.delete();
                }
                mainPanel.removeAll();
                databaseHelper.printCars(mainPanel, bottomPanel, "");
                mainPanel.revalidate();
                mainPanel.repaint();
            }
        });

        mainPanel.add(Box.createVerticalStrut(10));
        mainPanel.add(panel);
        mainPanel.revalidate();
        mainPanel.repaint();
    }
    
    public void setComponentDesign(JComponent component, Dimension dim, Color backgroundColor, Color foregroundColor, Font font) {
        component.setMinimumSize(dim);
        component.setPreferredSize(dim);
        component.setMaximumSize(dim);
        component.setBackground(backgroundColor);
        component.setForeground(foregroundColor);
        component.setFont(font);
        if(component instanceof JButton) {
            component.setFocusable(false);
            component.setBorder(null);
        } else if(component instanceof JTextField) {
            ((JTextField) component).setCaretColor(foregroundColor);
            component.setBorder(null);
        }
    }

    public void setPanelDesign(JPanel panel, Dimension dim, Color backgroundColor, FlowLayout layout, MatteBorder bf) {
        panel.setMinimumSize(dim);
        panel.setPreferredSize(dim);
        panel.setMaximumSize(dim);
        panel.setBackground(backgroundColor);
        panel.setLayout(layout);
        panel.setBorder(bf);
    }

    public boolean isVinValid(String vin) {
        int[] values = { 1, 2, 3, 4, 5, 6, 7, 8, 0, 1, 2, 3, 4, 5, 0, 7, 0, 9,
                2, 3, 4, 5, 6, 7, 8, 9 };
        int[] weights = { 8, 7, 6, 5, 4, 3, 2, 10, 0, 9, 8, 7, 6, 5, 4, 3, 2 };

        String s = vin;
        s = s.replaceAll("-", "");
        s = s.replaceAll(" ", "");
        s = s.toUpperCase();
        if (s.length() != 17)
            throw new RuntimeException("VIN number must be 17 characters");

        int sum = 0;
        for (int i = 0; i < 17; i++) {
            char c = s.charAt(i);
            int value;
            int weight = weights[i];

            if (c >= 'A' && c <= 'Z') {
                value = values[c - 'A'];
                if (value == 0) {
                    throw new RuntimeException("Illegal character: " + c);
                }
            }
            else if (c >= '0' && c <= '9') {
                value = c - '0';
            }
            else {
                throw new RuntimeException("Illegal character: " + c);
            }
            sum = sum + weight * value;
        }

        sum = sum % 11;
        char check = s.charAt(8);
        if ((sum == 10 && check == 'X') || sum == transliterate(check)) {
            System.out.println("Valid");
            return true;
        } else {
            System.out.println("Invalid");
            return false;
        }
    }

    private static int transliterate(char check) {
        if(check == 'A' || check == 'J'){
            return 1;
        } else if(check == 'B' || check == 'K' || check == 'S'){
            return 2;
        } else if(check == 'C' || check == 'L' || check == 'T'){
            return 3;
        } else if(check == 'D' || check == 'M' || check == 'U'){
            return 4;
        } else if(check == 'E' || check == 'N' || check == 'V'){
            return 5;
        } else if(check == 'F' || check == 'W'){
            return 6;
        } else if(check == 'G' || check == 'P' || check == 'X'){
            return 7;
        } else if(check == 'H' || check == 'Y'){
            return 8;
        } else if(check == 'R' || check == 'Z'){
            return 9;
        } else if(Integer.valueOf(Character.getNumericValue(check)) != null){ //hacky but works
            return Character.getNumericValue(check);
        }
        return -1;
    }

    public boolean isDataValid(String numTextPlate, String vinTextField) {
        return numTextPlate.length() >= 3 && isVinValid(vinTextField);
    }

    public long daysLeft(LocalDate chosenDate) {
        LocalDate currentDate = LocalDate.now();

        return ChronoUnit.DAYS.between(currentDate, chosenDate);
    }

    public void loadMainPanel(JPanel mainPanel, JPanel topPanel, JPanel bottomPanel) {
        DatabaseHelper databaseHelper = new DatabaseHelper();

        mainPanel.removeAll();
        topPanel.setVisible(true);
        bottomPanel.setVisible(false);
        databaseHelper.printCars(mainPanel, bottomPanel, "");
    }

    public void setCalendar(JSpinner spinDate) {
        spinDate.setBackground(new Color(45, 45, 45));
        spinDate.setBorder(null);
        spinDate.setEditor(new JSpinner.DateEditor(spinDate, "dd/MM/yyyy"));
        spinDate.setPreferredSize(new Dimension(350, 40));
        spinDate.setMinimumSize(new Dimension(350, 40));
        spinDate.setMaximumSize(new Dimension(350, 40));
        spinDate.setFont(new Font("Arial", Font.BOLD, 16));
        JComponent editor = spinDate.getEditor();
        int n = editor.getComponentCount();
        for(int i=0; i<n; i++) {
            Component c = editor.getComponent(i);
            if(c instanceof JTextField) {
                c.setForeground(new Color(150, 150, 150));
                c.setBackground(new Color(45, 45, 45));
                ((JTextField) c).setCaretColor(Color.white);
            }
        }
        for(Component comp :spinDate.getComponents()) {
            if (comp instanceof JButton) {
                spinDate.remove(comp);
            }
        }
    }

    public String createCarDocument(String numTextPlate) {
        String userHome = System.getProperty("user.home") + File.separator;

        String directoryPath = userHome + "Expify" + File.separator + "Documents" + File.separator + numTextPlate;
        File directory = new File(directoryPath);
        if(!directory.exists()) {
            directory.mkdir();
        }
        return directoryPath;
    }

    public File createDirectory() {
        String userHome = System.getProperty("user.home") + File.separator;
        File expifyFile = new File(userHome.concat("Expify"));
        File documentsFile = new File(expifyFile, "Documents");
        if(!expifyFile.exists()) {
            expifyFile.mkdir();
            documentsFile.mkdir();
        }
        return expifyFile;
    }

    public Map<String, List<Integer>> getAllMinDays() {
        DatabaseHelper databaseHelper = new DatabaseHelper();
        Map<String, List<Integer>> carDaysMap = databaseHelper.getAllDays();
        System.out.println(carDaysMap);
        for(Map.Entry<String, List<Integer>> entry :carDaysMap.entrySet()) {
            String key = entry.getKey();
            List<Integer> value = new ArrayList<>(entry.getValue());

            value.removeIf(num -> num > 30 || num < 0);
            carDaysMap.put(key, value);
        }
        carDaysMap.entrySet().removeIf(e -> e.getValue().isEmpty());
        System.out.println("days left: " + carDaysMap);
        return carDaysMap;
    }

    public long getMinDays(String rcaDate, String insDate) {
        long rcaDays = daysLeft(LocalDate.parse(rcaDate, DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        long insDays = daysLeft(LocalDate.parse(insDate, DateTimeFormatter.ofPattern("yyyy-MM-dd")));

        if (rcaDays < 0 && insDays < 0) {
            rcaDays = 0;
            insDays = 0;
        }
        if(rcaDays < 0) {
            rcaDays = insDays;
        }
        if(insDays < 0) {
            insDays = rcaDays;
        }
        return Math.min(rcaDays, insDays);
    }

    public void insert10Cars() {
        DatabaseHelper databaseHelper = new DatabaseHelper();

        for(int i=0; i<10; i++) {
            databaseHelper.insertValue("car " + i, "Vin number", LocalDate.now(), LocalDate.now());
        }
    }
}
