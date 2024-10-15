package com.zahid.echo.text_filter;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;

public class LabelHighlighted extends JLabel {
    private final List<Rectangle2D> rectangles = new ArrayList<>();
    private final Color colorHighlight = Color.YELLOW;

    public void reset() {
        rectangles.clear();
        repaint();
    }

    public void highlightText(String textToHighlight) {
        if (textToHighlight == null) {
            return;
        }
        reset();

        final String textToMatch = textToHighlight.toLowerCase().trim();
        if (textToMatch.isEmpty()) {
            return;
        }
        final String labelText = getText().toLowerCase();
        if (labelText.contains(textToMatch)) {
            FontMetrics fontMetrics = getFontMetrics(getFont());
            float width = -1;
            final float height = fontMetrics.getHeight() - 1;
            int i = 0;
            while (true) {
                i = labelText.indexOf(textToMatch, i);
                if (i == -1) {
                    break;
                }
                if (width == -1) {
                    String matchingText = getText().substring(i,
                            i + textToHighlight.length());
                    width = fontMetrics.stringWidth(matchingText);
                }
                String preText = getText().substring(0, i);
                float x = fontMetrics.stringWidth(preText);
                rectangles.add(new Rectangle2D.Float(x, 1, width, height));
                i = i + textToMatch.length();
            }
            repaint();
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        g.setColor(getBackground());
        g.fillRect(0, 0, getWidth(),getHeight());
        if (!rectangles.isEmpty()) {
            Graphics2D g2d = (Graphics2D) g;
            Color c = g2d.getColor();
            for (Rectangle2D rectangle : rectangles) {
                g2d.setColor(colorHighlight);
                g2d.fill(rectangle);
                g2d.setColor(Color.LIGHT_GRAY);
                g2d.draw(rectangle);
            }
            g2d.setColor(c);
        }
        super.paintComponent(g);

    }
}