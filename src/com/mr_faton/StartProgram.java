package com.mr_faton;

import com.mr_faton.frame.MainFrame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * Created by Mr_Faton on 15.04.2015.
 */
public class StartProgram {
    public static void main(String[] args) {
        EventQueue.invokeLater(new RunnableImpl());
    }
}

class RunnableImpl implements Runnable {
    @Override
    public void run() {
        JFrame mainFrame = new MainFrame();
        mainFrame.setTitle("ГЦСТ Загрузчик метеокарт (by Mr_Faton)");

        //обработчик закрытия окна
        mainFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.out.println("User try to close window");
                System.exit(0);
            }
        });

        //поверх других окон на экране
        mainFrame.toFront();
        mainFrame.setVisible(true);
    }
}