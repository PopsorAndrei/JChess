package com.chess;

import com.chess.engine.board.Board;
import com.chess.engine.board.Move;
import com.chess.engine.piece.Queen;
import com.chess.engine.player.WhitePlayer;
import com.chess.gui.CheckMateMessage;
import com.chess.gui.Menu;
import com.chess.gui.MenuPanel;
import com.chess.gui.Table;
import org.w3c.dom.ls.LSOutput;

import java.util.ArrayList;
import java.util.List;

public class JChess {

    public static String GLOBALLIST = "";

    public static void main(String[] args) {
        //Board board = Board.createStandardBoard2();
        // System.out.println(board.toString());
        //     System.out.println();
        //   System.out.println("plm");


        Table.get().show(); //LINIA ASTA II baza


       //  MenuPanel menu = new MenuPanel();
       // new CheckMateMessage("plm");



        

    }

    public  String WordChain(){

        return "plm";
    }

public static String transformToChess(int coordinate){

        if(coordinate ==64){
            return "H8";
        }


        Integer row = 0;
        if(coordinate <=8){
            row = coordinate;
        }else{
        row =coordinate/8 +1;
        }
        char col ='A';
        int catPlusCol = coordinate%8;
        for(int i =1;i<catPlusCol;i++){
            col++;
        }
     return col+row.toString();

}
public void printBoardMaiProst(Board board){
    char col = 'A';
    int row = 8;
    System.out.print(" ");
    for(int j= 0;j<8;j++){

        System.out.print(col);
        col++;
    }
    System.out.println();
    System.out.print(row--);
    for(int i = 1;i<64;i++){

        System.out.print(board.getTile(i-1));
        if(i%8==0){
            System.out.println("");
            System.out.print(row--);
        }
    }
    System.out.print(board.getTile(63).toString());
    System.out.println("");
    for(Move move : board.getAllLegalMoves()){
        // System.out.println( move.getMovedPiece().getPieceAliance()+" "+move.getMovedPiece().getPieceType()+" "+transformToChess(move.getCurrentCoordinate())+ " to " +transformToChess(move.getDestinationCoordonate()) );
        System.out.println( move.getMovedPiece().getPieceAliance()+" "+move.getMovedPiece().getPieceType()+" "+move.getCurrentCoordinate()+ " to "
                +move.getDestinationCoordonate() );



        System.out.println(move.getCurrentCoordinate()- move.getDestinationCoordonate());
    }



}
}









