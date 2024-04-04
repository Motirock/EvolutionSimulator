package game;

import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.BasicStroke;

import java.util.ArrayList;

public class Predator extends Agent {
    double rotationSpeed = 0.02;
    double maxSpeed = 10;

    public Predator(Vector2D position, NeuralNetwork nn) {
        this.position = position;
        this.nn = nn;
    }

    public Predator(Vector2D position, NeuralNetwork nn, Color color) {
        this.position = position;
        this.nn = nn;
        this.color = color;
    }

    public Predator(double x, double y, NeuralNetwork nn) {
        position.x = x;
        position.y = y;
        this.nn = nn;
    }

    public Predator(double x, double y, NeuralNetwork nn, Color color) {
        position.x = x;
        position.y = y;
        this.nn = nn;
        this.color = color;
    }

    public double preyInDirection(Vector2D direction, double FOV, double range) {
        for (Prey p : prey) {
            if (Math.abs(p.position.subtract(position).angle(direction)) <= FOV/2.0 && p.position.subtract(position).magnitude() < range) {
                // /System.out.println((range-f.position.subtract(position).magnitude())*0.75+range*0.25);
                return 1+Math.sqrt((range-p.position.subtract(position).magnitude())/range);
            }
        }
        return -1;
    }

    public static ArrayList<Prey> prey;
    @Override
    public void update() {
        if (alive) {
            for (Prey p : prey) {
                Food f = new Food(p.position);
                if (f.isEaten(this)) {
                    fitness++;
                    hunger = 0;
                    p.alive = false;
                }
            }

            double FOV = Math.PI/16;
            nn.input[0] = preyInDirection(new Vector2D(Math.cos(rotation-FOV*4), Math.sin(rotation-FOV*4)), Math.PI/4, 200);
            nn.input[1] = preyInDirection(new Vector2D(Math.cos(rotation-FOV*3), Math.sin(rotation-FOV*3)), Math.PI/4, 200);
            nn.input[2] = preyInDirection(new Vector2D(Math.cos(rotation-FOV*2), Math.sin(rotation-FOV*2)), Math.PI/4, 200);
            nn.input[3] = preyInDirection(new Vector2D(Math.cos(rotation-FOV*1), Math.sin(rotation-FOV*1)), Math.PI/4, 200);
            nn.input[4] = preyInDirection(new Vector2D(Math.cos(rotation+FOV*0), Math.sin(rotation+FOV*0)), Math.PI/4, 200);
            nn.input[5] = preyInDirection(new Vector2D(Math.cos(rotation+FOV*1), Math.sin(rotation+FOV*1)), Math.PI/4, 200);
            nn.input[6] = preyInDirection(new Vector2D(Math.cos(rotation+FOV*2), Math.sin(rotation+FOV*2)), Math.PI/4, 200);
            nn.input[7] = preyInDirection(new Vector2D(Math.cos(rotation+FOV*3), Math.sin(rotation+FOV*3)), Math.PI/4, 200);

            nn.feedForward();

            rotation += (nn.output[0]*2-1)*rotationSpeed;
            rotation = rotation % (2*Math.PI);
            velocity = new Vector2D((nn.output[1])*Math.cos(rotation), (nn.output[1])*Math.sin(rotation)).multiply(maxSpeed);

            velocity = velocity.limit(maxSpeed);
            hunger += velocity.magnitude()/maxSpeed*0.5;
            hunger += 1;

            position = position.add(velocity);

            if (hunger >= 500)
                alive = false;

            timeAlive++;
        }
    }

    @Override
    public void draw(Graphics2D g2, double GS, double zoom, Vector2D cameraPosition) {
        if (alive) {
            g2.setColor(color);
            g2.fillOval((int) ((position.x-radius-cameraPosition.x)*GS/zoom), (int) ((position.y-radius-cameraPosition.y)*GS/zoom), (int) ((2*radius)*GS/zoom), (int) ((2*radius)*GS/zoom));
            
            g2.setStroke(new BasicStroke(5));
            g2.setColor(color);
            g2.drawLine((int) ((position.x-cameraPosition.x)*GS/zoom), (int) ((position.y-cameraPosition.y)*GS/zoom), (int) ((position.x+Math.cos(rotation)*(radius+5)-cameraPosition.x)*GS/zoom), (int) ((position.y+Math.sin(rotation)*(radius+5)-cameraPosition.y)*GS/zoom));
            
            g2.setColor(Color.BLACK);
            g2.fillOval((int) ((position.x-radius/2.0-cameraPosition.x)*GS/zoom), (int) ((position.y-radius/2.0-cameraPosition.y)*GS/zoom), (int) ((radius)*GS/zoom), (int) ((radius)*GS/zoom));
        }
    }
}
