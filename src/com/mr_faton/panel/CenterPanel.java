package com.mr_faton.panel;

import com.mr_faton.entity.WarningMessenger;
import com.mr_faton.handler.OpenButtonHandler;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Created by Mr_Faton on 15.04.2015.
 */
public final class CenterPanel {
    private JPanel panel;//панель куда добавим таблицу и которую вернём гавному фрейму по его запросу
    private MyTableModel model;//модель таблицы на основании которой постоим таблицу с результатами
    private JTable table;//таблица результатов
    private String[] columnNames;//массив с название колонок
    private static CenterPanel centerPanel;//наша центральная панель - синглетон, поэтому будем делать только один объект

    /*
    инициализируем наш объект центральной панели (в основном это требуется для того, что разные части программы,
    а именно панели и обработчики должны обмениваться данными, хранящимися только в контексте определённой панели и
    для того, чтобы каждый раз как только другой панели нужны данные этой панели (например обработчику кнопки "Открыть"
    нужно знать какую строку выделил пользователь и для того, чтобы не создавать новый экземпляр этой панлеи мы
    вернём по запросу уже созданный ранее экземпляр и там можно будет вызвать метод, возвращающий необходимые
    данные. По такому принципу реализованы все панели))

     */
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

        table.setAutoCreateRowSorter(true);
        table.setBackground(Color.white);
        table.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane scrollPane = new JScrollPane(table);

        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent event) {
                if (event.getClickCount() == 2) {
                    String selectedMapHeader = getSelectedMap();
                    if (selectedMapHeader != null && selectedMapHeader.length() != 0) {
                        OpenButtonHandler openButtonHandler = new OpenButtonHandler();
                        openButtonHandler.openMapInEditor(selectedMapHeader);
                    }
                }
            }
        });

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
/*
Объект этого класса - это просто таблица с результатми (или без них - таблица с шапкой). Создаётся таблица, добавляется
на панель и добавляется в центральную часть главного фрейма
 */

class MyTableModel extends DefaultTableModel {
    MyTableModel(int rowCount, int columnCount) {
        super(rowCount, columnCount);
    }

    @Override
    public boolean isCellEditable(int row, int column) {
        return false;
    }
}
/*
Настледник класса DefaultTableModel, нужен для того, чтобы переопределить лишь один метод "isCellEditable". Это нужно
для того, чтобы сделать ячейки таблицы не редактируемыми.
 */