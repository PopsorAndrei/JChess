package com.chess.engine.piece;

import com.chess.engine.Aliance;
import com.chess.engine.board.Board;
import com.chess.engine.board.BoardUtils;
import com.chess.engine.board.Move;
import com.chess.engine.board.Tile;
import com.chess.engine.player.Player;

import java.text.CollationElementIterator;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class King extends Piece {

    private final static int[] CANDIDATE_MOVE_COORDINATE = {-1,-7,-8,-9,1,7,8,9};

    private final boolean isCastled;
    private final boolean isKingSideCastleCapable;
    public final boolean isQueenSideCastleCapable;


    public King(final int piecePosition,
                final Aliance pieceAliance,
                final boolean isKingSideCastleCapable,
                final boolean isQueenSideCastleCapable) {
        super(piecePosition, pieceAliance,PieceType.King,true);
        this.isCastled = false;
        this.isKingSideCastleCapable = isKingSideCastleCapable;
        this.isQueenSideCastleCapable = isQueenSideCastleCapable;
    }
    public King(final int piecePosition,
                final Aliance pieceAliance,
                boolean isFirstMove,
                boolean isCastled,
                final boolean isKingSideCastleCapable,
                final boolean isQueenSideCastleCapable) {
        super(piecePosition, pieceAliance,PieceType.King,isFirstMove);
        this.isCastled = isCastled;
        this.isKingSideCastleCapable = isKingSideCastleCapable;
        this.isQueenSideCastleCapable = isQueenSideCastleCapable;
    }
    public boolean isKingSideCastleCapable(){
        return this.isKingSideCastleCapable;
    }

    public boolean isQueenSideCastleCapable(){
        return this.isQueenSideCastleCapable;
    }

    public boolean isCastled(){
        return this.isCastled;
    }


    @Override
    public Collection<Move> calculateLegalMoves(Board board) {
        final List<Move> legalMoves = new ArrayList<>();


        for(final int currentCandidateOffSet : CANDIDATE_MOVE_COORDINATE){
            if(isFirstColumnExclusion(this.piecePosition,currentCandidateOffSet)||
                isEightColumnExclusion(this.piecePosition,currentCandidateOffSet)){
                continue;
            }

            final int candidateDestinationCoordinate = this.piecePosition + currentCandidateOffSet;
            if(BoardUtils.isValidTileCoordinate(candidateDestinationCoordinate)){
                final Tile candidateDestinationTile = board.getTile(candidateDestinationCoordinate);
                if(!candidateDestinationTile.isTileOccupied()){
                    legalMoves.add(new Move.MajorMove(board,this,candidateDestinationCoordinate));
                }else {
                    final Piece pieceAtDestination = candidateDestinationTile.getPiece();
                    final Aliance pieceAliance = pieceAtDestination.getPieceAliance();

                    if (this.pieceAliance != pieceAliance) {// if our piece is not the same time as the piece on destination
                        legalMoves.add(new Move.MajorAttackMove(board,this,candidateDestinationCoordinate,pieceAtDestination));
                    }
            }
        }
        }

        return List.copyOf(legalMoves);

    }
    @Override public String toString(){
        return PieceType.King.toString();
    }
    private static boolean isFirstColumnExclusion(final int currentPosition, int candidateOffset){
        return BoardUtils.FIRST_COLUMN[currentPosition] && ((candidateOffset == -9)||(candidateOffset == -1)||(candidateOffset == 7));
    }
    private static boolean isEightColumnExclusion(final int currentPosition, int candidateOffset){
        return BoardUtils.EIGHT_COLUMN[currentPosition]&&((candidateOffset == -7)||(candidateOffset == 1)||(candidateOffset == 9));
    }
    public King movePiece(Move move) {
        return new King(move.getDestinationCoordonate(),move.getMovedPiece().getPieceAliance(),false,move.isCastlingMove(),false,false);
    }
}
