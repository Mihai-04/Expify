import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExpifyFrame {
    JFrame frame = new JFrame("Expify");
    JPanel topPanel = new JPanel();
    JPanel mainPanel = new JPanel();
    JScrollPane scrollPane = new JScrollPane(mainPanel);
    JPanel bottomPanel = new JPanel();
    JButton addFileButton = new JButton("+");
    JButton searchCarButton = new JButton("-");
    JTextField searchCarField = new JTextField("Search car");
    JButton viewAllCars = new JButton("View cars");
    RedundantMethods redundantMethods = new RedundantMethods();
    DatabaseHelper databaseHelper = new DatabaseHelper();
    ImageIcon carIcon = new ImageIcon("icon.png");

    public ExpifyFrame() {
        frame.setVisible(true);
        frame.setSize(500, 580);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.getContentPane().setBackground(new Color(30, 30, 30));
        frame.setLayout(new BorderLayout());
        frame.setIconImage(carIcon.getImage());

        File path = new File(System.getProperty("user.home") + File.separator + "Expify");
        if(!path.exists()) {
            redundantMethods.createDirectory();
        }

        databaseHelper.updateClosestDate();
        Map<String, List<Integer>> carDaysMap = redundantMethods.getAllMinDays();
        Map<String, List<Integer>> dueDateMap = databaseHelper.getDueDateMap();
        Map<String, List<String>> reminderMap = databaseHelper.getReminderMap();
        if(!redundantMethods.mapsNotEmpty(carDaysMap, dueDateMap, reminderMap)) {
            System.out.println("test");
            SendMail mail = new SendMail();
            StringBuilder emailBody = new StringBuilder();
            if(!carDaysMap.isEmpty()) {
                emailBody.append("Remaining Expiration Days:");
                emailBody.append("\n");
                for(Map.Entry<String, List<Integer>> entry : carDaysMap.entrySet()) {
                    emailBody.append(entry.getKey() + ": ")
                            .append(entry.getValue().toString().replaceAll("[\\[\\]]", "") + " day(s) left");
                    emailBody.append("\n");
                }
            }
            if(!dueDateMap.isEmpty()) {
                emailBody.append("\n");
                emailBody.append("Due date soon:");
                emailBody.append("\n");
                for(Map.Entry<String, List<Integer>> entry : dueDateMap.entrySet()) {
                    emailBody.append(entry.getKey() + ": ")
                            .append(entry.getValue().toString().replaceAll("[\\[\\]]", "") + " day(s) left");
                    emailBody.append("\n");
                }
            }
            if(!reminderMap.isEmpty()) {
                emailBody.append("\n");
                emailBody.append("Reminders:");
                emailBody.append("\n");
                for(Map.Entry<String, List<String>> entry : reminderMap.entrySet()) {
                    emailBody.append(entry.getKey() + ": ".replaceAll("[\\[\\]]", ""));
                    emailBody.append("\n");
                    for(String reminder : entry.getValue()) {
                        emailBody.append(reminder.replaceAll("[\\[\\]]", ""));
                        emailBody.append("\n");
                    }
                }
            }
            try {
                mail.sendDocumentsMail("voicumihai81@gmail.com", "Reminders soon", emailBody.toString());
            } catch(Exception e) {
                throw new RuntimeException(e);
            }
        }

        topPanel.setBackground(new Color(45, 45, 45));
        topPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        topPanel.setBorder(BorderFactory.createMatteBorder(0,0, 2, 0, new Color(150, 150, 150)));
        frame.add(topPanel, BorderLayout.NORTH);

        mainPanel.setBackground(new Color(30, 30, 30));
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        if(databaseHelper.isDatabaseEmpty()) {
            topPanel.setEnabled(false);
            topPanel.setVisible(false);
            new NoCarsAdded(mainPanel, topPanel, bottomPanel);
            mainPanel.revalidate();
            mainPanel.repaint();
        } else {
            databaseHelper.printCars(mainPanel, bottomPanel, "");
        }

        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(10);
        frame.add(scrollPane, BorderLayout.CENTER);

        bottomPanel.setEnabled(false);
        bottomPanel.setVisible(false);
        bottomPanel.setBackground(new Color(45, 45, 45));
        bottomPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 15, 15));
        frame.add(bottomPanel, BorderLayout.SOUTH);

        //addFileButton
        redundantMethods.setComponentDesign(addFileButton, new Dimension(30, 30),
                new Color(45, 45, 45), new Color(150, 150, 150),
                new Font("Arial", Font.BOLD, 20));
            topPanel.add(addFileButton);

        //viewAllCars
        redundantMethods.setComponentDesign(viewAllCars, new Dimension(120, 30),
                new Color(45, 45, 45), new Color(150, 150, 150),
                new Font("Arial", Font.BOLD, 20));
        topPanel.add(viewAllCars);

        //searchCarButton
        redundantMethods.setComponentDesign(searchCarButton, new Dimension(30, 30),
                new Color(45, 45, 45), new Color(150, 150, 150),
                new Font("Arial", Font.BOLD, 20));
        topPanel.add(searchCarButton);

        //searchCarText
        redundantMethods.setComponentDesign(searchCarField, new Dimension(100, 30),
                new Color(70, 70, 70), new Color(150, 150, 150),
                new Font("Arial", Font.BOLD, 16));
        topPanel.add(searchCarField);

        addFileButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainPanel.removeAll();
                topPanel.setVisible(false);
                new NewCarAddedHandler(mainPanel, topPanel, bottomPanel);
            }
        });

        viewAllCars.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainPanel.removeAll();
                if(databaseHelper.isDatabaseEmpty()) {
                    new NoCarsAdded(mainPanel, topPanel, bottomPanel);
                } else {
                    databaseHelper.printCars(mainPanel, bottomPanel, "");
                }
                mainPanel.revalidate();
                mainPanel.repaint();
            }
        });

        searchCarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String search = searchCarField.getText().toUpperCase();
                System.out.println("search: " + search);
                if(databaseHelper.getValue(search)) { //if a car is found
                    databaseHelper.printCars(mainPanel, bottomPanel, search);
                } else if(search.trim().isEmpty() || search.equals("SEARCH CAR")) { //if no car is found or search if empty
                    databaseHelper.printCars(mainPanel, bottomPanel, "");
                } else if(!databaseHelper.getValue(search)) {
                    mainPanel.removeAll();
                    JTextField noCarFound = new JTextField("No car found!");
                    redundantMethods.setComponentDesign(noCarFound, new Dimension(300, 50),
                            new Color(45, 45, 45), new Color(150, 150, 150), new Font("Arial", Font.BOLD, 20));
                    noCarFound.setEditable(false);
                    noCarFound.setHorizontalAlignment(SwingConstants.CENTER);
                    mainPanel.add(noCarFound);

                    mainPanel.revalidate();
                    mainPanel.repaint();
                }
            }
        });

        searchCarField.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if(searchCarField.getText().isEmpty() || searchCarField.getText().equals("Search car")) {
                    searchCarField.setText("");
                }
            }
            @Override
            public void focusLost(FocusEvent e) {
                if(searchCarField.getText().isEmpty()) {
                    searchCarField.setText("Search car");
                }
            }
        });

        frame.repaint();
        frame.revalidate();
    }
}
