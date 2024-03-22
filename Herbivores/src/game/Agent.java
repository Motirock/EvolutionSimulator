package game;

import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.BasicStroke;

import java.util.ArrayList;

public class Agent {
    public Vector2D position = new Vector2D();
    public Vector2D velocity = new Vector2D();
    public double radius = 10;
    //public static final double acceleration = 1000.0;
    public static final double maxSpeed = 5;
    public double rotation = 0;
    public double rotationSpeed = 0.05;
    public NeuralNetwork nn;
    public boolean alive = true;
    public int score = 0;
    public int fitness = 0;
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

    public double foodInDirection(ArrayList<Food> foods, Vector2D direction, double FOV, double range) {
        for (Food f : foods) {
            if (Math.abs(f.position.subtract(position).angle(direction)) <= FOV/2.0 && f.position.subtract(position).magnitude() < range) {
                // /System.out.println((range-f.position.subtract(position).magnitude())*0.75+range*0.25);
                return ((range-f.position.subtract(position).magnitude())*0.75+range*0.25)/range*10;
            }
        }
        return -10;

    }

    public void update(ArrayList<Food> foods) {
        if (alive) {
            Food nearestFood = foods.get(0);
            double nearestFoodDistance = Double.MAX_VALUE;
            for (Food f : foods) {
                double distance = Math.sqrt(Math.pow(f.position.x-position.x, 2)+Math.pow(f.position.y-position.y, 2));
                if (distance < nearestFoodDistance) {
                    nearestFood = f;
                    nearestFoodDistance = distance;
                }
            }

            if (nearestFood.isEaten(this)) {
                fitness++;
                hunger = 0;
            }

            //nn.input[0] = nearestFoodDistance;
            double FOV = Math.PI/4;
            nn.input[0] = foodInDirection(foods, new Vector2D(Math.cos(rotation-FOV*7), Math.sin(rotation-FOV*7)), Math.PI/4, 800);
            nn.input[1] = foodInDirection(foods, new Vector2D(Math.cos(rotation-FOV*6), Math.sin(rotation-FOV*6)), Math.PI/4, 800);
            nn.input[2] = foodInDirection(foods, new Vector2D(Math.cos(rotation-FOV*5), Math.sin(rotation-FOV*5)), Math.PI/4, 800);
            nn.input[3] = foodInDirection(foods, new Vector2D(Math.cos(rotation-FOV*4), Math.sin(rotation-FOV*4)), Math.PI/4, 800);
            nn.input[4] = foodInDirection(foods, new Vector2D(Math.cos(rotation-FOV*3), Math.sin(rotation-FOV*3)), Math.PI/4, 800);
            nn.input[5] = foodInDirection(foods, new Vector2D(Math.cos(rotation-FOV*2), Math.sin(rotation-FOV*2)), Math.PI/4, 800);
            nn.input[6] = foodInDirection(foods, new Vector2D(Math.cos(rotation-FOV*7), Math.sin(rotation-FOV*1)), Math.PI/4, 800);
            nn.input[7] = foodInDirection(foods, new Vector2D(Math.cos(rotation), Math.sin(rotation)), Math.PI/4, 800);

            nn.feedForward();

            rotation += (nn.output[0]*2-1)*rotationSpeed;
            rotation = rotation % (2*Math.PI);
            velocity = new Vector2D((nn.output[2]*2-1)*Math.cos(rotation), (nn.output[2]*2-1)*Math.sin(rotation)).multiply(maxSpeed);

            velocity = velocity.limit(maxSpeed);
            hunger += velocity.magnitude()/10;
            hunger++;

            position = position.add(velocity);

            if (hunger >= 500)
                alive = false;

            timeAlive++;
        }
    }

    public void draw(Graphics2D g2, double GS, double scale) {
        if (alive) {
            g2.setColor(color);
            g2.fillOval((int) ((position.x-radius)*GS/scale), (int) ((position.y-radius)*GS/scale), (int) ((2*radius)*GS/scale), (int) ((2*radius)*GS/scale));
            
            g2.setStroke(new BasicStroke(5));
            g2.setColor(color);
            g2.drawLine((int) (position.x*GS/scale), (int) (position.y*GS/scale), (int) ((position.x+Math.cos(rotation)*(radius+5))*GS/scale), (int) ((position.y+Math.sin(rotation)*(radius+5))*GS/scale));
        }
    }
}
