package com.chess.pgn;

import com.chess.engine.Aliance;
import com.chess.engine.board.Board;
import com.chess.engine.board.BoardUtils;
import com.chess.engine.piece.*;

public class FenUtilities {

    private FenUtilities(){
        throw new RuntimeException("NOT INTANTIABLE");
    }

    public static Board createGameFromFen(final String fen){
        return null;
    }

    public static String createFENFromGame(final Board board){
        return calculateBoardTex(board) + " " +
                calculateCurrentPlayerText(board) + " ";
               // calculateCastleText(board) + " " +
              //  calculateEnPassantSquare(board);
    }

    private static String calculateBoardTex(final Board board) {
        final StringBuilder builder = new StringBuilder();
        for(int i=0;i<BoardUtils.NUM_TILES;i++){
            final String tileText = board.getTile(i).toString();
            builder.append(tileText);
        }
        builder.insert(8,"/");
        builder.insert(17,"/");
        builder.insert(26,"/");
        builder.insert(35,"/");
        builder.insert(44,"/");
        builder.insert(53,"/");
        builder.insert(62,"/");

        return builder.toString().replaceAll("--------","8")
                                  .replaceAll("-------","7")
                                    .replaceAll("------","6")
                                    .replaceAll("-----","5")
                                    .replaceAll("----","4")
                                    .replaceAll("---","3")
                                    .replaceAll("--","2")
                                    .replaceAll("-", "1");
    }

    private static String calculateEnPassantSquare(final Board board) {
        final Pawn enPassantPawn = board.getEnPassantPawn();

        if(enPassantPawn != null){
                return BoardUtils.getPositionAtCoordinate(enPassantPawn.getPiecePosition() +
                        (8)* enPassantPawn.getPieceAliance().getOppositeDirection());
        }
        return "-";
    }

    private static String calculateCastleText(final Board board) {
        final StringBuilder builder = new StringBuilder();

        if(board.whitePlayer().isKingSideCastleCapable()){
            builder.append("K");
        }
        if(board.whitePlayer().isQueenSideCastleCapable()){
            builder.append("Q");
        }
        if(board.blackPlayer().isKingSideCastleCapable()){
            builder.append("k");
        }
        if(board.blackPlayer().isQueenSideCastleCapable()){
            builder.append("q");
        }
        final String result = builder.toString();
        return result.isEmpty() ? "-" : result;
    }

    private static String calculateCurrentPlayerText(final Board board){
       // return board.currentPlayer().toString().substring(0,1).toLowerCase();
        return board.currentPlayer().getAliance().isWhite() ? "w" :"b";
    }

    public static Board createBoardFromFENString(final String fenString) {
        final String[] fenPartitions = fenString.trim().split(" ");
        final Board.Builder builder = new Board.Builder();
        final String gameConfiguration = fenPartitions[0];
        final char[] boardTiles = gameConfiguration.replaceAll("/", "")
                .replaceAll("8", "--------")
                .replaceAll("7", "-------")
                .replaceAll("6", "------")
                .replaceAll("5", "-----")
                .replaceAll("4", "----")
                .replaceAll("3", "---")
                .replaceAll("2", "--")
                .replaceAll("1", "-")
                .toCharArray();
        int i = 0;
        while (i < boardTiles.length) {
            switch (boardTiles[i]) {
                case 'r':
                    builder.setPiece(new Rook(i,Aliance.BLACK));
                    i++;
                    break;
                case 'n':
                    builder.setPiece(new Knight(i,Aliance.BLACK));
                    i++;
                    break;
                case 'b':
                    builder.setPiece(new Bishop(i, Aliance.BLACK));
                    i++;
                    break;
                case 'q':
                    builder.setPiece(new Queen(i,Aliance.BLACK));
                    i++;
                    break;
                case 'k':

                    builder.setPiece(new King(i,Aliance.BLACK,false,false));
                    i++;
                    break;
                case 'p':
                    builder.setPiece(new Pawn(i,Aliance.BLACK));
                    i++;
                    break;
                case 'R':
                    builder.setPiece(new Rook(i,Aliance.WHITE));
                    i++;
                    break;
                case 'N':
                    builder.setPiece(new Knight(i,Aliance.WHITE));
                    i++;
                    break;
                case 'B':
                    builder.setPiece(new Bishop(i,Aliance.WHITE));
                    i++;
                    break;
                case 'Q':
                    builder.setPiece(new Queen(i,Aliance.WHITE));
                    i++;
                    break;
                case 'K':
                    builder.setPiece(new King(i, Aliance.WHITE, false,false));
                    i++;
                    break;
                case 'P':
                    builder.setPiece(new Pawn(i, Aliance.WHITE));
                    i++;
                    break;
                case '-':
                    i++;
                    break;
                default:
                    throw new RuntimeException("Invalid FEN String " +gameConfiguration);
            }
        }

        builder.setMoveMaker(moveMaker(fenPartitions[1]));
        System.out.println(builder.build().toString());
        return builder.build();
    }

    private static Aliance moveMaker(final String moveMakerString) {
        if(moveMakerString.equals("w")) {
            return Aliance.WHITE;
        } else if(moveMakerString.equals("b")) {
            return Aliance.BLACK;
        }
        throw new RuntimeException("Invalid FEN String " +moveMakerString);
    }

}
