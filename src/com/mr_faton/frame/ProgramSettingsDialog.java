package com.mr_faton.frame;

import com.mr_faton.StartProgram;
import com.mr_faton.entity.SettingsWorker;
import com.mr_faton.entity.WarningMessenger;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;

/**
 * Created by root on 21.04.2015.
 */
public class ProgramSettingsDialog extends JDialog {
    private static int WIDTH = 340;
    private static int HEIGHT = 320;
    private SettingsWorker settingsWorker;

    public ProgramSettingsDialog() {
        super(StartProgram.mainFrame, "Настройки программы", true);
        settingsWorker = SettingsWorker.getInstance();
        final Map<String, String> authMap = settingsWorker.getLoginAndPass();

        JPanel mainPanel = new JPanel(new GridLayout(2, 0, 0, 10));

        //панель авторизационных данных
        JPanel authorizePanel = new JPanel(new GridLayout(4, 0));
        authorizePanel.setBorder(BorderFactory.createTitledBorder("Авторизационные данные"));
        final JLabel loginLabel = new JLabel("Логин:");
        final JTextField loginField = new JTextField(authMap.get("login"));
        final JLabel passwordLabel = new JLabel("Пароль:");
        final JTextField passwordField = new JTextField(authMap.get("password"));
        authorizePanel.add(loginLabel);
        authorizePanel.add(loginField);
        authorizePanel.add(passwordLabel);
        authorizePanel.add(passwordField);


        //панель системных настроек
        JPanel systemPanel = new JPanel(new GridLayout(4, 0));
        systemPanel.setBorder(BorderFactory.createTitledBorder("Системные настройки"));
        final JLabel mapEditorLabel = new JLabel("Путь к программе обработки карт:");
        final JTextField mapEditorField = new JTextField(settingsWorker.getMapEditorPath());
        JLabel cacheDirLabel = new JLabel("Папка для временного хранения карт:");
        JTextField cacheDirField = new JTextField(settingsWorker.getCacheDir());
        cacheDirField.setEditable(false);
        systemPanel.add(mapEditorLabel);
        systemPanel.add(mapEditorField);
        systemPanel.add(cacheDirLabel);
        systemPanel.add(cacheDirField);


        //Панель кнопок
        JPanel buttonsPanel = new JPanel();
        JButton saveButton = new JButton("Сохранить");
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String loginStr = loginField.getText();
                if (loginStr == null || loginStr.length() == 0) {
                    showMessage(loginLabel.getText());
                    return;
                }
                String passwordStr = passwordField.getText();
                if (passwordStr == null || passwordStr.length() == 0) {
                    showMessage(passwordLabel.getText());
                    return;
                }
                String mapEditorStr = mapEditorField.getText();
                if (mapEditorStr == null || mapEditorStr.length() == 0) {
                    showMessage(mapEditorLabel.getText());
                    return;
                }
                authMap.clear();
                authMap.put("login", loginStr);
                authMap.put("password", passwordStr);
                settingsWorker.setLoginAndPass(authMap);
                settingsWorker.setMapEditorPath(mapEditorStr);

                setVisible(false);
            }
        });
        JButton cancelButton = new JButton("Отмена");
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
            }
        });
        buttonsPanel.add(saveButton);
        buttonsPanel.add(cancelButton);

        mainPanel.add(authorizePanel);
        mainPanel.add(systemPanel);

        add(mainPanel, BorderLayout.CENTER);
        add(buttonsPanel, BorderLayout.SOUTH);

        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension monitorScreenSize = toolkit.getScreenSize();
        int monitorWidth = monitorScreenSize.width;
        int monitorHeight = monitorScreenSize.height;
        setSize(WIDTH, HEIGHT);
        setLocation(monitorWidth / 2 - WIDTH / 2, monitorHeight / 2 - HEIGHT / 2);
        setResizable(false);
    }

    private void showMessage(String rowName) {
        new WarningMessenger("Пустая строка!",
                "Вот незадача! Строка \"" + rowName + "\" пустая!\n" +
                        "Я не могу сохранить пустую строку.\n" +
                        "Вернитесь к настройкам и заполните пустую строку!"
        );
    }
}
