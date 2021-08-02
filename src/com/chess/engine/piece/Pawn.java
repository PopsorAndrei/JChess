package com.chess.engine.piece;

import com.chess.engine.Aliance;
import com.chess.engine.board.Board;
import com.chess.engine.board.BoardUtils;
import com.chess.engine.board.Move;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Pawn extends Piece {
    private final static int[] CANDIDATE_MOVE_COORDINATE = {7,8,9,16};

    public Pawn(final int piecePosition,
                final Aliance pieceAliance) {
        super(piecePosition, pieceAliance,PieceType.Pawn,true);
    }
    public Pawn(final int piecePosition,
                final Aliance pieceAliance,
                final boolean isFirstMove){
        super(piecePosition,pieceAliance,PieceType.Pawn,isFirstMove);

    }

    @Override
    public Collection<Move> calculateLegalMoves(Board board) {


        final List<Move> legalMoves = new ArrayList<>();

        for (final int currentCandidateOffset : CANDIDATE_MOVE_COORDINATE) {
            final int candidateDestinationCoordinate = this.piecePosition + (this.getPieceAliance().getDirection() * currentCandidateOffset);

            if (!BoardUtils.isValidTileCoordinate(candidateDestinationCoordinate)) {
                continue;
            }

            if (currentCandidateOffset == 8 && !board.getTile(candidateDestinationCoordinate).isTileOccupied()) {

                if(this.pieceAliance.isPawnPromotionSquare(candidateDestinationCoordinate)){
                    legalMoves.add(new Move.PawnPromotion(new Move.PawnMove(board,this,candidateDestinationCoordinate)));
                }else{
                    legalMoves.add(new Move.PawnMove(board, this, candidateDestinationCoordinate));
                }
            } else if (currentCandidateOffset == 16 && this.isFirstMove &&
                    ((BoardUtils.SEVENTH_RANK[this.piecePosition] && this.getPieceAliance().isBlack())
                    || (BoardUtils.SECOND_RANK[this.piecePosition] && this.getPieceAliance().isWhite()))) {
                final int behindDestinationCoordoante = this.piecePosition + (this.pieceAliance.getDirection()*8);
                if(!board.getTile(behindDestinationCoordoante).isTileOccupied()
                        && !board.getTile(candidateDestinationCoordinate).isTileOccupied()){

                    legalMoves.add(new Move.PawnJump(board, this, candidateDestinationCoordinate));
                }
            }else if(currentCandidateOffset==7 &&
                    !((BoardUtils.EIGHT_COLUMN[this.piecePosition]&& this.pieceAliance.isWhite()) ||
                    (BoardUtils.FIRST_COLUMN[this.piecePosition]&& this.pieceAliance.isBlack()))){
                    if(board.getTile(candidateDestinationCoordinate).isTileOccupied()){
                        final Piece pieceOnCandidate = board.getTile(candidateDestinationCoordinate).getPiece();
                        if(this.pieceAliance !=pieceOnCandidate.getPieceAliance()){
                            if(this.pieceAliance.isPawnPromotionSquare(candidateDestinationCoordinate)){
                                legalMoves.add(new Move.PawnPromotion(new Move.PawnAttackMove(board,this,candidateDestinationCoordinate,pieceOnCandidate)));
                            }else {
                                legalMoves.add(new Move.PawnAttackMove(board, this, candidateDestinationCoordinate, pieceOnCandidate));
                            }
                        }
                    }else if(board.getEnPassantPawn() != null){
                        if(board.getEnPassantPawn().getPiecePosition() == (this.piecePosition+ this.pieceAliance.getOppositeDirection())){
                            final Piece pieceOnCandidate = board.getEnPassantPawn();
                            if(pieceOnCandidate.getPieceAliance() != this.pieceAliance){
                                legalMoves.add(new Move.PawnEnPassantAttackMove(board,this,pieceOnCandidate.getPiecePosition()+pieceOnCandidate.getPieceAliance().getOppositeDirection()*8,pieceOnCandidate));
                            }
                        }
                    }




            }else if(currentCandidateOffset==9 &&
                    !((BoardUtils.FIRST_COLUMN[this.piecePosition]&& this.pieceAliance.isWhite()) ||
                            (BoardUtils.EIGHT_COLUMN[this.piecePosition]&& this.pieceAliance.isBlack()))){
                if(board.getTile(candidateDestinationCoordinate).isTileOccupied()){
                    final Piece pieceOnCandidate = board.getTile(candidateDestinationCoordinate).getPiece();
                    if(this.pieceAliance !=pieceOnCandidate.getPieceAliance()){
                        if(this.pieceAliance.isPawnPromotionSquare(candidateDestinationCoordinate)){
                            legalMoves.add(new Move.PawnPromotion(new Move.PawnAttackMove(board,this,candidateDestinationCoordinate,pieceOnCandidate)));
                        }else {
                            legalMoves.add(new Move.PawnAttackMove(board, this, candidateDestinationCoordinate, pieceOnCandidate));
                        }
                    }
                }


        }else if(board.getEnPassantPawn() != null){
                if(board.getEnPassantPawn().getPiecePosition() == (this.piecePosition - this.pieceAliance.getOppositeDirection())){
                    final Piece pieceOnCandidate = board.getEnPassantPawn();
                    if(pieceOnCandidate.getPieceAliance() != this.pieceAliance){
                        legalMoves.add(new Move.PawnEnPassantAttackMove(board,this,pieceOnCandidate.getPiecePosition()+pieceOnCandidate.getPieceAliance().getOppositeDirection()*8,pieceOnCandidate));
                    }
                }
            }
        }
        return List.copyOf(legalMoves);
}

    @Override
    public Pawn movePiece(Move move) {

        return new Pawn(move.getDestinationCoordonate(),move.getMovedPiece().getPieceAliance(),false);

    }

    @Override public String toString(){
        return PieceType.Pawn.toString();
    }

    public Piece getPromotionPiece(){
        return new Queen(this.piecePosition,this.pieceAliance,false);
    }
}