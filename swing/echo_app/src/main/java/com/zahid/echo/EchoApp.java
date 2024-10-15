package com.zahid.echo;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.IOException;
import java.util.*;
import java.util.List;
import org.json.JSONObject;

import com.zahid.echo.data_loader.DataLoader;
import com.zahid.echo.text_filter.RendererHighlighted;
import com.zahid.echo.text_filter.RowFilterUtil;

public class EchoApp extends JFrame {
    private JTabbedPane tabbedPane;
    private JTextArea logArea;
    private JTable dataTable;
    private JTextField filterField;
    private DefaultTableModel tableModel;

    private List<String> links = Arrays.asList(
            "https://echo.free.beeceptor.com",
            "https://httpbin.org/anything",
            "https://postman-echo.com/get",
            "http://echo.jsontest.com/key/value/one/two");
    public EchoApp() {
        super("Echo App");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 400);
        setLocationRelativeTo(null);

        tabbedPane = new JTabbedPane();
        getContentPane().add(tabbedPane);

        logArea = new JTextArea();
        logArea.setEditable(false);
        JScrollPane logScrollPane = new JScrollPane(logArea);
        tabbedPane.addTab("Log", logScrollPane);

        JPanel dataPanel = new JPanel(new BorderLayout());
        tableModel = new DefaultTableModel();
        dataTable = new JTable(tableModel);
        JScrollPane dataScrollPane = new JScrollPane(dataTable);
        dataPanel.add(dataScrollPane, BorderLayout.CENTER);

        filterField = RowFilterUtil.createRowFilter(dataTable);
        RendererHighlighted renderer = new RendererHighlighted(filterField);
        dataTable.setDefaultRenderer(Object.class, renderer);
        dataPanel.add(filterField, BorderLayout.NORTH);
        tabbedPane.addTab("Data", dataPanel);
        tableModel.addColumn("Key");
        tableModel.addColumn("Value");
        for (String echoURL : links) {
            DataLoader dataLoader = new DataLoader(echoURL);
            loadData(dataLoader);
        }
    }

    private void loadData(DataLoader dataLoader) {
        try {
            logArea.append(dataLoader.getRequestText());
            logArea.append(dataLoader.getResponseText());
            JSONObject jsonData = dataLoader.getJSONObject();
            Iterator<String> keys = jsonData.keys();
            while (keys.hasNext()) {
                String key = keys.next();
                List<String> rowData = new ArrayList<>();
                rowData.add(key);
                rowData.add(jsonData.get(key).toString());
                tableModel.addRow(rowData.toArray());
            }
            tableModel.addRow(new Object[] { "", "" });
        } catch (IOException | InterruptedException e) {
            logArea.append("Error: " + e.getMessage() + "\n");
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new EchoApp().setVisible(true));
    }
}
