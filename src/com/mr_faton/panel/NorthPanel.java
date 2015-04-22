package com.mr_faton.panel;

import com.mr_faton.entity.SettingsWorker;
import com.mr_faton.entity.WarningMessenger;
import com.mr_faton.handler.SearchButtonHandler;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;

/**
 * Created by Mr_Faton on 15.04.2015.
 */
public final class NorthPanel {
    private JPanel panel;//панель, которая содержит в себе все компоненты
    private JLabel mapNameLabel;//метка с названием карты (Микрокольцовка)
    private JLabel searchDeepLabel;//метка с глубиной поиска
    private JComboBox<String> mapNameCombo;//выпадающий список имён карт
    private JComboBox<String> searchDeepCombo;//выпадающий список глубины поиска
    private JButton searchButton;//кнопка запускающая поиск
    private Map<String, String> allPatternsMap;//мапа, содержащая в себе все шаблоны поиска карт из настроек
    private String[] deepSearch;//глубина поиска
    private static NorthPanel northPanel;//наша северная панель - синглетон

    //почему синглетон - описано в центральной панели
    public static synchronized NorthPanel getInstance() {
        if (northPanel == null) {
            northPanel = new NorthPanel();
        }
        return northPanel;
    }

    private NorthPanel() {
        //редактируем свойства панели
        panel = new JPanel();
        panel.setBorder(BorderFactory.createTitledBorder("Поисковый запрос"));
        //тип компоновки панели - сеточная с 2-я строками, 3-я столбцами и 30 пикселями отступа друг от друга по горизон
        panel.setLayout(new GridLayout(2, 3, 30, 0));

        //создаём метки - подписи к полям и выравниваем по левому краю
        mapNameLabel = new JLabel("Сокращённый заголовок карты:", JLabel.LEFT);
        searchDeepLabel = new JLabel("Глубина поиска:", JLabel.LEFT);

        //создаём выпадающий список с заголовками карт
        mapNameCombo = new JComboBox<>();

        //получаем мапу шаблонов карты из настроек и добавляе в выпадающий список имена карт
        SettingsWorker settingsWorker = SettingsWorker.getInstance();
        allPatternsMap = settingsWorker.getAllPatterns();
        for (Map.Entry<String, String> entry : allPatternsMap.entrySet()) {
            mapNameCombo.addItem(entry.getKey());
        }
        //разрешаем редактировать выпадющий список (если пользователю нужно найти карту которой нет в списке)
        mapNameCombo.setEditable(true);

        //создаём выпадающий список с вариантами глубины поиска
        deepSearch = new String[]{
                "1 сутки", "2 суток", "5 суток", "10 суток", "20 суток", "1 месяц", "2 месяца"
        };
        searchDeepCombo = new JComboBox<>();
        for (String elem : deepSearch) {
            searchDeepCombo.addItem(elem);
        }
        //запрещаем редактировать варианты глубины поиска
        searchDeepCombo.setEditable(false);

        //создаём кнопку поиска
        searchButton = new JButton("Искать");
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //получаем имя выбранной карты из выпадающего списка
                String mapName = getSelectedMapName();
                //заголовок карты (по нему мы ищем карту)
                String mapHeader;
                /*
                если мапа со списком шаблонов поиска карт (Микрокольцовка:QYUA98), содержит название карты полученной
                 из выпадающего списка
                 */
                if (allPatternsMap.containsKey(mapName)) {
                    //тогда по этому имени получаем заголовок карты
                    mapHeader = allPatternsMap.get(mapName);
                } else {
                    /*
                    если мапа не содержит такого ключа, значит пользователь ввёл уникальный заголовок карты - мы будем
                    искать его
                     */
                    mapHeader = mapName;
                }

                //получаем выбранную из выпадающего списка глубину поиска
                String deepSearchStr = getSelectedDeepSearch();
                //делим полученный результат на 2 токена: "1" и "сутки/месяц"
                String[] deepSearchParameters = deepSearchStr.split(" ");
                //получае значение первого токена (это всегда число)
                int deepSearch = Integer.valueOf(deepSearchParameters[0]);
                //получаем значение второго токена (это всегда строка (сутки или месяц))
                String conditionSearch = deepSearchParameters[1];
                //если второй токен начинается с "мес", значи выбранная глубина поиска исчисляется в месяцах (2 месяца)
                if (conditionSearch.startsWith("мес")) {
                    //тогда мы первый токен умножаем на 30 дней (2 месяца = 2*3 = 60 дней)
                    deepSearch = deepSearch * 30;
                }
                //преобразовываем глубину поиска из числа в строку
                deepSearchStr = deepSearch + "";
                //создаём новый экземпляр обработчика кнопки "Искать"
                SearchButtonHandler searchButtonHandler = new SearchButtonHandler();
                //передаём методу заголовок карты и глубину поиска и получаем двухмерный массив с найденными результатми
                String[][] foundMaps = searchButtonHandler.getSearchResult(mapHeader, deepSearchStr);
                //если вернувшийся массив существует
                if (foundMaps != null) {
                    //если вернувшийся массив не пустой
                    if (foundMaps.length > 0) {
                        //вывести в центральную таблицу найденные карты
                        CenterPanel.getInstance().addRowsInTable(foundMaps);
                    } else {
                        //иначе вывести сообщение о том, что поиск не дал результатов
                        new WarningMessenger("Поиск",
                                "По вашему запросу не найдено ни одной карты...\n" +
                                        "Введите новый запрос или лучше выберите карту из уже имеющегося списка"
                        );
                    }
                }
            }
        });

        //добавляем компоненты на панель
        panel.add(mapNameLabel);
        panel.add(searchDeepLabel);
        panel.add(new JLabel());//Т.к. у нас сетка, то над кнопкой "Искать" не должно быть метки
        panel.add(mapNameCombo);
        panel.add(searchDeepCombo);
        panel.add(searchButton);
    }

    //вернуть панель, содержащую все компоненты
    public JPanel getPanel() {
        return panel;
    }

    //вернуть выбраное в выпадающем списке имя карты
    public String getSelectedMapName() {
        return (String) mapNameCombo.getSelectedItem();
    }

    //вернуть выбранное в выпадающем списке глубину поиска
    public String getSelectedDeepSearch() {
        return (String) searchDeepCombo.getSelectedItem();
    }
}
