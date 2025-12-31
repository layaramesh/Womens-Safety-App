import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.net.URI;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Main {
    private static final String[] QUOTES = {"You can do it!", "Keep going!"};
    private static int index = 0;
    private static final int DELAY_MS = 5_000; // 5 seconds for testing (change to 600_000 for 10 minutes)

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Women's Safety & Mental Health");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(800, 600);
            frame.setLocationRelativeTo(null);

            JLabel quoteLabel = new JLabel(QUOTES[index], SwingConstants.CENTER);
            quoteLabel.setFont(new Font("SansSerif", Font.BOLD, 28));

            // --- Create Tabbed Pane ---
            JTabbedPane tabbedPane = new JTabbedPane();

            // ===== LOCATION TAB =====
            JPanel locationTab = new JPanel(new GridBagLayout());
            locationTab.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(5, 5, 5, 5);
            gbc.fill = GridBagConstraints.HORIZONTAL;

            JLabel disclaimerLabel = new JLabel("⚠ DEMO APP: No location will actually be sent");
            disclaimerLabel.setFont(new Font("SansSerif", Font.BOLD, 13));
            disclaimerLabel.setForeground(new Color(200, 100, 0));
            gbc.gridx = 0;
            gbc.gridy = 0;
            gbc.gridwidth = 2;
            gbc.insets = new Insets(5, 10, 15, 10);
            locationTab.add(disclaimerLabel, gbc);

            JLabel nameLabel = new JLabel("Your Name:");
            nameLabel.setFont(new Font("SansSerif", Font.BOLD, 12));
            gbc.gridx = 0;
            gbc.gridy = 1;
            gbc.gridwidth = 1;
            gbc.insets = new Insets(10, 10, 5, 10);
            locationTab.add(nameLabel, gbc);

            JTextField nameField = new JTextField(20);
            nameField.setFont(new Font("SansSerif", Font.PLAIN, 12));
            nameField.setToolTipText("Enter your name for phone calls");
            gbc.gridx = 1;
            gbc.gridy = 1;
            gbc.gridwidth = 1;
            gbc.insets = new Insets(10, 10, 5, 10);
            locationTab.add(nameField, gbc);

            JLabel shareLabel = new JLabel("Share Location to (comma-separated contacts):");
            shareLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
            gbc.gridx = 0;
            gbc.gridy = 2;
            gbc.gridwidth = 2;
            gbc.insets = new Insets(15, 10, 10, 10);
            locationTab.add(shareLabel, gbc);

            JTextField contactsField = new JTextField(25);
            contactsField.setFont(new Font("SansSerif", Font.PLAIN, 12));
            contactsField.setToolTipText("Phone numbers: +11234567890");
            gbc.gridx = 0;
            gbc.gridy = 3;
            gbc.gridwidth = 2;
            gbc.insets = new Insets(5, 10, 5, 10);
            locationTab.add(contactsField, gbc);

            JLabel formatLabel = new JLabel("Format: +11234567890 (comma-separated for multiple contacts)");
            formatLabel.setFont(new Font("SansSerif", Font.ITALIC, 13));
            formatLabel.setForeground(new Color(80, 80, 80));
            gbc.gridx = 0;
            gbc.gridy = 4;
            gbc.gridwidth = 2;
            gbc.insets = new Insets(3, 10, 10, 10);
            locationTab.add(formatLabel, gbc);

            JLabel durationLabel = new JLabel("Timeout (minutes):");
            durationLabel.setFont(new Font("SansSerif", Font.BOLD, 12));
            gbc.gridx = 0;
            gbc.gridy = 5;
            gbc.gridwidth = 1;
            gbc.insets = new Insets(15, 10, 5, 10);
            locationTab.add(durationLabel, gbc);

            JTextField durationField = new JTextField("10", 5);
            durationField.setFont(new Font("SansSerif", Font.PLAIN, 12));
            durationField.selectAll(); // Select all text so user can easily replace it
            gbc.gridx = 1;
            gbc.gridy = 5;
            gbc.gridwidth = 1;
            gbc.insets = new Insets(15, 10, 5, 10);
            locationTab.add(durationField, gbc);

            JButton startShare = new JButton("Start Sharing");
            startShare.setFont(new Font("SansSerif", Font.BOLD, 12));
            gbc.gridx = 0;
            gbc.gridy = 6;
            gbc.gridwidth = 1;
            gbc.insets = new Insets(15, 10, 5, 10);
            locationTab.add(startShare, gbc);

            JButton stopShare = new JButton("Stop Sharing");
            stopShare.setFont(new Font("SansSerif", Font.BOLD, 12));
            stopShare.setEnabled(false);
            gbc.gridx = 1;
            gbc.gridy = 6;
            gbc.gridwidth = 1;
            gbc.insets = new Insets(15, 10, 5, 10);
            locationTab.add(stopShare, gbc);

            JLabel shareStatus = new JLabel("Not sharing");
            shareStatus.setForeground(new Color(0, 100, 0));
            shareStatus.setFont(new Font("SansSerif", Font.BOLD, 16));
            gbc.gridx = 0;
            gbc.gridy = 7;
            gbc.gridwidth = 2;
            gbc.insets = new Insets(15, 10, 10, 10);
            locationTab.add(shareStatus, gbc);

            // Location sharing logic
            final double[] lat = {12.9716};
            final double[] lon = {77.5946};
            final int shareIntervalMs = 10_000; // 10 seconds between updates (demo)
            final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("uuuu-MM-dd HH:mm:ss");

            final Timer[] shareTimer = new Timer[1];
            final Timer[] timeoutTimer = new Timer[1];

            Runnable stopSharing = () -> {
                if (shareTimer[0] != null && shareTimer[0].isRunning()) shareTimer[0].stop();
                if (timeoutTimer[0] != null && timeoutTimer[0].isRunning()) timeoutTimer[0].stop();
                SwingUtilities.invokeLater(() -> {
                    startShare.setEnabled(true);
                    stopShare.setEnabled(false);
                    shareStatus.setText("Not sharing");
                    shareStatus.setForeground(new Color(100, 0, 0));
                    JOptionPane.showMessageDialog(frame, "Location sharing stopped.");
                });
            };

            startShare.addActionListener(e -> {
                String name = nameField.getText().trim();
                if (name.isEmpty()) {
                    JOptionPane.showMessageDialog(frame, "Please enter your name.");
                    return;
                }
                String contacts = contactsField.getText().trim();
                if (contacts.isEmpty()) {
                    JOptionPane.showMessageDialog(frame, "Please enter at least one contact.");
                    return;
                }
                float minutes = 10;
                String durationText = durationField.getText().trim();
                if (!durationText.isEmpty()) {
                    try {
                        Float parsedMinutes = Float.parseFloat(durationText);
                        minutes = parsedMinutes;
                    } catch (NumberFormatException ex) {
                        // Keep default of 10
                    }
                }

                // Show confirmation dialog with call preview
                String callMessage = name + " has shared their location with you for " + minutes + " minutes.";
                String[] contactList = contacts.split(",");
                StringBuilder previewMessage = new StringBuilder("The following phone calls will be made:\n\n");
                for (String contact : contactList) {
                    previewMessage.append("To: ").append(contact.trim()).append("\n");
                }
                previewMessage.append("\nMessage:\n\"").append(callMessage).append("\"");

                int confirm = JOptionPane.showConfirmDialog(frame, previewMessage.toString(), 
                    "Confirm Location Sharing", JOptionPane.YES_NO_OPTION);
                
                if (confirm != JOptionPane.YES_OPTION) {
                    return; // User cancelled
                }

                startShare.setEnabled(false);
                stopShare.setEnabled(true);
                shareStatus.setText("Sharing (timeout in " + minutes + " min)...");
                shareStatus.setForeground(new Color(0, 100, 0));

                // Make phone calls to contacts
                for (String contact : contactList) {
                    String cleanContact = contact.trim();
                    makeCall(cleanContact, callMessage, frame);
                }

                // Timer to simulate periodic location updates
                shareTimer[0] = new Timer(shareIntervalMs, new ActionListener() {
                    public void actionPerformed(ActionEvent ev) {
                        // Slightly change simulated coordinates
                        lat[0] += (Math.random() - 0.5) * 0.0005;
                        lon[0] += (Math.random() - 0.5) * 0.0005;
                        String timestamp = LocalDateTime.now().format(dtf);
                        String payload = String.format("%s | contacts=%s | lat=%.6f | lon=%.6f\n",
                                timestamp, contacts, lat[0], lon[0]);
                        // Append to local log (demo for sending)
                        try (FileWriter fw = new FileWriter("shared_locations.log", true)) {
                            fw.write(payload);
                        } catch (IOException ioex) {
                            SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(frame, "Unable to log location: " + ioex.getMessage()));
                        }
                        // Brief UI flash
                        SwingUtilities.invokeLater(() -> shareStatus.setText("Last update: " + timestamp));
                    }
                });
                shareTimer[0].setInitialDelay(0);
                shareTimer[0].start();

                // Timeout to auto-stop sharing
                float durationMs = minutes * 60 * 1000;
                timeoutTimer[0] = new Timer((int) durationMs, new ActionListener() {
                    public void actionPerformed(ActionEvent ev) {
                        stopSharing.run();
                    }
                });
                timeoutTimer[0].setRepeats(false);
                timeoutTimer[0].start();
            });

            stopShare.addActionListener(e -> stopSharing.run());

            tabbedPane.addTab("Location", locationTab);

            // ===== SOS TAB =====
            JPanel sosTab = new JPanel(new GridBagLayout());
            sosTab.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

            JLabel sosLabel = new JLabel("Emergency SOS Button");
            sosLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
            gbc.gridx = 0;
            gbc.gridy = 0;
            gbc.gridwidth = 1;
            sosTab.add(sosLabel, gbc);

            JButton sosButton = new JButton("TRIGGER SOS");
            sosButton.setBackground(new Color(255, 0, 0));
            sosButton.setForeground(Color.WHITE);
            sosButton.setFont(new Font("SansSerif", Font.BOLD, 16));
            sosButton.setPreferredSize(new Dimension(200, 60));
            gbc.gridx = 0;
            gbc.gridy = 1;
            gbc.insets = new Insets(20, 5, 5, 5);
            sosTab.add(sosButton, gbc);

            sosButton.addActionListener(e -> {
                int confirm = JOptionPane.showConfirmDialog(frame, "Trigger SOS? (Demo only)", "SOS", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    JOptionPane.showMessageDialog(frame, "SOS triggered — in a real app this would call contacts or send location.");
                }
            });

            tabbedPane.addTab("SOS", sosTab);

            // ===== RESOURCES TAB =====
            JPanel resourcesTab = new JPanel(new GridBagLayout());
            resourcesTab.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

            JLabel resourcesLabel = new JLabel("Mental Health & Safety Resources");
            resourcesLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
            gbc.gridx = 0;
            gbc.gridy = 0;
            gbc.gridwidth = 1;
            gbc.insets = new Insets(5, 5, 5, 5);
            resourcesTab.add(resourcesLabel, gbc);

            JButton resourcesButton = new JButton("Open WHO Mental Health Resources");
            gbc.gridx = 0;
            gbc.gridy = 1;
            gbc.insets = new Insets(15, 5, 5, 5);
            resourcesTab.add(resourcesButton, gbc);

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

            tabbedPane.addTab("Resources", resourcesTab);

            // ===== Main Layout =====
            frame.setLayout(new BorderLayout());
            frame.add(quoteLabel, BorderLayout.CENTER);
            frame.add(tabbedPane, BorderLayout.SOUTH);

            // Quote rotation timer
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

    // Helper method to make phone calls (demo implementation; integrate with Azure Communication Services for real calls)
    private static void makeCall(String phoneNumber, String message, JFrame frame) {
        try (FileWriter fw = new FileWriter("calls_made.log", true)) {
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("uuuu-MM-dd HH:mm:ss"));
            String logEntry = String.format("%s | To: %s | Message: %s\n", timestamp, phoneNumber, message);
            fw.write(logEntry);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("[CALL] To: " + phoneNumber + " | Message: " + message);
        // TODO: Integrate with Azure Communication Services API for real phone calls
        // For now, calls are logged to calls_made.log file as a demo
    }
}
