package com.mr_faton.panel;

import com.mr_faton.entity.WarningMessenger;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

/**
 * Created by Mr_Faton on 15.04.2015.
 */
public final class CenterPanel {
    private JPanel panel;
    private MyTableModel model;
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
        model = new MyTableModel(0, columnNames.length);
        model.setColumnIdentifiers(columnNames);


        table = new JTable(model);

//        table.setAutoResizeMode(JTable.AUTO_RESIZE_SUBSEQUENT_COLUMNS);
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
        model.setRowCount(0);//очистить таблицу
        for (String[] base : data) {
            model.addRow(base);
        }
    }

    public JPanel getPanel() {
        return panel;
    }

    public String getSelectedMap() {
        if (table.getSelectedRow() < 0) {
            new WarningMessenger("Внимание!", "Вы не выбрали ни одной карты!\n" +
                    "Выберите хотя бы одну карту, а потом открывайте её в редакторе.");
            return "";
        } else {
            int row = table.getSelectedRow();
            return (String) table.getValueAt(row, 1);
        }
    }
}

class MyTableModel extends DefaultTableModel {
    MyTableModel(int rowCount, int columnCount) {
        super(rowCount, columnCount);
    }

    @Override
    public boolean isCellEditable(int row, int column) {
        return false;
    }
}