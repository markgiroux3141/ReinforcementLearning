import java.util.Random;
import java.io.Serializable;

public class NeuralNet implements Serializable{
    public static final float INITIALIZATION_VAL = 0.5f;
    Random random = new Random();
    float[][][] weights;
    float[][] biases;
    float[][] neuronVals;
    int[] architecture;
    float[] output;
    float fitnessValue;

    public NeuralNet(int[] architecture, float[][][] weights, float[][] biases){
        this.weights = weights;
        this.biases = biases;
        this.architecture = architecture;
        int numLayers = architecture.length;
        neuronVals = new float[numLayers][];

        for(int i=0;i<numLayers;i++){
            int numNeurons = architecture[i];
            neuronVals[i] = new float[numNeurons];
        }
    }

    public NeuralNet(int[] architecture){
        this.architecture = architecture;
        int numLayers = architecture.length;

        neuronVals = new float[numLayers][];
        biases = new float[numLayers - 1][];
        weights = new float[numLayers - 1][][];

        for(int i=0;i<numLayers;i++){
            int numNeurons = architecture[i];
            neuronVals[i] = new float[numNeurons];
        }

        for(int i=0;i<numLayers - 1;i++){
            int numNeurons = architecture[i+1];
            biases[i] = new float[numNeurons];
            weights[i] = new float[numNeurons][];
            for(int n=0;n<numNeurons;n++){
                biases[i][n] = getRandRange(INITIALIZATION_VAL);
                weights[i][n] = new float[architecture[i]];
                for(int q=0;q<architecture[i];q++){
                    weights[i][n][q] = getRandRange(INITIALIZATION_VAL);
                }
            }
        }
    }

    public float[] run(float[] inputs){
        for(int i=0;i<inputs.length;i++){
            neuronVals[0][i] = inputs[i];
        }
        for(int i=1;i<architecture.length;i++){
            for(int n=0;n<architecture[i];n++){
                for(int q=0;q<architecture[i-1];q++){
                    neuronVals[i][n] += neuronVals[i-1][q] * weights[i-1][n][q];
                }
                neuronVals[i][n] += biases[i-1][n];
                neuronVals[i][n] = sigmoid(neuronVals[i][n]);
                //neuronVals[i][n] = tanh(neuronVals[i][n]);
                //neuronVals[i][n] = relu(neuronVals[i][n]);
                //neuronVals[i][n] = leakyRelu(neuronVals[i][n], 0.1f);
            }
        }
        this.output = neuronVals[neuronVals.length - 1];
//        System.out.println("    Input " + inputs[0] + " and " + inputs[1] + " Output " + output[0]);
        return neuronVals[neuronVals.length - 1];
    }

    public float tanh(float x){
        return ((float)Math.exp(2*x) - 1f)/((float)Math.exp(2*x) + 1f);
    }

    public float sigmoid(float x){
        return 1f / (1f + (float)Math.exp(-x));
    }

    public float relu(float x){
        return x>0?x:0;
    }

    public float leakyRelu(float x, float scale){
        return x>0?x:x*scale;
    }

    public float getRand(){
        return (random.nextFloat() * 2f) - 1f;
    }

    public float getRandRange(float range){
        return (random.nextFloat() * range*2f) - range;
    }

    public float[][][] getWeights() {
        return weights;
    }

    public void setWeights(float[][][] weights) {
        this.weights = weights;
    }

    public float[][] getBiases() {
        return biases;
    }

    public void setBiases(float[][] biases) {
        this.biases = biases;
    }

    public float[][] getNeuronVals() {
        return neuronVals;
    }

    public void setNeuronVals(float[][] neuronVals) {
        this.neuronVals = neuronVals;
    }

    public int[] getArchitecture() {
        return architecture;
    }

    public void setArchitecture(int[] architecture) {
        this.architecture = architecture;
    }

    public float getFitnessValue() {
        return fitnessValue;
    }

    public void setFitnessValue(float fitnessValue) {
        this.fitnessValue = fitnessValue;
    }

    public float[] getOutput() {
        return output;
    }

    public void setOutput(float[] output) {
        this.output = output;
    }
}
