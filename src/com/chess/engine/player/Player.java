package com.chess.engine.player;

import com.chess.engine.Aliance;
import com.chess.engine.board.Board;
import com.chess.engine.board.Move;
import com.chess.engine.piece.King;
import com.chess.engine.piece.Piece;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;

import java.text.CollationElementIterator;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public abstract class Player {
    protected final Board board;
    protected final King playerKing;
    protected final Collection<Move> legalMoves;
    private final boolean isInCheck;


    Player(final Board board,
           final Collection<Move> legalMoves,
           final Collection<Move> opponentLegalMoves){

        this.board = board;
        this.playerKing = establishKing();
        this.legalMoves = ImmutableList.copyOf(Iterables.concat(legalMoves,calulateKingCastle(legalMoves,opponentLegalMoves)));
        this.isInCheck = !Player.calculateAttacksOnTile(this.playerKing.getPiecePosition(),opponentLegalMoves).isEmpty();

    }
    public King getPlayerKing(){
        return this.playerKing;
    }
    public Collection<Move> getLegalMoves(){
        return this.legalMoves;
    }

    protected static Collection<Move> calculateAttacksOnTile(int piecePosition, Collection<Move> moves) {
        final List<Move> attackMove = new ArrayList<>();
        for(final Move move :moves){
            if(piecePosition == move.getDestinationCoordonate()){
                attackMove.add(move);
            }
        }
        return List.copyOf(attackMove);
    }


    private  King establishKing() {
        for(final Piece piece : getActivePieces()){
            if(piece.getPieceType().isKing()) {

                return (King) piece;

            }
        }
        throw new RuntimeException("should not reach here");
    }
    public boolean isMoveLegal(final Move move){
       return this.legalMoves.contains(move);
    }

    public boolean isInCheck(){
        return this.isInCheck;
    }
    public boolean isInCheckMate() {
        return this.isInCheck && !hasEscapeMoves();
    }
    public boolean isInStaleMate(){
        return !this.isInCheck && !hasEscapeMoves();
    }


    protected  boolean hasEscapeMoves(){
        for(final Move move : legalMoves){
            final MoveTransition transition = makeMove(move);
            if(transition.getMoveStatus().isDone())
                return true;
        }
        return false;
    }

    public boolean isCastled(){
        return false;
    }

    public MoveTransition makeMove(final Move move){
     if(!isMoveLegal(move)){
         return new MoveTransition(this.board,move,MoveStatus.ILLEGAL_MOVE);
     }
        // AICI AM FACUT MODIFICARI SA NU POATA MUTA IN SAH

     final Board transitionBoard = move.execute();

     final Collection<Move> kingAttacks = calculateAttacksOnTile(transitionBoard.currentPlayer().getOpponent().getPlayerKing().getPiecePosition(),
             transitionBoard.currentPlayer().getLegalMoves());
     if(!kingAttacks.isEmpty()){
        return new MoveTransition(transitionBoard,move,MoveStatus.LEAVES_PLAYER_IN_CHECK);
     }
        return new MoveTransition(transitionBoard,move,MoveStatus.DONE);

    }
    public boolean isKingSideCastleCapable(){
        return this.playerKing.isKingSideCastleCapable();
    }

    public boolean isQueenSideCastleCapable(){
        return this.playerKing.isQueenSideCastleCapable();
    }

    public  abstract Collection<Piece> getActivePieces();
    public abstract Aliance getAliance();
    public abstract Player getOpponent();
    protected abstract Collection<Move> calulateKingCastle(Collection<Move> playerLegals, Collection<Move> opponentsLegals);

//what is here is utterly useless

}
