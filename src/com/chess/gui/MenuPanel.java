package com.chess.gui;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.plaf.FontUIResource;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
//aici II MENIU
public class MenuPanel extends JFrame implements ActionListener{

    Color backgroundColor = new Color(0x123456);
    ImageIcon menuImage = new ImageIcon("fin.png");
    JButton button1;
    JButton button2;
    JButton button3;
    JButton button4;


    public MenuPanel(){

            Border titleBorder = BorderFactory.createLineBorder(Color.WHITE);

        this.setTitle("Chess Menu");
        this.setSize(420,420);
        this.setVisible(true);
        this.getContentPane().setBackground(backgroundColor);

        JLabel title = new JLabel();

        //resize and add img
        Image image = menuImage.getImage(); // transform it
        Image newimg = image.getScaledInstance(50, 50,  java.awt.Image.SCALE_SMOOTH);
        title.setIcon(menuImage);
        ImageIcon finalImage = new ImageIcon(newimg);

        //text and img
        title.setIcon(finalImage);
        title.setText("Play chess");
        title.setHorizontalTextPosition(JLabel.CENTER);
        title.setVerticalTextPosition(JLabel.BOTTOM);

        title.setForeground(Color.white);
        title.setFont(new Font("MV Boli", Font.PLAIN,20));
        //title.setIconTextGap(-35); // distant dintre imagine si text
        title.setBorder(titleBorder);
        title.setVerticalAlignment(JLabel.TOP);
        title.setHorizontalAlignment(JLabel.CENTER);
        title.setBounds(2,2,400,375);

        //adding buttons
        button1 = new JButton();
        button1.setBounds(150,100,100,40);
        button1.setText("Play");
        button1.setFont(new Font("MV Boli", Font.PLAIN,15));
        button1.setFocusable(false);
        button1.addActionListener(this);
        this.add(button1);


        /*
        //second button
        button2 = new JButton();
        button2.setBounds(150,150,100,40);
        button2.setText("PvC");
        button2.setFont(new Font("MV Boli", Font.PLAIN,15));
        button2.setFocusable(false);
        button2.addActionListener(this);
        this.add(button2);

        //third button
         button3 = new JButton();
        button3.setBounds(150,200,100,40);
        button3.setText("CvP");
        button3.setFont(new Font("MV Boli", Font.PLAIN,15));
        button3.setFocusable(false);
        button3.addActionListener(this);
        this.add(button3);

*/
        //test button
        JButton buttont = new JButton();
        buttont.setBounds(10,10,20,20);

        this.add(buttont);



        this.add(title);
        this.setLayout(null);


        ImageIcon icon = new ImageIcon("logo2.png");
        this.setIconImage(icon.getImage());

      //  Table.get().show();

      //  this.getContentPane().setBackground(new Color(0x123456));
    }
    @Override
    public void actionPerformed(ActionEvent e){
        if(e.getSource()==button1){

            Table.get().show();

        }
        if(e.getSource()==button2){
            System.out.println("robi ");

        }
        if(e.getSource()==button3){
            System.out.println();
           // Table tabla3 = new Table(true,true);

        }    }
}
