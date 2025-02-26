import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import javax.swing.plaf.basic.BasicProgressBarUI;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.List;

public class RedundantMethods {

    public void createCarPanel(JPanel mainPanel, JPanel bottomPanel, String plate, String vin, String daysLeft, String rcaDate,
                               long rcaDays, String insuranceDate, long insuranceDays, String dueDate, String totalLeasing, String monthlyPay) {
        DatabaseHelper databaseHelper = new DatabaseHelper();
        String editedRcaDate = "";
        String editedInsuranceDate = "";
        long editedRcaDays = 0;
        long editedInsDays = 0;

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
                /*rcaDateTextField.addFocusListener(new FocusListener() {
                    @Override
                    public void focusGained(FocusEvent e) {
                        if(rcaDateTextField.getText().contains("Edit:")) {
                            rcaDateTextField.setText(rcaDate);
                        }
                    }

                    @Override
                    public void focusLost(FocusEvent e) {
                        rcaDateTextField.setText("Edit: " + rcaDate);
                    }
                });*/

                JTextField insuranceDateTextField = new JTextField("Insurance expiration date: " + insuranceDate + " (" + insuranceDays + " days left)");
                setComponentDesign(insuranceDateTextField,  new Dimension(450, 30), null, new Color(150, 150, 150),
                        new Font("Arial", Font.BOLD, 16));
                insuranceDateTextField.setEditable(false);
                mainPanel.add(insuranceDateTextField);
                /*insuranceDateTextField.addFocusListener(new FocusListener() {
                    @Override
                    public void focusGained(FocusEvent e) {
                        if(insuranceDateTextField.getText().contains("Edit:")) {
                            insuranceDateTextField.setText(rcaDate);
                        }
                    }

                    @Override
                    public void focusLost(FocusEvent e) {
                        insuranceDateTextField.setText("Edit: " + rcaDate);
                    }
                });*/

                if(!dueDate.equals("0")) {
                    System.out.println("due date: " + dueDate);
                    JTextField totalLeasingAmount = new JTextField("Total leasing amount: " + totalLeasing);
                    setComponentDesign(totalLeasingAmount, new Dimension(450, 30), null, new Color(150, 150, 150),
                            new Font("Arial", Font.BOLD, 16));
                    totalLeasingAmount.setEditable(false);
                    mainPanel.add(totalLeasingAmount);

                    JTextField monthlyPayAmount = new JTextField("Monthly paid amount: " + monthlyPay);
                    setComponentDesign(monthlyPayAmount, new Dimension(450, 30), null, new Color(150, 150, 150),
                            new Font("Arial", Font.BOLD, 16));
                    monthlyPayAmount.setEditable(false);
                    mainPanel.add(monthlyPayAmount);

                    JTextField dueDateTextField = new JTextField("Due date: " + dueDate + " " + new SimpleDateFormat("MMM").format(Calendar.getInstance().getTime()).toUpperCase());
                    setComponentDesign(dueDateTextField, new Dimension(450, 30), null, new Color(150, 150, 150),
                            new Font("Arial", Font.BOLD, 16));
                    dueDateTextField.setEditable(false);
                    if(Integer.parseInt(dueDate) < LocalDate.now().getDayOfMonth() || Integer.parseInt(dueDate) == LocalDate.now().getDayOfMonth()) {
                        dueDateTextField.setText(String.format("Due date: " + dueDate + " " + String.valueOf(LocalDate.now().getMonth().plus(1)).substring(0, 3)));
                    }
                    mainPanel.add(dueDateTextField);

                    JProgressBar progressBar = new JProgressBar(0, 100);
                    setComponentDesign(progressBar, new Dimension(350, 40),
                            new Color(45, 45, 45), new Color(150, 150, 150), null);
                    progressBar.setBorder(BorderFactory.createMatteBorder(0, 2, 0, 2, new Color(150, 150, 150)));
                    progressBar.setStringPainted(true);
                    progressBar.setUI(new BasicProgressBarUI());
                    //long monthsLeft = redundantMethods.getMonths(String.valueOf(leasingLocalDate));
                    progressBar.setValue(0);
                    progressBar.setString("Paid Leasing (" + 0 + "%)");
                    setPercentage(Double.valueOf(monthlyPay), Double.valueOf(totalLeasing), progressBar);
                    mainPanel.add(progressBar);

                    mainPanel.add(Box.createVerticalStrut(10));
                }

                JTextArea notesField = new JTextArea("Notes:");
                databaseHelper.readNotesFromFile(plate, notesField);
                if(notesField.getText().trim().isEmpty() || notesField.getText().trim().equals("Notes:")) {
                    notesField.setText("Notes:");
                }
                setComponentDesign(notesField, new Dimension(350, 150), new Color(45, 45, 45), new Color(150, 150, 150), null);
                notesField.setLineWrap(true);
                notesField.setWrapStyleWord(true);
                notesField.setCaretColor(Color.white);
                notesField.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, new Color(150, 150, 150)));
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
                ((AbstractDocument) notesField.getDocument()).setDocumentFilter(new DocumentFilter() {
                    private final int MAX_ROWS = 8;

                    @Override
                    public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
                        if (canInsert(fb.getDocument(), string)) {
                            super.insertString(fb, offset, string, attr);
                        }
                    }

                    @Override
                    public void replace(DocumentFilter.FilterBypass fb, int offset, int length, String string, AttributeSet attr) throws BadLocationException {
                        if (canInsert(fb.getDocument(), string)) {
                            super.replace(fb, offset, length, string, attr);
                        }
                    }

                    private boolean canInsert(Document doc, String string) throws BadLocationException {
                        String text = doc.getText(0, doc.getLength()) + string;
                        int rows = text.split("\n").length;
                        return rows <= MAX_ROWS;
                    }
                });
                mainPanel.add(notesField);

                mainPanel.add(Box.createVerticalStrut(10));

                JTextArea reminderDescription = new JTextArea("Reminder description:");
                setComponentDesign(reminderDescription, new Dimension(350, 50), new Color(45, 45, 45), new Color(150, 150, 150),
                        new Font("Arial", Font.BOLD, 16));
                reminderDescription.setCaretColor(new Color(150, 150, 150));
                reminderDescription.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, new Color(150, 150, 150)));
                reminderDescription.setLineWrap(true);
                reminderDescription.setVisible(false);
                mainPanel.add(reminderDescription);

                String descriptionString = reminderDescription.getText();
                reminderDescription.addFocusListener(new FocusListener() {
                    @Override
                    public void focusGained(FocusEvent e) {
                        if(descriptionString.trim().equals("Reminder description:") || descriptionString.trim().equals("Error: Reminder description is empty")) {
                            reminderDescription.setText("");
                        }
                    }
                    @Override
                    public void focusLost(FocusEvent e) {
                        if(descriptionString.trim().isEmpty()) {
                            reminderDescription.setText("Reminder description:");
                        }
                    }
                });

                Date startDate = new Date(100, Calendar.JANUARY, 1);
                Date endDate = new Date(200, Calendar.DECEMBER, 31);
                SpinnerDateModel reminderModel = new SpinnerDateModel(new Date(), startDate, endDate, Calendar.DAY_OF_MONTH);
                JSpinner reminderSpinDate = new JSpinner(reminderModel);
                setCalendar(reminderSpinDate);
                reminderSpinDate.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, new Color(150, 150, 150)));
                reminderSpinDate.setVisible(false);
                mainPanel.add(reminderSpinDate);

                mainPanel.add(Box.createVerticalStrut(5));

                JButton addReminderButton = new JButton("Add Reminder");
                setComponentDesign(addReminderButton, new Dimension(150, 30), new Color(45, 45 ,45), new Color(150, 150, 150),
                        new Font("Arial", Font.BOLD, 16));
                addReminderButton.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, new Color(150, 150, 150)));
                addReminderButton.setAlignmentX(Component.CENTER_ALIGNMENT);
                mainPanel.add(addReminderButton);

                JButton saveReminderButton = new JButton("Save Reminder");
                setComponentDesign(saveReminderButton, new Dimension(150, 30), new Color(45, 45, 45), new Color(150, 150, 150),
                        new Font("Arial", Font.BOLD, 16));
                saveReminderButton.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, new Color(150, 150, 150)));
                saveReminderButton.setAlignmentX(Component.CENTER_ALIGNMENT);
                saveReminderButton.setVisible(false);
                mainPanel.add(saveReminderButton);

                mainPanel.add(Box.createVerticalStrut(5));

                Map<String, List<String>> reminderMap = databaseHelper.reminderMap(plate);
                for(Map.Entry<String, List<String>> entry :reminderMap.entrySet()) {
                    String key = entry.getKey();
                    for(String reminder :entry.getValue()) {
                        JPanel reminderPanel = new JPanel();
                        setPanelDesign(reminderPanel, new Dimension(350, 50), new Color(45, 45, 45),
                                new FlowLayout(FlowLayout.LEFT), BorderFactory.createMatteBorder(0, 0, 2, 0, new Color(150, 150, 150)));
                        mainPanel.add(reminderPanel);

                        JTextArea reminderTextArea = new JTextArea(key + ": " + reminder);
                        setComponentDesign(reminderTextArea, new Dimension(290, 40), new Color(45, 45, 45), new Color(150, 150, 150),
                                new Font("Arial", Font.BOLD, 16));
                        reminderTextArea.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 2, new Color(150, 150, 150)));
                        reminderTextArea.setCaretColor(new Color(150, 150, 150));
                        reminderTextArea.setLineWrap(true);
                        reminderTextArea.setEditable(false);
                        reminderPanel.add(reminderTextArea);

                        String description = reminderTextArea.getText().substring(12);
                        int reminderValue = databaseHelper.getReminderValue(plate, description, LocalDate.parse(key));
                        JButton markAsDone = new JButton(String.valueOf(reminderValue)); // "I" means is marked as done
                        setComponentDesign(markAsDone, new Dimension(20, 40), new Color(45, 45, 45), new Color(150, 150, 150),
                                new Font("Arial", Font.BOLD, 16));
                        markAsDone.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, new Color(150, 150, 150)));
                        reminderPanel.add(markAsDone);
                        markAsDone.addActionListener(new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                if(markAsDone.getText().equals("0")) {
                                    markAsDone.setText("1");
                                    databaseHelper.updateReminder(plate, description, LocalDate.parse(key), 1);
                                } else if(markAsDone.getText().equals("1")) {
                                    markAsDone.setText("0");
                                    databaseHelper.updateReminder(plate, description, LocalDate.parse(key), 0);
                                }
                            }
                        });

                        JButton removeReminderButton = new JButton("X");
                        setComponentDesign(removeReminderButton, new Dimension(20, 40), new Color(45, 45, 45), new Color(150, 150, 150),
                                new Font("Arial", Font.BOLD, 16));
                        removeReminderButton.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, new Color(150, 150, 150)));
                        reminderPanel.add(removeReminderButton);
                        removeReminderButton.addActionListener(new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                mainPanel.remove(reminderPanel);
                                mainPanel.revalidate();
                                mainPanel.repaint();
                                databaseHelper.removeReminder(plate, description, LocalDate.parse(key));
                            }
                        });
                    }
                }

                addReminderButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        reminderDescription.setVisible(true);
                        reminderDescription.setEditable(true);
                        saveReminderButton.setVisible(true);
                        reminderSpinDate.setVisible(true);
                        addReminderButton.setVisible(false);
                    }
                });

                saveReminderButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if(!reminderDescription.getText().trim().isEmpty()) {
                            reminderDescription.setEditable(false);
                            reminderDescription.setVisible(false);
                            saveReminderButton.setVisible(false);
                            reminderSpinDate.setVisible(false);
                            addReminderButton.setVisible(true);

                            String description = reminderDescription.getText();
                            Date date = reminderModel.getDate();
                            LocalDate reminderLocalDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                            databaseHelper.insertReminder(plate, description, reminderLocalDate);

                            JPanel reminderPanel = new JPanel();
                            setPanelDesign(reminderPanel, new Dimension(350, 50), new Color(45, 45, 45),
                                    new FlowLayout(FlowLayout.LEFT), BorderFactory.createMatteBorder(0, 0, 2, 0, new Color(150, 150, 150)));
                            mainPanel.add(reminderPanel);

                            JTextArea addedReminderDescription = new JTextArea(reminderLocalDate + ": " + reminderDescription.getText());
                            addedReminderDescription.setLineWrap(true);
                            setComponentDesign(addedReminderDescription, new Dimension(290, 40), new Color(45, 45, 45), new Color(150, 150, 150),
                                    new Font("Arial", Font.BOLD, 16));
                            addedReminderDescription.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 2, new Color(150, 150, 150)));
                            reminderPanel.add(addedReminderDescription);

                            JButton markAsDone = new JButton("0"); // "1" means is marked as done
                            setComponentDesign(markAsDone, new Dimension(20, 40), new Color(45, 45, 45), new Color(150, 150, 150),
                                    new Font("Arial", Font.BOLD, 16));
                            markAsDone.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, new Color(150, 150, 150)));
                            reminderPanel.add(markAsDone);
                            markAsDone.addActionListener(new ActionListener() {
                                @Override
                                public void actionPerformed(ActionEvent e) {
                                    markAsDone.setText("1");
                                    databaseHelper.updateReminder(plate, description, reminderLocalDate, 1);
                                }
                            });

                            JButton removeReminderButton = new JButton("X");
                            setComponentDesign(removeReminderButton, new Dimension(20, 40), new Color(45, 45, 45), new Color(150, 150, 150),
                                    new Font("Arial", Font.BOLD, 16));
                            removeReminderButton.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, new Color(150, 150, 150)));
                            reminderPanel.add(removeReminderButton);
                            removeReminderButton.addActionListener(new ActionListener() {
                                @Override
                                public void actionPerformed(ActionEvent e) {
                                    mainPanel.remove(reminderPanel);
                                    mainPanel.revalidate();
                                    mainPanel.repaint();
                                    databaseHelper.removeReminder(plate, description, reminderLocalDate);
                                }
                            });

                            mainPanel.add(Box.createVerticalStrut(5));
                            reminderDescription.setText("Reminder description:");
                            reminderModel.setValue(new Date());
                        } else {
                            reminderDescription.setText("Error: Reminder description is empty");
                        }
                    }
                });

                mainPanel.add(Box.createVerticalStrut(5));

                bottomPanel.removeAll();

                JButton viewDocumentButton = new JButton("View Document(s)");
                setComponentDesign(viewDocumentButton, new Dimension(150, 30), null, new Color(150, 150, 150),
                        new Font("Arial", Font.BOLD, 16));
                viewDocumentButton.setBorder(new MatteBorder(0 , 0, 2, 0, new Color(150, 150, 150)));
                viewDocumentButton.setAlignmentX(Component.CENTER_ALIGNMENT);
                bottomPanel.add(viewDocumentButton);
                viewDocumentButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        File folder = new File(System.getProperty("user.home") + File.separator + "Expify" + File.separator + "Documents" + File.separator + plate);
                        if (folder.exists() && Desktop.isDesktopSupported()) {
                            try {
                                Desktop.getDesktop().open(folder);
                            } catch (IOException ex) {
                                ex.printStackTrace();
                            }
                        }
                    }
                });

                JButton addDocumentButton = new JButton("Add Document");
                setComponentDesign(addDocumentButton, new Dimension(150, 30), null, new Color(150, 150, 150),
                        new Font("Arial", Font.BOLD, 16));
                addDocumentButton.setBorder(new MatteBorder(0 , 0, 2, 0, new Color(150, 150, 150)));
                bottomPanel.add(addDocumentButton);
                addDocumentButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        JFileChooser fileChooser = new JFileChooser();
                        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
                        fileChooser.setDialogTitle("Choose insurance file");
                        fileChooser.setAcceptAllFileFilterUsed(false);
                        Path destinationDir = Paths.get(System.getProperty("user.home"), "Expify", "Documents", plate);
                        int response = fileChooser.showOpenDialog(null);
                        if(response == JFileChooser.APPROVE_OPTION) {
                            try {
                                Files.createDirectories(destinationDir);

                                File selectedFile = fileChooser.getSelectedFile();
                                Path fileSourcePath = selectedFile.toPath();
                                Path fileDestinationPath = destinationDir.resolve(selectedFile.getName());
                                //fileSourcePath = String.valueOf(fileChooser.getSelectedFile().toPath());
                                //insuranceDestinationPath[0] = String.valueOf(destination.toPath().resolve(selectedFile.getName()));
                                Files.copy(fileSourcePath, fileDestinationPath, StandardCopyOption.REPLACE_EXISTING);
                            } catch (Exception ex) {
                                throw new RuntimeException(ex);
                            }
                        }
                    }
                });

                JButton editButton = new JButton("Edit Fields");
                setComponentDesign(editButton, new Dimension(100, 30), null, new Color(150, 150, 150),
                        new Font("Arial", Font.BOLD, 16));
                editButton.setBorder(new MatteBorder(0 , 0, 2, 0, new Color(150, 150, 150)));
                bottomPanel.add(editButton);

                JButton saveEditedFieldsButton = new JButton("Save");
                setComponentDesign(saveEditedFieldsButton, new Dimension(100, 30), null, new Color(150, 150, 150),
                        new Font("Arial", Font.BOLD, 16));
                saveEditedFieldsButton.setBorder(new MatteBorder(0 , 0, 2, 0, new Color(150, 150, 150)));
                saveEditedFieldsButton.setVisible(false);
                bottomPanel.add(saveEditedFieldsButton);

                editButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        saveEditedFieldsButton.setVisible(true);
                        editButton.setVisible(false);
                        rcaDateTextField.setEditable(true);
                        insuranceDateTextField.setEditable(true);

                        if(rcaDateTextField.getText().contains(rcaDate)) {
                            rcaDateTextField.setText("Edit: " + rcaDate);
                        } else {
                            String editedDate = rcaDateTextField.getText().substring(21, 31);
                            rcaDateTextField.setText("Edit: " + editedDate);
                        }
                        if(insuranceDateTextField.getText().contains(insuranceDate)) {
                            insuranceDateTextField.setText("Edit: " + insuranceDate);
                        } else {
                            String editedDate = insuranceDateTextField.getText().substring(27, 37);
                            insuranceDateTextField.setText("Edit: " + editedDate);
                        }
                    }
                });

                saveEditedFieldsButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        editButton.setVisible(true);
                        saveEditedFieldsButton.setVisible(false);
                        rcaDateTextField.setEditable(false);
                        insuranceDateTextField.setEditable(false);
                        String editedRcaDate = rcaDateTextField.getText().substring(6);
                        String editedInsuranceDate = insuranceDateTextField.getText().substring(6);
                        long editedRcaDays = daysLeft(LocalDate.parse(rcaDateTextField.getText().substring(6)));
                        long editedInsDays = daysLeft(LocalDate.parse(insuranceDateTextField.getText().substring(6)));

                        rcaDateTextField.setText("RCA expiration date: " + editedRcaDate + " (" + editedRcaDays + " days left)");
                        insuranceDateTextField.setText("Insurance expiration date: " + editedInsuranceDate + " (" + editedInsDays + " days left)");
                        databaseHelper.updateCalendar(plate, editedRcaDate, editedInsuranceDate);
                        databaseHelper.updateClosestDate();

                        String directoryPath = createCarDocument(plate);
                        String notesFileName = "Notes.txt";
                        try(FileWriter writer = new FileWriter(directoryPath + File.separator + notesFileName)) {
                            writer.write(notesField.getText());
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
                    }
                });

                bottomPanel.setVisible(true);
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
        return numTextPlate.length() >= 3 && isVinValid(vinTextField) && !numTextPlate.equals("Car Num Plate");
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

    public Date createLocalDate(Date date) {
        date.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
        return date;
    }

    public void setPercentage(Double monthlyPay, Double totalPay, JProgressBar progressBar) {
        int percentage = (int) ((monthlyPay / totalPay) * 100);
        progressBar.setValue(percentage);
        progressBar.setString("Paid leasing (" + percentage + "%)");
    }

    public String createCarDocument(String numTextPlate) {
        String userHome = System.getProperty("user.home") + File.separator;

        String directoryPath = userHome + "Expify" + File.separator + "Documents" + File.separator + numTextPlate.replaceAll(" +", " ").toUpperCase();
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

    public long getMonths(String leasingDate) {
        return ChronoUnit.MONTHS.between(LocalDate.parse(leasingDate), LocalDate.now());
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

    public Map<String, List<Integer>> getAllMinDays() {
        DatabaseHelper databaseHelper = new DatabaseHelper();
        Map<String, List<Integer>> carDaysMap = databaseHelper.getAllDays();
        for(Map.Entry<String, List<Integer>> entry :carDaysMap.entrySet()) {
            String key = entry.getKey();
            List<Integer> value = new ArrayList<>(entry.getValue());

            value.removeIf(num -> num > 30 || num < 0);
            carDaysMap.put(key, value);
        }
        carDaysMap.entrySet().removeIf(e -> e.getValue().isEmpty());
        System.out.println("all min days" + carDaysMap);

        return carDaysMap;
    }

    public boolean mapsNotEmpty(Map<String, List<Integer>> carDaysMap, Map<String, List<Integer>> dueDateMap, Map<String, List<String>> reminderMap) {
        return carDaysMap.isEmpty() || dueDateMap.isEmpty() || reminderMap.isEmpty();
    }

    public void setCheckBoxValue(boolean value, JPanel totalPanel,JPanel monthlyPayPanel,
                                 JPanel leasingProgressPanel, JPanel dueDatePanel) {
        totalPanel.setVisible(value);
        monthlyPayPanel.setVisible(value);
        leasingProgressPanel.setVisible(value);
        dueDatePanel.setVisible(value);
    }

    public void insert10Cars() {
        DatabaseHelper databaseHelper = new DatabaseHelper();

        for(int i=0; i<10; i++) {
            databaseHelper.insertValue("car " + i, "Vin number", LocalDate.now(), LocalDate.now(), 2, "4500", "1200");
        }
    }
}
