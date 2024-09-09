package com.zahid.echo.text_filter;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

public class RendererHighlighted extends DefaultTableCellRenderer {
    private final JTextField filterField;

    public RendererHighlighted(JTextField filterField) {
        this.filterField = filterField;
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value,
                                                   boolean isSelected, boolean hasFocus,
                                                   int row, int column) {
        Component component = super.getTableCellRendererComponent(table, value,
                isSelected, hasFocus, row, column);
        JLabel original = (JLabel) component;
        LabelHighlighted label = new LabelHighlighted();
        label.setFont(original.getFont());
        label.setText(original.getText());
        label.setBackground(original.getBackground());
        label.setForeground(original.getForeground());
        label.setHorizontalTextPosition(original.getHorizontalTextPosition());
        label.highlightText(filterField.getText());
        return label;
    }
}