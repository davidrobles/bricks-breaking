package net.davidrobles.bricks;

import net.games.board.IBoard;
import net.games.board.IMove;
import net.games.board.BoardAgent;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BricksBoard implements IBoard
{
    private int width;
    private int height;
    private BrickType[][] board = new BrickType[15][15];
    private int lives = 5;
    private int level = 0;
    private int bricksBroken = 0;
    private int ply = 0;
    private int score = 0;
    private boolean lostLife = false;
    private boolean printFlag = false;
    private List<BricksView> observers = new ArrayList<BricksView>();
    private static final Random rand = new Random();

    public BricksBoard(int width, int height)
    {
        this.width = width;
        this.height = height;

        newLevel();
    }

    public BricksBoard() {
        this(15, 15);
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public BrickType getCell(int width, int height) {
        return board[width][height];
    }

    public void setCell(int width, int height, BrickType type) {
        board[width][height] = type;
    }

    public boolean isLevelCompleted() {
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                if (board[i][j] != BrickType.EMPTY)
                    return false;
            }
        }
        return true;
    }

    private void newLevel()
    {
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                // Continuous color
                if (i > 0 && rand.nextDouble() < 0.20) {
                    board[i][j] = board[i - 1][j];
                } else if (j > 0 && rand.nextDouble() < 0.20) {
                    board[i][j] = board[i][j - 1];
                }
                // Random
                else {
                    int randIndex = BrickType.values().length - 1;
                    board[i][j] = BrickType.values()[rand.nextInt(randIndex)];
                }
            }
        }

        printMessage("Current level: " + level++);
    }

    private void settleBricks()
    {
        // Gravity
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height - 1; j++) {
                if (board[i][j] != BrickType.EMPTY && board[i][j + 1] == BrickType.EMPTY) {
                    for (int y = j; y >= 0; y--) {
                        board[i][y + 1] = board[i][y];
                        board[i][y] = BrickType.EMPTY;
                    }
                }
            }
        }

        // Empty columns
        for (int i = 0; i < width - 1; i++) {
            boolean empty = true;
            for (int j = 0; j < height; j++) {
                if (board[i + 1][j] != BrickType.EMPTY) {
                    empty = false;
                    break;
                }
            }
            if (empty) {
                for (int x = i; x >= 0; x--) {
                    for (int y = 0; y < height; y++) {
                        board[x + 1][y] = board[x][y];
                        board[x][y] = BrickType.EMPTY;
                    }
                }
            }
        }
    }

    public void floodFill(int x, int y, BrickType target)
    {
        if (board[x][y] != target)
            return;

        board[x][y] = BrickType.EMPTY;
        bricksBroken++;

        // west
        if (x > 0)
            floodFill(x - 1, y, target);
        
        // east
        if (x < width - 1)
            floodFill(x + 1, y, target);

        // north
        if (y > 0)
            floodFill(x, y - 1, target);

        // south
        if (y < height - 1)    
            floodFill(x, y + 1, target);
    }

    public void registerObserver(BricksView view) {
        observers.add(view);
    }

    public void notifyObservers() {
        for (BricksView view : observers) {
            view.update();
        }
    }

    @Override
    public BoardAgent[] getPlayers() {
        return new BoardAgent[0];
    }

    @Override
    public List<IMove> getMoves()
    {
        List<IMove> list = new ArrayList<IMove>();
        
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                if (board[i][j] != BrickType.EMPTY) {
                    list.add(new BricksMove(i, j));
                }
            }
        }

        return list;
    }

    @Override
    public void makeMove(IMove move) {

        int x = ((BricksMove) move).getX();
        int y = ((BricksMove) move).getY();

        if (lives == 0) {
            printMessage("Game is over already!");
            return;
        }

        // Illegal move
        if (board[x][y] == BrickType.EMPTY) {
            printMessage("ilegal move");
            return;
        }

//        System.out.println("Move: " + x + ", " + y);

        bricksBroken = 0;

        // remove bricks alike
        floodFill(x, y, board[x][y]);

        notifyObservers();

//        try {
//            Thread.sleep(100);
//        } catch (InterruptedException e) {
//            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
//        }

        // add score
        score += bricksBroken;

        // lose life
        if (bricksBroken == 1) {
            lives--;
            printMessage("Lost a life!");
            printMessage("Lives remaining: " + lives);
            lostLife = true;
        } else {
            lostLife = false;
        }

        ply++;

        settleBricks();
        notifyObservers();

        printMessage("-------------");

//        if (isLevelCompleted()) {
//            newLevel();
//        }
    }

    @Override
    public double evaluate(int playerIndex)
    {
        return lives;
//        // lost life
//        if (lostLife)
//            return -100;
//        else
//            return bricksBroken;
    }

    @Override
    public int currentPlayerIndex() {
        return 0;
    }

    @Override
    public boolean isGameOver() {
        return lives <= 0 || isLevelCompleted();
    }

    @Override
    public IBoard copy()
    {
        BricksBoard newBoard = new BricksBoard(width, height);

        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board.length; j++) {
                newBoard.board[i][j] = this.board[i][j];
            }
        }

        newBoard.lives = this.lives;
        newBoard.lostLife = this.lostLife;
        newBoard.level = this.level;
        newBoard.bricksBroken = this.bricksBroken;
        newBoard.score = this.score;

        return newBoard;
    }

    public void printMessage(String msg) {
        if (printFlag)
            System.out.println(msg);
    }

    public void setPrintFlag(boolean printFlag) {
        this.printFlag = printFlag;
    }
    
}
