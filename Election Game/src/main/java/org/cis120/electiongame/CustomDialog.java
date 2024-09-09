package org.cis120.electiongame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CustomDialog extends JDialog {

    private static SoundtrackPlayer globalPlayer; // Static reference to global SoundtrackPlayer
    private static final Color FONT_COLOR = new Color(0, 0, 0); // Custom font color
    private static int globalFontSize = 14; // Default font size
    private int selectedOption = -1; // Store the selected option

    // Cache for the button and background images
    private ImageIcon buttonIcon;
    private ImageIcon buttonHoverIcon;
    private ImageIcon buttonClickedIcon;
    
    private static Map<String, ImageIcon> imageCache = new HashMap<>();

    private int mouseX;
    private int mouseY;
    private String inputText = "";
    private JTextField inputField; // Add this line at the beginning of your class
    private List<JCheckBox> checkBoxes = new ArrayList<>(); // List to store checkboxes
    private List<String> selectedTags = new ArrayList<>(); // List to store selected tags
    
    public CustomDialog(Container parent, String message, String title, String[] options, String type) {
        this(parent, message, title, options, type, 1.0, 1.0); // Call the main constructor with defaults
    }

    public CustomDialog(Container parent, String message, String title, String[] options, String type,
                        double heightRatio, double widthRatio) {
        super(JOptionPane.getFrameForComponent(parent), title, true);

        setUndecorated(true); // Remove default decorations

        // Check if the system is macOS
        if (System.getProperty("os.name").toLowerCase().contains("mac")) {
            setModalityType(ModalityType.APPLICATION_MODAL);  // Set modality for macOS
        }
        
        // Determine the background image path
        String backgroundImage = getBackgroundImagePath(type, widthRatio, heightRatio);

        // Fetch the cached image
        ImageIcon backgroundIcon = getCachedImage(backgroundImage);

        // Create a panel to hold the background image
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Image background = backgroundIcon.getImage();
                g.drawImage(background, 0, 0, getWidth(), getHeight(), this);
            }
        };
        panel.setLayout(null); // Use absolute layout to place components freely
        setContentPane(panel);

        // Add the title image
        if (title != null && !title.isEmpty()) {
            ImageIcon titleIcon = new ImageIcon(new ImageIcon(getClass().getClassLoader().getResource(title))
                    .getImage().getScaledInstance(400, 100, Image.SCALE_SMOOTH));
            JLabel titleLabel = new JLabel(titleIcon);
            titleLabel.setBounds((int) ((900 * widthRatio - 400) / 2), 0, 400, 100); // Center the title horizontally and place it at the top
            panel.add(titleLabel);
        }

        // Bind the ESC key to close the dialog
        panel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
                "closeDialog");
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
        messageLabel.setBounds((int) (75 * widthRatio), (int) (120 * heightRatio), (int) (750 * widthRatio),
                (int) (300 * heightRatio)); // Adjusted position and size
        panel.add(messageLabel);

        // Create the input field if necessary
        if (type.equals("input")) {
            inputField = new JTextField();
            inputField.setFont(new Font("Dialog", Font.PLAIN, globalFontSize));
            inputField.setBounds((int) (75 * widthRatio), (int) (300 * heightRatio), (int) (750 * widthRatio),
                    (int) (50 * heightRatio)); // Adjusted position
            panel.add(inputField);
            SwingUtilities.invokeLater(() -> inputField.requestFocusInWindow());
        }

        // Create checkboxes if the type is checkbox
        if (type.equals("checkbox")) {
            JPanel checkboxPanel = new JPanel();
            checkboxPanel.setLayout(new GridLayout(0, 4, 5, 5)); // 3 columns, dynamic rows, with 5px gaps
            checkboxPanel.setOpaque(false); // Make the checkbox panel transparent

            for (String tag : options) {
                JCheckBox checkBox = new JCheckBox(tag);
                checkBox.setOpaque(false); // Make each checkbox transparent
                checkBoxes.add(checkBox);
                checkboxPanel.add(checkBox);
            }

            JCheckBox selectAllCheckBox = new JCheckBox("Select All");
            selectAllCheckBox.setOpaque(false); // Make the "Select All" checkbox transparent
            checkboxPanel.add(selectAllCheckBox);

            selectAllCheckBox.addActionListener(e -> {
                boolean selectAll = selectAllCheckBox.isSelected();
                checkBoxes.forEach(checkBox -> checkBox.setSelected(selectAll));
            });

            checkboxPanel.setBounds((int) (75 * widthRatio), (int) (265 * heightRatio), (int) (750 * widthRatio), (int) (200 * heightRatio));
            panel.add(checkboxPanel);

            // Only add the "Ok" button
            options = new String[]{"Ok"};
        }

        if (options == null) {
            // Only add the "Ok" button
            options = new String[]{"Ok"};
        }

        // Existing button handling code...
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

        JPanel buttonPanel = new JPanel(new GridLayout(rows, cols, 30, 30)); // Dynamic rows and cols, 30px gaps
        buttonPanel.setOpaque(false); // Make the button area transparent

        int buttonWidth = 225; // Fixed button width
        int buttonHeight = 75; // Fixed button height
        buttonIcon = getCachedImage("files/blankbutton.PNG");
        buttonHoverIcon = getCachedImage("files/blankbuttonhover.PNG");
        buttonClickedIcon = getCachedImage("files/blankbuttonclicked.PNG");

        for (int i = 0; i < options.length; i++) {
            String option = options[i];
            JButton button = new JButton(option);

            button.setIcon(buttonIcon);
            button.setFont(new Font("Dialog", Font.BOLD, globalFontSize));
            button.setForeground(new Color(222, 162, 6));
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
                    button.setFont(new Font("Dialog", Font.BOLD, (int) ((double) (globalFontSize) * 0.8)));
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
                    if (type.equals("input") && inputField != null) {
                        inputText = inputField.getText(); // Capture the input text
                    } else if (type.equals("checkbox")) {
                        selectedTags.clear();
                        for (JCheckBox checkBox : checkBoxes) {
                            if (checkBox.isSelected()) {
                                selectedTags.add(checkBox.getText());
                            }
                        }
                    }
                    globalPlayer.playSoundEffect("files/playbutton.MP3");
                    selectedOption = optionIndex;
                    setVisible(false);
                }
            });

            if (options.length == 1) {
                panel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0),
                        "pressButton");
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
        int buttonPanelHeight = buttonHeight * rows + (rows - 1) * 30; // Account for rows and gaps

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

        // Set the location relative to the parent container (centered)
        setLocationRelativeTo(parent);

        // If the type is "round", apply the vertical offset
        if ("round".equalsIgnoreCase(type)) {
            Point location = getLocation();
            location.y += 275;
            setLocation(location);
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
                //globalPlayer.playSoundEffect("files/electionwin.MP3"); // Round win sound
            } else {
                globalPlayer.playSoundEffect("files/menu.MP3"); // Default menu sound
            }
        }
    }

 // Preload background and button images with proper scaling
    public static void preloadImages() {
        String[] backgroundImagePaths = {
            "files/electionmenubackground.PNG",
            "files/menubackgroundwide.PNG",
            "files/menubackground.PNG"
        };

        // Preload background images
        for (String path : backgroundImagePaths) {
            if (!imageCache.containsKey(path)) {
                ImageIcon imageIcon = new ImageIcon(
                    CustomDialog.class.getClassLoader().getResource(path)
                );
                imageCache.put(path, imageIcon);
            }
        }

        // Preload and scale button images
        int buttonWidth = 225; // Fixed button width
        int buttonHeight = 75; // Fixed button height
        String[] buttonImagePaths = {
            "files/blankbutton.PNG",
            "files/blankbuttonhover.PNG",
            "files/blankbuttonclicked.PNG"
        };

        for (String path : buttonImagePaths) {
            if (!imageCache.containsKey(path)) {
                ImageIcon originalIcon = new ImageIcon(
                    CustomDialog.class.getClassLoader().getResource(path)
                );
                Image scaledImage = originalIcon.getImage().getScaledInstance(buttonWidth, buttonHeight, Image.SCALE_SMOOTH);
                ImageIcon scaledIcon = new ImageIcon(scaledImage);
                imageCache.put(path, scaledIcon);
            }
        }
    }

    private static ImageIcon getCachedImage(String path) {
        return imageCache.getOrDefault(path, null);
    }

    // Existing methods...

    private String getBackgroundImagePath(String type, double widthRatio, double heightRatio) {
        String path;
        if ("round".equalsIgnoreCase(type)) {
            path = "files/electionmenubackground.PNG";
        } else if (widthRatio >= 2 * heightRatio) {
            path = "files/menubackgroundwide.PNG";
        } else {
            path = "files/menubackground.PNG";
        }
        return path;
    }

    // Method to return the selected option
    public int getSelectedOption() {
        return selectedOption;
    }

    // Method to return the input text
    public String getInputText() {
        return inputText;
    }

    // Method to return the selected tags
    public List<String> getSelectedTags() {
        return selectedTags;
    }

    // Static method to easily create and show the dialog, and return the selected option
    public static int showCustomDialog(Container parent, String message, String title, String[] options, String type) {
        CustomDialog dialog = new CustomDialog(parent, message, title, options, type, 1.0, 1.0);
        return dialog.getSelectedOption();
    }

    // New static method to create and show the dialog with optional parameters
    public static int showCustomDialog(Container parent, String message, String title, String[] options, String type,
                                       double heightRatio, double widthRatio) {
        CustomDialog dialog = new CustomDialog(parent, message, title, options, type, heightRatio, widthRatio);
        return dialog.getSelectedOption();
    }

    // Static method to create and show the input dialog, and return the input string
    public static String showInputDialog(Container parent, String message, String title) {
        CustomDialog dialog = new CustomDialog(parent, message, title, null, "input", 1.0, 1.0);
        return dialog.getInputText();
    }

    // Static method to create and show the checkbox dialog, and return selected tags
    public static List<String> showCheckboxDialog(Container parent, String message, String[] options, String title) {
        CustomDialog dialog = new CustomDialog(parent, message, title, options, "checkbox", 1.0, 1.0);
        return dialog.getSelectedTags();
    }

    private ImageIcon resizeImageIcon(String imagePath, int width, int height) {
        ImageIcon icon = new ImageIcon(getClass().getClassLoader().getResource(imagePath));
        Image image = icon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
        return new ImageIcon(image);
    }
}
