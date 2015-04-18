package com.mr_faton.panel;

import com.mr_faton.entity.SettingsWorker;
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
    private JPanel panel;
    private JLabel shortMapHeaderLabel;
    private JLabel searchDeepLabel;
    private JComboBox<String> mapNameCombo;
    private JComboBox<String> searchDeepCombo;
    private JButton searchButton;
    private String[] deepSearch;
    private static NorthPanel northPanel;

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
        panel.setLayout(new GridLayout(2, 3, 30, 0));

        //создаём метки - подписи к полям
        shortMapHeaderLabel = new JLabel("Сокращённый заголовок карты:", JLabel.LEFT);
        searchDeepLabel = new JLabel("Глубина поиска:", JLabel.LEFT);

        //создаём выпадающий список с заголовками карт
        mapNameCombo = new JComboBox<>();

        SettingsWorker settingsWorker = SettingsWorker.getInstance();
        Map<String, String> allPatternsMap = settingsWorker.getAllPatterns();
        for (Map.Entry<String, String> entry : allPatternsMap.entrySet()) {
            mapNameCombo.addItem(entry.getKey());
        }
        mapNameCombo.setEditable(true);

        //создаём выпадающий список с вариантами глубины поиска
        deepSearch = new String[]{
                "1 сутки", "2 суток", "3 суток"
        };
        searchDeepCombo = new JComboBox<>();
        for (String elem : deepSearch) {
            searchDeepCombo.addItem(elem);
        }
        searchDeepCombo.setEditable(false);

        //создаём кнопку поиска
        searchButton = new JButton("Искать");
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String mapHeader = getSelectedShortMapHeader();

                String deepSearch = getSelectedDeepSearch();
                String[] deepSearchParameters = deepSearch.split(" ");
                deepSearch = deepSearchParameters[0];

                new SearchButtonHandler().getSearchResult(mapHeader, deepSearch);

                CenterPanel.getInstance().addRowsInTable(new SearchButtonHandler().getSearchResult(mapHeader, deepSearch));
            }
        });

        panel.add(shortMapHeaderLabel);
        panel.add(searchDeepLabel);
        panel.add(new JLabel());//Т.к. у нас сетка, то над кнопкой поиска не должно быть меток
        panel.add(mapNameCombo);
        panel.add(searchDeepCombo);
        panel.add(searchButton);
    }


    public JPanel getPanel() {
        return panel;
    }

    public String getSelectedShortMapHeader() {
        return (String) mapNameCombo.getSelectedItem();
    }

    public String getSelectedDeepSearch() {
        return (String) searchDeepCombo.getSelectedItem();
    }
}
