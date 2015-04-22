package com.mr_faton.frame;

import com.mr_faton.panel.CenterPanel;
import com.mr_faton.panel.MenuPanel;
import com.mr_faton.panel.NorthPanel;
import com.mr_faton.panel.SouthPanel;

import javax.swing.*;
import java.awt.*;
import java.net.URL;

/**
 * Created by Mr_Faton on 15.04.2015.
 */
public final class MainFrame extends JFrame {
    private static int MAIN_FRAME_WIDTH = 700;
    private static int MAIN_FRAME_HEIGHT = 500;


    public MainFrame() throws HeadlessException {
        //получаем разрешение монитора для того, чтобы выводить наш фрейм по центру
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension monitorScreenSize = toolkit.getScreenSize();
        int monitorWidth = monitorScreenSize.width;
        int monitorHeight = monitorScreenSize.height;

        //устанавливаем размер
        setSize(MAIN_FRAME_WIDTH, MAIN_FRAME_HEIGHT);
        //устанавливаем положение (координаты фрейма)
        setLocation(monitorWidth / 2 - MAIN_FRAME_WIDTH / 2, monitorHeight / 2 - MAIN_FRAME_HEIGHT / 2);
        //делаем чтобы окно не могло менять размер
        setResizable(false);

        //внутри пакета храниться иконка фрейма, этот блок нужен чтобы получить к ней путь (получить иконку)
        URL iconURL = getClass().getResource("/com/mr_faton/image/frameIcon.png");
        Image frameIcon = new ImageIcon(iconURL).getImage();
        setIconImage(frameIcon);

        //устанавливает строку меня, получая объект "MenuBar" из моего класса MenuPanel
        setJMenuBar(new MenuPanel().getMenuBar());
        //добавляет панели на главный фрейм, получаемых из мох классов
        add(NorthPanel.getInstance().getPanel(), BorderLayout.NORTH);
        add(CenterPanel.getInstance().getPanel(), BorderLayout.CENTER);
        add(SouthPanel.getInstance().getPanel(), BorderLayout.SOUTH);

    }
}
/*
Основной фрейм программы
 */