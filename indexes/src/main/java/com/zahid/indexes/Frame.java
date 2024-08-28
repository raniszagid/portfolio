package com.zahid.indexes;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Frame extends JFrame {
    private final JTextArea inputField;
    private final JTextArea outputNumberSequence;
    private final JTextArea outputUniqueGroups;
    public Frame() {
        super("Индексация");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 600);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();

        JLabel inputLabel = new JLabel("Введите текст (по примеру по умолчанию):");
        panel.add(inputLabel);

        String example = "1,3-5\n2\n3-4";
        inputField = new JTextArea(example,7,40);
        JScrollPane scrollPaneInput = new JScrollPane(inputField);
        panel.add(scrollPaneInput);

        JLabel exceptionLabel = new JLabel("Неверный формат входных данных");
        exceptionLabel.setVisible(false);

        JButton convertButton = new JButton("Преобразовать");
        convertButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                exceptionLabel.setVisible(false);
                try {
                    performOutput(inputField);
                } catch (Exception exception) {
                    exceptionLabel.setForeground(Color.RED);
                    exceptionLabel.setVisible(true);
                }
            }
        });
        panel.add(convertButton);

        JLabel firstOutputLabel = new JLabel("Преобразование массива строк в массив\n" +
                "последовательностей чисел");
        panel.add(firstOutputLabel);

        outputNumberSequence = new JTextArea(7, 40);
        outputNumberSequence.setLineWrap(true);
        outputNumberSequence.setEditable(false);
        JScrollPane scrollPaneSequence = new JScrollPane(outputNumberSequence);
        panel.add(scrollPaneSequence);

        JLabel secondOutputLabel = new JLabel("Уникальные упорядоченные группы элементов полученных массивов чисел");
        panel.add(secondOutputLabel);
        outputUniqueGroups = new JTextArea(7, 40);
        outputUniqueGroups.setLineWrap(true);
        outputUniqueGroups.setEditable(false);
        JScrollPane scrollPaneGroups = new JScrollPane(outputUniqueGroups);
        panel.add(scrollPaneGroups);

        panel.add(exceptionLabel);
        add(panel);
        setVisible(true);
    }

    private void performOutput(JTextArea input) throws Exception {
        String[] array = getArray(input);
        Port port = new Port(array);
        outputNumberSequence.setText(port.getStringOfConvertedArray());
        outputUniqueGroups.setText(port.getUniqueGroups().toString());
    }

    private String[] getArray(JTextArea input) {
        return input.getText().split("\n");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Frame());
    }
}
