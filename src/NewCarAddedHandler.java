
import javax.swing.*;
import javax.swing.border.MatteBorder;
import javax.swing.plaf.basic.BasicProgressBarUI;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;
import java.nio.file.Path;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;

public class NewCarAddedHandler {
    public NewCarAddedHandler(JPanel mainPanel, JPanel topPanel, JPanel bottomPanel) {
        bottomPanel.removeAll();
        JButton saveButton = new JButton("Save");
        JButton cancelButton = new JButton("Cancel");

        final String[] rcaFilePath = {""};
        final String[] insuranceFilePath = {""};
        final String[] rcaDestinationPath = {""};
        final String[] insuranceDestinationPath = {""};

        RedundantMethods redundantMethods = new RedundantMethods();
        DatabaseHelper databaseHelper = new DatabaseHelper();

        bottomPanel.setEnabled(true);
        bottomPanel.setVisible(true);
        JPanel notificationPanel = new JPanel();

        JPanel vinPanel = new JPanel();
        redundantMethods.setPanelDesign(vinPanel, new Dimension(350, 50), new Color(45, 45,45),
                new FlowLayout(FlowLayout.LEFT), BorderFactory.createMatteBorder(0 , 0, 2, 0, new Color(150, 150, 150)));

        JPanel platePanel = new JPanel();
        redundantMethods.setPanelDesign(platePanel, new Dimension(350, 50), new Color(45, 45,45),
                new FlowLayout(FlowLayout.LEFT), BorderFactory.createMatteBorder(0 , 0, 2, 0, new Color(150, 150, 150)));

        mainPanel.add(Box.createRigidArea(new Dimension(0, 15)));

        JTextField vinTextDescription = new JTextField("Vehicle Identification Number:");
        redundantMethods.setComponentDesign(vinTextDescription, new Dimension(300, 25), null, new Color(150, 150, 150),
                new Font("Arial", Font.PLAIN, 12));
        vinTextDescription.setEditable(false);
        mainPanel.add(vinTextDescription);

        JTextField vinTextField = new JTextField("Vehicle Identification Number");
        redundantMethods.setComponentDesign(vinTextField, new Dimension(350, 40), null, new Color(150, 150, 150),
                new Font("Arial", Font.BOLD, 14));
        vinTextField.setEditable(true);
        vinTextField.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if(vinTextField.getText().equals("Vehicle Identification Number") || vinTextField.getText().trim().isEmpty()) {
                    //final int[] limit = {17};
                    int limit = 17;
                    vinTextField.setText("");
                    vinTextField.setDocument(new PlainDocument() {
                        @Override
                        public void insertString(int offs, String str, AttributeSet a) throws BadLocationException {
                            if(getLength() + str.length() <= limit) {
                                super.insertString(offs, str, a);
                            }
                        }
                    });
                } else {
                    vinTextField.setText(vinTextField.getText().toUpperCase());
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if(vinTextField.getText().trim().isEmpty()) {
                    final int[] limit = {30};
                    vinTextField.setText("Vehicle Identification Number");
                } else {
                    vinTextField.setText(vinTextField.getText().toUpperCase());
                }
            }
        });
        vinPanel.add(vinTextField);
        mainPanel.add(vinPanel);

        JTextField plateTextDescription = new JTextField("Car Num Plate:");
        redundantMethods.setComponentDesign(plateTextDescription, new Dimension(300, 25), null, new Color(150, 150, 150),
                new Font("Arial", Font.PLAIN, 12));
        plateTextDescription.setEditable(false);
        mainPanel.add(plateTextDescription);

        JTextField numTextPlate = new JTextField("Car Num Plate");
        redundantMethods.setComponentDesign(numTextPlate, new Dimension(350, 40), null, new Color(150, 150, 150),
                new Font("Arial", Font.BOLD, 14));
        numTextPlate.setEditable(true);
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
        redundantMethods.setPanelDesign(rcaPanel, new Dimension(350, 50), new Color(45, 45,45),
                new FlowLayout(FlowLayout.LEFT), BorderFactory.createMatteBorder(0 , 0, 2, 0, new Color(150, 150, 150)));

        JTextField rcaTextDescription = new JTextField("RCA Expiring Date:");
        redundantMethods.setComponentDesign(rcaTextDescription, new Dimension(300, 25), null, new Color(150, 150, 150),
                new Font("Arial", Font.PLAIN, 12));
        rcaTextDescription.setEditable(false);
        mainPanel.add(rcaTextDescription);

        Date startDate = new Date(100, Calendar.JANUARY, 1);
        Date endDate = new Date(200, Calendar.DECEMBER, 31);
        SpinnerDateModel rcaModel = new SpinnerDateModel(new Date(), startDate, endDate, Calendar.DAY_OF_MONTH);
        JSpinner spinDate = new JSpinner(rcaModel);
        redundantMethods.setCalendar(spinDate);
        rcaPanel.add(spinDate);
        mainPanel.add(rcaPanel);

        mainPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        JButton selectRCAButton = new JButton("Select RCA");
        redundantMethods.setComponentDesign(selectRCAButton, new Dimension(120, 50), Color.darkGray, Color.white,
                new Font("Arial", Font.BOLD, 12));
        selectRCAButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        selectRCAButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //File destination = new File(redundantMethods.createCarDocument(numTextPlate.getText()));
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
                fileChooser.setDialogTitle("Choose RCA file");
                fileChooser.setAcceptAllFileFilterUsed(false);
                int response = fileChooser.showOpenDialog(null);
                if(response == JFileChooser.APPROVE_OPTION) {
                    try {
                        File selectedFile = fileChooser.getSelectedFile();
                        rcaFilePath[0] = String.valueOf(fileChooser.getSelectedFile().toPath());
                        //rcaDestinationPath[0] = String.valueOf(destination.toPath().resolve(selectedFile.getName()));
                    } catch (Exception ex) {
                        throw new RuntimeException(ex);
                    }
                }
            }
        });
        mainPanel.add(selectRCAButton);

        JTextField insuranceTextDescription = new JTextField("Insurance Expiring Date:");
        redundantMethods.setComponentDesign(insuranceTextDescription, new Dimension(300, 25), null, new Color(150, 150, 150),
                new Font("Arial", Font.PLAIN, 12));
        insuranceTextDescription.setEditable(false);
        mainPanel.add(insuranceTextDescription);

        JPanel insurancePanel = new JPanel();
        redundantMethods.setPanelDesign(insurancePanel, new Dimension(350, 50), new Color(45, 45,45),
                new FlowLayout(FlowLayout.LEFT), BorderFactory.createMatteBorder(0 , 0, 2, 0, new Color(150, 150, 150)));

        SpinnerDateModel insuranceModel = new SpinnerDateModel(new Date(), startDate, endDate, Calendar.DAY_OF_MONTH);
        JSpinner insuranceDate = new JSpinner(insuranceModel);
        redundantMethods.setCalendar(insuranceDate);
        insurancePanel.add(insuranceDate);
        mainPanel.add(insurancePanel);

        mainPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        JButton selectInsuranceButton = new JButton("Select Insurance");
        redundantMethods.setComponentDesign(selectInsuranceButton, new Dimension(120, 50), Color.darkGray, Color.white,
                new Font("Arial", Font.BOLD, 12));
        selectInsuranceButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        selectInsuranceButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //File destination = new File(redundantMethods.createCarDocument(numTextPlate.getText()));
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
                fileChooser.setDialogTitle("Choose insurance file");
                fileChooser.setAcceptAllFileFilterUsed(false);
                int response = fileChooser.showOpenDialog(null);
                if(response == JFileChooser.APPROVE_OPTION) {
                    try {
                        File selectedFile = fileChooser.getSelectedFile();
                        insuranceFilePath[0] = String.valueOf(fileChooser.getSelectedFile().toPath());
                        //insuranceDestinationPath[0] = String.valueOf(destination.toPath().resolve(selectedFile.getName()));
                        //Files.copy(selectedPath, insuranceDestinationPath, StandardCopyOption.REPLACE_EXISTING);
                    } catch (Exception ex) {
                        throw new RuntimeException(ex);
                    }
                }
            }
        });
        mainPanel.add(selectInsuranceButton);

        JCheckBox leasingCheckBox = new JCheckBox("Leasing");
        redundantMethods.setComponentDesign(leasingCheckBox, new Dimension(70, 50), null,
                new Color(150, 150, 150), new Font("Arial", Font.PLAIN, 12));
        leasingCheckBox.setBorder(BorderFactory.createMatteBorder(0 , 0, 2, 0, new Color(150, 150, 150)));
        leasingCheckBox.setFocusable(false);
        leasingCheckBox.setAlignmentX(Component.CENTER_ALIGNMENT);
        //JPanel leasingPanel = new JPanel();
        mainPanel.add(leasingCheckBox);

        JPanel totalPanel = new JPanel();
        redundantMethods.setPanelDesign(totalPanel, new Dimension(350, 50), new Color(45, 45,45),
                new FlowLayout(FlowLayout.LEFT), BorderFactory.createMatteBorder(0 , 0, 2, 0, new Color(150, 150, 150)));
        mainPanel.add(totalPanel);

        JTextField totalLeasing = new JTextField("Total Leasing");
        redundantMethods.setComponentDesign(totalLeasing, new Dimension(350, 40),
                new Color(45, 45, 45), new Color(150, 150, 150), new Font("Arial", Font.BOLD, 16));
        totalPanel.setVisible(false);
        totalPanel.add(totalLeasing);

        totalPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        JPanel monthlyPayPanel = new JPanel();
        redundantMethods.setPanelDesign(monthlyPayPanel, new Dimension(350, 50), new Color(45, 45, 45),
                new FlowLayout(FlowLayout.LEFT), BorderFactory.createMatteBorder(0 , 0, 2, 0, new Color(150, 150, 150)));
        mainPanel.add(monthlyPayPanel);

        JTextField monthlyPay = new JTextField("Monthly Pay");
        redundantMethods.setComponentDesign(monthlyPay, new Dimension(350, 40),
                new Color(45, 45, 45), new Color(150, 150, 150), new Font("Arial", Font.BOLD, 16));

        monthlyPayPanel.setVisible(false);
        monthlyPayPanel.add(monthlyPay);

        monthlyPayPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        JPanel leasingProgressPanel = new JPanel();
        redundantMethods.setPanelDesign(leasingProgressPanel, new Dimension(350, 50), new Color(45, 45, 45),
                new FlowLayout(FlowLayout.LEFT), BorderFactory.createMatteBorder(0 , 0, 2, 0, new Color(150, 150, 150)));
        leasingProgressPanel.setVisible(false);
        mainPanel.add(leasingProgressPanel);

        JProgressBar progressBar = new JProgressBar(0, 100);
        redundantMethods.setComponentDesign(progressBar, new Dimension(340, 40),
                new Color(45, 45, 45), new Color(150, 150, 150), null);
        progressBar.setBorder(BorderFactory.createMatteBorder(0, 2, 0, 2, new Color(150, 150, 150)));
        progressBar.setStringPainted(true);
        progressBar.setUI(new BasicProgressBarUI());
        //long monthsLeft = redundantMethods.getMonths(String.valueOf(leasingLocalDate));
        progressBar.setValue(0);
        progressBar.setString("Paid Leasing (" + 0 + "%)");
        leasingProgressPanel.add(progressBar);

        leasingProgressPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        String[] days = new String[32];
        days[0] = "Due date payment";
        for(int i = 1; i < 32; i++) {
            days[i] = String.valueOf(i);
        }
        JPanel dueDatePanel = new JPanel();
        redundantMethods.setPanelDesign(dueDatePanel, new Dimension(210, 50), new Color(45, 45, 45),
                new FlowLayout(FlowLayout.LEFT), BorderFactory.createMatteBorder(0 , 0, 2, 0, new Color(150, 150, 150)));
        dueDatePanel.setVisible(false);
        mainPanel.add(dueDatePanel);

        JComboBox daysComboBox = new JComboBox(days);
        redundantMethods.setComponentDesign(daysComboBox, new Dimension(200, 40),
                new Color(45, 45, 45), new Color(150, 150, 150), new Font("Arial", Font.BOLD, 16));
        dueDatePanel.add(daysComboBox);

        totalLeasing.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if(totalLeasing.getText().equals("Total Leasing")) {
                    totalLeasing.setText("");
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if(totalLeasing.getText().equals("Total Leasing") || totalLeasing.getText().trim().equals("")) {
                    totalLeasing.setText("Total Leasing");
                } else if(!monthlyPay.getText().equals("Monthly Pay") && !totalLeasing.getText().equals("Total Leasing")) {
                    redundantMethods.setPercentage(Double.parseDouble(monthlyPay.getText()), Double.parseDouble(totalLeasing.getText()), progressBar);
                }
            }
        });

        monthlyPay.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if(monthlyPay.getText().equals("Monthly Pay")) {
                    monthlyPay.setText("");
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if(monthlyPay.getText().equals("Monthly Pay") || monthlyPay.getText().trim().equals("")) {
                    monthlyPay.setText("Monthly Pay");
                } else if(!monthlyPay.getText().equals("Monthly Pay") && !totalLeasing.getText().equals("Total Leasing")) {
                    redundantMethods.setPercentage(Double.parseDouble(monthlyPay.getText()), Double.parseDouble(totalLeasing.getText()), progressBar);
                }
            }
        });

        leasingCheckBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(leasingCheckBox.isSelected()) {
                    redundantMethods.setCheckBoxValue(true, totalPanel, monthlyPayPanel, leasingProgressPanel, dueDatePanel);
                } else {
                    redundantMethods.setCheckBoxValue(false, totalPanel, monthlyPayPanel, leasingProgressPanel, dueDatePanel);
                }
                mainPanel.revalidate();
                mainPanel.repaint();
            }
        });

        mainPanel.add(Box.createRigidArea(new Dimension(0, 10))); // gap between mainPanel and bottomPanel

        redundantMethods.setComponentDesign(saveButton, new Dimension(60, 30),
                new Color(45, 45,45), new Color(150, 150, 150),
                new Font("Arial", Font.BOLD, 20));
        saveButton.setBorder(BorderFactory.createMatteBorder(0,0, 2, 0, new Color(150, 150, 150)));
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String badValue = databaseHelper.isDataUnique(numTextPlate.getText().replaceAll(" +", "").toUpperCase(), vinTextField.getText());
                if(badValue.equals("") && redundantMethods.isDataValid(numTextPlate.getText(), vinTextField.getText())) {
                    topPanel.setVisible(true);
                    bottomPanel.setVisible(false);
                    //redundantMethods.createDirectory(numTextPlate.getText()); asta nu trebuie repus
                    String directoryPath = redundantMethods.createCarDocument(numTextPlate.getText());
                    String notesFileName = "Notes.txt";
                    File textFile = new File(directoryPath + File.separator, notesFileName);
                    try(FileWriter writer = new FileWriter(textFile)) {
                        writer.write("");
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                    if(rcaFilePath[0] != null || insuranceFilePath[0] != null) {
                        try {
                            Path insuranceSourcePath = Paths.get(insuranceFilePath[0]);
                            String insuranceFileName = insuranceSourcePath.getFileName().toString();
                            Path rcaSourcePath = Paths.get(rcaFilePath[0]);
                            String rcaFileName = rcaSourcePath.getFileName().toString();

                            Path destinationDir = Paths.get(System.getProperty("user.home"), "Expify", "Documents", numTextPlate.getText().replaceAll(" +", " ").replaceFirst("\\s++$", "").toUpperCase());
                            //Files.createDirectories(destinationDir);

                            Path insuranceDestinationPath = destinationDir.resolve(insuranceFileName);
                            Path rcaDestinationPath = destinationDir.resolve(rcaFileName);

                            if(insuranceSourcePath.toFile().exists()) Files.copy(insuranceSourcePath, insuranceDestinationPath, StandardCopyOption.REPLACE_EXISTING);
                            if(rcaSourcePath.toFile().exists()) Files.copy(rcaSourcePath, rcaDestinationPath, StandardCopyOption.REPLACE_EXISTING);

                            rcaFilePath[0] = null;
                            insuranceFilePath[0] = null;
                        } catch (IOException ex) {
                            throw new RuntimeException(ex);
                        }
                    }
                    Date rcaDate = rcaModel.getDate();
                    LocalDate rcaLocalDate = rcaDate.toInstant()
                            .atZone(ZoneId.systemDefault())
                            .toLocalDate();
                    Date insuranceDate = insuranceModel.getDate();
                    LocalDate insuranceLocalDate = insuranceDate.toInstant()
                            .atZone(ZoneId.systemDefault())
                            .toLocalDate();
                    String numPlate = numTextPlate.getText().toUpperCase().trim().replaceAll(" +", " ");
                    String vinNumber = vinTextField.getText().toUpperCase();
                    String totalLeasingAmount = totalLeasing.getText();
                    String monthlyPayAmount = monthlyPay.getText();
                    if(totalLeasingAmount.equals("Total Leasing")) {
                        totalLeasingAmount = "0";
                    }
                    if(monthlyPayAmount.equals("Monthly Pay")) {
                        monthlyPayAmount = "0";
                    }
                    databaseHelper.insertValue(numPlate, vinNumber, rcaLocalDate, insuranceLocalDate, daysComboBox.getSelectedIndex(), totalLeasingAmount, monthlyPayAmount);
                    databaseHelper.printCars(mainPanel, bottomPanel, "");
                } else if(!badValue.equals("")) {
                    if(badValue.equals(numTextPlate.getText().replaceAll(" +", "").toUpperCase())) {
                        numTextPlate.setText("INVALID VALUE!");
                    } else {
                        vinTextField.setText("INVALID VALUE!");
                        System.out.println("vin Value: " + vinTextField.getText());
                    }
                    redundantMethods.setPanelDesign(notificationPanel, new Dimension(130, 40),
                            null, new FlowLayout(FlowLayout.LEFT), null);
                    JTextField notificationTextField = new JTextField("Invalid input");
                    redundantMethods.setComponentDesign(notificationTextField, new Dimension(120, 30),
                            null, new Color(150, 150, 150), new Font("Arial", Font.BOLD, 14));
                    notificationTextField.setEditable(false);
                    notificationTextField.setBorder(new MatteBorder(0,0, 2, 0, new Color(150, 150, 150)));
                    notificationTextField.setHorizontalAlignment(JTextField.CENTER);
                    notificationPanel.add(notificationTextField);
                    mainPanel.add(notificationPanel, BorderLayout.CENTER);
                    mainPanel.revalidate();
                    mainPanel.repaint();
                }
            }
        });
        bottomPanel.add(saveButton);

        redundantMethods.setComponentDesign(cancelButton, new Dimension(80, 30),
                new Color(45, 45, 45), new Color(150, 150, 150),
                new Font("Arial", Font.BOLD, 20));
        cancelButton.setBorder(BorderFactory.createMatteBorder(0,0, 2, 0, new Color(150, 150, 150)));
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(databaseHelper.isDatabaseEmpty()) {
                    new NoCarsAdded(mainPanel, topPanel, bottomPanel);
                } else {
                    redundantMethods.loadMainPanel(mainPanel, topPanel, bottomPanel);
                }
            }
        });
        bottomPanel.add(cancelButton);

        mainPanel.repaint();
        mainPanel.revalidate();
    }
}
