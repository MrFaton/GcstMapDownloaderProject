package com.mr_faton.panel;

import com.mr_faton.frame.AboutDialog;
import com.mr_faton.frame.MapPatternsDialog;
import com.mr_faton.frame.ProgramSettingsDialog;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by Mr_Faton on 15.04.2015.
 */
public final class MenuPanel {
    private JMenuBar menuBar;//строка меню
    private JDialog mapPatternsDialog;//дочерний фрейм-диалог настроек шаблонов поиска карт
    private JDialog programSettingsDialog;//дочерний фрейм-диалог настроек программы
    private JDialog aboutDialog;//дочерний фрейм-диалог показывающий информацию о программе

    public MenuPanel() {
        mapPatternsDialog = null;
        programSettingsDialog = null;
        aboutDialog = null;
        //создаём новое пнель меню
        menuBar = new JMenuBar();
        //создаём 2 материнских объекта меню
        JMenu options = new JMenu("Настройки");
        JMenu help = new JMenu("Помощь");

        //создаём 2 подменю объекта меню "Настройки"
        JMenuItem programSettings = new JMenuItem("Настройки программы");
        JMenuItem patternSettings = new JMenuItem("Настройки шаблонов");

        //добавляем слушателей к дочерним объектам меню
        programSettings.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (programSettingsDialog == null) {
                    programSettingsDialog = new ProgramSettingsDialog();
                }
                //показываем доечрний фрейм-диалог
                programSettingsDialog.setVisible(true);
            }
        });

        patternSettings.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (mapPatternsDialog == null) {
                    mapPatternsDialog = new MapPatternsDialog();
                }
                //показываем доечрний фрейм-диалог
                mapPatternsDialog.setVisible(true);
            }
        });

        //добавляем в материнский объект меню "Настройки" 2 дочерних подменю
        options.add(programSettings);
        options.add(patternSettings);

        //создаём подменю объекта меню "Помощь"
        JMenuItem about = new JMenuItem("О Программе");

        //добавляем слушателя к дочернему объекту меню
        about.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (aboutDialog == null) {
                    aboutDialog = new AboutDialog();
                }
                //показываем доечрний фрейм-диалог
                aboutDialog.setVisible(true);
            }
        });

        //добавляем в материнский объект меню "Помощь" дочернее подменю
        help.add(about);

        //добавляем на основное меню 2 материнских объекта меню
        menuBar.add(options);
        menuBar.add(help);
    }

    //возвращаем наш сформированный объект меню
    public JMenuBar getMenuBar() {
        return menuBar;
    }
}
/*
Это объект типа JMenuBar создаёт строку меню
 */