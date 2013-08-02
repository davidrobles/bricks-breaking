package net.davidrobles.bricks;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class BricksView extends JPanel implements MouseListener {

    private BricksBoard board;
    private static final int CELL_WIDTH = 25;
    private static final int CELL_HEIGHT = 25;

    public BricksView(BricksBoard board) {
        this.board = board;
        addMouseListener(this);
    }

    @Override
    protected void paintComponent(Graphics g)
    {
        super.paintComponent(g);

        for (int i = 0; i < board.getWidth(); i++)
        {
            for (int j = 0; j < board.getHeight(); j++)
            {
                // Draw bricks

                if (board.getCell(i, j) == BrickType.GREEN) {
                    g.setColor(Color.GREEN);
                } else if (board.getCell(i, j) == BrickType.RED) {
                    g.setColor(Color.RED);
                } else if (board.getCell(i, j) == BrickType.YELLOW) {
                    g.setColor(Color.YELLOW);
                } else if (board.getCell(i, j) == BrickType.EMPTY) {
                    g.setColor(Color.BLACK);
                }

                g.fillRect(i * CELL_WIDTH, j * CELL_HEIGHT, CELL_WIDTH, CELL_HEIGHT);

                // Draw Border
                g.setColor(Color.BLACK);
                g.drawRect(i * CELL_WIDTH, j * CELL_HEIGHT, CELL_WIDTH, CELL_HEIGHT);
            }
        }
    }

    public void update() {
        repaint();
    }

    @Override
    public void mouseClicked(MouseEvent e)
    {
        int x = (int) e.getPoint().getX() / CELL_WIDTH;
        int y = (int) e.getPoint().getY() / CELL_HEIGHT;

        if (x >= 0 && y >= 0 && x < (board.getWidth() * CELL_WIDTH) && y < (board.getHeight() * CELL_HEIGHT))

        board.makeMove(new BricksMove(x , y));
    }

    @Override
    public void mousePressed(MouseEvent e) { }

    @Override
    public void mouseReleased(MouseEvent e) { }

    @Override
    public void mouseEntered(MouseEvent e) { }

    @Override
    public void mouseExited(MouseEvent e) { }
    
}
