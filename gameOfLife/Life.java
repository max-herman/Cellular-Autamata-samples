package gameOfLife;

import java.util.Random;
import java.util.Set;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import java.awt.event.MouseEvent;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.border.Border;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.Timer;
import javax.swing.event.MouseInputListener;

public class Life implements MouseInputListener{

    private int[][] board;
    Set<JLabel> positions;
    JFrame frame;
    JLabel[][] graphicBoard;
    Map<JLabel, int[]> labelToPos = new HashMap<>();
    Border border = BorderFactory.createLineBorder(Color.gray);
    int rows;
    int cols;
    int delay;
    Boolean iterate;
    Random r = new Random();
    Timer timer;

    public Life(int rows, int cols, int size, int delay) {

        // generate board data
        this.rows = rows;
        this.cols = cols;
        this.board = new int[rows][];
        this.positions = new HashSet<>();
        this.iterate = false;
        this.delay = delay;

        for(int i = 0; i < rows; i++) {
            this.board[i] = new int[cols];

            for(int j = 0; j < cols; j++) {
                this.board[i][j] = 0;
            }
        }

        // create graphics
        this.frame = new JFrame();
        this.graphicBoard = new JLabel[rows][cols];

        for(int i = 0; i < rows; i++) {

            this.graphicBoard[i] = new JLabel[cols];

            for(int j = 0; j < cols; j++) {
                JLabel l = new JLabel();
                l.setBounds(j * size, i * size, size, size);
                l.setBackground(Color.WHITE);
                l.setOpaque(true);
                l.setBorder(border);
                l.addMouseListener(this);
                this.frame.add(l);
                this.graphicBoard[i][j] = l;
                this.labelToPos.put(l, new int[] {i, j});
            }
        }

        // initialize frame settings
        this.frame.setLayout(null);
        this.frame.setSize(cols * size, rows * size + 28);
        this.frame.setVisible(true);
        this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Set up keylistener
        this.frame.addKeyListener(new KeyListener() {

            @Override
            public void keyTyped(KeyEvent e) {
                System.out.println(e.getKeyChar());
                switch(e.getKeyChar()) {
                    case 's':
                        if(Life.this.iterate) {
                            Life.this.iterate = false;
                            
                            if(Life.this.timer != null) {
                                Life.this.timer.stop();
                            }
                        } else {
                            Life.this.iterate = true;
                            Life.this.timer = Life.this.iterationHelper();
                            Life.this.timer.start();
                        }
                        break;

                    case 'g':
                        Life.this.generateRandom(0.1);
                        break;

                    case 'c':
                        Life.this.clearBoard();
                        break;

                    case '-':
                        if(Life.this.delay >= 300)
                            Life.this.delay -= 200;
                        break;
                    
                    case '=':
                        Life.this.delay += 200;
                        break;
                }
            }
            @Override
            public void keyPressed(KeyEvent e) {}
            @Override
            public void keyReleased(KeyEvent e) {}
        });
    }

