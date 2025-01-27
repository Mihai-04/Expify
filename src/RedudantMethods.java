import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.MatteBorder;
import java.awt.*;

public class RedudantMethods {
    public void setButtonDesign(JButton button, Dimension dim, Color c1, Color c2, Font font) {
        button.setMinimumSize(dim);
        button.setPreferredSize(dim);
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
        panel.setBorder((Border) bf);
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
}
