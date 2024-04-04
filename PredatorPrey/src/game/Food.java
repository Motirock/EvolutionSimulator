package game;

import java.awt.Graphics2D;
import java.awt.Color;

public class Food {
    Vector2D position = new Vector2D();
    double radius = 10;
    double value = 1;
    boolean alive = true;

    public Food(Vector2D position) {
        this.position = position;
    }

    public Food(Vector2D position, double value) {
        this.position = position;
        this.value = value;
    }

    public Food(double x, double y) {
        position.x = x;
        position.y = y;
    }

    public Food(double x, double y, double value) {
        position.x = x;
        position.y = y;
        this.value = value;
    }

    public boolean isEaten(Agent a) {
        if (alive && Math.sqrt(Math.pow(position.x-a.position.x, 2)+Math.pow(position.y-a.position.y, 2)) < radius+a.radius) {
            a.score += value;
            alive = false;
            return true;
        }
        return false;
    }

    public void draw(Graphics2D g2, double GS, double scale) {
        g2.setColor(new Color(0, 255, 0));
        g2.fillOval((int) ((position.x-radius)*GS/scale), (int) ((position.y-radius)*GS/scale), (int) (2*radius*GS/scale), (int) (2*radius*GS/scale));
    }

}
