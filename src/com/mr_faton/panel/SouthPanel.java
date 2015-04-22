package com.mr_faton.panel;

import com.mr_faton.handler.OpenButtonHandler;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by Mr_Faton on 15.04.2015.
 */
public final class SouthPanel {
    //only button
    private static SouthPanel southPanel;//панель является синглетоном
    private JButton openButton;//кнопка "Открыть"
    private JPanel panel;//панель, содержащая только одну кнопку. Во фрейм не желательно добавлять элемент, лучше панель

    //почему синглетон - смотри описание в центральной панели
    public static synchronized SouthPanel getInstance() {
        if (southPanel == null) {
            southPanel = new SouthPanel();
        }
        return southPanel;
    }

    public SouthPanel() {
        //создаём кнопку и добавляем к ней слушателя
        openButton = new JButton("Открыть в редакторе");
        openButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //получить заголовок выбранной в таблице карты
                String selectedMapHeader = CenterPanel.getInstance().getSelectedMap();
                //если карту выбрали и её заголовок не пуст
                if (selectedMapHeader != null && selectedMapHeader.length() != 0) {
                    //создать обработчика кнопки "Открыть" и открыть выбранную карту в редакторе
                    OpenButtonHandler openButtonHandler = new OpenButtonHandler();
                    openButtonHandler.openMapInEditor(selectedMapHeader);
                }
            }
        });

        //создать панель и установить её свойства
        panel = new JPanel();
        panel.setLayout(new BorderLayout());

        //добавить кнопку на панель
        panel.add(openButton);
    }

    //вернуть панель, содержащую нашу кнопку
    public JPanel getPanel() {
        return panel;
    }
}
