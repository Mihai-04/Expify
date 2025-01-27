import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/*
1. pe mainPanel a apara toate masinile salvate
2. cand apesi pe o masina din cele din main panel sa poti vedea toate documentele salvate
3. cand apesi pe addFile sa apara cate un textField/docField unde sa completezi cerintele
4. bottomPanel sa aiba butoane gen save, cancel, return etc
5. toate textele din newCarAdded sa fie uppercase (vinText, carPlate)
6.limita de caractere in vinText, carPlate etc
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
    ImageIcon carIcon = new ImageIcon("icon.png");


    public ExpifyFrame() {
        frame.setVisible(true);
        frame.setSize(500, 500);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.getContentPane().setBackground(new Color(30, 30, 30));
        frame.setLayout(new BorderLayout());
        frame.setIconImage(carIcon.getImage());

        topPanel.setBackground(new Color(45, 45, 45));
        topPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        topPanel.setBorder(BorderFactory.createMatteBorder(0,0, 2, 0, new Color(150, 150, 150)));
        frame.add(topPanel, BorderLayout.NORTH);

        mainPanel.setBackground(new Color(30, 30, 30));
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        if(mainPanel.getComponentCount() == 0) {
            topPanel.setEnabled(false);
            topPanel.setVisible(false);
            new NoCarsAdded(mainPanel, bottomPanel);
            mainPanel.revalidate();
            mainPanel.repaint();
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
                new NewCarAddedHandler(mainPanel, bottomPanel);
                addFileButton.setEnabled(false);
            }
        });

        frame.repaint();
        frame.revalidate();
    }
}
