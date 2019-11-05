import java.util.Random;

public class NeuralNetHelper {
    Random random = new Random();

    public NeuralNet neuralNetCrossOver(NeuralNet neuralNet1, NeuralNet neuralNet2, float weightMutationRate, float weightMutationEffect, float weightMutationReplaceRate, float biasMutationRate, float biasMutationEffect, float biasMutationReplaceRate){
        int[] architecture = neuralNet1.getArchitecture();
        NeuralNet newNet = new NeuralNet(architecture);
        for(int i=1; i<architecture.length;i++){
            for(int n=0;n<architecture[i];n++){
                int randomBias = randBit(0.5f);
                int biasMutation = randBit(biasMutationRate);
                int biasMutationReplace = randBit(biasMutationReplaceRate);
                newNet.getBiases()[i-1][n] = (randomBias == 0)? neuralNet1.getBiases()[i-1][n]: neuralNet2.getBiases()[i-1][n];
                if(biasMutation == 1) newNet.getBiases()[i-1][n] += getRandRange(biasMutationEffect);
                if(biasMutationReplace == 1) newNet.getBiases()[i-1][n] = getRand();
                for (int q=0;q<architecture[i-1];q++){
                    int randomWeight = randBit(0.5f);
                    int weightMutation = randBit(weightMutationRate);
                    int weightMutationReplace = randBit(weightMutationReplaceRate);
                    newNet.getWeights()[i-1][n][q] = (randomWeight == 0)? neuralNet1.getWeights()[i-1][n][q]: neuralNet2.getWeights()[i-1][n][q];
                    if(weightMutation == 1) newNet.getWeights()[i-1][n][q] += getRandRange(weightMutationEffect);
                    if(weightMutationReplace == 1) newNet.getWeights()[i-1][n][q] = getRand();
                }
            }
        }
        return newNet;
    }

    public float[][][] copyWeights(float[][][] weights){
        float[][][] newWeights = new float[weights.length][][];
        for(int i=0;i<weights.length;i++){
            newWeights[i] = new float[weights[i].length][];
            for(int n=0;n<weights[i].length;n++){
                newWeights[i][n] = new float[weights[i][n].length];
                for(int q=0;q<weights[i][n].length;q++){
                    newWeights[i][n][q] = weights[i][n][q];
                }
            }
        }
        return newWeights;
    }

    public float[][] copyBiases(float[][] biases){
        float[][] newBiases = new float[biases.length][];
        for(int i=0;i<biases.length;i++){
            newBiases[i] = new float[biases[i].length];
            for(int n=0;n<biases[i].length;n++){
                newBiases[i][n] = biases[i][n];
            }
        }
        return newBiases;
    }

    public void printWeights(float[][][] weights){
        System.out.println("--------------WEIGHTS--------------");
        for(int i=0;i<weights.length;i++){
            System.out.println("Layer " + i);
            for(int n=0;n<weights[i].length;n++){
                System.out.println("    Neuron " + n);
                for(int q=0;q<weights[i][n].length;q++){
                    System.out.println("        Weight " + q);
                    System.out.println("        Value " + weights[i][n][q]);
                }
            }
        }
    }

    public void printBiases(float[][] biases){
        System.out.println("--------------BIASES--------------");
        for(int i=0;i<biases.length;i++){
            System.out.println("Layer " + i);
            for(int n=0;n<biases[i].length;n++){
                System.out.println("    Neuron " + n);
                System.out.println("    Value " + biases[i][n]);
            }
        }
    }

    public int randBit(float probability){
        return random.nextFloat() > probability? 0: 1;
    }

    public float getRand(){
        return (random.nextFloat() * 2f) - 1f;
    }

    public float getRandRange(float range){
        return (random.nextFloat() * range*2f) - range;
    }
}
