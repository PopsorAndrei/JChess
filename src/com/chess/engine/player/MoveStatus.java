package com.chess.engine.player;

public enum MoveStatus {
    DONE{
        @Override
        public boolean isDone(){
            return true;
        }
        @Override
        public boolean leavesPlayerInCheck(){
            return false;
        }
    },
    ILLEGAL_MOVE{
        @Override
        public boolean isDone() {
            return false;
        }
        @Override
        public boolean leavesPlayerInCheck(){
            return false;
        }
    },
    LEAVES_PLAYER_IN_CHECK{
        @Override
        public boolean isDone(){
            return false;
        }
        @Override
        public boolean leavesPlayerInCheck(){
            return true;
        }
    },
    LEAVES_PLAYER_IN_CHECKMATE{
        @Override
        public boolean isDone(){return false;}
        @Override
        public boolean leavesPlayerInCheck(){
            return true;
        }
    };

    public abstract boolean isDone();
    public abstract boolean leavesPlayerInCheck();
}
