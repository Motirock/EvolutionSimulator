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

    static double mutationRate = 0.5;
    public void mutate() {
        for (int i = 0; i < numInputs; i++) {
            if (Math.random() < 0.1) {
                weights[i] += (-1+2*Math.random())*mutationRate;
            }
        }
        if (Math.random() < 0.1) {
            bias += (-1+2*Math.random())*mutationRate;
        }
    }
}
