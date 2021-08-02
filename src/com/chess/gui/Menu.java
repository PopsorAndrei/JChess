package com.chess.gui;

import javax.swing.*;
import java.awt.*;

public class Menu {
    private final JFrame menuFrame;
    private final JPanel gamePanel;

    public Menu() {
        this.menuFrame = new JFrame("Menu");
        this.gamePanel = new JPanel();
        gamePanel.setBorder(BorderFactory.createEmptyBorder(1000,30,10,30));
        gamePanel.setLayout( new GridLayout(0,1));

        menuFrame.add(gamePanel,BorderLayout.CENTER);

        menuFrame.setTitle("plm");
        menuFrame.pack();
        menuFrame.setVisible(true);
    }
}
