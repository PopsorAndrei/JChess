package com.chess.engine.piece;

import com.chess.engine.Aliance;
import com.chess.engine.board.*;

import java.util.Collection;

public abstract class Piece {
    protected final PieceType pieceType;
    protected final int piecePosition;
    protected final Aliance pieceAliance;
    protected boolean isFirstMove;
    private int cachedHashCode;

    Piece(final int piecePosition,
      final Aliance pieceAliance,
      final PieceType pieceType,
      final boolean isFirstMove){

    this.pieceType = pieceType;
    this.piecePosition = piecePosition;
    this.pieceAliance = pieceAliance;
    this.isFirstMove = isFirstMove; //Nu stiu daca e bine inainte era false
    this.cachedHashCode = computeHashCode();
}

    private int computeHashCode() {
        int result = pieceType.hashCode();
        result = 31*result + pieceAliance.hashCode();
        result = 31*result + piecePosition;
        result = 31*result + (isFirstMove ? 1: 0);;
        return result;
    }

    public int getPiecePosition(){
    return this.piecePosition;
}

    public boolean isFirstMove(){
    return this.isFirstMove; //schimbat de mine nush daca ii bine
    }


public Aliance getPieceAliance(){
    return this.pieceAliance;
}


public PieceType getPieceType(){
    return this.pieceType;
}

public int getPieceValue(){
        return this.getPieceType().getPieceValue();
}






@Override
public boolean equals(final Object other){
    if(this==other){
        return true;
    }
    if(!(other instanceof Piece)){
        return false;
    }
    final Piece otherPiece = (Piece) other;
    return piecePosition == otherPiece.getPiecePosition()
            && pieceType ==otherPiece.getPieceType()
            && pieceAliance ==otherPiece.getPieceAliance()
            && isFirstMove == otherPiece.isFirstMove;
}

@Override
public int hashCode(){
    return this.cachedHashCode;
}



public abstract Collection<Move> calculateLegalMoves(final Board board);
public abstract Piece movePiece(Move move);

    public enum PieceType {
        Bishop("B",300){
            @Override
            public boolean isKing() {
                return false;
            }
            @Override
            public boolean isRook() {
                return false;
            }
        },
        King("K",10000) {
            @Override
            public boolean isKing() {
                return true;
            }
            @Override
            public boolean isRook() {
                return false;
            }
        },
        Knight("N",300) {
            @Override
            public boolean isKing() {
                return false;
            }
            @Override
            public boolean isRook() {
                return false;
            }
        },
        Pawn("P",100) {
            @Override
            public boolean isKing() {
                return false;
            }
            @Override
            public boolean isRook() {
                return false;
            }
        },
        Queen("Q",900) {
            @Override
            public boolean isKing() {
                return false;
            }
            @Override
            public boolean isRook() {
                return false;
            }
        },
        Rook("R",500) {
            @Override
            public boolean isKing() {
                return false;
            }
            @Override
            public boolean isRook() {
                return true;
            }
        };


        private String pieceName;
        private int pieceValue;


        PieceType(final String piecename,final int pieceValue) {
            this.pieceName = piecename;
            this.pieceValue = pieceValue;
        }

        @Override
        public String toString() {
            return this.pieceName;
        }

        public abstract boolean isKing();

        public abstract boolean isRook();

        public int getPieceValue(){
            return this.pieceValue;
        }
    }
}
