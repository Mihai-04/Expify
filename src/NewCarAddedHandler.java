import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;

public class NewCarAddedHandler {
    JButton saveButton = new JButton("Save");
    JButton cancelButton = new JButton("Cancel");

    public NewCarAddedHandler(JPanel mainPanel, JPanel bottomPanel) {
        RedudantMethods redudantMethods = new RedudantMethods();

        bottomPanel.setEnabled(true);
        bottomPanel.setVisible(true);

        JPanel vinPanel = new JPanel();
        redudantMethods.setPanelDesign(vinPanel, new Dimension(350, 50), new Color(45, 45,45),
                new FlowLayout(FlowLayout.LEFT), BorderFactory.createMatteBorder(0 , 0, 2, 0, new Color(150, 150, 150)));

        JPanel platePanel = new JPanel();
        redudantMethods.setPanelDesign(platePanel, new Dimension(350, 50), new Color(45, 45,45),
                new FlowLayout(FlowLayout.LEFT), BorderFactory.createMatteBorder(0 , 0, 2, 0, new Color(150, 150, 150)));

        mainPanel.add(Box.createRigidArea(new Dimension(10, 15)));

        JTextField vinTextDescription = new JTextField("Vehicle Identification Number:");
        redudantMethods.setTextFieldDesign(vinTextDescription, new Dimension(300, 25), null, new Color(150, 150, 150),
                new Font("Arial", Font.PLAIN, 12), new Color(45, 45, 45), false);
        mainPanel.add(vinTextDescription);

        JTextField vinTextField = new JTextField("Vehicle Identification Number");
        redudantMethods.setTextFieldDesign(vinTextField, new Dimension(350, 40), null, new Color(150, 150, 150),
                new Font("Arial", Font.BOLD, 14), Color.white, true);
        vinTextField.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if(vinTextField.getText().equals("Vehicle Identification Number") || vinTextField.getText().trim().isEmpty()) {
                    vinTextField.setText("");
                } else {
                    vinTextField.setText(vinTextField.getText().toUpperCase());
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if(vinTextField.getText().trim().isEmpty()) {
                    vinTextField.setText("Vehicle Identification Number");
                }
            }
        });
        vinPanel.add(vinTextField);
        mainPanel.add(vinPanel);

        JTextField plateTextDescription = new JTextField("Car Num Plate:");
        redudantMethods.setTextFieldDesign(plateTextDescription, new Dimension(300, 25), null, new Color(150, 150, 150),
                new Font("Arial", Font.PLAIN, 12), new Color(45, 45, 45), false);
        mainPanel.add(plateTextDescription);

        JTextField numTextPlate = new JTextField("Car Num Plate");
        redudantMethods.setTextFieldDesign(numTextPlate, new Dimension(350, 40), null, new Color(150, 150, 150),
                new Font("Arial", Font.BOLD, 14), Color.white, true);
        numTextPlate.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if(numTextPlate.getText().equals("Car Num Plate") || numTextPlate.getText().trim().isEmpty()) {
                    numTextPlate.setText("");
                } else {
                    numTextPlate.setText(numTextPlate.getText().toUpperCase());
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if(numTextPlate.getText().trim().isEmpty()) {
                    numTextPlate.setText("Car Num Plate");
                }
            }
        });
        platePanel.add(numTextPlate);
        mainPanel.add(platePanel);

        JPanel rcaPanel = new JPanel();
        redudantMethods.setPanelDesign(rcaPanel, new Dimension(350, 50), new Color(45, 45,45),
                new FlowLayout(FlowLayout.LEFT), BorderFactory.createMatteBorder(0 , 0, 2, 0, new Color(150, 150, 150)));

        JTextField rcaTextDescription = new JTextField("RCA Expiring Date:");
        redudantMethods.setTextFieldDesign(rcaTextDescription, new Dimension(300, 25), null, new Color(150, 150, 150),
                new Font("Arial", Font.PLAIN, 12), new Color(45, 45, 45), false);
        mainPanel.add(rcaTextDescription);

        Date startDate = new Date(100, Calendar.JANUARY, 1);
        Date endDate = new Date(200, Calendar.DECEMBER, 31);
        SpinnerDateModel model = new SpinnerDateModel(new Date(), startDate, endDate, Calendar.DAY_OF_MONTH);
        JSpinner spinDate = new JSpinner(model);
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
            if(comp instanceof JButton) {
                spinDate.remove(comp);
            }
        }
        rcaPanel.add(spinDate);
        mainPanel.add(rcaPanel);

        /*
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        */

        redudantMethods.setButtonDesign(saveButton, new Dimension(60, 30),
                new Color(45, 45,45), new Color(150, 150, 150),
                new Font("Arial", Font.BOLD, 20));
        saveButton.setBorder(BorderFactory.createMatteBorder(0,0, 2, 0, new Color(150, 150, 150)));
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println(redudantMethods.isVinValid(vinTextField.getText()));
            }
        });
        bottomPanel.add(saveButton);

        redudantMethods.setButtonDesign(cancelButton, new Dimension(80, 30),
                new Color(45, 45, 45), new Color(150, 150, 150),
                new Font("Arial", Font.BOLD, 20));
        cancelButton.setBorder(BorderFactory.createMatteBorder(0,0, 2, 0, new Color(150, 150, 150)));
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                /*bottomPanel.setVisible(false);
                bottomPanel.setEnabled(false);*/
            }
        });
        bottomPanel.add(cancelButton);

        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Date date = model.getDate();
                LocalDate chosenDate = date.toInstant()
                        .atZone(ZoneId.systemDefault())
                        .toLocalDate();
                LocalDate localDate = LocalDate.now();
                long difference = ChronoUnit.DAYS.between(localDate, chosenDate);

                System.out.println("test " + date);
                System.out.println("diff " + difference);
            }
        });
        mainPanel.repaint();
        mainPanel.revalidate();
    }
}
