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
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension monitorScreenSize = toolkit.getScreenSize();
        int monitorWidth = monitorScreenSize.width;
        int monitorHeight = monitorScreenSize.height;

        setSize(MAIN_FRAME_WIDTH, MAIN_FRAME_HEIGHT);
        setLocation(monitorWidth / 2 - MAIN_FRAME_WIDTH / 2, monitorHeight / 2 - MAIN_FRAME_HEIGHT / 2);
        setResizable(false);

        URL iconURL = getClass().getResource("/com/mr_faton/image/frameIcon.png");
        Image frameIcon = new ImageIcon(iconURL).getImage();
        setIconImage(frameIcon);

        setJMenuBar(new MenuPanel().getMenuBar());
        add(NorthPanel.getInstance().getPanel(), BorderLayout.NORTH);
        add(CenterPanel.getInstance().getPanel(), BorderLayout.CENTER);
        add(SouthPanel.getInstance().getPanel(), BorderLayout.SOUTH);

    }
}
