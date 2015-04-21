package com.mr_faton.frame;

import com.mr_faton.StartProgram;
import com.mr_faton.entity.SettingsWorker;
import com.mr_faton.entity.WarningMessenger;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by Mr_Faton on 20.04.2015.
 */
public final class MapPatternsDialog extends JDialog {
    private static int WIDTH = 450;
    private static int HEIGHT = 400;

    public MapPatternsDialog() {
        super(StartProgram.mainFrame, "Настройка шаблонов карт", true);

        String[] columnNames = {"Название", "Заголовок"};
        Map<String, String> mapPatterns = SettingsWorker.getInstance().getAllPatterns();

        final DefaultTableModel tableModel = new DefaultTableModel(0, 2);
        tableModel.setColumnIdentifiers(columnNames);

        int i = 0;
        for (Map.Entry<String, String> entry : mapPatterns.entrySet()) {
            String mapName = entry.getKey();
            String mapHeader = entry.getValue();
            tableModel.insertRow(i, new String[]{mapName, mapHeader});
            i++;
        }


        final JTable table = new JTable(tableModel);
        table.setBackground(Color.white);
        JScrollPane scrollPane = new JScrollPane(table);

        JButton addButton = new JButton("Добавить");
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                tableModel.addRow(new String[]{});
            }
        });

        JButton removeButton = new JButton("Удалить");
        removeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int row = table.getSelectedRow();
                tableModel.removeRow(row);
            }
        });

        JButton saveButton = new JButton("Сохранить");
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Map<String, String> allPatternsMap = new LinkedHashMap<String, String>();
                int rowCount = table.getRowCount();
                for (int row = 0; row < rowCount; row++) {
                    String mapName = (String) tableModel.getValueAt(row, 0);
                    if (mapName == null || mapName.length() == 0) {
                        showMessage(row + 1, table.getColumnName(0));
                        return;
                    }
                    String mapHeader = (String) tableModel.getValueAt(row, 1);
                    if (mapHeader == null || mapHeader.length() == 0) {
                        showMessage(row + 1, table.getColumnName(1));
                        return;
                    }
                    allPatternsMap.put(mapName, mapHeader);
                }
                SettingsWorker.getInstance().setPatterns(allPatternsMap);
            }

            private void showMessage(int row, String columnName) {
                new WarningMessenger("Пустая ячейка!",
                        "Не не не, мы не будем сохранять данные,\n" +
                                "когда ячейка в строке \"" + row + "\" и столбце \"" + columnName + "\" пустая.\n" +
                                "Вернитесь к таблице и заполните пустую ячейку!"
                );
            }
        });

        JButton cancelButton = new JButton("Отмена");
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
            }
        });

        JPanel buttonsPanel = new JPanel(new GridLayout(1, 4, 5, 5));
        buttonsPanel.add(addButton);
        buttonsPanel.add(removeButton);
        buttonsPanel.add(saveButton);
        buttonsPanel.add(cancelButton);

        add(scrollPane, BorderLayout.CENTER);
        add(buttonsPanel, BorderLayout.SOUTH);

        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension monitorScreenSize = toolkit.getScreenSize();
        int monitorWidth = monitorScreenSize.width;
        int monitorHeight = monitorScreenSize.height;
        setSize(WIDTH, HEIGHT);
        setLocation(monitorWidth / 2 - WIDTH / 2, monitorHeight / 2 - HEIGHT / 2);
    }
}
