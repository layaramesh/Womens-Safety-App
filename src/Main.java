import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.net.URI;

public class Main {
    private static final String[] QUOTES = {"You can do it!", "Keep going!"};
    private static int index = 0;
    private static final int DELAY_MS = 5_000; // 5 seconds for testing (change to 600_000 for 10 minutes)

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Women's Safety & Mental Health");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(600, 400);
            frame.setLocationRelativeTo(null);

            JLabel quoteLabel = new JLabel(QUOTES[index], SwingConstants.CENTER);
            quoteLabel.setFont(new Font("SansSerif", Font.BOLD, 28));

            JButton sosButton = new JButton("SOS");
            sosButton.addActionListener(e -> {
                int confirm = JOptionPane.showConfirmDialog(frame, "Trigger SOS? (Demo only)", "SOS", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    JOptionPane.showMessageDialog(frame, "SOS triggered — in a real app this would call contacts or send location.");
                }
            });

            JButton resourcesButton = new JButton("Resources");
            resourcesButton.addActionListener(e -> {
                String url = "https://www.who.int/health-topics/mental-health";
                try {
                    if (Desktop.isDesktopSupported()) {
                        Desktop.getDesktop().browse(new URI(url));
                    } else {
                        JOptionPane.showMessageDialog(frame, "Cannot open browser on this platform.");
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(frame, "Unable to open browser: " + ex.getMessage());
                }
            });

            JPanel bottom = new JPanel();
            bottom.add(sosButton);
            bottom.add(resourcesButton);

            frame.setLayout(new BorderLayout());
            frame.add(quoteLabel, BorderLayout.CENTER);
            frame.add(bottom, BorderLayout.SOUTH);

            Timer timer = new Timer(DELAY_MS, new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    index = (index + 1) % QUOTES.length;
                    quoteLabel.setText(QUOTES[index]);
                }
            });
            timer.setInitialDelay(DELAY_MS);
            timer.start();

            frame.setVisible(true);
        });
    }
}
