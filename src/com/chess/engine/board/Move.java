package com.chess.engine.board;

//import com.chess.engine.Aliance;
import com.chess.engine.piece.Pawn;
import com.chess.engine.piece.Piece;
import com.chess.engine.piece.Queen;
import com.chess.engine.piece.Rook;

import java.util.*;

import static com.chess.engine.board.Board.*;

public  abstract class Move {
    protected final Board board;
    protected final Piece movedPiece;
    protected final int destinationCoordonate;
    protected final boolean isFirstMove;

//    public static final Move NULL_MOVE = new CastleMove.NullMove();


    private Move(final Board board, final Piece piece, final int destinationCoordonate) {
        this.board = board;
        this.movedPiece = piece;
        this.destinationCoordonate = destinationCoordonate;
        this.isFirstMove = movedPiece.isFirstMove();
    }
    private Move(final Board board,
                 final int destinationCoordonate){
        this.board = board;
        this.destinationCoordonate = destinationCoordonate;
        this.movedPiece = null;
        this.isFirstMove = false;

    }
    @Override
    public int hashCode(){
        final int prime = 31;
        int result = 1;
        result = prime * result + this.destinationCoordonate;
        result = prime * result + this.movedPiece.hashCode();
        result = prime * result + this.movedPiece.getPiecePosition();

        return result;
    }
    @Override
    public boolean equals(final Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof Move)) {
            return false;
        }
        final Move move = (Move) other;
        return (getCurrentCoordinate() == ((Move) other).getCurrentCoordinate()&&
                this.getDestinationCoordonate()==move.getDestinationCoordonate() &&
                this.getMovedPiece().equals(move.getMovedPiece()));
    }
    public Board getBoard(){
        return this.board;
    }

    public int getDestinationCoordonate() {
        return this.destinationCoordonate;
    }

    public int getCurrentCoordinate() {
        return this.movedPiece.getPiecePosition();
    }


    public Piece getMovedPiece() {
        return movedPiece;
    }

    public boolean isAttack(){
        return false;
    }

    public boolean isCastlingMove(){
        return false;
    }
    public Piece getAttackedPiece(){
        return null;
    }

    public Board execute() {
        final Builder builder = new Builder();

        for (final Piece piece : this.board.currentPlayer().getActivePieces()) {
            //TODO hascode and equals for pieces
            if (!this.movedPiece.equals(piece)) {
                //this places all the peices on the new boarrd EXEPT THE MOVING PIECE
                builder.setPiece(piece);
            }

        }
        for (final Piece piece : this.board.currentPlayer().getOpponent().getActivePieces()) {
            builder.setPiece(piece);
        }
        //move the moved piece
        builder.setPiece(this.movedPiece.movePiece(this));
        builder.setMoveMaker(this.board.currentPlayer().getOpponent().getAliance());


        return builder.build();
    }



    public static final class MajorMove extends Move{

       public  MajorMove(final Board board,
                  final Piece piece,
                  final int destinationCoordonate) {
            super(board, piece, destinationCoordonate);
        }


        @Override
        public boolean equals(final Object other){
           return this == other || other instanceof MajorMove && super.equals(other);

        }
        @Override
        public String toString(){
           return getMovedPiece().getPieceType().toString() + BoardUtils.getPositionAtCoordinate(this.destinationCoordonate);
        }



    }
    public static class MajorAttackMove extends AttackMove{

        public MajorAttackMove(final Board board,
                               final Piece movedPiece,
                               final int destinationCoordinate,
                               final Piece pieceAttacked){
            super(board,movedPiece,destinationCoordinate,pieceAttacked);
        }
        @Override
        public boolean equals(Object other){
           return this==other||other instanceof MajorAttackMove && super.equals(other);
        }
        @Override
        public String toString(){
            return this.movedPiece.getPieceType().toString()+"x"+BoardUtils.getPositionAtCoordinate(destinationCoordonate);
        }

    }
    public static class AttackMove extends Move{
        final Piece attackedPiece;
       public  AttackMove(final Board board,
                   final Piece piece,
                   final int destinationCoordonate,
                   final Piece attackedPiece) {
            super(board, piece, destinationCoordonate);
            this.attackedPiece = attackedPiece;
        }

        @Override
        public Board execute() {
            final Builder builder = new Builder();
            for(final Piece piece : this.board.currentPlayer().getActivePieces()){
                if(!this.movedPiece.equals(piece)){
                    builder.setPiece(piece);
                }
            }
            for(final Piece piece :this.board.currentPlayer().getOpponent().getActivePieces()){
                if(!this.getAttackedPiece().equals(piece))
                builder.setPiece(piece);
            }

            final Piece movedPiece = (Piece) this.movedPiece.movePiece(this);
            builder.setPiece(movedPiece);
            builder.setMoveMaker(this.board.currentPlayer().getOpponent().getAliance());
            return builder.build();

        }

        @Override
        public boolean isAttack(){
           return true;
        }
        @Override
        public Piece getAttackedPiece(){
            return this.attackedPiece;
        }
        @Override
        public int hashCode(){
           final int prime = 31;
                return this.attackedPiece.hashCode() + super.hashCode();
        }
        @Override
        public boolean equals(Object other){
           if(this == other){
               return true;
           }
           if(!(other instanceof AttackMove)){
               return false;
           }
           final AttackMove otherAttackMove = (AttackMove) other;
            return super.equals(otherAttackMove)&& getAttackedPiece().equals(otherAttackMove.getAttackedPiece());
        }
            public String toString(){
                return this.movedPiece.getPieceType().toString().substring(0,1)+BoardUtils.getPositionAtCoordinate(this.destinationCoordonate);
        }


    }
    public static final class PawnMove extends Move{

        public  PawnMove(final Board board,
                          final Piece piece,
                          final int destinationCoordonate) {
            super(board, piece, destinationCoordonate);
        }

        @Override
        public boolean equals(final Object other){
            return this==other || other instanceof PawnMove && super.equals(other);
        }
        @Override
        public String toString(){
            return BoardUtils.getPositionAtCoordinate(destinationCoordonate);
        }

    }
    public static class PawnAttackMove extends AttackMove{

        public  PawnAttackMove(final Board board,
                         final Piece piece,
                         final int destinationCoordonate,
                         final Piece attackedPiece) {
            super(board, piece, destinationCoordonate,attackedPiece);
        }
        public String toString(){
            return this.movedPiece.getPieceType().toString().substring(0,1)+"x"+BoardUtils.getPositionAtCoordinate(this.destinationCoordonate);
        }
    }
    public static final class PawnEnPassantAttackMove extends PawnAttackMove{

        public  PawnEnPassantAttackMove(final Board board,
                               final Piece piece,
                               final int destinationCoordonate,
                               final Piece attackedPiece) {
            super(board, piece, destinationCoordonate,attackedPiece);
        }
        @Override
        public boolean equals(Object other){
            return this==other||other instanceof PawnEnPassantAttackMove &&super.equals(other);
        }
        @Override
        public Board execute(){
            final Builder builder = new Builder();
            for(final Piece piece : this.board.currentPlayer().getActivePieces()){
                if(!this.movedPiece.equals(piece)){
                    builder.setPiece(piece);
                }
            }
            for(final Piece piece : this.board.currentPlayer().getOpponent().getActivePieces()){
                if(!this.getAttackedPiece().equals(piece)) {
                    builder.setPiece(piece);
                }
            }
            builder.setPiece(this.movedPiece.movePiece(this));
            builder.setMoveMaker(this.board.currentPlayer().getOpponent().getAliance());
            return builder.build();

        }

        @Override
        public String toString(){
            return this.movedPiece.getPieceType().toString().substring(0,1)+BoardUtils.getPositionAtCoordinate(this.destinationCoordonate);
        }
    }

    public static class PawnPromotion extends Move{

        final Move decoratedMove;
        final Pawn promotedPawn;

        public  PawnPromotion(final Move decoratedMove) {
            super(  decoratedMove.getBoard(),
                    decoratedMove.movedPiece,
                    decoratedMove.getDestinationCoordonate());
            this.decoratedMove = decoratedMove;
            this.promotedPawn = (Pawn) decoratedMove.getMovedPiece();
        }

        @Override
        public Board execute() {
            final Board movedPawnBoard = decoratedMove.execute();
            final Builder builder = new Builder();

            for(final Piece piece: movedPawnBoard.currentPlayer().getActivePieces()){
                if(!piece.equals(this.promotedPawn)){
                    builder.setPiece(piece);
                }
            }

            for(final Piece piece: movedPawnBoard.currentPlayer().getOpponent().getActivePieces()){
                builder.setPiece(piece);
            }

            builder.setPiece(this.promotedPawn.getPromotionPiece().movePiece(this));

            builder.setMoveMaker(movedPawnBoard.currentPlayer().getAliance());
            return builder.build();
        }
        @Override
        public boolean isAttack(){
            return this.decoratedMove.isAttack();
        }
        @Override
        public Piece getAttackedPiece(){
            return this.decoratedMove.getAttackedPiece();
        }
        @Override
        public String toString(){
            return "";
        }
        @Override
        public int hashCode(){
            return decoratedMove.hashCode() + (31 * promotedPawn.hashCode());
        }
        @Override
        public boolean equals(final Object other){
            return this==other||other instanceof PawnPromotion&& super.equals(other);
        }

    }




    public static final class PawnJump extends Move{

        public PawnJump(final Board board,
                        final Piece piece,
                        final int destinationCoordonate) {
            super(board, piece, destinationCoordonate);
        }
        @Override
        public Board execute(){
            final Builder builder = new Builder();
            for(final Piece piece : this.board.currentPlayer().getActivePieces()){
                if(!this.movedPiece.equals(piece)){
                    builder.setPiece(piece);
                }
            }
            for(final Piece piece :this.board.currentPlayer().getOpponent().getActivePieces()){
                builder.setPiece(piece);
            }
            final Pawn movedPawn = (Pawn) this.movedPiece.movePiece(this);
            builder.setPiece(movedPawn);
            builder.setEnPassantPawn(movedPawn);
            builder.setMoveMaker(this.board.currentPlayer().getOpponent().getAliance());
            return builder.build();

        }
        @Override
        public String toString(){
            return BoardUtils.getPositionAtCoordinate(destinationCoordonate);
        }

    }
    public static abstract class CastleMove extends Move {
        protected final Rook castleRook;
        protected final int castleRookStart;
        protected final int castleRookDestination;

        public CastleMove(final Board board,
                          final Piece piece,
                          final int destinationCoordonate,
                          final Rook castleRook,
                          final int castleRookStart,
                          final int castleRookDestination) {
            super(board, piece, destinationCoordonate);
            this.castleRook = castleRook;
            this.castleRookStart = castleRookStart;
            this.castleRookDestination = castleRookDestination;
        }
        public Rook getCastleRook(){
            return this.castleRook;
        }
        @Override
        public boolean isCastlingMove(){
            return true;
        }



        @Override
        public Board execute(){

            final Builder builder = new Builder();
            for(final Piece piece : this.board.currentPlayer().getActivePieces()){
                if(!this.movedPiece.equals(piece)&& !this.castleRook.equals(piece)){
                    builder.setPiece(piece);
                }
            }
            for(final Piece piece :this.board.currentPlayer().getOpponent().getActivePieces()){
                builder.setPiece(piece);
        }
            builder.setPiece(this.movedPiece.movePiece(this));
            builder.setPiece(new Rook(this.castleRookDestination,this.castleRook.getPieceAliance()));
            builder.setMoveMaker(this.board.currentPlayer().getOpponent().getAliance());
            return builder.build();

    }
    @Override
    public int hashCode(){
        final int prime =31;
        int result = super.hashCode();
        result = prime*result + this.castleRook.hashCode();
        result = prime*result + this.castleRookDestination;
        return result;
    }
    @Override
    public boolean equals(final Object other){
            if(this==other){
                return true;
            }
            if(!(other instanceof CastleMove)){
                return false;
            }
            final CastleMove otherCastleMove = (CastleMove) other;
            return super.equals(otherCastleMove) && this.castleRook.equals(otherCastleMove.castleRook);
    }
    public static final class KingSideCastleMove extends CastleMove {

        public KingSideCastleMove(final Board board,
                                  final Piece piece,
                                  final int destinationCoordonate,
                                  final Rook castleRook,
                                  final int castleRookStart,
                                  final int castleRookDestination) {
            super(board, piece, destinationCoordonate,castleRook,castleRookStart,castleRookDestination);
        }
        @Override
        public String toString(){
            return "0-0";
        }
        @Override
        public boolean equals(final Object other){
            return this==other||other instanceof KingSideCastleMove && super.equals(other);
    }}
    public static final class QueenSideCastleMove extends CastleMove {

        public QueenSideCastleMove(final Board board,
                                   final Piece piece,
                                   final int destinationCoordonate,
                                   final Rook castleRook,
                                   final int castleRookStart,
                                   final int castleRookDestination) {
            super(board, piece, destinationCoordonate,castleRook,castleRookStart,castleRookDestination);}
            @Override
            public String toString(){
                return "0-0-0";
            }
        @Override
        public boolean equals(final Object other){
            return this==other||other instanceof QueenSideCastleMove && super.equals(other);
        }

    }
    }
    public static final class NullMove extends Move {

        public NullMove() {
            super(null, -1);
        }
        @Override
        public Board execute(){
            throw new RuntimeException("cant execute a null move motherfucker");
        }

        @Override
        public int getCurrentCoordinate(){
            return -1;
        }
    }
    public static class MoveFactory{
        public MoveFactory(){
            throw  new RuntimeException("not plm");
        }

        public static Move CreateMove(final Board board,
                                      final int currentPosition,
                                      final int destinationCoordinate){

            for(final Move move : board.getAllLegalMoves()){
                if(move.getCurrentCoordinate()== currentPosition &&
                   move.destinationCoordonate == destinationCoordinate){
                    return move;
                }
            }
            return null;
        }

    }


}
