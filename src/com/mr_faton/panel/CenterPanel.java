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
        //инициализируем массив с название колонок
        columnNames = new String[]{"Название", "Заголовок", "Срок"};
        //создаём модель таблицы без строк и с тремя колонками
        model = new MyTableModel(0, columnNames.length);
        //устанавливаем назавание колонок
        model.setColumnIdentifiers(columnNames);

        //создаём таблицу из модели
        table = new JTable(model);
        //создаём автосортировщик
        table.setAutoCreateRowSorter(true);
        //устанавливаем цвет фона
        table.setBackground(Color.white);
        //накладываем ограничение на выделение только одной строки
        table.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        //устанавливаем жирный шрифт 14-того размера
        table.setFont(new Font("default", Font.BOLD, 14));
        //пристоединяем к таблице скроллек
        JScrollPane scrollPane = new JScrollPane(table);
        //добавляем к таблице слушателя мыши, для того чтобы открыть карту для пользователя по двойному щелчку
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

        //создаём новую панель
        panel = new JPanel();
        //устанавливаем в ней тип компоновки элементов (таблица будет растянута на всю область)
        panel.setLayout(new BorderLayout());
        //установить рамочку вокруг панели
        panel.setBorder(BorderFactory.createTitledBorder("Результаты поиска:"));
        //добавить на панель таблицу в виде скроллера
        panel.add(scrollPane);
    }

    //добавляет строки из двухмерного массива в таблицу
    public void addRowsInTable(String[][] data) {
        //очистить таблицу
        model.setRowCount(0);
        for (String[] base : data) {
            model.addRow(base);
        }
    }

    //возвращает объект панели
    public JPanel getPanel() {
        return panel;
    }

    //возвращает заголовок карты в выделенной строке
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

final class MyTableModel extends DefaultTableModel {
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