package game;

import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.BasicStroke;

import java.util.ArrayList;

public class Agent {
    public Vector2D position = new Vector2D();
    public Vector2D velocity = new Vector2D();
    public double radius = 20;
    //public static final double acceleration = 1000.0;
    public static final double maxSpeed = 5;
    public double rotation = 0;
    public double rotationSpeed = 0.05;
    public NeuralNetwork nn;
    public boolean alive = true;
    public int score = 0;
    public double fitness = 0;
    public int timeAlive = 0;
    public int hunger = 0;
    public Color color = Color.WHITE;

    public Agent(Vector2D position, NeuralNetwork nn) {
        this.position = position;
        this.nn = nn;
    }

    public Agent(Vector2D position, NeuralNetwork nn, Color color) {
        this.position = position;
        this.nn = nn;
        this.color = color;
    }

    public Agent(double x, double y, NeuralNetwork nn) {
        position.x = x;
        position.y = y;
        this.nn = nn;
    }

    public Agent(double x, double y, NeuralNetwork nn, Color color) {
        position.x = x;
        position.y = y;
        this.nn = nn;
        this.color = color;
    }

    protected Agent() {}

    public double foodInDirection(ArrayList<Food> foods, Vector2D direction, double FOV, double range) {
        for (Food f : foods) {
            if (Math.abs(f.position.subtract(position).angle(direction)) <= FOV/2.0 && f.position.subtract(position).magnitude() < range) {
                // /System.out.println((range-f.position.subtract(position).magnitude())*0.75+range*0.25);
                return ((range-f.position.subtract(position).magnitude())*0.75+range*0.25)/range*10;
            }
        }
        return -10;
    }

    public void update() {}

    public void draw(Graphics2D g2, double GS, double zoom, Vector2D cameraPosition) {}
}
