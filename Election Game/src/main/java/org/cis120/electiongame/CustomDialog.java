package org.cis120.electiongame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

public class CustomDialog extends JDialog {

    private static SoundtrackPlayer globalPlayer; // Static reference to global SoundtrackPlayer
    private static final Color FONT_COLOR = new Color(222, 162, 6); // Custom font color
    private static int globalFontSize = 14; // Default font size
    private int selectedOption = -1; // Store the selected option
    
    // Cache for the button images
    private ImageIcon buttonIcon;
    private ImageIcon buttonHoverIcon;
    private ImageIcon buttonClickedIcon;
    
    private int mouseX;
    private int mouseY;


    // Constructor with optional parameters for custom background, height ratio, and width ratio
    public CustomDialog(Frame parent, String message, String title, String[] options, String type) {
        this(parent, message, title, options, type, null, 1.0, 1.0); // Call the main constructor with defaults
    }

    public CustomDialog(Frame parent, String message, String title, String[] options, String type, String customBackground, double heightRatio, double widthRatio) {
        super(parent, title, true);

        setUndecorated(true); // Remove default decorations

        // Determine the background image
        String backgroundImage = (customBackground != null) ? customBackground : getBackgroundImagePath(type);

        // Create a panel to hold the background image
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Image background = new ImageIcon(getClass().getClassLoader().getResource(backgroundImage)).getImage();
                g.drawImage(background, 0, 0, getWidth(), getHeight(), this);
            }
        };
        panel.setLayout(null); // Use absolute layout to place components freely
        setContentPane(panel);
        
        // Bind the ESC key to close the dialog
        panel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "closeDialog");
        panel.getActionMap().put("closeDialog", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                selectedOption = -1; // Indicate that the dialog was closed without selection
                setVisible(false); // Close the dialog
                globalPlayer.playSoundEffect("files/playbutton.MP3");
            }
        });

        panel.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mousePressed(java.awt.event.MouseEvent evt) {
                // Record the current mouse coordinates on the screen
                mouseX = evt.getX();
                mouseY = evt.getY();
            }
        });

        panel.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            @Override
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                // Calculate the new location of the dialog
                int x = evt.getXOnScreen() - mouseX;
                int y = evt.getYOnScreen() - mouseY;
                // Move the dialog to the new location
                setLocation(x, y);
            }
        });

        // Play sound effect based on the type
        playSoundEffect(type);

        // Adjust the message text for newline
        JLabel messageLabel = new JLabel("<html>" + message.replace("\n", "<br>") + "</html>");
        messageLabel.setFont(new Font("Dialog", Font.BOLD, globalFontSize));
        messageLabel.setForeground(FONT_COLOR);
        messageLabel.setBounds((int) (75 * widthRatio), (int) (100 * heightRatio), (int) (750 * widthRatio), (int) (300 * heightRatio)); // Adjusted position and size
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
        JPanel buttonPanel = new JPanel(new GridLayout(rows, cols, 30, 30)); // Dynamic rows and cols, 30px gaps
        buttonPanel.setOpaque(false); // Make the button area transparent

        int buttonWidth = 225; // Fixed button width
        int buttonHeight = 75; // Fixed button height
        buttonIcon = resizeImageIcon("files/blankbutton.PNG", buttonWidth, buttonHeight);
        buttonHoverIcon = resizeImageIcon("files/blankbuttonhover.PNG", buttonWidth, buttonHeight);
        buttonClickedIcon = resizeImageIcon("files/blankbuttonclicked.PNG", buttonWidth, buttonHeight);

        for (int i = 0; i < options.length; i++) {
            String option = options[i];
            JButton button = new JButton(option);

            button.setIcon(buttonIcon);
            button.setFont(new Font("Dialog", Font.BOLD, globalFontSize));
            button.setForeground(FONT_COLOR);
            button.setHorizontalTextPosition(SwingConstants.CENTER); // Center the text on the image
            button.setFocusPainted(false); // Remove focus border
            button.setContentAreaFilled(false); // Ensure only the image is shown
            button.setBorderPainted(false); // Remove button border
            button.setOpaque(false); // Make the button fully transparent except for the image

            // Add hover and click effects
            button.addMouseListener(new java.awt.event.MouseAdapter() {
                @Override
                public void mouseEntered(java.awt.event.MouseEvent evt) {
                    button.setIcon(buttonHoverIcon);
                }

                @Override
                public void mouseExited(java.awt.event.MouseEvent evt) {
                    button.setIcon(buttonIcon);
                }

                @Override
                public void mousePressed(java.awt.event.MouseEvent evt) {
                    button.setIcon(buttonClickedIcon);
                    button.setFont(new Font("Dialog", Font.BOLD, (int)((double)(globalFontSize) * 0.8)));
                }

                @Override
                public void mouseReleased(java.awt.event.MouseEvent evt) {
                    button.setIcon(buttonHoverIcon);
                    button.setFont(new Font("Dialog", Font.BOLD, globalFontSize));
                }
            });

            final int optionIndex = i;
            button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    globalPlayer.playSoundEffect("files/playbutton.MP3");
                    selectedOption = optionIndex;
                    setVisible(false);
                }
            });

            // If there's only one button, bind the Enter key to this button's action
            if (options.length == 1) {
                panel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "pressButton");
                panel.getActionMap().put("pressButton", new AbstractAction() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        button.doClick();
                    }
                });
            }

            buttonPanel.add(button);
        }
        
        


        // Calculate the Y position for the buttons to be at the bottom
        int dialogHeight = (int) (600 * heightRatio); // Scaled dialog height
        int buttonPanelHeight = buttonHeight * rows + (rows - 1) * 30; // Account for rows and gaps (buttons fixed size)

        // Calculate the total width of the button panel (buttons + gaps)
        int totalButtonPanelWidth = buttonWidth * cols + (cols - 1) * 30;

        // Center the button panel horizontally
        int buttonX = (int) ((900 * widthRatio - totalButtonPanelWidth) / 2);

        // Calculate Y position for buttons
        int buttonY = dialogHeight - buttonPanelHeight - (int) (60 * heightRatio); 

        buttonPanel.setBounds(buttonX, buttonY, totalButtonPanelWidth, buttonPanelHeight); // Adjust for GridLayout
        panel.add(buttonPanel);

        // Create the close ("X") button in the top right corner
        JButton closeButton = new JButton("X");
        closeButton.setBounds((int) (825 * widthRatio), 0, (int) (75 * widthRatio), (int) (75 * heightRatio)); // Scaled position and size
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
        setSize((int) (900 * widthRatio), (int) (600 * heightRatio)); // Scaled size
        
        // Check the type and adjust the position
        if ("round".equalsIgnoreCase(type)) {
            // Shift the dialog 200 pixels below the center
            setLocationRelativeTo(parent);
            Point location = getLocation();
            setLocation(location.x, location.y + 275);
        } else {
            // Center on parent for other types
            setLocationRelativeTo(parent);
        }
        
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
                //globalPlayer.playSoundEffect("files/roundwin.MP3"); // Round win sound
            	//Fix this mess somehow
            } else {
                globalPlayer.playSoundEffect("files/menu.MP3"); // Default menu sound
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
                return "files/electionmenuimage.PNG";
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

    // New static method to create and show the dialog with optional parameters
    public static int showCustomDialog(Frame parent, String message, String title, String[] options, String type, String customBackground, double heightRatio, double widthRatio) {
        CustomDialog dialog = new CustomDialog(parent, message, title, options, type, customBackground, heightRatio, widthRatio);
        return dialog.getSelectedOption();
    }
    
    private ImageIcon resizeImageIcon(String imagePath, int width, int height) {
        ImageIcon icon = new ImageIcon(getClass().getClassLoader().getResource(imagePath));
        Image image = icon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
        return new ImageIcon(image);
    }
}
