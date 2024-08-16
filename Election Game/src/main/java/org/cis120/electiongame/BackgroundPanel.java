// Updated BackgroundPanel with width cropping and x-start ratio
package org.cis120.electiongame;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import javax.swing.ImageIcon;
import javax.swing.JPanel;

public class BackgroundPanel extends JPanel {
    private Image backgroundImage;
    private BufferedImage croppedImage;
    private double cropHeightRatio;
    private double startYRatio;
    private double cropWidthRatio;
    private double startXRatio;

    // Constructor with height cropping option
    public BackgroundPanel(String imagePath) {
        this(imagePath, 1, 1, 0, 0); // Full width and no horizontal cropping by default
    }

    // Constructor with both height and width cropping options
    public BackgroundPanel(String imagePath, double cropHeightRatio, double startYRatio, double cropWidthRatio, double startXRatio) {
        // Load the background image
        backgroundImage = new ImageIcon(getClass().getClassLoader().getResource(imagePath)).getImage();
        this.cropHeightRatio = cropHeightRatio;
        this.startYRatio = startYRatio;
        this.cropWidthRatio = cropWidthRatio;
        this.startXRatio = startXRatio;
        cropImage();
    }

    private void cropImage() {
        if (cropHeightRatio > 0 && cropWidthRatio > 0) {
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
        // Draw the cropped image
        if (croppedImage != null) {
            g.drawImage(croppedImage, 0, 0, getWidth(), getHeight(), this);
        }
    }
}
