import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/*
1. pe mainPanel a apara toate masinile salvate (done)
2. cand apesi pe o masina din cele din main panel sa poti vedea toate informatiile salvate
3. cand apesi pe addFile sa apara cate un textField/docField unde sa completezi cerintele (done)
4. bottomPanel sa aiba butoane gen save, cancel, return etc (done)
5. toate textele din newCarAdded sa fie uppercase (vinText, carPlate) (done)
6. limita de caractere in vinText, carPlate etc (done)
7. isVINvalid trebuie lucrat oleaca (1FA6P8R07M5555324, U5YPC813DDL414732)
8. la fiecare masina in parte sa poti sa adaugi notes (unde sa scrii km, cand ai facut ultimul schimb de ulei etc)
9. daca iau doar o restanta sa imi iau adidasii aia (https://eu.puma.com/ro/en/pd/suede-xl-sneakers-unisex/395205.html?dwvar_395205_color=02)
10. daca trece o zi, in baza de date ramane tot atatea daysLeft (done)
(trebuie facut ca atunci cand deschizi aplicatia zilele ramase sa isi dea refresh)
11. daca un field nu este unic sa apara o notificare sa zica chestia asta + sa poti sa apesi "x"
 sa o inchizi (sa zica care informatie trebuie schimbata)
12. sa poti adauga informatii despre masina cand apesi pe o masina
13. daysLeft sa nu mai fie in baza de date si sa fie calculata cea mai apropiata data de expirare cand  apesi pe o masina
SAU sa arate cate zile mai are fiecare doc in parte (done)
 */

public class ExpifyFrame {
    JFrame frame = new JFrame("Expify");
    JPanel topPanel = new JPanel();
    JPanel mainPanel = new JPanel();
    JPanel bottomPanel = new JPanel();
    JButton addFileButton = new JButton("+");
    JButton removeFileButton = new JButton("-");
    JButton viewAllCars = new JButton("View cars");
    RedudantMethods redudantMethods = new RedudantMethods();
    DatabaseHelper databaseHelper = new DatabaseHelper();
    ImageIcon carIcon = new ImageIcon("icon.png");

    public ExpifyFrame() {
        frame.setVisible(true);
        frame.setSize(500, 500);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.getContentPane().setBackground(new Color(30, 30, 30));
        frame.setLayout(new BorderLayout());
        frame.setIconImage(carIcon.getImage());

        //databaseHelper.updateDaysLeft();
        databaseHelper.closestDate();

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
            databaseHelper.printCars(mainPanel);
        }
        frame.add(mainPanel, BorderLayout.CENTER);

        bottomPanel.setEnabled(false);
        bottomPanel.setVisible(false);
        bottomPanel.setBackground(new Color(45, 45, 45));
        bottomPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 15, 15));
        frame.add(bottomPanel, BorderLayout.SOUTH);

        //addFileButton
        redudantMethods.setButtonDesign(addFileButton, new Dimension(30, 30),
                new Color(45, 45, 45), new Color(150, 150, 150),
                new Font("Arial", Font.BOLD, 20));
            topPanel.add(addFileButton);

        //removeFileButton
        redudantMethods.setButtonDesign(removeFileButton, new Dimension(30, 30),
                new Color(45, 45, 45), new Color(150, 150, 150),
                new Font("Arial", Font.BOLD, 20));
        topPanel.add(removeFileButton);

        //viewAllCars
        redudantMethods.setButtonDesign(viewAllCars, new Dimension(120, 30),
                new Color(45, 45, 45), new Color(150, 150, 150),
                new Font("Arial", Font.BOLD, 20));
        topPanel.add(viewAllCars);

        addFileButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainPanel.removeAll();
                topPanel.setVisible(false);
                new NewCarAddedHandler(mainPanel, topPanel, bottomPanel);
                //addFileButton.setEnabled(false);
            }
        });

        viewAllCars.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainPanel.removeAll();
                databaseHelper.printCars(mainPanel);
                mainPanel.revalidate();
                mainPanel.repaint();
            }
        });

        frame.repaint();
        frame.revalidate();
    }
}
