
package tetris;

import java.awt.Color;
import java.awt.Graphics;

/**
 *
 * @author Paul Ashbourne
 */
public class Cell {

    private int x1; //start x coordinate
    private int x2; //end x coordinate
    private int y1; //start y coordinate
    private int y2; //end y coordinate
    private Color dColor; //default backcolor of cell
    private Color color; //color of cell
    private boolean isFilled; //denotes whether cell is filled

    Cell(Color defaultColor) {
        this.dColor = defaultColor;
        this.color = defaultColor;
    }

    public void setxy(int x1, int y1, int x2, int y2) {
        this.x1 = x1;
        this.x2 = x2;
        this.y1 = y1;
        this.y2 = y2;
    }

    /**
     * Clears the cell
     */
    public void clear() {
        isFilled = false;
        color = dColor;
    }

    /**
     * Fills the cell with a block of a certain color
     *
     * @param c the color of the block
     */
    public void fill(Color c) {
        isFilled = true;
        color = c;
    }

    /**
     * Returns true if cell is filled, false if cell is empty
     *
     * @return True if cell is filled, false if cell is empty
     */
    public boolean isFilled() {
        return isFilled;
    }

    /**
     * Get the color of the cell
     *
     * @return the color of the cell
     */
    public Color getColor() {
        return color;
    }

    public void setColor(Color c) {
        color = c;
    }

    /**
     * Draws this cell on a graphics object
     *
     * @param g The graphics object on which the cell will be drawn
     */
    public void drawCell(Graphics g) {
        //draw color
        g.setColor(color);
        g.fillRect(x1, y1, x2 - x1, y2 - y1);
        //draw outline
        g.setColor(Color.black);
        g.drawRect(x1, y1, x2 - x1, y2 - y1);
    }
}
