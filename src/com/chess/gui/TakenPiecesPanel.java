package com.chess.gui;

import com.chess.engine.board.Move;
import com.chess.engine.piece.Piece;
import com.google.common.primitives.Ints;
//import org.checkerframework.checker.units.qual.A;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.swing.*;
import javax.swing.border.EtchedBorder;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.List;

public class TakenPiecesPanel extends JPanel {

        public static final EtchedBorder PANEL_BOARDER = new EtchedBorder(EtchedBorder.RAISED);
        private static final Color PANEL_COLOR = Color.decode("#F5F5DC");
        private static final Dimension TAKEN_PIECES_DIMENSION = new Dimension(60,20);

        private final JPanel notrthPanel;
        private final JPanel southPanel;



    public TakenPiecesPanel(){
        super(new BorderLayout());
        setBackground(PANEL_COLOR);
        setBorder(PANEL_BOARDER);
        this.notrthPanel = new JPanel(new GridLayout(8,2));
        this.southPanel = new JPanel(new GridLayout(8,2));
        this.notrthPanel.setBackground(PANEL_COLOR);
        this.southPanel.setBackground(PANEL_COLOR);
        this.add(this.notrthPanel,BorderLayout.NORTH);
        this.add(this.southPanel,BorderLayout.SOUTH);
        setPreferredSize(TAKEN_PIECES_DIMENSION);

    }
    public void redo(final Table.MoveLog moveLog){
        this.southPanel.removeAll();
        this.notrthPanel.removeAll();

         List<Piece> whiteTakenPieces = new ArrayList<>();
         List<Piece> blackTakenPieces = new ArrayList<>();

        for(final Move move: moveLog.getMoves()){
                if(move.isAttack()){
                    final Piece takenPiece = move.getAttackedPiece();
                    if(takenPiece.getPieceAliance().isWhite()){
                        whiteTakenPieces.add(takenPiece);
                    }else if(takenPiece.getPieceAliance().isBlack()){
                        blackTakenPieces.add(takenPiece);
                    }else {
                        throw new RuntimeException("should not reach here (in Taken Pieces Panel redo)");
                    }
                }

        }
        Collections.sort(whiteTakenPieces, new Comparator<Piece>() {
            @Override
            public int compare(Piece o1, Piece o2) {
                return Ints.compare(o1.getPieceValue(),o2.getPieceValue());
            }
        });
        Collections.sort(blackTakenPieces, new Comparator<Piece>() {
            @Override
            public int compare(Piece o1, Piece o2) {
                return Ints.compare(o1.getPieceValue(),o2.getPieceValue());
            }
        });

        for(final Piece takenPiece : whiteTakenPieces){
            try{
                final BufferedImage image = ImageIO.read(new File("art/fancy/"
                        +takenPiece.getPieceAliance().toString().substring(0,1)
                        +""+takenPiece.toString() +".png"));
                final ImageIcon ic = new ImageIcon(image);
                final JLabel imageLabel = new JLabel(new ImageIcon(ic.getImage().getScaledInstance(
                        ic.getIconWidth() - 15, ic.getIconWidth() - 15, Image.SCALE_SMOOTH)));
                this.southPanel.add(imageLabel);


            }catch (final IOException e){
                    e.printStackTrace();
            }
        }
        for(final Piece takenPiece : blackTakenPieces){
            try{
                final BufferedImage image = ImageIO.read(new File("art/fancy/"
                        +takenPiece.getPieceAliance().toString().substring(0,1)
                        +""+takenPiece.toString() +".png"));
                final ImageIcon ic = new ImageIcon(image);
                final JLabel imageLabel = new JLabel(new ImageIcon(ic.getImage().getScaledInstance(
                        ic.getIconWidth() - 15, ic.getIconWidth() - 15, Image.SCALE_SMOOTH)));
                this.notrthPanel.add(imageLabel);

            }catch (final IOException e){
                e.printStackTrace();
            }
        }
        validate();


    }


}
