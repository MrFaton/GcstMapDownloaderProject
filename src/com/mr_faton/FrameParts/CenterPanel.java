package com.mr_faton.FrameParts;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

/**
 * Created by Mr_Faton on 15.04.2015.
 */
public final class CenterPanel {
    private JPanel panel;
    private DefaultTableModel model;
    private JTable table;
    private String[] columnNames;
    private static CenterPanel centerPanel;

    public static synchronized CenterPanel getInstance() {
        if (centerPanel == null) {
            centerPanel = new CenterPanel();
        }
        return centerPanel;
    }
    public CenterPanel() {
        columnNames = new String[]{"Название", "Заголовок", "Срок"};
        model = new DefaultTableModel(0, columnNames.length);
        model.setColumnIdentifiers(columnNames);
        table = new JTable(model);
        table.setAutoCreateRowSorter(true);
        table.setBackground(Color.white);
        table.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane scrollPane = new JScrollPane(table);
//        scrollPane.setPreferredSize(new Dimension(500, 400));

        panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Результаты поиска:"));
        panel.add(scrollPane);
    }

    public void addRowsInTable(String[][] data) {
        for (String[] base : data) {
            model.addRow(base);
        }
    }

    public JPanel getPanel() {
        return panel;
    }

    public String getSelectedMap() {
        if (table.getSelectedRow() < 0){
            System.out.println("строка не выбрана");
            return "";
        } else {
            int row = table.getSelectedRow();
            return (String) table.getValueAt(row, 1);
        }
    }
}