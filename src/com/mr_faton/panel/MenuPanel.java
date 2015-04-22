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
    private JMenuBar menuBar;
    private JDialog mapPatternsDialog;
    private JDialog programSettingsDialog;
    private JDialog aboutDialog;

    public MenuPanel() {
        mapPatternsDialog = null;
        programSettingsDialog = null;
        aboutDialog = null;
        menuBar = new JMenuBar();
        JMenu options = new JMenu("Настройки");
        JMenu help = new JMenu("Помощь");

        JMenuItem programSettings = new JMenuItem("Настройки программы");
        JMenuItem patternSettings = new JMenuItem("Настройки шаблонов");

        programSettings.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (programSettingsDialog == null) {
                    programSettingsDialog = new ProgramSettingsDialog();
                }
                programSettingsDialog.setVisible(true);
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
                if (aboutDialog == null) {
                    aboutDialog = new AboutDialog();
                }
                aboutDialog.setVisible(true);
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
