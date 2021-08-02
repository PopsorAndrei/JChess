package com.chess.engine.board;

import com.chess.engine.Aliance;
import com.chess.engine.piece.*;
import com.chess.engine.player.BlackPlayer;
import com.chess.engine.player.Player;
import com.chess.engine.player.WhitePlayer;

import java.text.CollationElementIterator;
import java.util.*;

public class Board {
    private final List<Tile> gameBoard;
    private final Collection<Piece> whitePieces;
    private final Collection<Piece> blackPieces;
    private final WhitePlayer whitePlayer;
    private final BlackPlayer blackPlayer;
    private final Player currentPLayer;

    private final Pawn enPassantPawn;



    private Board(final Builder builder){

        this.gameBoard = createGameboard(builder);

        this.whitePieces = calculateActivePieces(this.gameBoard,Aliance.WHITE);
        this.blackPieces = calculateActivePieces(this.gameBoard,Aliance.BLACK);
        this.enPassantPawn = builder.enPassantPawn;

        final Collection<Move> whiteStandardLegalMove = calculateLegalMoves(this.whitePieces);
        final Collection<Move> blackStandardLegalMove = calculateLegalMoves(this.blackPieces);

        this.whitePlayer = new WhitePlayer(this, whiteStandardLegalMove,blackStandardLegalMove);
        this.blackPlayer = new BlackPlayer(this,whiteStandardLegalMove,blackStandardLegalMove);

        this.currentPLayer = builder.nextMoveMaker.choosePlayer(this.whitePlayer,this.blackPlayer);

    }
    @Override
    public String toString(){
        final StringBuilder builder = new StringBuilder();
        for(int i = 0;i< BoardUtils.NUM_TILES;i++){
            final String tileText = this.gameBoard.get(i).toString();
            builder.append(String.format("%3s",tileText));
            if((i+1)% BoardUtils.NUM_TITLES_PER_ROW ==0){
                builder.append("\n");
            }
        }
                return builder.toString();
    }
    public Player whitePlayer(){
        return this.whitePlayer;
    }
    public Player blackPlayer(){
        return this.blackPlayer;
    }
    public Player currentPlayer() {
        return this.currentPLayer;
    }
    public Collection<Piece> getBlackPieces(){
        return this.blackPieces;
    }
    public Collection<Piece> getWhitePieces(){
        return this.whitePieces;
    }

    public Pawn getEnPassantPawn(){
        return this.enPassantPawn;
    }

    private Collection<Move> calculateLegalMoves(Collection<Piece> pieces) {
        final List<Move> legalMoves = new ArrayList<>();

        for(final Piece piece : pieces){
            legalMoves.addAll(piece.calculateLegalMoves(this));
        }

        return List.copyOf(legalMoves);
    }

    public Tile getTile(final int tileCoordinate){
        return gameBoard.get(tileCoordinate);
    }
    private static List<Tile> createGameboard(final Builder builder){
        final Tile[] tiles = new Tile[BoardUtils.NUM_TILES];
        for (int i = 0; i<BoardUtils.NUM_TILES; i++){
            tiles[i] = Tile.createTile(i,builder.boardConfiguration.get(i));
        }

        //de aci pan la uramatoru comentariu
     List<Tile> finalList =  new ArrayList<>();
        for( int j = 0;j<BoardUtils.NUM_TILES;j++){
            finalList.add(tiles[j]);
        }
        //diferite de tutorial ca nu ai libaratia pulii
        return List.copyOf(finalList);
    }

    public static Board createStandardBoard(){
        final Builder builder = new Builder();


        //black Setup
        builder.setPiece(new Rook(0,Aliance.BLACK,true));
        builder.setPiece(new Knight(1,Aliance.BLACK,true));
        builder.setPiece(new Bishop(2,Aliance.BLACK,true));
        builder.setPiece(new Queen(3,Aliance.BLACK,true));
        builder.setPiece(new King(4,Aliance.BLACK,true,true));
        builder.setPiece(new Bishop(5,Aliance.BLACK,true));
        builder.setPiece(new Knight(6,Aliance.BLACK,true));
        builder.setPiece(new Rook(7,Aliance.BLACK,true));
        builder.setPiece(new Pawn(8,Aliance.BLACK,true));
        builder.setPiece(new Pawn(9,Aliance.BLACK,true));
        builder.setPiece(new Pawn(10,Aliance.BLACK,true));
        builder.setPiece(new Pawn(11,Aliance.BLACK,true));
        builder.setPiece(new Pawn(12,Aliance.BLACK,true));
        builder.setPiece(new Pawn(13,Aliance.BLACK,true));
        builder.setPiece(new Pawn(14,Aliance.BLACK,true));
        builder.setPiece(new Pawn(15,Aliance.BLACK,true));


        // White Setup

        builder.setPiece(new Pawn(48,Aliance.WHITE));
        builder.setPiece(new Pawn(49,Aliance.WHITE));
        builder.setPiece(new Pawn(50,Aliance.WHITE));
        builder.setPiece(new Pawn(51,Aliance.WHITE));
        builder.setPiece(new Pawn(52,Aliance.WHITE));
        builder.setPiece(new Pawn(53,Aliance.WHITE));
        builder.setPiece(new Pawn(54,Aliance.WHITE));
        builder.setPiece(new Pawn(55,Aliance.WHITE));
        builder.setPiece(new Rook(56,Aliance.WHITE));
        builder.setPiece(new Knight(57,Aliance.WHITE));//form 57
        builder.setPiece(new Bishop(58,Aliance.WHITE));
        builder.setPiece(new Queen(59,Aliance.WHITE));
        builder.setPiece(new King(60,Aliance.WHITE,true,true));
        builder.setPiece(new Bishop(61,Aliance.WHITE));
        builder.setPiece(new Knight(62,Aliance.WHITE));
        builder.setPiece(new Rook(63,Aliance.WHITE,true));

        builder.setMoveMaker(Aliance.WHITE);


        return builder.build();
    }


