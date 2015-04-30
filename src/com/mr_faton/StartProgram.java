package com.mr_faton;

import com.mr_faton.entity.CacheCleaner;
import com.mr_faton.frame.MainFrame;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Mr_Faton on 15.04.2015.
 */
public final class StartProgram {
    public static JFrame mainFrame;

    public static void main(String[] args) {
        /*
        создаём основное окно программы. Его нужно создать именно здесь, а не во внутреннем классе "Program", т.к.
        все дочерние фреймы-диалоги должны иметь фрейма-родителя - это наш "mainFrame", но другие классы не могут
        получить доступ к внутреннему классу, поэтому мы конструируем основное окно тут и передаём его в конструкторе
        внутреннему классу
         */
        mainFrame = new MainFrame();
        //запускаем программу в отдельном потоке
        EventQueue.invokeLater(new Program(mainFrame));
    }
}
/*
Объект, который стартует экземпляр программы в отдельном потоке
 */

class Program implements Runnable {
    private JFrame mainFrame;//основное окно программы

    Program(JFrame mainFrame) {
        //получаем основное окно программы
        this.mainFrame = mainFrame;
    }

    @Override
    public void run() {
        //устанавливаем заголовок окна
        mainFrame.setTitle("ГЦСТ Загрузчик метеокарт (by Mr_Faton)");

        //обработчик закрытия окна
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //поверх других окон на экране
        mainFrame.toFront();
        mainFrame.setVisible(true);

        /*
        при каждом старте программы будет очишать кеш папку (куда сохраняются скачанные карты). Если в этой папке
        собралось много карт, то процесс очистки может занять некоторое время, поэтому, чтобы не мешать загружаться
        основному окну, выполняем очистку кеша в отдельном потоке
         */
        new Thread(new Runnable() {
            @Override
            public void run() {
                new CacheCleaner().cleanCache();
            }
        }).start();
    }
}
/*
Посути это поток работы остнвоной программы, т.е. тут осуществляется построение оконной системы программы и её работа.
Это приватный иннер класс
 */