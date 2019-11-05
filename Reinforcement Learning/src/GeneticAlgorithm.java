import java.io.ObjectOutputStream;
import java.util.Arrays;
import java.util.Collections;

public class GeneticAlgorithm {
    public static final float MIN_FITNESS = -999999f;

    NeuralNetHelper netHelper;
    Agent[] population;
    float weightMutationRate;
    float weightMutationEffect;
    float weightMutationReplaceRate;
    float biasMutationRate;
    float biasMutationEffect;
    float biasMutationReplaceRate;
    int numChildren;
    float breedingFraction;
    float bestFitnessValue;
    float[][][] bestWeights;
    float[][] bestBiases;
    boolean saveAgentPending = false;

    public GeneticAlgorithm(Agent[] population, float weightMutationRate, float weightMutationEffect, float weightMutationReplaceRate, float biasMutationRate, float biasMutationEffect, float biasMutationreplaceRate,  float breedingFraction){
        netHelper = new NeuralNetHelper();
        this.population = population;
        this.weightMutationRate = weightMutationRate;
        this.weightMutationEffect = weightMutationEffect;
        this.weightMutationReplaceRate = weightMutationReplaceRate;
        this.biasMutationRate = biasMutationRate;
        this.biasMutationEffect = biasMutationEffect;
        this.biasMutationReplaceRate = biasMutationreplaceRate;

        bestFitnessValue = MIN_FITNESS;

        this.breedingFraction = breedingFraction;
        this.numChildren = (int)(2f / breedingFraction);
    }

    public void run(){
        evaluateFitness();
        breedPopulation();
        System.out.println(bestFitnessValue);
    }

    public void run(int epochs){
        for(int i=0;i<epochs;i++){
            evaluateFitness();
            if(epochs%100 == 0) System.out.println("Best Fitness = " + bestFitnessValue);
            if(i != epochs - 1) breedPopulation();
        }
    }

    public void evaluateFitness(){
        for(int i=0;i<population.length;i++){
            //Distance From Start Method
//            population[i].getBrain().setFitnessValue(population[i].getDistanceTravelled());
            //Theta Method (move counterclockwise around a track)
            float x = population[i].getPosition().getX();
            float y = population[i].getPosition().getY();
            population[i].getBrain().setFitnessValue(GeometryHelper.getAngleFromPoints(Simulator.CENTER_ANCHOR_POINT, new Point(x,y)));
            //Move towards a target method
//            float distToTarget = GeometryHelper.getDistance(population[i].getPosition(),Simulator.TARGET_POINT);
//            population[i].getBrain().setFitnessValue(-distToTarget);
            //Move forward in x direction
//            population[i].getBrain().setFitnessValue(population[i].getPosition().getX());
        }
    }

    public void breedPopulation(){
        Arrays.sort(population, Collections.reverseOrder());
        if(saveAgentPending == true){
            population[0].setBrain(new NeuralNet(Simulator.BRAIN_ARCHITECTURE, bestWeights, bestBiases));
            ObjectWriteRead.writeObjectToFile(Simulator.AGENT_SAVE_FILE, population[0]);
            saveAgentPending = false;
        }
        if(population[0].getBrain().getFitnessValue() > bestFitnessValue){
            bestFitnessValue = population[0].getBrain().getFitnessValue();
            bestWeights = netHelper.copyWeights(population[0].getBrain().getWeights());
            bestBiases = netHelper.copyBiases(population[0].getBrain().getBiases());
        }
        NeuralNet[] newPop = new NeuralNet[population.length];
        for(int i=0;i<(int)(population.length * breedingFraction);i+=2){
            for(int n=0;n<numChildren;n++){
                NeuralNet child = netHelper.neuralNetCrossOver(population[i].getBrain(), population[i+1].getBrain(), weightMutationRate, weightMutationEffect, weightMutationReplaceRate, biasMutationRate, biasMutationEffect, biasMutationReplaceRate);
                newPop[(int)(i/breedingFraction) + n] = child;
            }
        }
        for(int i=0;i<population.length;i++){
            population[i].setBrain(newPop[i]);
        }
    }

    public void saveAgentAtNextIteration(){
        saveAgentPending = true;
    }

    public Agent[] getPopulation() {
        return population;
    }

    public void setPopulation(Agent[] population) {
        this.population = population;
    }

    public float getBestFitnessValue() {
        return bestFitnessValue;
    }

    public void setBestFitnessValue(float bestFitnessValue) {
        this.bestFitnessValue = bestFitnessValue;
    }

    public float[][][] getBestWeights() {
        return bestWeights;
    }

    public void setBestWeights(float[][][] bestWeights) {
        this.bestWeights = bestWeights;
    }

    public float[][] getBestBiases() {
        return bestBiases;
    }

    public void setBestBiases(float[][] bestBiases) {
        this.bestBiases = bestBiases;
    }
}
