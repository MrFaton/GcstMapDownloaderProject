package com.mr_faton;

import com.mr_faton.frame.MainFrame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

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
        mainFrame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        mainFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                int selection = JOptionPane.showConfirmDialog(mainFrame, "Вы действительно хотите выйти?",
                        "Выход", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                if (selection == JOptionPane.YES_OPTION) {
                    mainFrame.dispose();
                    System.exit(0);
                }
            }
        });

        //поверх других окон на экране
        mainFrame.toFront();
        mainFrame.setVisible(true);
    }
}