package org.cis120.electiongame;

import java.awt.Color;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Path2D;


// Huge WIP
public class PresidentCardDesign extends JPanel {
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
// top left trapezoid
		Path2D topLeftTrap = new Path2D.Double();
		topLeftTrap.moveTo(0.33 * 100, 0);
		topLeftTrap.lineTo(0.33 * 100, 1.9 * 100);
		topLeftTrap.lineTo(2.69 * 100, 1.57 * 100);
		topLeftTrap.lineTo(2.69 * 100, 0);
		topLeftTrap.closePath();
		g2.setColor(Color.RED);
		g2.fill(topLeftTrap);
// top right trapezoid
		Path2D topRightTrap = new Path2D.Double();
		topRightTrap.moveTo(3.24 * 100, 0);
		topRightTrap.lineTo(3.24 * 100, 0.75 * 100);
		topRightTrap.lineTo(2.94 * 100, 1.57 * 100);
		topRightTrap.lineTo(2.94 * 100, 0);
		topRightTrap.closePath();
		g2.setColor(Color.RED);
		g2.draw(topRightTrap);

// red rectangle on left
		g2.setColor(Color.RED);
		g2.fillRoundRect((int) (0.33 * 100), (int) (1.9 * 100), (int) (1.71 * 100), (int) (0.65 * 100), 10, 10);

// blurred rectangle on right
		g2.setColor(Color.BLACK);
		g2.fillRoundRect((int) (2.12 * 100), (int) (1.9 * 100), (int) (1.87 * 100), (int) (1.41 * 100), 10, 10);

// center text
		JLabel centerText = new JLabel(
				"“Silent Cal” was known for his laissez-faire economic policy during the boom of the Roaring 20s, as well as his support of racial equality.",
				SwingConstants.CENTER);
		centerText.setBounds((int) (0.25 * 100), (int) (3.34 * 100), (int) (3.07 * 100), (int) (0.51 * 100));
		centerText.setOpaque(true);
		centerText.setBackground(Color.RED);
		centerText.setForeground(Color.WHITE);
		add(centerText);

// bottom left trapezoid
		Path2D bottomLeftTrap = new Path2D.Double();
		bottomLeftTrap.moveTo(0.33 * 100, 4.24 * 100);
		bottomLeftTrap.lineTo(0.33 * 100, 5.00 * 100);
		bottomLeftTrap.lineTo(2.69 * 100, 4.89 * 100);
		bottomLeftTrap.lineTo(2.69 * 100, 4.24 * 100);
		bottomLeftTrap.closePath();
		g2.setColor(Color.WHITE);
		g2.fill(bottomLeftTrap);

// bottom right trapezoid
		Path2D bottomRightTrap = new Path2D.Double();
		bottomRightTrap.moveTo(3.24 * 100, 4.24 * 100);
		bottomRightTrap.lineTo(3.24 * 100, 5.00 * 100);
		bottomRightTrap.lineTo(2.94 * 100, 4.89 * 100);
		bottomRightTrap.lineTo(2.94 * 100, 4.24 * 100);
		bottomRightTrap.closePath();
		g2.setColor(Color.RED);
		g2.fill(bottomRightTrap);
// bold red text at bottom
		JLabel bottomText = new JLabel("TAX CUTS", SwingConstants.CENTER);
		bottomText.setBounds((int) (0.33 * 100), (int) (4.39 * 100), (int) (3.57 * 100), (int) (0.11 * 100));
		bottomText.setOpaque(false);
		bottomText.setForeground(Color.RED);
		bottomText.setFont(new Font("Arial", Font.BOLD, 14));
		add(bottomText);

// white text in top left trapezoid
		JLabel topLeftText = new JLabel("Calvin Coolidge", SwingConstants.LEFT);
		topLeftText.setBounds((int) (0.33 * 100), 0, (int) (1.9 * 100), (int) (0.33 * 100));
		topLeftText.setOpaque(false);
		topLeftText.setForeground(Color.WHITE);
		topLeftText.setFont(new Font("Arial", Font.BOLD, 14));
		add(topLeftText);

// red text in top right trapezoid
		JLabel topRightText = new JLabel("MA", SwingConstants.RIGHT);
		topRightText.setBounds((int) (2.69 * 100), 0, (int) (0.75 * 100), (int) (0.33 * 100));
		topRightText.setOpaque(false);
		topRightText.setForeground(Color.RED);
		topRightText.setFont(new Font("Arial", Font.BOLD, 14));
		add(topRightText);

// red text below rectangles
		JLabel belowRectanglesText = new JLabel("REPUBLICAN · NORTHEAST · LIBERTARIAN", SwingConstants.CENTER);
		belowRectanglesText.setBounds((int) (0.33 * 100), (int) (3.34 * 100), (int) (3.57 * 100), (int) (0.11 * 100));
		belowRectanglesText.setOpaque(false);
		belowRectanglesText.setForeground(Color.RED);
		belowRectanglesText.setFont(new Font("Arial", Font.BOLD, 14));
		add(belowRectanglesText);
	}
}