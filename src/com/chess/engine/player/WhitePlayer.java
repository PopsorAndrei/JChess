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

public class WhitePlayer extends Player{
    public WhitePlayer(final Board board,
                       final Collection<Move> whiteStandardLegalMove,
                       final Collection<Move> blackStandardLegalMove) {
        super(board, whiteStandardLegalMove,blackStandardLegalMove);
    }

    @Override
    public Collection<Piece> getActivePieces() {
        return this.board.getWhitePieces();
    }

    @Override
    public Aliance getAliance() {
        return Aliance.WHITE;
    }

    @Override
    public Player getOpponent() {
        return this.board.blackPlayer();
    }

    @Override
    protected Collection<Move> calulateKingCastle(final Collection<Move> playerLegals,final  Collection<Move> opponentsLegals) {
        final List<Move> kingCastle = new ArrayList<>();



        for(Move move :opponentsLegals){
            if(move.getDestinationCoordonate()==this.playerKing.getPiecePosition()){
                return ImmutableList.copyOf(kingCastle);
            }
        }
        if(this.playerKing.isFirstMove() && !isInCheck()){
            if (!this.board.getTile(61).isTileOccupied()&& !this.board.getTile(62).isTileOccupied()){
                    final Tile rookTile = this.board.getTile(63);
                    if(rookTile.isTileOccupied() && rookTile.getPiece().isFirstMove()){
                       if(Player.calculateAttacksOnTile(61,opponentsLegals).isEmpty() &&
                          Player.calculateAttacksOnTile(62,opponentsLegals).isEmpty() &&
                          rookTile.getPiece().getPieceType().isRook())
                           System.out.println("WHITE PLAYER CASTLE");

                        // TODO
                        kingCastle.add(new Move.CastleMove.KingSideCastleMove(board,
                                                                              this.playerKing,
                                                             62,
                                                                              (Rook) rookTile.getPiece(),
                                                                              rookTile.getPiece().getPiecePosition(),  // \/aici ii diferit de tutorial acolo era rookTile.getTileCoordinate si mie lene sa schimb acu
                                                             61));
                    }
            }
           if(!this.board.getTile(59).isTileOccupied()&& !this.board.getTile(58).isTileOccupied()&&!this.board.getTile(57).isTileOccupied()){
            final Tile rookTile = this.board.getTile(56);
            if(rookTile.isTileOccupied() && rookTile.getPiece().isFirstMove()&&
               Player.calculateAttacksOnTile(58,opponentsLegals).isEmpty()&&
               Player.calculateAttacksOnTile(59,opponentsLegals).isEmpty()&&
               rookTile.getPiece().getPieceType().isRook()){
                // TODO
                kingCastle.add(new Move.CastleMove.QueenSideCastleMove( board,
                                                                        this.playerKing,
                                                                        58,
                                                                        (Rook) rookTile.getPiece(),
                                                                        rookTile.getPiece().getPiecePosition(),
                                                                        59));
            }
        }
        }

        return ImmutableList.copyOf(kingCastle);
    }
}

