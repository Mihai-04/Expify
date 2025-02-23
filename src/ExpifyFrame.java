import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.io.File;
import java.util.List;
import java.util.Map;

/*
1. pe mainPanel a apara toate masinile salvate (done)
2. cand apesi pe o masina din cele din main panel sa poti vedea toate informatiile salvate (done)
3. cand apesi pe addFile sa apara cate un textField/docField unde sa completezi cerintele (done)
4. bottomPanel sa aiba butoane gen save, cancel, return etc (done)
5. toate textele din newCarAdded sa fie uppercase (vinText, carPlate) (done)
6. limita de caractere in vinText, carPlate etc (done)
7. isVINvalid trebuie lucrat oleaca (1FA6P8R07M5555324, U5YPC813DDL414732)
8. la fiecare masina in parte sa poti sa adaugi notes (unde sa scrii km, cand ai facut ultimul schimb de ulei etc) (done)
9. daca iau doar o restanta sa imi iau adidasii aia (https://eu.puma.com/ro/en/pd/suede-xl-sneakers-unisex/395205.html?dwvar_395205_color=02)
10. daca trece o zi, in baza de date ramane tot atatea daysLeft (done)
(trebuie facut ca atunci cand deschizi aplicatia zilele ramase sa isi dea refresh)
11. daca un field nu este unic sa apara o notificare sa zica chestia asta + sa poti sa apesi "x" (done)
 sa o inchizi (sa zica care informatie trebuie schimbata)
12. sa poti adauga informatii despre masina cand apesi pe o masina (done)
13. daysLeft sa nu mai fie in baza de date si sa fie calculata cea mai apropiata data de expirare cand  apesi pe o masina
SAU sa arate cate zile mai are fiecare doc in parte (done)
14. cand dai selectFile se creaza folderul chiar daca nu ai dat save (folderul trebuie creat DUPA ce dai save) (done)
15. daca dai cancel sau view cars cand nu e nici o masina in baza de date nu apare chestia aia cu no cars added (done)
16. daca dai remove la o masina, ramane gap de 20 de pixeli intre celelalte masini in loc de 10 (done)
17. masina trebuie scoasa si din baza de date (done)
18. la remove car trebuie sters si folderul din Documents (done)
19. notesField nu merge cum trebuie cand dai save (textul se imparte pe mai multe randuri)
20. test unit (partial done)
21. search function (done)
22. darkMode/lightMode
23. scroll in mainPanel cand sunt mai multe masini (done)
24. closest day ar trebui inlocuit cu daysLeft (din databaeHelper sa iei doar valorile, nu se le si calculezi) (done)
25. dotenv (done)
26. cand dai select file se creaza folderul chiar daca nu dai save (FIX: daca dai cancel sa se stearga fisierul SAU sa nu se creeze deloc decat cand dai save)
27. lista aia cu cate zile mai sunt sa fie Map<String, List<Integer>> String = nr de inmatriculare, lista = rca si ins (done)
28. invalid input apare de mai multe ori (done)
29. in map adauga si valori mai mici de 0 (done)
 */

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

        databaseHelper.closestDate();
        Map<String, List<Integer>> carDaysMap = redundantMethods.getAllMinDays();
        if(!carDaysMap.isEmpty()) {
            SendMail mail = new SendMail();
            StringBuilder emailBody = new StringBuilder();
            emailBody.append("Remaining Expiration Days:");
            emailBody.append("\n");

            for(Map.Entry<String, List<Integer>> entry : carDaysMap.entrySet()) {
                emailBody.append(entry.getKey() + ": ")
                        .append(entry.getValue().toString().replaceAll("[\\[\\]]", "") + " days ");
                emailBody.append("\n");
            }
            //mail.sendMail("voicumihai81@gmail.com", "Document expiring soon", emailBody.toString());
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
                } else if(search.trim().isEmpty()) { //if no car is found or search if empty
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