    public static Board createStandardBoard2(){

        final Builder builder = new Builder();
        builder.setPiece(new Rook(7,Aliance.BLACK,true));
        builder.setPiece(new Pawn(8,Aliance.BLACK,true));
        builder.setPiece(new Pawn(9,Aliance.BLACK,true));
        builder.setPiece(new Pawn(10,Aliance.BLACK,true));
        builder.setPiece(new Pawn(11,Aliance.BLACK,true));
        builder.setPiece(new Pawn(12,Aliance.BLACK,true));
        builder.setPiece(new Pawn(13,Aliance.BLACK,true));
        builder.setPiece(new Pawn(14,Aliance.BLACK,true));
        builder.setPiece(new Pawn(15,Aliance.BLACK,true));
        builder.setPiece(new King(4,Aliance.BLACK,true,true));
        builder.setPiece(new Rook(0,Aliance.BLACK,true));
        builder.setPiece(new Rook(7,Aliance.BLACK,true));


        builder.setPiece(new Knight(40,Aliance.WHITE));

        builder.setPiece(new Pawn(48,Aliance.WHITE));
        builder.setPiece(new Pawn(49,Aliance.WHITE));
        builder.setPiece(new Pawn(50,Aliance.WHITE));
        builder.setPiece(new Pawn(51,Aliance.WHITE));
        builder.setPiece(new Pawn(52,Aliance.WHITE));
        builder.setPiece(new Pawn(53,Aliance.WHITE));
        builder.setPiece(new Pawn(54,Aliance.WHITE));
        builder.setPiece(new Pawn(55,Aliance.WHITE));
        builder.setPiece(new Rook(56,Aliance.WHITE,true));
        builder.setPiece(new Rook(63,Aliance.WHITE,true));
        builder.setPiece(new King(60,Aliance.WHITE,true,true));


        builder.setMoveMaker(Aliance.WHITE);

        return builder.build();
    }

    private static Collection<Piece> calculateActivePieces(List<Tile> gameBoard,Aliance aliance){
        List<Piece> activePieces = new ArrayList<>();
        for(Tile tile:gameBoard){
            if(tile.isTileOccupied()){
             final Piece piece = tile.getPiece();
             if(piece.getPieceAliance()==aliance){
                 activePieces.add(piece);
                }
            }
        }
        return List.copyOf(activePieces);
    }
//this method IS NOT THE SAME AS THE VIDEO
    public List<Move> getAllLegalMoves() {
        List<Move> allLegalMoves = new ArrayList<>();

        for(final Move move : whitePlayer.getLegalMoves()){
            allLegalMoves.add(move);
        }
        for(final Move move : blackPlayer.getLegalMoves()){
            allLegalMoves.add(move);
        }

        return List.copyOf(allLegalMoves);
    }

    public Piece getPiece(int tileId) {
        return this.getTile(tileId).getPiece();
    }


    public static class Builder{

        Map<Integer, Piece> boardConfiguration;
        Aliance nextMoveMaker;
        Pawn enPassantPawn;
        public Builder(){
            this.boardConfiguration = new HashMap<>();
        }
        public Builder setPiece(final Piece piece){
            this.boardConfiguration.put(piece.getPiecePosition(), piece);
             return this;
        }
        public Builder setMoveMaker(final Aliance nextMoveMaker){
            this.nextMoveMaker = nextMoveMaker;
            return this;
        }




        public Board build(){
            return new Board(this);
        }


        public void setEnPassantPawn(Pawn enPassantPawn) {
            this.enPassantPawn = enPassantPawn;
        }
    }
}
