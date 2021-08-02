package com.chess.gui;

import com.chess.engine.board.Board;
import com.chess.engine.board.Move;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;


public class GameHistoryPanel extends JPanel {

    private static final Dimension HISTORY_PANNEL_DIMENSION = new Dimension(100,400);

    private final DataModel model;
    private final JScrollPane scrollPanel;

    GameHistoryPanel(){
        this.setLayout(new BorderLayout());
        this.model = new DataModel();
        final JTable table = new JTable(model);
        table.setRowHeight(15);
        this.scrollPanel = new JScrollPane(table);
        this.scrollPanel.setPreferredSize(HISTORY_PANNEL_DIMENSION);
        scrollPanel.setColumnHeaderView(table.getTableHeader());
        this.add(scrollPanel, BorderLayout.CENTER);
        this.setVisible(true);


    }
    void redo(final Board board,
              final Table.MoveLog moveHistory){


        int currentRow = 0;
        this.model.clear();
        for(final Move move :moveHistory.getMoves()){
            final String moveText = move.toString();
            if(move.getMovedPiece().getPieceAliance().isWhite()){
                this.model.setValueAt(moveText,currentRow,0);
            }
            if(move.getMovedPiece().getPieceAliance().isBlack()){
                this.model.setValueAt(moveText,currentRow,1);
                currentRow++;
            }
        }
        if(moveHistory.getMoves().size() >0){
            final Move lastMove = moveHistory.getMoves().get(moveHistory.size()-1);
            final String moveText = lastMove.toString();

            if(lastMove.getMovedPiece().getPieceAliance().isWhite()){
                this.model.setValueAt(moveText + calculateCheckandCheckMate(board),currentRow,0);
            }else if(lastMove.getMovedPiece().getPieceAliance().isBlack()){
                this.model.setValueAt(moveText +calculateCheckandCheckMate(board),currentRow-1,1);
            }
        }
        final JScrollBar vertical = scrollPanel.getVerticalScrollBar();
        vertical.setValue(vertical.getMaximum());

    }

    private String calculateCheckandCheckMate(final Board board) {
    if(board.currentPlayer().isInCheckMate()){

        String winningMessage;
        if(board.currentPlayer().getAliance().toString().equals("B")){
            winningMessage = "White Player won!";
        }else {
            winningMessage = "Black Player won!";
        }

        //new CheckMateMessage(winningMessage);
        return"#";

    }else if(board.currentPlayer().isInCheck()){
        return "+";
    }else
        return "";

    }


    private static class DataModel extends DefaultTableModel{

        private final List<Row> values;
        private static final String[] names ={"WHITE","BLACK"};

        DataModel(){
            this.values = new ArrayList<>();
        }

        public void clear(){
            this.values.clear();
            setRowCount(0);
        }
        @Override
        public int getRowCount(){
            if(this.values==null){
                return 0;
            }
            return this.values.size();
        }

        @Override
        public int getColumnCount(){
            return this.names.length;
        }

        @Override
        public Object getValueAt(final int row,final int col){
            final Row currentRow = this.values.get(row);

            if(col==0){
                return currentRow.getWhiteMove();
            }else if(col==1){
                return currentRow.getBlackMove();
            }
                return null;
        }

        @Override
        public void setValueAt(final Object aValue,
                               final int row,
                               final int col){

           final Row currentRow;
           if(this.values.size() <= row){
               currentRow = new Row();
               this.values.add(currentRow);
           }else{
               currentRow = this.values.get(row);
           }
           if(col==0){
               currentRow.setWhiteMove((String) aValue);
               fireTableRowsInserted(row,row);
           }else if(col == 1){
               currentRow.setBlackMove((String) aValue);
               fireTableCellUpdated(row,col);
           }


        }

        @Override
        public Class<?> getColumnClass(final int column){
            return Move.class;
        }

        @Override
        public String getColumnName(final int column){
            return names[column];
        }



    }

    private static class Row{

        private String whiteMove;
        private String blackMove;
        Row(){

        }

        public String getWhiteMove(){
            return this.whiteMove;
        }
        public String getBlackMove(){
            return this.blackMove;
        }

        public void setWhiteMove(String whiteMove){
            this.whiteMove = whiteMove;
        }

        public void setBlackMove(String blackMove){
            this.blackMove = blackMove;
        }
    }

}
