package com.chess.engine.board;

import com.chess.engine.piece.Piece;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public abstract class Tile {

    protected final int tileCoordinate;

    private static final Map<Integer, EmptyTile> EMPTY_TILE = createAllPossibleEmptyTiles();

    private static Map<Integer, EmptyTile> createAllPossibleEmptyTiles(){
        final Map<Integer, EmptyTile> emptyTileMap = new HashMap<>();

       for(int i=0 ;i<BoardUtils.NUM_TILES;i++){
           emptyTileMap.put(i,new EmptyTile(i));
       }

        return Collections.unmodifiableMap(emptyTileMap);
    }


    public static Tile createTile(final int tileCoordinate, final Piece piece){
        return piece !=null ?new OcupiedTile(tileCoordinate,piece): EMPTY_TILE.get(tileCoordinate);
        //if the tile coordinate is not empty, return the piece that is there, elese return the emptytle tyhat is.
    }
    public int getTileCoordinate(){
        return this.tileCoordinate;
    }

    private Tile(final int tileCoordinate){
        this.tileCoordinate = tileCoordinate;
    }

    public abstract boolean isTileOccupied();
    
    public abstract Piece getPiece();

    public static final class EmptyTile extends Tile{

         private EmptyTile(final int coordinate){
            super(coordinate);
        }
        @Override
        public String toString(){
             return "-";
        }

        @Override
        public boolean isTileOccupied(){
            return false;
        }
        @Override
        public Piece getPiece(){
            return null;
        }
    }
    public static final class OcupiedTile extends Tile{
        private final Piece pieceOnTile;

        private OcupiedTile(int cooridnate,Piece piece){
            super(cooridnate);
            this.pieceOnTile = piece;
        }
        @Override
        public String toString(){
            return getPiece().getPieceAliance().isBlack() ? getPiece().toString().toLowerCase()
                    :getPiece().toString();
        }
        @Override
        public boolean isTileOccupied(){
            return true;

        }

        @Override
        public Piece getPiece() {
            return this.pieceOnTile;
        }
    }

}
