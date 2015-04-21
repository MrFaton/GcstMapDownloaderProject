package com.mr_faton.panel;

import com.mr_faton.handler.OpenButtonHandler;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by Mr_Faton on 15.04.2015.
 */
public final class SouthPanel {
    //only button
    private static SouthPanel southPanel;
    private JButton openButton;
    private JPanel panel;

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
                String selectedMapHeader = CenterPanel.getInstance().getSelectedMap();
                if (selectedMapHeader != null && selectedMapHeader.length() != 0) {
                    OpenButtonHandler openButtonHandler = new OpenButtonHandler();
                    openButtonHandler.openMapInEditor(selectedMapHeader);
                }

            }
        });

        panel = new JPanel();
        panel.setLayout(new BorderLayout());

        panel.add(openButton);
    }

    public JPanel getPanel() {
        return panel;
    }
}
