package com.chess.engine.piece;

import com.chess.engine.Aliance;
import com.chess.engine.board.Board;
import com.chess.engine.board.BoardUtils;
import com.chess.engine.board.Move;
import com.chess.engine.board.Tile;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

//tura


public class Rook extends Piece{
    private final static int[] CANDIDATE_MOVE_VECTOR_COORDINATES = {-8,-1,1,8};

     public Rook(final int piecePosition,
                 final Aliance pieceAliance) {
        super(piecePosition, pieceAliance,PieceType.Rook,true);
    }
    public Rook(final int piecePosition,
                final Aliance pieceAliance,
                final boolean isFirstMove) {
        super(piecePosition, pieceAliance,PieceType.Rook,isFirstMove);
    }

    @Override
    public Collection<Move> calculateLegalMoves(final Board board) {
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
        return PieceType.Rook.toString();
    }
    private static boolean isFirstColumnExclusion(final int currentPosition, final int candidateOffset){
        return BoardUtils.FIRST_COLUMN[currentPosition]&& candidateOffset==-1;
    }
    private static boolean isEightColumnExclusion(final int currentPosition, final int candidateOffset){
        return BoardUtils.EIGHT_COLUMN[currentPosition]&& (candidateOffset==1);
    }
    public Rook movePiece(Move move) {
        return new Rook(move.getDestinationCoordonate(),move.getMovedPiece().getPieceAliance());
    }

}
