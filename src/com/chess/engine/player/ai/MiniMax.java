package com.chess.engine.player.ai;

import com.chess.engine.board.Board;
import com.chess.engine.board.Move;
import com.chess.engine.player.MoveTransition;

public class MiniMax implements MoveStrategy {

    private final BoardEvaluator boardEvaluator;
    private final int searchDepth;

    public MiniMax(final int searchDeapth) {
        this.boardEvaluator =new StandardBoardEvaluator();
        this.searchDepth = searchDeapth;
    }

    @Override
    public String toString() {
        return "MiniMax";
    }

    @Override
    public Move execute(Board board) {
        int i = 0;
        final long startTime = System.currentTimeMillis();

        Move bestMove=null;

        int highestSeenValue = Integer.MIN_VALUE;
        int lowestSeenValue=Integer.MAX_VALUE;
        int currentValue;
        System.out.println(board.currentPlayer() + "THINKING with depth = " +this.searchDepth);

        int nullMoves=board.currentPlayer().getLegalMoves().size();

        for(final Move move :board.currentPlayer().getLegalMoves()){

            final MoveTransition moveTransition = board.currentPlayer().makeMove(move);

            if(moveTransition.getMoveStatus().isDone()){

                currentValue = board.currentPlayer().getAliance().isWhite() ?
                        min(moveTransition.getTransitionBoard(),this.searchDepth-1) :
                        max(moveTransition.getTransitionBoard(),this.searchDepth-1);

                if(board.currentPlayer().getAliance().isWhite() && currentValue >=highestSeenValue){
                    highestSeenValue = currentValue;
                    bestMove = move;
                }else if(board.currentPlayer().getAliance().isBlack() && currentValue < lowestSeenValue){
                    lowestSeenValue = currentValue;
                    bestMove = move;
                }
            }
        }
        final long executiontime = System.currentTimeMillis() - startTime;

        return bestMove;
    }


    public int min(final Board board,
                   final int depth) {
        if (depth == 0|| isEndGameScenario(board)) { //if depth==0  or game over
            return this.boardEvaluator.evaluate(board, depth);
        }
        int lowestSeenValue = Integer.MAX_VALUE;
        for (final Move move : board.currentPlayer().getLegalMoves()) {
            final MoveTransition moveTransition = board.currentPlayer().makeMove(move);
            if (moveTransition.getMoveStatus().isDone()) {
                final int currentValue = max(moveTransition.getTransitionBoard(), depth - 1);
                if (currentValue < lowestSeenValue) {
                    lowestSeenValue = currentValue;
                }
            }
        }
        return lowestSeenValue;
    }

    public int max(final Board board,
                   final int depth) {

        if (depth == 0|| isEndGameScenario(board)) { //if depth==0  or game over
            return this.boardEvaluator.evaluate(board, depth);
        }
        int highestSeenValue = Integer.MIN_VALUE;
        for (final Move move : board.currentPlayer().getLegalMoves()) {
            final MoveTransition moveTransition = board.currentPlayer().makeMove(move);
            if (moveTransition.getMoveStatus().isDone()) {
                final int currentValue = min (moveTransition.getTransitionBoard(), depth - 1);
                if (currentValue > highestSeenValue) {
                    highestSeenValue = currentValue;
                }
            }
        }
        return highestSeenValue;
    }

    private static boolean isEndGameScenario(Board board) {
    return board.currentPlayer().isInCheckMate() ||
           board.currentPlayer().isInStaleMate();
    }

}