import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class NoCarsAdded {
    JTextField noCarsAddedText = new JTextField("There are no cars added.");
    JButton addCarButton = new JButton("+");
    RedudantMethods redudantMethods = new RedudantMethods();

    public NoCarsAdded(JPanel mainPanel, JPanel topPanel, JPanel bottomPanel) {
        mainPanel.add(Box.createRigidArea(new Dimension(0, 30)));
        redudantMethods.setTextFieldDesign(noCarsAddedText, new Dimension(250, 50), new Color(30, 30, 30), new Color(150, 150, 150),
                new Font("Arial", Font.BOLD, 20), new Color(30, 30, 30), false);
        noCarsAddedText.setHorizontalAlignment(SwingConstants.CENTER);
        noCarsAddedText.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, new Color(150, 150, 150)));
        noCarsAddedText.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(noCarsAddedText);

        mainPanel.add(Box.createRigidArea(new Dimension(0, 100)));

        redudantMethods.setButtonDesign(addCarButton, new Dimension(350, 120),
                null, new Color(150, 150, 150), new Font("Arial", Font.BOLD, 120));
        addCarButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        addCarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainPanel.removeAll();
                new NewCarAddedHandler(mainPanel, topPanel, bottomPanel);
                addCarButton.setEnabled(false);
            }
        });
        mainPanel.add(addCarButton);
    }
}
