import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.MatteBorder;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Set;

public class RedudantMethods {

    public void setButtonDesign(JButton button, Dimension dim, Color c1, Color c2, Font font) {
        button.setMinimumSize(dim);
        button.setPreferredSize(dim);
        button.setMaximumSize(dim);
        button.setBackground(c1);
        button.setForeground(c2);
        button.setFont(font);
        button.setFocusable(false);
        button.setBorder(null);
    }

    public void setTextFieldDesign(JTextField textField, Dimension dim, Color backgroundColor, Color foregroundColor,
                                   Font font, Color caretColor, Boolean isEditable) {
        textField.setMinimumSize(dim);
        textField.setPreferredSize(dim);
        textField.setMaximumSize(dim);
        textField.setBackground(backgroundColor);
        textField.setForeground(foregroundColor);
        textField.setFont(font);
        textField.setCaretColor(caretColor);
        textField.setEditable(isEditable);
        textField.setBorder(null);
    }

    public void setPanelDesign(JPanel panel, Dimension dim, Color backgroundColor, FlowLayout layout, MatteBorder bf) {
        panel.setMinimumSize(dim);
        panel.setPreferredSize(dim);
        panel.setMaximumSize(dim);
        panel.setBackground(backgroundColor);
        panel.setLayout(layout);
        panel.setBorder(bf);
    }

    public Boolean isVinValid(String vin) {
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
        databaseHelper.printCars(mainPanel);
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

    public void createDirectory(String numTextPlate) {
        String PATH = "D:\\Java 2024\\Expify\\Documents\\";
        String directoryName = PATH.concat(numTextPlate);
        File directory = new File(directoryName);
        if(!directory.exists()) {
            directory.mkdir();
        }
    }

    public void addFileToDirectory(File selectedFile, Path selectedPath, Path destinationPath) {
        try {
            Files.copy(selectedPath, destinationPath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
