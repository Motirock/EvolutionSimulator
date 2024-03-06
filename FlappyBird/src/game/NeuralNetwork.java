package game;

import java.awt.Graphics2D;
import java.awt.Color;

public class NeuralNetwork {
    int inputSize;
    int outputSize;
    int[] layerSizes;
    int layerCount;

    public double[] input = new double[inputSize];
    public double[] output = new double[outputSize];
    public double[] thresholds = new double[outputSize];
    public Layer[] layers = new Layer[layerCount];
    
    public NeuralNetwork(int inputSize, int outputSize, int...layerSizes) {
        this.inputSize = inputSize;
        this.outputSize = outputSize;
        this.layerCount = layerSizes.length+1;
        this.layers = new Layer[layerCount];

        input = new double[inputSize];
        output = new double[outputSize];
        thresholds = new double[outputSize];
        layers = new Layer[layerCount];

        for (int i = 0; i < inputSize; i++) {
            input[i] = Math.random()*2-1;
        }

        for (int i = 0; i < outputSize; i++) {
            thresholds[i] = Math.random();
        }

        for (int i = 0; i < layerCount; i++) {
            int numNeurons;
            if (i < layerCount-1) 
                numNeurons = layerSizes[i];
            else
                numNeurons = outputSize;
            int numInputs;
            if (i == 0)
                numInputs = inputSize;
            else
                numInputs = layerSizes[i-1];

            layers[i] = new Layer(numNeurons, numInputs);
        }
    }

    public NeuralNetwork(NeuralNetwork nn) {
        this.inputSize = nn.inputSize;
        this.outputSize = nn.outputSize;
        this.layerSizes = nn.layerSizes;
        this.layerCount = nn.layerCount;

        this.input = new double[inputSize];
        this.output = new double[outputSize];
        this.thresholds = nn.thresholds.clone();
        this.layers = nn.layers.clone();
    }

    public void mutate() {
        for (int i = 0; i < layerCount; i++) {
            layers[i].mutate();
        }
    }

    public void feedForward() {
        double[] inputs = input.clone();
        for (int i = 0; i < layers.length; i++) {
            inputs = layers[i].feedForward(inputs);
        }
        for (int i = 0; i < outputSize; i++) {
            output[i] = Utils.sigmoid(inputs[i]);
        }
    }


    double neuronDiameter = 100;
    double neuronRadius = neuronDiameter/2.0;
    double verticalGap = 150;
    double horizontalGap = 250;
    double leftEdge = 800;
    public void draw(Graphics2D g2, double GS) {
        for (int i = 0; i < inputSize; i++) {
            g2.setColor(new Color((int) (Utils.sigmoid(input[i])*255), (int) (Utils.sigmoid(input[i])*255), (int) (Utils.sigmoid(input[i])*255)));
            g2.fillOval(
                (int) ((leftEdge-neuronDiameter/2.0)*GS), 
                (int) ((450-(input.length-1)*(verticalGap/2.0) + (verticalGap)*i-neuronDiameter/2.0)*GS), 
                (int) ((neuronDiameter)*GS), 
                (int) ((neuronDiameter)*GS)
            );
        }

        double[] inputs = input.clone();
        for (int i = 0; i < layers.length; i++) {
            inputs = layers[i].feedForward(inputs);
            
            for (int j = 0; j < layers[i].neurons.length; j++) {
                g2.setColor(new Color((int) (Utils.sigmoid(inputs[j])*255), (int) (Utils.sigmoid(inputs[j])*255), (int) (Utils.sigmoid(inputs[j])*255)));
                g2.fillOval(
                    (int) ((leftEdge+horizontalGap*(i+1)-neuronDiameter/2.0)*GS), 
                    (int) ((450-(layers[i].neurons.length-1)*verticalGap/2.0 + (verticalGap)*j-neuronDiameter/2.0)*GS), 
                    (int) ((neuronDiameter)*GS), 
                    (int) ((neuronDiameter)*GS)
                );
            }
        }

        inputs = input.clone();
        for (int i = 0; i < layers.length; i++) {
            double[] inputCopy = new double[inputs.length];
            for (int j = 0; j < inputs.length; j++) {
                inputCopy[j] = Utils.sigmoid(inputs[j]);
            }
            inputs = layers[i].feedForward(inputs);
            
            for (int j = 0; j < layers[i].neurons.length; j++) {
                for (int k = 0; k < inputCopy.length; k++) {
                    g2.setColor(new Color((int) (inputCopy[k]*255), (int) (inputCopy[k]*255), (int) (inputCopy[k]*255)));
                    if (i == 0) {
                        g2.drawLine(
                            (int) ((leftEdge+horizontalGap*i)*GS), 
                            (int) ((450-(inputSize-1)*verticalGap/2.0 + (verticalGap)*k)*GS), 
                            (int) ((leftEdge+horizontalGap*(i+1))*GS), 
                            (int) ((450-(layers[i].neurons.length-1)*verticalGap/2.0 + (verticalGap)*j)*GS)
                        );
                    }
                    else {
                        g2.drawLine(
                            (int) ((leftEdge+horizontalGap*i)*GS), 
                            (int) ((450-(layers[i-1].neurons.length-1)*verticalGap/2.0 + (verticalGap)*k)*GS), 
                            (int) ((leftEdge+horizontalGap*(i+1))*GS), 
                            (int) ((450-(layers[i].neurons.length-1)*verticalGap/2.0 + (verticalGap)*j)*GS)
                        );
                    }
                }
            }
        }


    }
}
