package com.mr_faton;

import com.mr_faton.entity.CacheCleaner;
import com.mr_faton.frame.MainFrame;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Mr_Faton on 15.04.2015.
 */
public final class StartProgram {
    public static JFrame mainFrame = new MainFrame();

    public static void main(String[] args) {
        EventQueue.invokeLater(new Program(mainFrame));
    }
}

class Program implements Runnable {
    private JFrame mainFrame;

    Program(JFrame mainFrame) {
        this.mainFrame = mainFrame;
    }

    @Override
    public void run() {
        mainFrame.setTitle("ГЦСТ Загрузчик метеокарт (by Mr_Faton)");

        //обработчик закрытия окна
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //поверх других окон на экране
        mainFrame.toFront();
        mainFrame.setVisible(true);

        new Thread(new Runnable() {
            @Override
            public void run() {
                new CacheCleaner().cleanCache();
            }
        }).start();
    }
}