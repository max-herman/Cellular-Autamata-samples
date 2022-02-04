package langtonsAnts;

import java.util.Random;
import java.awt.Color;

public class Ant {
    int x;
    int y;
    int degree;
    int rep;
    Color color;

    Random r = new Random();
    
    public Ant(int x, int y, Color color) {
        this.x = x;
        this.y = y;
        this.color = color;
        this.rep = r.nextInt(5);
        this.degree = (r.nextInt(4) + 1) * 90;
    }

    /**
     * Move an ant one unit vertically or horizontally using geometry
     */
    public void step() {
        this.x += Math.round(Math.cos(this.degree * Math.PI / 180));
        this.y += Math.round(Math.sin(this.degree * Math.PI / 180));
    }

    /**
     * Rotate an ant 90 degrees left
     */
    public void left() {
        this.degree -= 90;
        if(this.degree < 0)
            this.degree = 270;

    }

    /**
     * Rotate an ant 90 degrees right
     */
    public void right() {
        this.degree += 90;
        if(this.degree > 360)
            this.degree = 90;
    }
}


class BlueAnt extends Ant {

    public BlueAnt(int x, int y, Color color) {
        super(x, y, color);
    }

    public void left() {

        this.degree += 90;
        if(this.degree > 360)
            this.degree = 90;
    }

    public void right() {

        this.degree -= 90;
        if(this.degree < 0)
            this.degree = 270;
    }
}