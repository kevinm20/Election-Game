package org.cis120.electiongame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CustomDialog extends JDialog {

    private static SoundtrackPlayer globalPlayer; // Static reference to global SoundtrackPlayer
    private static final Color FONT_COLOR = new Color(222, 162, 6); // Custom font color
    private static int globalFontSize = 14; // Default font size
    private int selectedOption = -1; // Store the selected option

    public CustomDialog(Frame parent, String message, String title, String[] options, String type) {
        super(parent, title, true);

        setUndecorated(true); // Remove default decorations

        // Create a panel to hold the background image
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Image background = new ImageIcon(getClass().getClassLoader().getResource(getBackgroundImagePath(type))).getImage();
                g.drawImage(background, 0, 0, getWidth(), getHeight(), this);
            }
        };
        panel.setLayout(null); // Use absolute layout to place components freely
        setContentPane(panel);

        // Play sound effect based on the type
        playSoundEffect(type);

        // Adjust the message text for newline
        JLabel messageLabel = new JLabel("<html>" + message.replace("\n", "<br>") + "</html>");
        messageLabel.setFont(new Font("Dialog", Font.BOLD, globalFontSize));
        messageLabel.setForeground(FONT_COLOR);
        messageLabel.setBounds(75, 180, 750, 150); // Adjusted position and size (50% larger)
        panel.add(messageLabel);

        // Determine the number of rows and columns based on the number of buttons
        int buttonCount = options.length;
        int rows, cols;

        if (buttonCount == 1) {
            rows = 1;
            cols = 1;
        } else if (buttonCount == 2) {
            rows = 1;
            cols = 2;
        } else if (buttonCount == 3) {
            rows = 1;
            cols = 3;
        } else {
            rows = 2;
            cols = (buttonCount + 1) / 2; // Distribute buttons evenly across two rows
        }

        // Add option buttons with a dynamic GridLayout
        JPanel buttonPanel = new JPanel(new GridLayout(rows, cols, 30, 30)); // Dynamic rows and cols, 30px gaps (50% larger)
        buttonPanel.setOpaque(false); // Make the button area transparent

        int buttonWidth = 225; // Increase button width by 50%
        int buttonHeight = 75; // Increase button height by 50%

        for (int i = 0; i < options.length; i++) {
            String option = options[i];
            JButton button = new JButton(option);
            button.setPreferredSize(new Dimension(buttonWidth, buttonHeight));
            button.setForeground(Color.WHITE);
            button.setBackground(new Color(100, 100, 100)); // Customize as needed
            button.setFont(new Font("Dialog", Font.BOLD, (int)((double)(globalFontSize)*.75)));
            final int optionIndex = i;
            button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    globalPlayer.playSoundEffect("files/playbutton.MP3");
                    selectedOption = optionIndex;
                    setVisible(false);
                }
            });
            buttonPanel.add(button);
        }

        // Calculate the Y position for the buttons to be at the bottom
        int dialogHeight = 600; // Increase dialog height by 50%
        int buttonPanelHeight = buttonHeight * rows + (rows - 1) * 30; // Account for rows and gaps
        int buttonY = dialogHeight - buttonPanelHeight - 60; // Calculate Y position for buttons
        int buttonX = (900 - (buttonWidth * cols + (cols - 1) * 30)) / 2; // Adjust for dialog width (50% larger)
        buttonPanel.setBounds(buttonX, buttonY, cols * (buttonWidth + 30), buttonPanelHeight); // Adjust for GridLayout
        panel.add(buttonPanel);

        // Create the close ("X") button in the top right corner
        JButton closeButton = new JButton("X");
        closeButton.setBounds(825, 0, 75, 75); // Increase position and size by 50%
        closeButton.setForeground(Color.WHITE);
        closeButton.setBackground(new Color(212, 38, 38)); // Initial background color
        closeButton.setFocusPainted(false); // Remove the focus outline
        closeButton.setBorderPainted(false); // Remove the border for a cleaner look
        closeButton.setContentAreaFilled(false); // Makes sure the button takes the full area
        closeButton.setOpaque(true); // Ensures the background color is visible

        // Add mouse listener for hover effect
        closeButton.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                closeButton.setBackground(new Color(150, 0, 0)); // Darker shade of red on hover
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                closeButton.setBackground(new Color(212, 38, 38)); // Original color when not hovered
            }
        });

        closeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                selectedOption = -1; // Indicate that the dialog was closed without selection
                setVisible(false); // Close the dialog
                globalPlayer.playSoundEffect("files/playbutton.MP3");
            }
        });
        panel.add(closeButton);

        pack();
        setSize(900, 600); // Increase size by 50%

        setLocationRelativeTo(parent); // Center on parent
        setVisible(true); // Automatically show the dialog
    }


    // Static method to set the global SoundtrackPlayer
    public static void setGlobalPlayer(SoundtrackPlayer player) {
        globalPlayer = player;
    }

    // Static method to set the global font size
    public static void setGlobalFontSize(int fontSize) {
        globalFontSize = fontSize;
    }

    // Static method to play the appropriate sound based on the type
    private static void playSoundEffect(String type) {
        if (globalPlayer != null) {
            if ("error".equalsIgnoreCase(type) || "resign".equalsIgnoreCase(type)) {
                globalPlayer.playSoundEffect("files/error.MP3"); // Error sound
            } else if ("round".equalsIgnoreCase(type)) {
                globalPlayer.playSoundEffect("files/roundwin.MP3"); // Round win sound
            } else {
            	//System.out.println("Trying to play...");
                //globalPlayer.playSoundEffect("files/menu.MP3"); // Default menu sound
            }
        }
    }

    // Get background image path based on dialog type
    private String getBackgroundImagePath(String type) {
        switch (type.toLowerCase()) {
            case "settings":
                return "files/settingsmenuimage.PNG";
            case "help":
                return "files/helpmenuimage.PNG";
            case "resign":
                return "files/resignmenuimage.PNG";
            case "round":
                return "files/roundmenuimage.PNG";
            case "setup":
                return "files/setupmenuimage.PNG";
            case "error":
                return "files/errormenuimage.PNG";
            default:
                return "files/menuimage.PNG";
        }
    }

    // Method to return the selected option
    public int getSelectedOption() {
        return selectedOption;
    }

    // Static method to easily create and show the dialog, and return the selected option
    public static int showCustomDialog(Frame parent, String message, String title, String[] options, String type) {
        CustomDialog dialog = new CustomDialog(parent, message, title, options, type);
        return dialog.getSelectedOption();
    }
}
