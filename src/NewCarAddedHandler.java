import javax.swing.*;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;

public class NewCarAddedHandler {
    public NewCarAddedHandler(JPanel mainPanel, JPanel topPanel, JPanel bottomPanel) {
        bottomPanel.removeAll();
        JButton saveButton = new JButton("Save");
        JButton cancelButton = new JButton("Cancel");

        RedudantMethods redudantMethods = new RedudantMethods();
        DatabaseHelper databaseHelper = new DatabaseHelper();

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
        final int[] limit = {17};
        vinTextField.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if(vinTextField.getText().equals("Vehicle Identification Number") || vinTextField.getText().trim().isEmpty()) {
                    limit[0] = 17;
                    vinTextField.setText("");
                    vinTextField.setDocument(new PlainDocument() {
                        @Override
                        public void insertString(int offs, String str, AttributeSet a)
                                throws BadLocationException {
                            if(getLength() + str.length() <= limit[0])
                                super.insertString(offs, str, a);
                        }
                    });
                } else {
                    vinTextField.setText(vinTextField.getText().toUpperCase());
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if(vinTextField.getText().trim().isEmpty()) {
                    limit[0] = 30;
                    vinTextField.setText("Vehicle Identification Number");
                } else {
                    vinTextField.setText(vinTextField.getText().toUpperCase());
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
                } else {
                    numTextPlate.setText(numTextPlate.getText().toUpperCase());
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
        redudantMethods.setCalendar(spinDate);
        rcaPanel.add(spinDate);
        mainPanel.add(rcaPanel);

        mainPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        JButton selectRCAButton = new JButton("Select RCA");
        redudantMethods.setButtonDesign(selectRCAButton, new Dimension(100, 50), Color.darkGray, Color.white,
                new Font("Arial", Font.BOLD, 12));
        selectRCAButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        selectRCAButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                File destination = new File("D:\\Java 2024\\Expify\\Documents\\" + numTextPlate.getText());
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
                fileChooser.setDialogTitle("Choose RCA file");
                fileChooser.setAcceptAllFileFilterUsed(false);
                int response = fileChooser.showOpenDialog(null);
                if(response == JFileChooser.APPROVE_OPTION) {
                    try {
                        String PATH = "D:\\Java 2024\\Expify\\Documents\\";
                        String directoryName = PATH.concat(numTextPlate.getText());
                        File directory = new File(directoryName);
                        if(!directory.exists()) {
                            directory.mkdir();
                        }
                        File selectedFile = fileChooser.getSelectedFile();
                        Path selectedPath = fileChooser.getSelectedFile().toPath();
                        Path destinationPath = destination.toPath().resolve(selectedFile.getName());
                        Files.copy(selectedPath, destinationPath, StandardCopyOption.REPLACE_EXISTING);
                    } catch (Exception ex) {
                        throw new RuntimeException(ex);
                    }
                }
            }
        });
        mainPanel.add(selectRCAButton);

        JTextField insuranceTextDescription = new JTextField("Insurance Expiring Date:");
        redudantMethods.setTextFieldDesign(insuranceTextDescription, new Dimension(300, 25), null, new Color(150, 150, 150),
                new Font("Arial", Font.PLAIN, 12), new Color(45, 45, 45), false);
        mainPanel.add(insuranceTextDescription);

        JPanel insurancePanel = new JPanel();
        redudantMethods.setPanelDesign(insurancePanel, new Dimension(350, 50), new Color(45, 45,45),
                new FlowLayout(FlowLayout.LEFT), BorderFactory.createMatteBorder(0 , 0, 2, 0, new Color(150, 150, 150)));

        SpinnerDateModel insuranceModel = new SpinnerDateModel(new Date(), startDate, endDate, Calendar.DAY_OF_MONTH);
        JSpinner insuranceDate = new JSpinner(insuranceModel);
        redudantMethods.setCalendar(insuranceDate);
        insurancePanel.add(insuranceDate);
        mainPanel.add(insurancePanel);

        mainPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        JButton selectInsuranceButton = new JButton("Select Insurance");
        redudantMethods.setButtonDesign(selectInsuranceButton, new Dimension(100, 50), Color.darkGray, Color.white,
                new Font("Arial", Font.BOLD, 12));
        selectInsuranceButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        selectInsuranceButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                File destination = new File("D:\\Java 2024\\Expify\\Documents\\" + numTextPlate.getText());
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
                fileChooser.setDialogTitle("Choose insurance file");
                fileChooser.setAcceptAllFileFilterUsed(false);
                int response = fileChooser.showOpenDialog(null);
                if(response == JFileChooser.APPROVE_OPTION) {
                    try {
                        redudantMethods.createDirectory(numTextPlate.getText());
                        File selectedFile = fileChooser.getSelectedFile();
                        Path selectedPath = fileChooser.getSelectedFile().toPath();
                        Path destinationPath = destination.toPath().resolve(selectedFile.getName());
                        Files.copy(selectedPath, destinationPath, StandardCopyOption.REPLACE_EXISTING);
                    } catch (Exception ex) {
                        throw new RuntimeException(ex);
                    }
                }
            }
        });
        mainPanel.add(selectInsuranceButton);

        redudantMethods.setButtonDesign(saveButton, new Dimension(60, 30),
                new Color(45, 45,45), new Color(150, 150, 150),
                new Font("Arial", Font.BOLD, 20));
        saveButton.setBorder(BorderFactory.createMatteBorder(0,0, 2, 0, new Color(150, 150, 150)));
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(!databaseHelper.isDataUnique(numTextPlate.getText().replaceAll("\\s+",""), vinTextField.getText())
                        && redudantMethods.isDataValid(numTextPlate.getText(), vinTextField.getText())) {
                    redudantMethods.createDirectory(numTextPlate.getText());
                    Date rcaDate = model.getDate();
                    LocalDate rcaLocalDate = rcaDate.toInstant()
                            .atZone(ZoneId.systemDefault())
                            .toLocalDate();
                    Date insuranceDate = insuranceModel.getDate();
                    LocalDate insuranceLocalDate = insuranceDate.toInstant()
                            .atZone(ZoneId.systemDefault())
                            .toLocalDate();
                    String numPlate = numTextPlate.getText().toUpperCase().trim().replaceAll(" +", " ");
                    String vinNumber = vinTextField.getText().toUpperCase();
                    databaseHelper.insertValue(numPlate, vinNumber, rcaLocalDate, insuranceLocalDate);
                }
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
                redudantMethods.loadMainPanel(mainPanel, topPanel, bottomPanel);
            }
        });
        bottomPanel.add(cancelButton);

        mainPanel.repaint();
        mainPanel.revalidate();
    }
}
