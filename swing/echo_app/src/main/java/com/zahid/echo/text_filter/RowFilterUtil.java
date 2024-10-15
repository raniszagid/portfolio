package com.zahid.echo.text_filter;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

public class RowFilterUtil {
    public static JTextField createRowFilter(JTable table) {
        RowSorter<? extends TableModel> sorter = table.getRowSorter();
        if (sorter == null) {
            table.setAutoCreateRowSorter(true);
            sorter = table.getRowSorter();
        }

        TableRowSorter<? extends TableModel> rowSorter =
                (sorter instanceof TableRowSorter) ? (TableRowSorter<? extends TableModel>) sorter : null;

        if (rowSorter == null) {
            throw new RuntimeException("Cannot find appropriate row sorter: " + sorter);
        }

        final JTextField filterField = new JTextField(15);
        filterField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent event) {
                update();
            }
            @Override
            public void removeUpdate(DocumentEvent event) {
                update();
            }
            @Override
            public void changedUpdate(DocumentEvent event) {
                update();
            }
            private void update() {
                String text = filterField.getText();
                if (text.trim().isEmpty()) {
                    rowSorter.setRowFilter(null);
                } else {
                    rowSorter.setRowFilter(RowFilter.regexFilter("(?i)" + text));
                }
            }
        });
        return filterField;
    }
}