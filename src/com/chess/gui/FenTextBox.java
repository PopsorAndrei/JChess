package com.chess.gui;

import javax.swing.*;
import java.awt.*;

public class FenTextBox extends JFrame {

    public FenTextBox(String fenText){
      this.setTitle("FEN text ");
        this.setSize(400,100);
        this.setVisible(true);
        this.getContentPane().setBackground(Color.WHITE);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setResizable(false);

       JTextField text  = new JTextField(fenText);
       //text.setEditable(false);
       text.setHorizontalAlignment(0);


       JTextField helpingMessage = new JTextField("Copy this string of text and when you want to play just add it!");

        this.add(text);
}



}
