/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tetris;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;
import javax.swing.Timer;

/**
 *
 * @author Paul Ashbourne
 */
public class Grid extends javax.swing.JPanel {

    private Cell[][] cell = new Cell[10][20];
    private int stepValue;
    private Block b;
    private Color bColor = Color.BLACK;
    private Timer timer = new Timer(500, new TimerListener());

    /**
     * Creates new form Board
     */
    public Grid() {
        initComponents();
        for (int x = 0; x < 10; x++) {
            for (int y = 0; y < 20; y++) {
                cell[x][y] = new Cell(bColor);
            }
        }
        scaleCells();
        timer.start();
        newBlock();
    }

    private void scaleCells() {
        //calculate distance between cells
        if (this.getWidth() * 2 > this.getHeight()) {
            stepValue = this.getHeight() / 20;
        } else {
            stepValue = this.getWidth() / 10;
        }
        //loop through all cells, create cell in correct locations
        for (int x = 0; x < 10; x++) {
            for (int y = 0; y < 20; y++) {
                cell[x][y].setxy(x * stepValue, y * stepValue, (x + 1) * stepValue, (y + 1) * stepValue);
            }
        }
    }

    private void newBlock() {
        Random r = new Random();
        b = new Block(cell, r.nextInt(7));
        repaint();
    }

    private void clearRows() {
        for (int y = 19; y >= 0; y--) {
            boolean clear = true;
            for (int x = 0; x < 10; x++) {
                if (!cell[x][y].isFilled()) {
                    clear = false;
                    break;
                }
            }
            if (clear) {
                for (int y1 = y; y1 >= 0; y1--) {
                    if (y1 == 0) {
                        for (int x = 0; x < 10; x++) {
                            cell[x][y1].clear();
                        }
                    } else {
                        for (int x = 0; x < 10; x++) {
                            if (cell[x][y1 - 1].isFilled()) {
                                cell[x][y1].fill(cell[x][y1 - 1].getColor());
                            } else {
                                cell[x][y1].clear();
                            }
                        }
                    }
                }
                y++;
            }
        }
        repaint();
    }

    @Override
    public void paintComponent(Graphics g) {
        scaleCells();
        //loop through all cells, create cell in correct locations
        for (int x = 0; x < 10; x++) {
            for (int y = 0; y < 20; y++) {
                cell[x][y].drawCell(g);
            }
        }
    }

    public boolean moveBlock(int deltax, int deltay) {
        boolean v = b.move(deltax, deltay);
        repaint();
        return v;
    }

    public void dropBlock() {
        boolean end = false;
        do {
            end = moveBlock(0, 1);
        } while (end);
        b.freeze();
        clearRows();
        newBlock();
    }

    public void rotate(int direction) {
        b.rotate(direction);
        repaint();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setMinimumSize(new java.awt.Dimension(100, 200));
        setPreferredSize(new java.awt.Dimension(200, 400));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 200, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables

    private class TimerListener implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            if (!moveBlock(0, 1)) {
                b.freeze();
                clearRows();
                newBlock();
            }
        }
    }
}