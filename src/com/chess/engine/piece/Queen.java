package com.chess.engine.piece;

import com.chess.engine.Aliance;
import com.chess.engine.board.Board;
import com.chess.engine.board.BoardUtils;
import com.chess.engine.board.Move;
import com.chess.engine.board.Tile;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Queen extends Piece {
    public final int CANDIDATE_MOVE_VECTOR_COORDINATES[] ={-9,-8,-7,-1,1,7,8,9};


    public Queen(final int piecePosition,
                 final Aliance pieceAliance) {
        super(piecePosition, pieceAliance,PieceType.Queen,true);
    }
    public Queen(final int piecePosition,
                 final Aliance pieceAliance,
                 final boolean isFirstMove) {
        super(piecePosition, pieceAliance,PieceType.Queen,isFirstMove);
    }


    @Override
    public Collection<Move> calculateLegalMoves(Board board) {
            final List<Move> legalMoves = new ArrayList<>();


        for(final int currentCandidate : CANDIDATE_MOVE_VECTOR_COORDINATES) {
            int candidateDestiantionCoordinate = this.piecePosition;
            while (BoardUtils.isValidTileCoordinate(candidateDestiantionCoordinate)){
                if(isFirstColumnExclusion(candidateDestiantionCoordinate,currentCandidate)||
                        isEightColumnExclusion(candidateDestiantionCoordinate, currentCandidate)){
                    break;
                }
                candidateDestiantionCoordinate +=currentCandidate;
                if(BoardUtils.isValidTileCoordinate(candidateDestiantionCoordinate)){
                final Tile candidateDestinationTile = board.getTile(candidateDestiantionCoordinate);
                if(!candidateDestinationTile.isTileOccupied()){
                    legalMoves.add(new Move.MajorMove(board,this,candidateDestiantionCoordinate));
                }else{
                    final Piece pieceOnTile = candidateDestinationTile.getPiece();
                    final Aliance pieceAliance = pieceOnTile.getPieceAliance();
                    if(this.pieceAliance != pieceAliance){
                        legalMoves.add(new Move.MajorAttackMove(board,this,candidateDestiantionCoordinate,pieceOnTile));

                    }
                    break;
                }
            }
            }
        }
    return List.copyOf(legalMoves);
    }
    @Override public String toString(){
        return PieceType.Queen.toString();
    }

    private static boolean isFirstColumnExclusion(final int currentPosition, final int candidateOffset){
        return BoardUtils.FIRST_COLUMN[currentPosition]&& (candidateOffset==-9||candidateOffset ==7||candidateOffset==-1);
    }
    private static boolean isEightColumnExclusion(final int currentPosition, final int candidateOffset){
        return BoardUtils.EIGHT_COLUMN[currentPosition]&& (candidateOffset==-7||candidateOffset ==9||candidateOffset==1);
    }
    @Override
    public Queen movePiece(Move move) {
        return new Queen(move.getDestinationCoordonate(),move.getMovedPiece().getPieceAliance());
    }
}
