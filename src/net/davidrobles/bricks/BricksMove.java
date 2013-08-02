package net.davidrobles.bricks;

import net.games.board.IMove;

public class BricksMove implements IMove {

    private int x;
    private int y;

    public BricksMove(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
    
}
