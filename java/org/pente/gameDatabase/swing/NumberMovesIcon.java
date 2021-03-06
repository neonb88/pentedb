package org.pente.gameDatabase.swing;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.*;

/**
 * @author dweebo
 */
public class NumberMovesIcon implements Icon {

	public static Color white[] = { new Color(220, 220, 220), new Color(240, 240, 240) };

    private int diameter;


	public NumberMovesIcon(int r) {
		diameter = r;
	}

	public int getIconHeight() {
		return diameter;
	}

	public int getIconWidth() {
		return diameter;
	}

	public void paintIcon(Component comp, Graphics g, int x, int y) {

        Graphics2D g2 = (Graphics2D)g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
              RenderingHints.VALUE_ANTIALIAS_ON);

        //int r = width - 1;
        //int offset = width < 30 ? 1 : 2;

        Color c[] = white;


        //int darkWidth = r - 1;
        //int lightWidth = (int) ((double) darkWidth) * 5 / 6;
        //int reflectionWidth = (darkWidth < 18) ? 4 : 6;
        //int reflectionOffset = darkWidth / 4;

        // don't show selected color behind icon if selected
        //g2.setColor(UIManager.getColor("Tree.textBackground"));
        //g2.fillRect(x, y, diameter, diameter);

        fillOval(g2, x, y, diameter - 1, c[0]);  // dark
        fillOval(g2, x + 1, y + 1, diameter - 3, c[1]); // light
        //fillOval(g2, x + reflectionOffset, y + reflectionOffset, reflectionWidth, Color.white); // reflection

        g2.setColor(Color.black);


        int fontSize = 8;
        if (diameter > 14) {
            fontSize += 2;
        }
        if (diameter > 24) {
            fontSize += 2;
        }

        //anyway to save this font?
        Font f = new Font("Helvetica", Font.PLAIN, fontSize);
        g2.setFont(f);
        FontMetrics fm = g.getFontMetrics();

        g2.drawString("1",
        	x + (diameter / 2) - fm.stringWidth("1") / 2,
        	y + diameter - 5);
	}

    private void fillOval(Graphics g, int x, int y, int r, Color c) {
        g.setColor(c);
        g.fillOval(x, y, r, r);
    }
}
