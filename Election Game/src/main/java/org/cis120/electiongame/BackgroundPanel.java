package org.cis120.electiongame;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import javax.swing.ImageIcon;
import javax.swing.JPanel;

public class BackgroundPanel extends JPanel {
    private Image backgroundImage;
    private BufferedImage croppedImage;
    private boolean useCropping = false;
    private double cropHeightRatio;
    private double startYRatio;
    private double cropWidthRatio;
    private double startXRatio;

    // Constructor for full image display (no cropping)
    public BackgroundPanel(String imagePath) {
        // Load the background image
        backgroundImage = new ImageIcon(getClass().getClassLoader().getResource(imagePath)).getImage();
        useCropping = false;
    }

    // Constructor with both height and width cropping options
    public BackgroundPanel(String imagePath, double cropHeightRatio, double startYRatio, double cropWidthRatio, double startXRatio) {
        // Load the background image
        backgroundImage = new ImageIcon(getClass().getClassLoader().getResource(imagePath)).getImage();
        this.cropHeightRatio = cropHeightRatio;
        this.startYRatio = startYRatio;
        this.cropWidthRatio = cropWidthRatio;
        this.startXRatio = startXRatio;
        useCropping = true;
        cropImage();
    }
    
    // Method to set or reset the background image with cropping
    public void setBackgroundImage(String imagePath, double cropHeightRatio, double startYRatio, double cropWidthRatio, double startXRatio) {
        // Load the background image
        backgroundImage = new ImageIcon(getClass().getClassLoader().getResource(imagePath)).getImage();
        this.cropHeightRatio = cropHeightRatio;
        this.startYRatio = startYRatio;
        this.cropWidthRatio = cropWidthRatio;
        this.startXRatio = startXRatio;
        useCropping = true;
        cropImage();
        repaint(); // Repaint the panel with the new cropped image
    }

    private void cropImage() {
        if (useCropping && cropHeightRatio > 0 && cropWidthRatio > 0) {
            int imageWidth = backgroundImage.getWidth(null);
            int imageHeight = backgroundImage.getHeight(null);

            // Calculate the cropping start positions and dimensions
            int startX = (int) (imageWidth * startXRatio);
            int startY = (int) (imageHeight * startYRatio);
            int cropWidth = (int) (imageWidth * cropWidthRatio);
            int cropHeight = (int) (imageHeight * cropHeightRatio);

            // Crop the image based on the calculated positions and dimensions
            croppedImage = new BufferedImage(cropWidth, cropHeight, BufferedImage.TYPE_INT_ARGB);
            Graphics g = croppedImage.getGraphics();
            g.drawImage(backgroundImage, 0, 0, cropWidth, cropHeight, startX, startY, startX + cropWidth, startY + cropHeight, null);
            g.dispose();
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        // Draw the appropriate image (either cropped or full)
        if (useCropping && croppedImage != null) {
            g.drawImage(croppedImage, 0, 0, getWidth(), getHeight(), this);
        } else {
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }
    }
}
