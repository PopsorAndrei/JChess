package com.chess.engine.piece;

import com.chess.engine.Aliance;
import com.chess.engine.board.Board;
import com.chess.engine.board.Move;
import com.chess.engine.board.Tile;
import com.chess.engine.board.*;
import com.google.common.collect.ImmutableList;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Knight extends Piece {
    //piece position, Alliance
    private final int[] CANDIDATE_MOVE_COORDINATES = {-17,-15,-10,-6,6,10,15,17};

    public Knight(final int position,
                  final Aliance aliance){
        super(position,aliance,PieceType.Knight,true);

    }
    public Knight(final int position,
                  final Aliance aliance,
                  final boolean isFirstMove){
        super(position,aliance,PieceType.Knight,isFirstMove);

    }
    public Collection<Move> calculateLegalMoves(Board board) {
        final List<Move> legalMoves = new ArrayList<>();


        for (final int currentCandidate : CANDIDATE_MOVE_COORDINATES) {
           final int candidateDestinationCoordinate = this.piecePosition + currentCandidate;


            if (BoardUtils.isValidTileCoordinate(candidateDestinationCoordinate)) {
                //is valid location not out of board

                if(isFirstColumnExclusion(this.piecePosition,currentCandidate)
                       ||isSecondColumnExclusion(this.piecePosition,currentCandidate)
                        ||isSeventhColumnExclusion(this.piecePosition,currentCandidate)
                        ||isEightColumnExclusion(this.piecePosition,currentCandidate)){
                    continue;
                }




                final Tile candidateDestinationTile = board.getTile(candidateDestinationCoordinate);

                if (!candidateDestinationTile.isTileOccupied()) {
                    legalMoves.add(new Move.MajorMove(board,this, candidateDestinationCoordinate));
                } else {
                    final Piece pieceAtDestination = candidateDestinationTile.getPiece();
                    final Aliance pieceAliance = pieceAtDestination.getPieceAliance();

                    if (this.pieceAliance != pieceAliance) {// if our piece is not the same type as the piece on destination
                        legalMoves.add(new Move.MajorAttackMove(board,this,candidateDestinationCoordinate,pieceAtDestination));
                    }

                }


            }
        }
        return ImmutableList.copyOf(legalMoves);
    }
    @Override public String toString(){
        return PieceType.Knight.toString();
    }
    private static boolean isFirstColumnExclusion(final int currentPosition, int candidateOffset){
        return BoardUtils.FIRST_COLUMN[currentPosition] && ((candidateOffset == -17)||(candidateOffset == -10)||(candidateOffset == 6)||(currentPosition == 15));
    }
    private static boolean isSecondColumnExclusion(final int currnetPosition, int candidateOffset){
        return BoardUtils.SECOND_COLUMN[currnetPosition]&&((candidateOffset == -10)||(candidateOffset == 6));
    }

    private static boolean isSeventhColumnExclusion(final int currentPosition,int candidateOffset){
        return BoardUtils.SEVENTH_COLUMN[currentPosition]&&((candidateOffset==-6)||(candidateOffset==10));
    }
    private static boolean isEightColumnExclusion(final int currentPosition,int candidateOffset){
        return BoardUtils.EIGHT_COLUMN[currentPosition] &&((candidateOffset==15)||(candidateOffset==-6)||(candidateOffset==10)||(candidateOffset==17));

    }
    public Knight movePiece(Move move) {
        return new Knight(move.getDestinationCoordonate(),move.getMovedPiece().getPieceAliance());
    }

}