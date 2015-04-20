package com.mr_faton.panel;

import com.mr_faton.frame.MapPatternsDialog;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by Mr_Faton on 15.04.2015.
 */
public final class MenuPanel {
    private JMenuBar menuBar;
    private JDialog mapPatternsDialog;

    public MenuPanel() {
        mapPatternsDialog = null;
        menuBar = new JMenuBar();
        JMenu options = new JMenu("Настройки");
        JMenu help = new JMenu("Помощь");

        JMenuItem programSettings = new JMenuItem("Настройки программы");
        JMenuItem patternSettings = new JMenuItem("Настройки шаблонов");

        programSettings.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("program settings menu clicked");
            }
        });

        patternSettings.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (mapPatternsDialog == null) {
                    mapPatternsDialog = new MapPatternsDialog();
                }
                mapPatternsDialog.setVisible(true);
            }
        });

        options.add(programSettings);
        options.add(patternSettings);

        JMenuItem about = new JMenuItem("О Программе");

        about.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("about menu clicked");
            }
        });

        help.add(about);

        menuBar.add(options);
        menuBar.add(help);
    }

    public JMenuBar getMenuBar() {
        return menuBar;
    }
}
