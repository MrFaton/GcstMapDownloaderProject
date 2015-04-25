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
    private static SettingsWorker settingsWorker;//обработчик xml-файла с настройками

    public MapPatternsDialog() {
        /*
        взываем конструктор предка (JDialog), передаём ему родительсткий фрейм, заголовок фрейма и признак модальности
        (т.е. когда это окно открыто и если "true", то пользователь не может работать с остальными окнами, пока не
        закроется это окно)
         */
        super(StartProgram.mainFrame, "Настройка шаблонов карт", true);

        settingsWorker = SettingsWorker.getInstance();

        //имя колонок в таблице
        String[] columnNames = {"Название", "Заголовок"};
        //получить мапу всех ранее определённых шаблонов из файла настроек
        Map<String, String> mapPatterns = settingsWorker.getAllPatterns();

        //создать модель таблицы без строк и с двумя столбцами, final т.к. используетя для создание таблицы
        final DefaultTableModel tableModel = new DefaultTableModel(0, 2);
        //установить имена столбцов
        tableModel.setColumnIdentifiers(columnNames);

        //перебрать всю мапу с шаблонами и добавить строки с шаблонами в модель таблицы
        int i = 0;
        for (Map.Entry<String, String> entry : mapPatterns.entrySet()) {
            String mapName = entry.getKey();
            String mapHeader = entry.getValue();
            tableModel.insertRow(i, new String[]{mapName, mapHeader});
            i++;
        }

        //создаём таблицу на основе уже созданной модели
        final JTable table = new JTable(tableModel);
        //устанавливаем цвет фона ячеек белым
        table.setBackground(Color.white);
        //добавляем скроллер в таблицу (так же это позволяет сделать видимыми название столбцов таблицы)
        JScrollPane scrollPane = new JScrollPane(table);

        //создаём 4 кнопки и добавляем к ним слушателей

        JButton addButton = new JButton("Добавить");
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //добавляем пустую строку в таблицу
                tableModel.addRow(new String[]{});
            }
        });

        JButton removeButton = new JButton("Удалить");
        removeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //удалить выделенную строку из таблицы
                int row = table.getSelectedRow();
                if (row >= 0) {
                    tableModel.removeRow(row);
                } else {
                    new WarningMessenger("Строка не выбрана!", "Строка для удаления не выбрана!\n" +
                            "Пожалуйста выберите строку, а потом удаляйте!");
                }
            }
        });

        JButton saveButton = new JButton("Сохранить");
        saveButton.addActionListener(new ActionListener() {
            //сохранить всю таблиыу в настройки
            @Override
            public void actionPerformed(ActionEvent e) {
                //создаём мапу, в которой будет храниться таблица (ключ: названик карты, значение: заголовок карты)
                Map<String, String> allPatternsMap = new LinkedHashMap<>();
                //определяем кол-во строк
                int rowCount = table.getRowCount();
                //запускаем цикл по каждой строке
                for (int row = 0; row < rowCount; row++) {
                    //получем имя карты из текущей строки
                    String mapName = (String) tableModel.getValueAt(row, 0);
                    //если его нет либо оно пустое, показываем пользователю сообщение о том, что ячейка пустая
                    if (mapName == null || mapName.length() == 0) {
                        showMessage(row + 1, table.getColumnName(0));
                        return;
                    }
                    //получаем заголовок карты из текущей строки
                    String mapHeader = (String) tableModel.getValueAt(row, 1);
                    //если его нет либо оно пустое, показываем пользователю сообщение о том, что ячейка пустая
                    if (mapHeader == null || mapHeader.length() == 0) {
                        showMessage(row + 1, table.getColumnName(1));
                        return;
                    }
                    //ложем в нашу мапу имя карты и её заголовок
                    allPatternsMap.put(mapName, mapHeader);
                }
                //передаём нашему обработчику xml-файла с настройками мапу шаблонов для сохранения в xml
                settingsWorker.setPatterns(allPatternsMap);
                //сохранить настройки в xml-файл, true - чтобы показать сообщение об успешном сохранении
                settingsWorker.saveSettings(true);
            }

            //конструирует объект с предупреждающим сообщением (если есть пустая ячейка)
            private void showMessage(int row, String columnName) {
                new WarningMessenger("Пустая ячейка!",
                        "Не не не, мы не будем сохранять данные,\n" +
                                "когда ячейка в строке № \"" + row + "\" и столбце \"" + columnName + "\" пустая.\n" +
                                "Вернитесь к таблице и заполните пустую ячейку!"
                );
            }
        });

        JButton cancelButton = new JButton("Отмена");
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //просто делает окно настроек шаблоно невидимым
                setVisible(false);
            }
        });

        //создаём панель для кнопок в виде сеточной компоновки на 1 строку, 4 ячейки и отступами по вертик и по горизонт
        JPanel buttonsPanel = new JPanel(new GridLayout(1, 4, 5, 5));
        //добавляем на панель кнопки
        buttonsPanel.add(addButton);
        buttonsPanel.add(removeButton);
        buttonsPanel.add(saveButton);
        buttonsPanel.add(cancelButton);

        //добавляем в центр нашего фрейма таблицу
        add(scrollPane, BorderLayout.CENTER);
        //добавляем в южную часть фрейма панель с кнопаками
        add(buttonsPanel, BorderLayout.SOUTH);

        //получаем разрешение монитора для того, чтобы выводить наш фрейм по центру
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension monitorScreenSize = toolkit.getScreenSize();
        int monitorWidth = monitorScreenSize.width;
        int monitorHeight = monitorScreenSize.height;
        //устанавливаем размер
        setSize(WIDTH, HEIGHT);
        //устанавливаем положение (координаты фрейма)
        setLocation(monitorWidth / 2 - WIDTH / 2, monitorHeight / 2 - HEIGHT / 2);
    }
}
/*
Объект дочернего фрейма-диалога, который реагирует на вызов "Настройки -* Настройки шаблонов"
В этом диалоговом фрейме происходит настройка шаблонов карт, а именно указывается название карты и её заголовок для
поиска
 */