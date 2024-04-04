package game;

import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.BasicStroke;

import java.util.ArrayList;

public class Prey extends Agent {
    public Prey(Vector2D position, NeuralNetwork nn) {
        this.position = position;
        this.nn = nn;
    }

    public Prey(Vector2D position, NeuralNetwork nn, Color color) {
        this.position = position;
        this.nn = nn;
        this.color = color;
    }

    public Prey(double x, double y, NeuralNetwork nn) {
        position.x = x;
        position.y = y;
        this.nn = nn;
    }

    public Prey(double x, double y, NeuralNetwork nn, Color color) {
        position.x = x;
        position.y = y;
        this.nn = nn;
        this.color = color;
    }

    public double predatorInDirection(Vector2D direction, double FOV, double range) {
        for (Predator p : predators) {
            if (Math.abs(p.position.subtract(position).angle(direction)) <= FOV/2.0 && p.position.subtract(position).magnitude() < range) {
                // /System.out.println((range-f.position.subtract(position).magnitude())*0.75+range*0.25);
                return Math.sqrt((range-p.position.subtract(position).magnitude())/range);
            }
        }
        return -10;
    }

    public static ArrayList<Predator> predators;
    @Override
    public void update() {
        if (alive) {
            double FOV = Math.PI/4;
            nn.input[0] = -predatorInDirection(new Vector2D(Math.cos(rotation-FOV*7), Math.sin(rotation-FOV*7)), Math.PI/4, 50);
            nn.input[1] = -predatorInDirection(new Vector2D(Math.cos(rotation-FOV*6), Math.sin(rotation-FOV*6)), Math.PI/4, 50);
            nn.input[2] = -predatorInDirection(new Vector2D(Math.cos(rotation-FOV*5), Math.sin(rotation-FOV*5)), Math.PI/4, 50);
            nn.input[3] = -predatorInDirection(new Vector2D(Math.cos(rotation-FOV*4), Math.sin(rotation-FOV*4)), Math.PI/4, 50);
            nn.input[4] = -predatorInDirection(new Vector2D(Math.cos(rotation-FOV*3), Math.sin(rotation-FOV*3)), Math.PI/4, 50);
            nn.input[5] = -predatorInDirection(new Vector2D(Math.cos(rotation-FOV*2), Math.sin(rotation-FOV*2)), Math.PI/4, 50);
            nn.input[6] = -predatorInDirection(new Vector2D(Math.cos(rotation-FOV*7), Math.sin(rotation-FOV*1)), Math.PI/4, 50);
            nn.input[7] = -predatorInDirection(new Vector2D(Math.cos(rotation), Math.sin(rotation)), Math.PI/4, 100);

            nn.feedForward();

            rotation += (nn.output[0]*2-1)*rotationSpeed;
            rotation = rotation % (2*Math.PI);
            velocity = new Vector2D((nn.output[1]*2-1)*Math.cos(rotation), (nn.output[1]*2-1)*Math.sin(rotation)).multiply(maxSpeed);
            fitness += (1.0-(velocity.magnitude()/maxSpeed))*0.01;
            fitness += 0.001;

            velocity = velocity.limit(maxSpeed);

            position = position.add(velocity);

            

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

            g2.setColor(Color.WHITE);
            g2.fillOval((int) ((position.x-radius/2.0-cameraPosition.x)*GS/zoom), (int) ((position.y-radius/2.0-cameraPosition.y)*GS/zoom), (int) ((radius)*GS/zoom), (int) ((radius)*GS/zoom));
        }
    }
}
