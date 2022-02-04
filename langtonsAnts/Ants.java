package langtonsAnts;

import java.util.Set;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.Timer;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import langtonsAnts.Ant;

public class Ants {
    int[][] farm;
    Set<Ant> ants;
    JFrame frame;
    JLabel[][] labels;
    List<Color> colors;
    List<Integer> values;
    
    int rows;
    int cols;
    int generations = 0;
    String pattern;

    public Ants(int rows, int cols, int size, String pattern) {
        this.cols = cols;
        this.rows = rows;
        
        // generate JFrame board and virtual board
        this.frame = new JFrame();
        this.farm = new int[rows][];
        this.labels = new JLabel[rows][];
        for(int i = 0; i < rows; i++) {
            this.farm[i] = new int[cols];
            this.labels[i] = new JLabel[cols];
            for(int j = 0; j < cols; j++) {
                this.farm[i][j] = 0;
                JLabel l = new JLabel();
                l.setBounds(j * size, i * size, size, size);
                l.setBackground(Color.WHITE);
                l.setOpaque(true);
                this.frame.add(l);
                this.labels[i][j] = l;
            }
        }

        this.frame.setLayout(null);
        this.frame.setSize(cols * size, rows * size);
        this.frame.setVisible(true);
        this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        this.ants = new HashSet<>();
        this.colors = new ArrayList<Color>(Arrays.asList(Color.BLACK, Color.WHITE, Color.LIGHT_GRAY, Color.GRAY,
                                                        Color.DARK_GRAY, Color.BLUE, Color.CYAN, Color.GREEN,
                                                        Color.PINK, Color.YELLOW, Color.MAGENTA, Color.orange));
        Collections.shuffle(this.colors);
        
        this.pattern = pattern;
        this.generatePattern();
    }

    /**
     * Transcribe string pattern to List<Integer> for cleaner use
     */
    public void generatePattern() {

        this.values = new ArrayList<Integer>();

        for(char move: this.pattern.toCharArray()) {
            switch(move) {
                case 'R':
                    this.values.add(1);
                    break;

                case 'L':
                    this.values.add(0);
                    break;
            }

            // if more steps than colors, just double the number of colors
            if(this.values.size() > this.colors.size())
                this.colors.addAll(this.colors);
        }
    }

    /**
     * Move an ant depending on the color it is currently on
     */
    public void iterate() {
        for(Ant ant: this.ants) {

            int i = ant.y;
            int j = ant.x;

            // Move ant right
            if((int) this.values.get(this.farm[i][j]) == 0) {
                ant.right();
                ant.step();
                this.checkBounds(ant);
                this.labels[i][j].setBackground((Color) this.colors.get(this.farm[i][j]));
                this.farm[i][j]++;

            // Move ant left
            } else if((int) this.values.get(this.farm[i][j]) == 1) {
                ant.left();
                ant.step();
                this.checkBounds(ant);
                this.labels[i][j].setBackground((Color) this.colors.get(this.farm[i][j]));
                this.farm[i][j]++;
            }

            if(this.farm[i][j] >= this.values.size())
                this.farm[i][j] = 0;

            this.labels[ant.y][ant.x].setBackground(ant.color);
        }
    }

    /**
     * Check whether an ant is exiting the boundry of the canvas, if so adjust its position
     * @param ant
     */
    public void checkBounds(Ant ant) {
        if(ant.x < 0)
            ant.x = this.cols - 1;
        else if(ant.x >= this.cols)
            ant.x = 0;
        
        if(ant.y < 0)
            ant.y= this.rows - 1;
        else if(ant.y >= this.rows)
            ant.y = 0;
    }

    /**
     * Append new ant to List<Ant> ants
     * @param ant
     */
    public void placeAnt(Ant ant) {
        ants.add(ant);
    }

    /**
     * display ants in ascii (debugging)
     */
    public void printFarm() {
        for(int i = 0; i < this.rows; i++) {

            String row = "";
            for(int j = 0; j < this.cols; j++) {
                row += this.farm[i][j] == 1 ? " " : "o";
            }

            System.out.println(row);
        }
        System.out.println();
    }

    public static void main(String[] args) {
        int cols = Integer.parseInt(args[0]);
        int rows = Integer.parseInt(args[1]);
        int size = Integer.parseInt(args[2]);
        int speed = Integer.parseInt(args[3]);
        String pattern = args[4];
        Boolean multiple = Boolean.parseBoolean(args[5]);

        Ants ants = new Ants(cols, rows, size, pattern);
        Random r = new Random();

        // Display multiple ants
        if(multiple) {
            int n = Integer.parseInt(args[6]);

            for(int i = 0; i < n; i++) {
                ants.placeAnt(new Ant(r.nextInt(rows), r.nextInt(cols), Color.RED));
            }

            for(int i = 0; i < n; i++) {
                ants.placeAnt(new BlueAnt(r.nextInt(rows), r.nextInt(cols), Color.BLUE));
            }
        
        // Simulate a single ant
        } else {
            ants.placeAnt(new Ant(rows / 2 - 1, cols / 2 - 1, Color.RED));
        }

        // Properly modify JFrame using a Timer
        Timer timer = new Timer(speed, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ants.iterate();

                if(ants.generations % 1000 == 0)
                    System.out.println(ants.generations);

                ants.generations++;
            }
        });

        timer.start();
    }
}
