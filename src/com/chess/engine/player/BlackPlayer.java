package com.chess.engine.player;

import com.chess.engine.Aliance;
import com.chess.engine.board.Board;
import com.chess.engine.board.Move;
import com.chess.engine.board.Tile;
import com.chess.engine.piece.Piece;
import com.chess.engine.piece.Rook;
import com.google.common.collect.ImmutableList;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class BlackPlayer extends Player {
    public BlackPlayer(final Board board,
                       final Collection<Move> whiteStandardLegalMove,
                       final Collection<Move> blackStandardLegalMove) {
        super(board,blackStandardLegalMove,whiteStandardLegalMove);

    }

    @Override
    public  Collection<Piece> getActivePieces() {
            return this.board.getBlackPieces();
    }

    @Override
    public Aliance getAliance() {
        return Aliance.BLACK;
    }

    @Override
    public Player getOpponent() {
        return this.board.whitePlayer();
    }

    @Override
    protected Collection<Move> calulateKingCastle(final Collection<Move> playerLegals, final Collection<Move> opponentsLegals) {

        final List<Move> kingCastle = new ArrayList<>();

        for(Move move :opponentsLegals){
            if(move.getDestinationCoordonate()==this.playerKing.getPiecePosition()){
                return ImmutableList.copyOf(kingCastle);
            }
        }


        if(this.playerKing.isFirstMove() && !isInCheck()){
            if (!this.board.getTile(5).isTileOccupied()&& !this.board.getTile(6).isTileOccupied()){
                final Tile rookTile = this.board.getTile(7);
                if(rookTile.isTileOccupied() && rookTile.getPiece().isFirstMove()){
                    if(     Player.calculateAttacksOnTile(5,opponentsLegals).isEmpty() &&
                            Player.calculateAttacksOnTile(6,opponentsLegals).isEmpty() &&
                            rookTile.getPiece().getPieceType().isRook()){


                        // TODO
                        kingCastle.add(new Move.CastleMove.KingSideCastleMove(this.board,
                                                                            this.playerKing,
                                                                            6,
                                                                            (Rook) rookTile.getPiece(),
                                                                            rookTile.getPiece().getPiecePosition(),
                                                                            5));}
                }
            }
            if(!this.board.getTile(1).isTileOccupied()&& !this.board.getTile(2).isTileOccupied()&&!this.board.getTile(3).isTileOccupied()){
                final Tile rookTile = this.board.getTile(0);
                if(rookTile.isTileOccupied() && rookTile.getPiece().isFirstMove()&&
                Player.calculateAttacksOnTile(2,opponentsLegals).isEmpty()&&
                Player.calculateAttacksOnTile(3,opponentsLegals).isEmpty()&&
                rookTile.getPiece().getPieceType().isRook()){
                    // TODO

                    kingCastle.add(new Move.CastleMove.QueenSideCastleMove(this.board,
                                                                        this.playerKing,
                                                                        2,
                                                                        (Rook) rookTile.getPiece(),
                                                                        rookTile.getPiece().getPiecePosition(),
                                                                        3));
                }
            }
        }

        return ImmutableList.copyOf(kingCastle);
    }
}
