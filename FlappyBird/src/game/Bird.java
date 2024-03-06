package game;

import java.awt.Graphics2D;
import java.awt.Color;

public class Bird {
    NeuralNetwork brain;
    final static double x = 100;
    double y;
    double vy;
    final static double gravity = 0.1;
    final static double width = 20, height = 20;
    int timeAlive = 0;

    public Bird(NeuralNetwork brain) {
        this.brain = brain;
        y = 450;
        vy = 0;
    }

    public void jump() {
        vy = -5;
    }

    public void update(PipePair nextPipePair) {
        this.vy += gravity;
        this.y += vy;

        brain.input[0] = (nextPipePair.x-x)/300;
        brain.input[1] = (nextPipePair.gapY-y)/300;
        brain.input[2] = Utils.sigmoid(vy);
        brain.feedForward();
        if (brain.output[0] > brain.thresholds[0]) {
            jump();
        }

        timeAlive++;
    }

    public void draw(Graphics2D g2, double GS) {
        g2.setColor(Color.RED);
        g2.fillOval((int)((x-width/2.0)*GS), (int)((y-height/2.0)*GS), (int)(width*GS), (int)(height*GS));
    }
}
