package com.mr_faton.entity;

import com.mr_faton.StartProgram;

import javax.swing.*;

/**
 * Created by Mr_Faton on 20.04.2015.
 */
public final class ErrorMessenger {

    public ErrorMessenger(String message) {
        JOptionPane.showMessageDialog(StartProgram.mainFrame, message, "Ошибка!", JOptionPane.ERROR_MESSAGE);
    }
}
