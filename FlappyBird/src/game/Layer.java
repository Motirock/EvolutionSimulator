package game;

public class Layer {
    int numNeurons;
    Neuron[] neurons;

    public Layer(int numNeurons, int numInputs) {
        this.numNeurons = numNeurons;
        neurons = new Neuron[numNeurons];
        for (int i = 0; i < numNeurons; i++) {
            neurons[i] = new Neuron(numInputs);
        }
    }

    public double[] feedForward(double[] inputs) {
        double[] outputs = new double[numNeurons];
        for (int i = 0; i < numNeurons; i++) {
            outputs[i] = neurons[i].feedForward(inputs);
        }
        return outputs;
    }

    public void mutate() {
        for (int i = 0; i < numNeurons; i++) {
            neurons[i].mutate();
        }
    }

    public Layer clone() {
        Layer l = new Layer(numNeurons, neurons[0].numInputs);
        for (int i = 0; i < numNeurons; i++) {
            l.neurons[i] = neurons[i].clone();
        }
        return l;
    }

    public Layer crossover(Layer l) {
        Layer child = new Layer(numNeurons, neurons[0].numInputs);
        for (int i = 0; i < numNeurons; i++) {
            child.neurons[i] = neurons[i].crossover(l.neurons[i]);
        }
        return child;
    }
}
