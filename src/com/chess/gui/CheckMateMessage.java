package com.chess.gui;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CheckMateMessage extends JFrame implements ActionListener {

    JButton buttonNewGame;
    JButton buttonClose;
    public CheckMateMessage(String text){
        this.setTitle("Chess Menu");
        this.setSize(360,150);
        this.setVisible(true);
        this.getContentPane().setBackground(Color.WHITE);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);



        JLabel label= new JLabel(text,JLabel.CENTER);

        label.setAlignmentX(0);
        label.setAlignmentY(100);
      //  label.setBorder(border);
     //   label.setPreferredSize(new Dimension(30, 15));


        //newGameButton
        buttonNewGame = new JButton();
        buttonNewGame.setBounds(70,70,70,30);
        buttonNewGame.setText("New Game");
        buttonNewGame.setFont(new Font("MV Boli", Font.PLAIN,10));
        buttonNewGame.setFocusable(false);
        buttonNewGame.addActionListener(this);
        this.add(buttonNewGame);


        //closeGameButton
        buttonClose = new JButton();
        buttonClose.setBounds(210,70,70,30);
        buttonClose.setText("CLose");
        buttonClose.setFont(new Font("MV Boli", Font.PLAIN,10));
        buttonClose.setFocusable(false);
        buttonClose.addActionListener(this);
        this.add(buttonClose);
        this.add(label);


    }
    public void actionPerformed(ActionEvent e){
        if(e.getSource()==buttonNewGame){


        }
        if(e.getSource()==buttonClose){
            System.exit(0);
        }
    }
}
