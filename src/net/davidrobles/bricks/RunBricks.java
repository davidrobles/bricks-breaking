package net.davidrobles.bricks;

import net.games.board.BoardAgent;
import net.games.board.agents.MonteCarloAgent;

import javax.swing.*;
import java.awt.*;
import java.util.Random;

public class RunBricks {

    private static final Random rand = new Random();

    private static void autoPlay()
    {
//        BoardAgent player = new RandomAgent();
        BoardAgent player = new MonteCarloAgent();

        BricksBoard board = new BricksBoard();
        board.setPrintFlag(true);
        BricksView view = new BricksView(board);

        board.registerObserver(view);

        JFrame frame = new JFrame();
        frame.setBackground(Color.BLUE);
        frame.add(view);
        frame.setSize(new Dimension(500, 500));
        frame.setVisible(true);

        while (!board.isGameOver()) {
//            int randIndex = rand.nextInt(board.getMoves().size());
//            System.out.println(randIndex);
//            board.makeMove(board.getMoves().get(randIndex));
            board.makeMove(player.move(board));
            try {
                Thread.sleep(250);
            } catch (InterruptedException ignored) { }
        }
    }

    private static void humanPlay()
    {
        BricksBoard board = new BricksBoard();
        BricksView view = new BricksView(board);

        board.registerObserver(view);

        JFrame frame = new JFrame();
        frame.setBackground(Color.BLUE);
        frame.add(view);
        frame.setSize(new Dimension(500, 500));
        frame.setVisible(true);

        BricksSC sc = new BricksSC(board);
        sc.capture();

        try {
            Thread.sleep(500);
        } catch (InterruptedException ignored) {

        }
    }

    public static void main(String[] args)
    {

//        humanPlay();
        autoPlay();
    }

}
