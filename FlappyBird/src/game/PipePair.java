package game;

import java.awt.Graphics2D;
import java.awt.Color;

public class PipePair {
    double x;
    double gapY;
    double width;
    double gapHeight;
    static double minGapHeight = 100, maxGapHeight = 300;
    static double pipePairSpacing = 200;

    public PipePair(double x, double gapY, double width, double gapHeight) {
        this.x = x;
        this.gapY = gapY;
        this.width = width;
        this.gapHeight = gapHeight;
    }

    public void update() {
        x--;
    }

    public void draw(Graphics2D g2, double GS) {
        g2.setColor(Color.GREEN);
        g2.fillRect((int)(x*GS), 0, (int)(width*GS), (int)(gapY-gapHeight/2*GS));
        g2.fillRect((int)(x*GS), (int)((gapY+gapHeight/2.0)*GS), (int)(width*GS), (int)((900-gapY-gapHeight/2.0)*GS));
    }
}
