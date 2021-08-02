package com.chess.engine.piece;

import com.chess.engine.Aliance;
import com.chess.engine.board.Board;
import com.chess.engine.board.BoardUtils;
import com.chess.engine.board.Move;
import com.chess.engine.board.Tile;
import com.chess.engine.player.MoveTransition;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Bishop extends Piece{

    private final static int[] CANDIDATE_MOVE_VECTOR_COORDINATES = {-9,-7,7,9};

    public Bishop(int piecePosition,
                  Aliance pieceAliance) {
        super(piecePosition, pieceAliance,PieceType.Bishop,true);
    }
    public Bishop(int piecePostion,
                  Aliance pieceAliance,
                  boolean isFirstMove){
        super(piecePostion,pieceAliance,PieceType.Bishop,isFirstMove);
    }

    @Override
    public Collection<Move> calculateLegalMoves(final Board board) {

        final List<Move> legalMoves = new ArrayList<>();

        for(final int candidateCoordinateOffset : CANDIDATE_MOVE_VECTOR_COORDINATES){

            int candidateDestiantionCoordinate = this.piecePosition;
            while(BoardUtils.isValidTileCoordinate(candidateDestiantionCoordinate)){
                if(isFirstColumnExclusion(candidateDestiantionCoordinate, candidateCoordinateOffset)
                        ||isEightColumnExclusion(candidateDestiantionCoordinate,candidateCoordinateOffset)){
                    break;
                }
                candidateDestiantionCoordinate += candidateCoordinateOffset;
                if(BoardUtils.isValidTileCoordinate(candidateDestiantionCoordinate)){
                    final Tile candidateDestinationTile = board.getTile(candidateDestiantionCoordinate);
                    if(!candidateDestinationTile.isTileOccupied()){

                        legalMoves.add(new Move.MajorMove(board,this,candidateDestiantionCoordinate));

                    }else{
                        final Piece pieceAtDestination = candidateDestinationTile.getPiece();
                        final Aliance pieceAliance = pieceAtDestination.getPieceAliance();
                        if(this.pieceAliance!=pieceAliance){
                            legalMoves.add(new Move.MajorAttackMove(board,this,candidateDestiantionCoordinate,pieceAtDestination));
                        }
                        break;
                    }
                }
            }
        }
        return List.copyOf(legalMoves);
    }
    @Override public String toString(){
        return PieceType.Bishop.toString();
    }

    private static boolean isFirstColumnExclusion(final int currentPosition, final int candidateOffset){
        return BoardUtils.FIRST_COLUMN[currentPosition]&& (candidateOffset==-9||candidateOffset ==7);
    }
    private static boolean isEightColumnExclusion(final int currentPosition, final int candidateOffset){
        return BoardUtils.EIGHT_COLUMN[currentPosition]&& (candidateOffset==-7||candidateOffset ==9);
    }
    public Bishop movePiece(Move move) {
        return new Bishop(move.getDestinationCoordonate(),move.getMovedPiece().getPieceAliance(),false);
    }

}
