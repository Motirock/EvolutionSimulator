package game;

public class Neuron {
    int numInputs;
    double[] weights;
    double bias;
    double output;

    public Neuron(int numInputs) {
        this.numInputs = numInputs;
        weights = new double[numInputs];
        for (int i = 0; i < numInputs; i++) {
            weights[i] = -1+2*Math.random();
        }
        bias = -1+2*Math.random();
    }

    public double feedForward(double[] inputs) {
        double sum = 0;
        for (int i = 0; i < numInputs; i++) {
            sum += inputs[i] * weights[i];
        }
        //System.out.println(sum);
        sum += bias;
        //output = Utils.sigmoid(sum);
        output = sum;
        return output;
    }

    static double mutationChancePartial = 0.1;
    static double mutationChanceFull = 0.1;
    static double mutationRatePartial = 0.5;
    public void mutate() {
        for (int i = 0; i < numInputs; i++) {
            if (Math.random() < mutationChancePartial) {
                weights[i] += (-1+2*Math.random())*mutationRatePartial;
            }
            else if (Math.random() < mutationChancePartial+mutationChanceFull) {
                weights[i] = -1+2*Math.random();
            }
        }
        if (Math.random() < mutationRatePartial) {
            bias += (-1+2*Math.random())*mutationRatePartial;
        }
        else if (Math.random() < mutationChancePartial+mutationChanceFull) {
            bias = -1+2*Math.random();
        }
    }

    public Neuron clone() {
        Neuron n = new Neuron(numInputs);
        for (int i = 0; i < numInputs; i++) {
            n.weights[i] = weights[i];
        }
        n.bias = bias;
        return n;
    }

    public Neuron crossover(Neuron n) {
        Neuron child = new Neuron(numInputs);
        for (int i = 0; i < numInputs; i++) {
            if (Math.random() > 0.5) {
                child.weights[i] = weights[i];
            }
            else {
                child.weights[i] = n.weights[i];
            }
        }
        if (Math.random() > 0.5) {
            child.bias = bias;
        }
        else {
            child.bias = n.bias;
        }
        return child;
    }
}
