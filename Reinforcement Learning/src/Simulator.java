import java.awt.Graphics;
import java.awt.event.InputEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

public class Simulator implements ISimulator{
    public static final float PI = 3.14159265358f;
    public static final int SIMULATOR_SIZE_X = 1550;
    public static final int SIMULATOR_SIZE_Y = 1000;
    public static final int NUM_AGENTS = 60;
    public static final int NUM_AGENT_RAYS = 8;
    public static final int NUM_HIDDEN_NODES = 10;
    public static final int NUM_AGENT_OUTPUTS = 2;
    public static final int[] BRAIN_ARCHITECTURE = new int[]{NUM_AGENT_RAYS + NUM_AGENT_OUTPUTS, NUM_HIDDEN_NODES, NUM_AGENT_OUTPUTS};
    public static final Point AGENT_START = new Point(1375,400);
    public static final float TIME_CONST = 0.01f;
    public static final float AGENT_VELOCITY_MULTIPLIER = 8.0f;
    public static final float RAY_GRADIENT = 0.1f;
    public static final float RAY_SCALING_FACTOR = 1000f;
    public static final Point CENTER_ANCHOR_POINT = new Point(700,500);
    public static final Point TARGET_POINT = new Point(1475,150);
    public static final String AGENT_SAVE_FILE = "Agent.txt";
    public static final float RUNNING_SLOW_FACTOR = 2f;
    //Genetic Algorithm
    public static final float WEIGHT_MUTATION_RATE = 0.3f;
    public static final float WEIGHT_MUTATION_EFFECT = 0.01f;
    public static final float WEIGHT_MUTATION_REPLACE_RATE = 0.01f;
    public static final float BIAS_MUTATION_RATE = 0.3f;
    public static final float BIAS_MUTATION_EFFECT = 0.01f;
    public static final float BIAS_MUTATION_REPLACE_RATE = 0.01f;
    public static final float BREEDING_FRACTION = 0.5f;
    public static final float DEAD_FITNESS = -999999;
    private static final int TIME_STEPS_PER_EPOCH = 500;

    public enum SIMULATOR_STATE{
        TRAINING,
        RUNNING
    }

    private SIMULATOR_STATE simulatorState = SIMULATOR_STATE.TRAINING;
    private Maps maps = new Maps();
    private List<Line> activeMap = maps.map4;
    private Agent[] agent;
    private Agent loadedAgent = null;
    private GeneticAlgorithm geneticAlgorithm;
    private int timeStepsFromLastEpoch = 0;

    public Simulator(){
        agent = new Agent[NUM_AGENTS];
        for(int i=0;i<NUM_AGENTS;i++){
            agent[i] = new Agent(AGENT_START, BRAIN_ARCHITECTURE);
        }
        geneticAlgorithm = new GeneticAlgorithm(agent, WEIGHT_MUTATION_RATE, WEIGHT_MUTATION_EFFECT, WEIGHT_MUTATION_REPLACE_RATE, BIAS_MUTATION_RATE, BIAS_MUTATION_EFFECT, BIAS_MUTATION_REPLACE_RATE, BREEDING_FRACTION);
    }

    public void drawGraphicsTraining(Graphics g){
        activeMap.forEach(l -> {
            g.drawLine((int)l.getStart().getX(),(int)l.getStart().getY(),(int)l.getEnd().getX(), (int)l.getEnd().getY());
        });
        for(int i=0;i<agent.length;i++){
            agent[i].drawAgent(g,i);
//            for(int n=0;n<agent[i].getRays().length;n++){
//                agent[i].getRays()[n].drawRay(g);
//            }
        }
        g.drawString("" + geneticAlgorithm.getBestFitnessValue(),10, 10);
    }

    public void drawGraphicsRunning(Graphics g){
        activeMap.forEach(l -> {
            g.drawLine((int)l.getStart().getX(),(int)l.getStart().getY(),(int)l.getEnd().getX(), (int)l.getEnd().getY());
        });
        loadedAgent.drawAgent(g,0);
    }

    @Override
    public void drawGraphics(Graphics g){
        if(simulatorState == SIMULATOR_STATE.TRAINING){
            drawGraphicsTraining(g);
        }else if(simulatorState == SIMULATOR_STATE.RUNNING){
            drawGraphicsRunning(g);
        }
    }

    public void runTraining(float runTime){
        double counter = 0;
        int runTimeTicks = (int)(runTime/TIME_CONST);
        while(counter < runTimeTicks){
            for(int i=0;i<agent.length;i++){
                float[] rayCollisions = PhysicsHelper.getRayCollisions(activeMap, agent[i].getRays(), RAY_GRADIENT, RAY_SCALING_FACTOR);
                float[] input = Utilities.appendArrays(rayCollisions, new float[]{agent[i].getVelocity().getX(), agent[i].getVelocity().getY()});
                agent[i].runAgent(input);
            }
            if(timeStepsFromLastEpoch >= TIME_STEPS_PER_EPOCH){
                for(int i=0;i<agent.length;i++){
                    agent[i].calculateAgentEndEpoch();
                }
                geneticAlgorithm.run();
                for(int i=0;i<agent.length;i++){
                    agent[i].reset();
                }
                timeStepsFromLastEpoch = 0;
            }
            timeStepsFromLastEpoch++;
            counter ++;
        }
    }

    public void runSingleAgent(float runTime){
        double counter = 0;
        int runTimeTicks = (int)(runTime/(TIME_CONST * RUNNING_SLOW_FACTOR));
        while(counter < runTimeTicks){
            float[] rayCollisions = PhysicsHelper.getRayCollisions(activeMap, loadedAgent.getRays(), RAY_GRADIENT, RAY_SCALING_FACTOR);
            float[] input = Utilities.appendArrays(rayCollisions, new float[]{loadedAgent.getVelocity().getX(), loadedAgent.getVelocity().getY()});
            loadedAgent.runAgent(input);
            counter ++;
        }
    }

    @Override
    public void run(float runTime){
        if(simulatorState == SIMULATOR_STATE.TRAINING){
            runTraining(runTime);
        }else if(simulatorState == SIMULATOR_STATE.RUNNING){
            runSingleAgent(runTime);
        }
    }

    public void loadAgent(){
        if(loadedAgent == null){
            loadedAgent = (Agent)ObjectWriteRead.readObjectFromFile(AGENT_SAVE_FILE);
        }
        loadedAgent.reset();
        simulatorState = SIMULATOR_STATE.RUNNING;
    }

    @Override
    public void receiveInput(InputEvent e){

    }

    public GeneticAlgorithm getGeneticAlgorithm() {
        return geneticAlgorithm;
    }

    public void setGeneticAlgorithm(GeneticAlgorithm geneticAlgorithm) {
        this.geneticAlgorithm = geneticAlgorithm;
    }

    public SIMULATOR_STATE getSimulatorState() {
        return simulatorState;
    }

    public void setSimulatorState(SIMULATOR_STATE simulatorState) {
        this.simulatorState = simulatorState;
    }
}
