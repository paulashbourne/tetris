package tetris;

import java.awt.Color;
import java.util.Arrays;

public class Block {

    /*
     * Shape List:
     * 0 - Line
     * 1 - Reverse 'L'
     * 2 - 'L'
     * 3 - Box
     * 4 - 'S'
     * 5 - 'Z'
     * 6 - 'T'
     */
    //shape index, square, x/y
    private static int[][][] startpos = {
        {
            {3, 0}, {4, 0}, {5, 0}, {6, 0}},
        {
            {5, 1}, {5, 0}, {4, 0}, {3, 0}},
        {
            {3, 1}, {3, 0}, {4, 0}, {5, 0}},
        {
            {4, 0}, {5, 0}, {4, 1}, {5, 1}},
        {
            {5, 0}, {4, 0}, {4, 1}, {3, 1}},
        {
            {3, 0}, {4, 0}, {4, 1}, {5, 1}},
        {
            {4, 0}, {3, 1}, {4, 1}, {5, 1}}};
    private Color[] color = {
        new Color(0, 255, 255), //light blue
        new Color(0, 0, 255), //dark blue
        new Color(255, 128, 0), //orange
        new Color(255, 255, 0), //yellow
        new Color(0, 255, 0), //green
        new Color(255, 0, 0), //red
        new Color(128, 0, 128) //purple
    };
    private int[][] squarepos = new int[4][2]; //first index for square 0-3, second index for coordinate x, y
    private int pos = 0; //position from 0 to 3 of block
    private int index; //index of block 0-6
    private Cell[][] cell;
    private boolean conflict;

    Block(Cell[][] cell, int type) {
        index = type;
        for (int b = 0; b < 4; b++) {
            squarepos[b] = Arrays.copyOf(startpos[index][b], 2);
            cell[squarepos[b][0]][squarepos[b][1]].setColor(color[index]);
        }
        this.cell = cell;
    }

    public boolean move(int deltax, int deltay) {
        if (!canMove(deltax, deltay)) {
            return false;
        }
        for (int b = 0; b < 4; b++) {
            cell[squarepos[b][0]][squarepos[b][1]].clear();
        }
        for (int b = 0; b < 4; b++) {
            squarepos[b][0] += deltax;
            squarepos[b][1] += deltay;
            cell[squarepos[b][0]][squarepos[b][1]].setColor(color[index]);
        }
        return true;
    }

    private boolean canMove(int deltax, int deltay) {
        for (int b = 0; b < 4; b++) {
            int x = squarepos[b][0] + deltax;
            int y = squarepos[b][1] + deltay;
            if (x < 0 || x > 9 || y < 0 || y > 19 || cell[x][y].isFilled()) {
                return false;
            }
        }
        return true;
    }

    public void freeze() {
        for (int b = 0; b < 4; b++) {
            cell[squarepos[b][0]][squarepos[b][1]].fill(color[index]);
        }
    }

    public boolean rotate(int dir) {
        if (dir == 0) {
            return false;
        } else if (dir > 0) {
            dir = 1;
        } else {
            dir = -1;
        }
        return pRotate(dir, 0, 0);
    }

