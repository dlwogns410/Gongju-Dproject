import javax.swing.*;
import java.awt.*;

class backgroundPanel extends JPanel {
    private JLabel backgroundPanel;  

    backgroundPanel() {
        setLayout(null);
        setBounds(0, 0, 800, (600 * 8 / 5) - 81);
        setSize(1300, (600 * 8 / 5) - 81);

        backgroundPanel = new JLabel();
        backgroundPanel.setBounds(10, -100, 700, 1000);

        // Load and set the background image
        ImageIcon backgroundImage = new ImageIcon("images/title.jpg");
        backgroundPanel.setIcon(backgroundImage);

        // Make the label opaque so it displays the background image
        backgroundPanel.setOpaque(false);

        // Add the background label to this panel
        add(backgroundPanel);

        // Set the background color of the panel
        setBackground(Color.WHITE);
    }
}