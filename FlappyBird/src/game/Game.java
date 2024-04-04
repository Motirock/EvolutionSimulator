package game;

import main.GamePanel;

import java.awt.Graphics2D;
import java.lang.reflect.Array;
import java.awt.Color;

import java.util.ArrayList;

public class Game {
    private long updates = 0;

    private GamePanel gp;

    final int maxBirds = 100;

    NeuralNetwork nn;

    ArrayList<Bird> birds = new ArrayList<Bird>();
    ArrayList<PipePair> pipePairs = new ArrayList<PipePair>();

    int inputSize = 3;
    int outputSize = 1;
    int[] layerSizes = {4, 3};

    public Game(GamePanel gp) {
        this.gp = gp;

        //nn = new NeuralNetwork(inputSize, outputSize, layerSizes);

        for (int i = 0; i < 10; i++) {
            pipePairs.add(new PipePair(400+i*PipePair.pipePairSpacing, Math.random()*600+150, Math.random()*(PipePair.maxGapHeight-PipePair.minGapHeight)+PipePair.minGapHeight));
        }
        Bird.startingY = pipePairs.get(0).gapY;

        for (int i = 0; i < maxBirds; i++) {
            birds.add(new Bird(new NeuralNetwork(inputSize, outputSize, layerSizes)));
        }
    }

    Bird parent1 = new Bird(new NeuralNetwork(inputSize, outputSize, layerSizes));
    Bird parent2 = new Bird(new NeuralNetwork(inputSize, outputSize, layerSizes));
    public void update() {
        updates++;

        //nn.input[0] = (updates%100-50)/20.0;        

        //nn.feedForward();

        if (birds.size() == 0) {
            pipePairs.clear();
            for (int i = 0; i < 10; i++) {
                pipePairs.add(new PipePair(400+i*PipePair.pipePairSpacing, Math.random()*600+150, Math.random()*(PipePair.maxGapHeight-PipePair.minGapHeight)+PipePair.minGapHeight));
            }
            Bird.startingY = pipePairs.get(0).gapY;

            System.out.println("Best time alive: "+parent1.timeAlive);
            parent1.timeAlive = 0;
            parent2.timeAlive = 0;
            birds.add(parent1);
            birds.add(parent2);
            birds.add(new Bird(parent1.brain.crossover(parent2.brain)));
            for (int i = 0; i < maxBirds-3; i++) {
                NeuralNetwork brain; 
                if (Math.random() < 0.95) {
                    brain = parent1.brain.crossover(parent2.brain);
                    brain.mutate();
                }
                else
                    brain = new NeuralNetwork(inputSize, outputSize, layerSizes);
                birds.add(new Bird(brain));
            }
        }

        ArrayList<Integer> deadPipePairIndices = new ArrayList<Integer>();
        for (PipePair pipePair : pipePairs) {
            pipePair.update();
            if (pipePair.x+pipePair.width < 0) {
                deadPipePairIndices.add(pipePairs.indexOf(pipePair));
            }
        }

        for (int i = deadPipePairIndices.size()-1; i >= 0; i--) {
            pipePairs.remove((int)deadPipePairIndices.get(i));
            pipePairs.add(new PipePair(pipePairs.get(pipePairs.size()-1).x+PipePair.pipePairSpacing, Math.random()*600+150, Math.random()*(PipePair.maxGapHeight-PipePair.minGapHeight)+PipePair.minGapHeight));
        }

        PipePair nextPipePair = pipePairs.get(0);
        for (int i = 1; i < pipePairs.size(); i++) {
            if (nextPipePair.x+nextPipePair.width > Bird.x) {
                break;
            }
            nextPipePair = pipePairs.get(i);
        }

        ArrayList<Integer> deadBirdsIndices = new ArrayList<Integer>();
        for (int i = 0; i < birds.size(); i++) {
            Bird bird = birds.get(i);
            bird.update(nextPipePair);
            if (bird.y < 0 || bird.y > 900 || (Bird.x > nextPipePair.x && Bird.x < nextPipePair.x+nextPipePair.width && (bird.y < nextPipePair.gapY-nextPipePair.gapHeight/2 || bird.y > nextPipePair.gapY+nextPipePair.gapHeight/2))) {
                deadBirdsIndices.add(i);
            }
        }


        for (int i = deadBirdsIndices.size()-1; i >= 0; i--) {
            if (birds.get(i).timeAlive > parent1.timeAlive) {
                parent2 = parent1;
                parent1 = birds.get(i);
            }
            else if (birds.get(i).timeAlive > parent2.timeAlive) {
                parent2 = birds.get(i);
            }
            birds.remove((int)deadBirdsIndices.get(i));
        }

        // System.out.println(birds.get(0).timeAlive);
        // double averageTimeAlive = 0;
        // for (int i = 0; i < birds.size(); i++) {
        //     averageTimeAlive += birds.get(i).timeAlive;
        // }
        // averageTimeAlive /= birds.size();
        // System.out.println(averageTimeAlive+"\n");
    }

    public void draw(Graphics2D g2, double GS) {
        //nn.draw(g2, GS);

        for (int i = 0; i < pipePairs.size(); i++) {
            PipePair pipe = pipePairs.get(i);
            pipe.draw(g2, GS);
        }

        for (int i = 0; i < 100 && i < birds.size(); i++) {
            Bird bird = birds.get(i);
            bird.draw(g2, GS);
        }

        if (birds.size() > 0)
            birds.get(0).brain.draw(g2, GS, 300, 450);
        if (birds.size() > 1)
            birds.get(1).brain.draw(g2, GS, 1000, 450);

        

    }
}