    private boolean pRotate(int dir, int deltax, int deltay) {
        int newpos = pos;
        if (dir == 1) {
            newpos += 1;
            if (newpos == 4) {
                newpos = 0;
            }
        }
        for (int b = 0; b < 4; b++) {
            int y = squarepos[b][1] + deltay + (dir * changepos[index][newpos][b][1]);
            if (y > 19) {
                return false;
            }
            if (y < 0) {
                if (canMove(deltax, ++deltay)) {
                    return pRotate(dir, deltax, deltay);
                } else {
                    return false;
                }
            }
            int x = squarepos[b][0] + deltax + (dir * changepos[index][newpos][b][0]);
            if (x < 0) {
                if (deltax < 0) {
                    return false;
                }
                if (canMove(++deltax, deltay)) {
                    return pRotate(dir, deltax, deltay);
                } else {
                    return false;
                }
            }
            if (x > 9) {
                if (deltax > 0) {
                    return false;
                }
                if (canMove(--deltax, deltay)) {
                    return pRotate(dir, deltax, deltay);
                } else {
                    return false;
                }
            }
            if (cell[squarepos[b][0] + deltax][y].isFilled()) {
                if (canMove(deltax, ++deltay)) {
                    return pRotate(dir, deltax, deltay);
                } else {
                    return false;
                }
            }
            if (cell[x][y].isFilled()) {
                if (dir * changepos[index][newpos][b][0] > 0) {
                    if (deltax > 0) {
                        return false;
                    }
                    if (canMove(--deltax, deltay)) {
                        return pRotate(dir, deltax, deltay);
                    } else {
                        return false;
                    }
                } else {
                    if (deltax < 0) {
                        return false;
                    }
                    if (canMove(++deltax, deltay)) {
                        return pRotate(dir, deltax, deltay);
                    } else {
                        return false;
                    }
                }
            }
        }
        for (int b = 0; b < 4; b++) {
            cell[squarepos[b][0]][squarepos[b][1]].clear();
        }
        for (int b = 0; b < 4; b++) {
            squarepos[b][0] += (deltax + (dir * changepos[index][newpos][b][0]));
            squarepos[b][1] += (deltay + (dir * changepos[index][newpos][b][1]));
            cell[squarepos[b][0]][squarepos[b][1]].setColor(color[index]);
        }
        pos += dir;
        if (pos == 4) {
            pos = 0;
        } else if (pos == -1) {
            pos = 3;
        }
        System.out.println(dir + ", " + pos);
        return true;
    }
    //[shape][nextposition][square][x/y]
    private static int[][][][] changepos = {
        { //shape 0
            { //position 0
                {-1, 1}, {0, 0}, {1, -1}, {2, -2}
            },
            { //position 1
                {1, -1}, {0, 0}, {-1, 1}, {-2, 2}
            },
            { //position 2
                {-1, 1}, {0, 0}, {1, -1}, {2, -2}
            },
            { //position 3
                {1, -1}, {0, 0}, {-1, 1}, {-2, 2}
            },},
        { //shape 1
            { //position 0
                {0, 2}, {1, 1}, {0, 0}, {-1, -1}
            },
            { //position 1
                {-2, 0}, {-1, 1}, {0, 0}, {1, -1}
            },
            { //position 2
                {0, -2}, {-1, -1}, {0, 0}, {1, 1}
            },
            { //position 3
                {2, 0}, {1, -1}, {0, 0}, {-1, 1}
            }
        },
        { //shape 2
            { //position 0
                {-2, 0}, {-1, -1}, {0, 0}, {1, 1}
            },
            { //position 1
                {0, -2}, {1, -1}, {0, 0}, {-1, 1}
            },
            { //position 2
                {2, 0}, {1, 1}, {0, 0}, {-1, -1}
            },
            { //position 3
                {0, 2}, {-1, 1}, {0, 0}, {1, -1}
            }
        },
        { //shape 3
            { //position 0
                {0, 0}, {0, 0}, {0, 0}, {0, 0}
            },
            { //position 1
                {0, 0}, {0, 0}, {0, 0}, {0, 0}
            },
            { //position 2
                {0, 0}, {0, 0}, {0, 0}, {0, 0}
            },
            { //position 3
                {0, 0}, {0, 0}, {0, 0}, {0, 0}
            }
        },
        { //shape 4
            { //position 0
                {0, -2}, {-1, -1}, {0, 0}, {-1, 1}
            },
            { //position 1
                {0, 2}, {1, 1}, {0, 0}, {1, -1}
            },
            { //position 2
                {0, -2}, {-1, -1}, {0, 0}, {-1, 1}
            },
            { //position 3
                {0, 2}, {1, 1}, {0, 0}, {1, -1}
            }
        },
        { //shape 5
            { //position 0
                {-2, 0}, {-1, -1}, {0, 0}, {1, -1}
            },
            { //position 1
                {2, 0}, {1, 1}, {0, 0}, {-1, 1}
            },
            { //position 2
                {-2, 0}, {-1, -1}, {0, 0}, {1, -1}
            },
            { //position 3
                {2, 0}, {1, 1}, {0, 0}, {-1, 1}
            }
        },
        { //shape 6
            { //position 0
                {1, -1}, {-1, -1}, {0, 0}, {1, 1}
            },
            { //position 1
                {1, 1}, {1, -1}, {0, 0}, {-1, 1}
            },
            { //position 2
                {-1, 1}, {1, 1}, {0, 0}, {-1, -1}
            },
            { //position 3
                {-1, -1}, {-1, 1}, {0, 0}, {1, -1}
            }
        },};
}