    /**
     * Toggle the life of a cell by mouse click
     */
    @Override
    public void mouseClicked(MouseEvent e) {
        if(e.getSource() instanceof JLabel) {
            JLabel label = (JLabel) e.getSource();
            int[] pos = this.labelToPos.get(label);

            this.board[pos[0]][pos[1]] = this.board[pos[0]][pos[1]] == 0 ? 1 : 0;
            
            // if giving life, keep track of black cells and all cells surrounding for next generation
            if(this.graphicBoard[pos[0]][pos[1]].getBackground() == Color.WHITE) {
                this.graphicBoard[pos[0]][pos[1]].setBackground(Color.BLACK);

                int i = pos[0];
                int j = pos[1];
                for(int k = -1; k < 2; k++) {
                    for(int l = -1; l < 2; l++) {
                        if((0 <= i + k && i + k < this.rows) && (0 <= j + l && l + j < this.cols)) {
                            this.positions.add(this.graphicBoard[i + k][j + l]);
                        }
                    }
                }
            } else {
                this.graphicBoard[pos[0]][pos[1]].setBackground(Color.WHITE);
            }
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {}
    @Override
    public void mouseReleased(MouseEvent e) {}
    @Override
    public void mouseEntered(MouseEvent e) {}
    @Override
    public void mouseExited(MouseEvent e) {}
    @Override
    public void mouseDragged(MouseEvent e) {}
    @Override
    public void mouseMoved(MouseEvent e) {}

    /**
     * Toggle the life of each cell depending on its surroundings
     */
    public void iterate() {
        int[][] generation = new int[this.rows][this.cols];
        Set<JLabel> nextPositions = new HashSet<>();
        Set<JLabel> nextLife = new HashSet<>();
        System.out.println(this.positions.size());

        for(JLabel label: this.positions) {

            int[] pos = this.labelToPos.get(label);
            int i = pos[0];
            int j = pos[1];

            generation[i][j] = 0;
            this.graphicBoard[i][j].setBackground(Color.WHITE);

            // Collect all neighbors
            int numNeighbors = 0;
            for(int k = -1; k < 2; k++) {
                for(int l = -1; l < 2; l++) {
                    if((0 <= i + k && i + k < this.rows) && (0 <= j + l && l + j < this.cols) && this.board[i + k][j + l] == 1)
                        numNeighbors++;
                }
            }

            // if currently alive
            if(this.board[i][j] == 1) {

                // Need 2 or 3 neighbors to stay alive
                if(numNeighbors == 3 || numNeighbors == 4) {
                    generation[i][j] = 1;
                    nextLife.add(this.graphicBoard[i][j]);
                    for(int k = -1; k < 2; k++) {
                        for(int l = -1; l < 2; l++) {
                            if((0 <= i + k && i + k < this.rows) && (0 <= j + l && l + j < this.cols))
                                nextPositions.add(this.graphicBoard[i + k][j + l]);
                        }
                    }

                // Interesting to see the previous generation overlayed with current
                } else {
                    // this.graphicBoard[i][j].setBackground(Color.GRAY);
                }
            
            // if dead
            } else {

                // Need 2 neighbors to come to life
                if(numNeighbors == 3) {
                    generation[i][j] = 1;
                    nextLife.add(this.graphicBoard[i][j]);
                    for(int k = -1; k < 2; k++) {
                        for(int l = -1; l < 2; l++) {
                            if((0 <= i + k && i + k < this.rows) && (0 <= j + l && l + j < this.cols))
                                nextPositions.add(this.graphicBoard[i + k][j + l]);
                        }
                    }
                }
            }
        }

        // color new life
        for(JLabel label: nextLife) {
            label.setBackground(Color.BLACK);
        }

        // copy boards
        this.board = generation;
        this.positions = nextPositions;
    }

    /**
     * Propogate random life
     * @param percent How many cells to create
     */
    public void generateRandom(double percent) {

        // Each iteration generate a random x, y coordinate and check if it is dead
        for(int q = 0; q < Math.floor(this.cols * this.rows * percent); q++) {
            int i = r.nextInt(this.rows);
            int j = r.nextInt(this.cols);

            while(this.board[i][j] == 1) { 
                i = r.nextInt(this.rows);
                j = r.nextInt(this.cols);
            }

            this.board[i][j] = 1;
            this.graphicBoard[i][j].setBackground(Color.BLACK);

            for(int k = -1; k < 2; k++) {
                for(int l = -1; l < 2; l++) {
                    if((0 <= i + k && i + k < this.rows) && (0 <= j + l && l + j < this.cols))
                        this.positions.add(this.graphicBoard[i + k][j + l]);
                }
            }
        }
    }

    /**
     * Delete all board data
     */
    public void clearBoard() {
        for(int i = 0; i < this.rows; i++) {
            for(int j = 0; j < this.cols; j++) {
                this.board[i][j] = 0;
            }
        }

        for(int i = 0; i < this.rows; i++) {

            for(int j = 0; j < this.cols; j++) {
                this.graphicBoard[i][j].setBackground(Color.WHITE);;
            }
        }

        this.positions = new HashSet<>();
    }

    /**
     * Return number of living cells (incase you want to stop the program on death)
     * @return count of living cells
     */
    public int getLifeCount() {
        int alive = 0;

        for(int i = 0; i < this.rows; i++) {
            alive += Arrays.stream(this.board[i]).sum();
        }

        return alive;
    }

    /**
     * Create new timer to smoothly modify JFrame
     * @return new timer
     */
    public Timer iterationHelper() {

        Timer timer = new Timer(this.delay, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Life.this.iterate();
            }
        });

        return timer;
    }

    public static void main(String[] args) throws InterruptedException {

        int rows = Integer.parseInt(args[0]);
        int cols = Integer.parseInt(args[1]);
        int size = Integer.parseInt(args[2]);
        int delay = Integer.parseInt(args[3]);

        new Life(cols, rows, size, delay);
    }
}
