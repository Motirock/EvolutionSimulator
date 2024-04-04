package game;

import main.GamePanel;

import java.awt.Graphics2D;
import java.awt.Color;

import java.util.ArrayList;

public class Game {
    private long updates = 0;

    private GamePanel gp;

    final int startingAgents = 500;
    final int maxPrey = 500;
    final int maxFoods = 300;
    double preyProportion = 0.8;

    double scale = 5.0;
    double zoom = 1.0;
    Vector2D cameraPosition = new Vector2D(0, 0);

    int inputSize = 8;
    int outputSize = 2;
    int[] layerSizes = {5, 4};

    public ArrayList<Agent> agents = new ArrayList<Agent>();
    public ArrayList<Food> foods = new ArrayList<Food>();

    public Game(GamePanel gp) {
        this.gp = gp;

        for (int i = 0; i < startingAgents*preyProportion; i++) {
            agents.add(new Prey(Math.random()*1600*scale, Math.random()*900*scale, new NeuralNetwork(inputSize, outputSize, layerSizes), new Color((int) (Math.random()*255), (int) (Math.random()*255), (int) (Math.random()*255))));
        }
        for (int i = (int) (startingAgents*preyProportion); i < startingAgents; i++) {
            agents.add(new Predator(Math.random()*1600*scale, Math.random()*900*scale, new NeuralNetwork(inputSize, outputSize, layerSizes), new Color((int) (Math.random()*255), (int) (Math.random()*255), (int) (Math.random()*255))));
        }

        for (int i = 0; i < maxFoods && false; i++) {
            foods.add(new Food(Math.random()*1600*scale, Math.random()*900*scale));
        }
    }

    public void update() {
        updates++;

        if (gp.keyH.upArrowIsPressed)
            cameraPosition.y -= 10*zoom;
        if (gp.keyH.downArrowIsPressed)
            cameraPosition.y += 10*zoom;
        if (gp.keyH.leftArrowIsPressed)
            cameraPosition.x -= 10*zoom;
        if (gp.keyH.rightArrowIsPressed)
            cameraPosition.x += 10*zoom;

        
        if (gp.mouseScrollAmount > 0) {
            zoom *= 1.1;
            System.out.println("A");
        }
        else if (gp.mouseScrollAmount < 0) {
            zoom /= 1.1;
            System.out.println("B");
        }
        gp.mouseH.mouseScrollAmount = 0;
        // gp.mouseScrollAmount = 0;

        ArrayList<Prey> prey = new ArrayList<Prey>();
        ArrayList<Predator> predators = new ArrayList<Predator>();

        for (Agent a : agents) {
            if (a instanceof Prey) {
                prey.add((Prey) a);
            } else if (a instanceof Predator) {
                predators.add((Predator) a);
            }
        }
        
        Prey.predators = predators;
        Predator.prey = prey;

        //System.out.println(prey.size()+" "+predators.size());
        

        for (Agent a : agents) {
            a.update();

            if (a.alive) {
                // if (a.position.x < 0)
                //     a.position.x = 0;
                // else if (a.position.x > 1600*scale)
                //     a.position.x = 1600*scale;
                // if (a.position.y < 0)
                //     a.position.y = 0;
                // else if (a.position.y > 900*scale)
                //     a.position.y = 900*scale;
                if (a.position.x < 0)
                    a.position.x = 1600*scale+a.position.x;
                else if (a.position.x > 1600*scale)
                    a.position.x = a.position.x % 1600*scale;
                if (a.position.y < 0)
                    a.position.y = 900*scale+a.position.y;
                else if (a.position.y > 900*scale)
                    a.position.y = a.position.y % 900*scale;
            }
        }

        //if (foods.size() < maxFoods && updates % 2 == 0)
        //    foods.add(new Food(Math.random()*1600*scale, Math.random()*900*scale));

        if (updates % 100 == 0) {
            for (int i = 0; i < agents.size(); i++) {
                Agent a = agents.get(i);
                if (!a.alive)
                    continue;
                if (a.fitness > 1) {
                    int r = a.color.getRed()+(int) (Math.random()*10-5);
                    int g = a.color.getGreen()+(int) (Math.random()*10-5);
                    int b = a.color.getBlue()+(int) (Math.random()*10-5);
                    if (r < 0)
                        r = 0;
                    else if (r > 255)
                        r = 255;
                    if (g < 0)
                        g = 0;
                    else if (g > 255)
                        g = 255;
                    if (b < 0)
                        b = 0;
                    else if (b > 255)
                        b = 255;
                    if (a instanceof Prey && prey.size() < maxPrey)
                        agents.add(new Prey(a.position.x, a.position.y, a.nn.getMutated(), new Color(r, g, b)));
                    else if (a instanceof Predator)
                        agents.add(new Predator(a.position.x, a.position.y, a.nn.getMutated(), new Color(r, g, b)));
                    a.fitness--;
                }
            }
        }

        for (int i = 0; i < agents.size(); i++) {
            if (!agents.get(i).alive) {
                agents.remove(i);
                i--;
                //System.out.println("Agent died");
            }
        }
        
        for (int i = 0; i < foods.size(); i++) {
            if (!foods.get(i).alive) {
                foods.remove(i);
                i--;
            }
        }

        for (int i = 0; i < agents.size(); i++) {
            if (!agents.get(i).alive) {
                agents.remove(i);
                i--;
                System.out.println("Agent died");
            }
        }
    }

    public void draw(Graphics2D g2, double GS) {
        for (Food f : foods) {
            if (f.alive)
                f.draw(g2, GS, zoom);
        }

        for (Agent a : agents) {
            if (a.alive)
                a.draw(g2, GS, zoom, cameraPosition);
        }

        //System.out.println("Updates: "+updates);

        if (agents.size() > 0) {
            Agent bestAgent = agents.get(agents.size()-1);
            for (Agent a : agents) {
                if (a.timeAlive > bestAgent.timeAlive && a instanceof Predator) {
                    bestAgent = a;
                }
            }
            if (!bestAgent.alive)
                return;
            // bestAgent.color = Color.RED;
            //System.out.println(bestAgent.color.getRed()+" "+bestAgent.color.getGreen()+" "+bestAgent.color.getBlue()+": "+bestAgent.timeAlive+" "+bestAgent.getClass());
            bestAgent.nn.draw(g2, GS, 800, 450);
            // bestAgent.draw(g2, GS, zoom, cameraPosition);
            //System.out.println()
            // bestAgent.color = Color.WHITE;
            //System.out.println()
        }
    }
}
