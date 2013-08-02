package net.davidrobles.bricks;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class BricksSC extends JFrame {

    private Robot robot;
    private BufferedImage screenshot;
    private static final int SIZE = 375;

    private static final int GREEN = -12517568;
    private static final int RED = -49088;
    private static final int YELLOW = -192;

    BricksBoard board;

    public BricksSC(BricksBoard board)
    {
        this.board = board;
        setBackground(Color.WHITE);
        setSize(SIZE, SIZE + 21);
        setLocation(1000, 400);
        setVisible(true);

        try {
            robot = new Robot();
        } catch (AWTException ignored) {
        }

        locateGame();
    }

    public void locateGame()
    {

    }

    @Override
    public void paint(Graphics g)
    {
        super.paint(g);

        g.drawImage(screenshot, 0, 12, null);

        g.setColor(test);
        g.fillRect(100, 100, 5, 5);
    }

    public int coordToIndex(int x, int y)
    {    
        return (y * SIZE ) + x;
    }

    Color test = new Color(30, 30, 30);

//    private BrickType[][] board = new BrickType[15][15];

    public void capture()
    {
        while (true)
        {
            int[] screenPixels;
            screenshot = robot.createScreenCapture(new Rectangle(300, 300, SIZE, SIZE + 10));
            screenPixels = screenshot.getRGB(0, 0, SIZE, SIZE, null, 0, SIZE);

            // color
//            int colorson = screenPixels[coordToIndex(12, 24)];
//            System.out.println(colorson);
//            test = new Color(colorson);

            // first pixel
            if (screenshot.getRGB(12, 23) == GREEN || screenshot.getRGB(12, 23) == RED
                    || screenshot.getRGB(12, 23) == YELLOW) {
                System.out.println("detected!!!!!!");
            }

            // read
            for (int i = 0; i < 15; i++) {
                for (int j = 0; j < 15; j++) {
                    int xx = (i * 25) + 12;
                    int yy = (j * 25) + 23;
//                    System.out.println(i + ", " + j + ":" + screenshot.getRGB(xx, yy));
//                    screenshot.setRGB(xx, yy, new Color(0, 0, 255).getRGB());
                    if (screenshot.getRGB(xx, yy) == GREEN) {
                        board.setCell(i, j, BrickType.GREEN);
                    } else if (screenshot.getRGB(xx, yy) == RED) {
                        board.setCell(i, j, BrickType.RED);
                    } else if (screenshot.getRGB(xx, yy) == YELLOW) {
                        board.setCell(i, j, BrickType.YELLOW);
                    } else {
                        board.setCell(i, j, BrickType.EMPTY);
                    }
                }
            }
            
            board.notifyObservers();
            repaint();

            try {
                Thread.sleep(500);
            } catch (InterruptedException ignored) { }
        }
    }
    
}
