package com.mr_faton.panel;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by Mr_Faton on 15.04.2015.
 */
public final class SouthPanel {
    //only button
    private static SouthPanel southPanel;
    private JButton openButton;

    public static synchronized SouthPanel getInstance() {
        if (southPanel == null) {
            southPanel = new SouthPanel();
        }
        return southPanel;
    }

    public SouthPanel() {
        openButton = new JButton("Открыть в редакторе");
        openButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("open");
                System.out.println(CenterPanel.getInstance().getSelectedMap());
            }
        });
    }

    public JButton getButton() {
        return openButton;
    }

}
