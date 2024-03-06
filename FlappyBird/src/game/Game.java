package game;

import main.GamePanel;

import java.awt.Graphics2D;
import java.lang.reflect.Array;
import java.awt.Color;

import java.util.ArrayList;

public class Game {
    private long updates = 0;

    private GamePanel gp;

    NeuralNetwork nn;

    ArrayList<Bird> birds = new ArrayList<Bird>();
    ArrayList<PipePair> pipePairs = new ArrayList<PipePair>();

    int inputSize = 3;
    int outputSize = 1;
    int[] layerSizes = {4, 3};

    public Game(GamePanel gp) {
        this.gp = gp;

        //nn = new NeuralNetwork(inputSize, outputSize, layerSizes);

        for (int i = 0; i < 1000; i++) {
            birds.add(new Bird(new NeuralNetwork(inputSize, outputSize, layerSizes)));
        }

        for (int i = 0; i < 10; i++) {
            pipePairs.add(new PipePair(200+i*PipePair.pipePairSpacing, Math.random()*600+150, 50, Math.random()*(PipePair.maxGapHeight-PipePair.minGapHeight)+PipePair.minGapHeight));
        }
    }

    public void update() {
        updates++;

        //nn.input[0] = (updates%100-50)/20.0;        

        //nn.feedForward();

        ArrayList<Integer> deadPipePairIndices = new ArrayList<Integer>();
        for (PipePair pipePair : pipePairs) {
            pipePair.update();
            if (pipePair.x+pipePair.width < 0) {
                deadPipePairIndices.add(pipePairs.indexOf(pipePair));
            }
        }

        for (int i = deadPipePairIndices.size()-1; i >= 0; i--) {
            pipePairs.remove((int)deadPipePairIndices.get(i));
            pipePairs.add(new PipePair(pipePairs.get(pipePairs.size()-1).x+PipePair.pipePairSpacing, Math.random()*600+150, 50, Math.random()*(PipePair.maxGapHeight-PipePair.minGapHeight)+PipePair.minGapHeight));
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
            birds.remove((int)deadBirdsIndices.get(i));
            if (Math.random() > 1) {
                birds.add(new Bird(new NeuralNetwork(birds.get(0).brain)));
                birds.get(birds.size()-1).brain.mutate();
            }
            else
                birds.add(new Bird(new NeuralNetwork(inputSize, outputSize, layerSizes)));
        }

        System.out.println(birds.get(0).timeAlive);
    }

    public void draw(Graphics2D g2, double GS) {
        //nn.draw(g2, GS);

        for (int i = 0; i < pipePairs.size(); i++) {
            PipePair pipe = pipePairs.get(i);
            pipe.draw(g2, GS);
        }

        for (int i = 0; i < 1; i++) {
            Bird bird = birds.get(i);
            bird.draw(g2, GS);
        }

        birds.get(0).brain.draw(g2, GS);

        

    }
}
