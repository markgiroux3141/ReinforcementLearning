import java.awt.*;
import java.io.Serializable;

public class Agent implements Comparable, Serializable {
    private static final float MIN_VELOCITY = 0.05f;
    private static final int RADIUS = 5;
    private static final float PI = 3.14159265358f;
    private NeuralNet brain;
    private int numRays;
    private Point position;
    private Point velocity;
    private Point lastVelocity;
    private float speed;
    private float lastSpeed;
    private float cumulativeSpeed;
    private float cumulativeAcceleration;
    private float averageSpeed;
    private float distanceTravelled;
    private Ray[] rays;
    private boolean alive = true;
    private int timeAlive = 0;

    public Agent(Point position, int[] architecture){
        brain = new NeuralNet(architecture);
        this.numRays = architecture[0] - Simulator.NUM_AGENT_OUTPUTS;
        this.position = position;
        this.velocity = new Point(0,0);
        initRays();
    }

    public void runAgent(float[] input){
        if(alive){
            brain.run(input);
            velocity.setX(calculateVelocityFromOutput(brain.getOutput()[0]));
            velocity.setY(calculateVelocityFromOutput(brain.getOutput()[1]));
            calculateSpeed();
            cumulativeSpeed += speed;
            cumulativeAcceleration += (speed - lastSpeed);
            position = PhysicsHelper.getPositionFromVelocity(velocity, position);
            recalculateRays();
            calculateDeathCondition();
            timeAlive++;
            lastSpeed = speed;
            lastVelocity = velocity;
        }
    }

    public void initRays(){
        rays = new Ray[numRays];
        recalculateRays();
    }

    public void recalculateRays(){
        float angleSize = (2*PI)/numRays;
        for(int i=0;i<numRays;i++){
            Point collisionPoint;
            float collisionDistance;
            if(rays[i] != null){
                collisionPoint = rays[i].getCollisionPoint();
                collisionDistance = rays[i].getCollisionDistance();
            }else{
                collisionPoint= new Point(this.position.getX(), this.position.getY());
                collisionDistance = Ray.INITIAL_COLLISION_DISTANCE;
            }
            rays[i] = new Ray(this.position, collisionPoint, collisionDistance, GeometryHelper.createLineFromPointAndAngle(this.position, i * angleSize), i * angleSize);
        }
    }

    public void calculateAgentEndEpoch(){
        distanceTravelled = calculateTotalDistanceTravelled();
        averageSpeed = cumulativeSpeed / timeAlive;
    }

    public void calculateSpeed(){
        speed = PhysicsHelper.getSpeedFromVelocity(velocity);
    }

    public float calculateTotalDistanceTravelled(){
        return GeometryHelper.getDistance(Simulator.AGENT_START, position);
    }

    public void calculateDeathCondition(){
        for(int i=0;i<rays.length;i++){
            if(rays[i].getCollisionDistance() < RADIUS){
                alive = false;
            }
        }
    }

    public void reset(){
        position = Simulator.AGENT_START;
        velocity = new Point(0,0);
        speed = 0;
        alive = true;
        recalculateRays();
    }

    public void drawAgent(Graphics g, int agentId){
        if(!alive) g.setColor(new Color(255,0,0));
        GraphicsHelper.drawCircle((int)position.getX(), (int)position.getY(),RADIUS, g);
        if(!alive) g.setColor(new Color(0,0,0));
        //g.drawString("X " + position.getX() + " Y " + position.getY(),(int)position.getX() - 15, (int)position.getY() - 18);
        //g.drawString("" + agentId,(int)position.getX(), (int)position.getY());
    }

    public float calculateVelocityFromOutput(float output){
        return (output * Simulator.AGENT_VELOCITY_MULTIPLIER * 2) - Simulator.AGENT_VELOCITY_MULTIPLIER;
    }

    @Override
    public int compareTo(Object o){
        return (this.getBrain().fitnessValue < ((Agent) o).getBrain().getFitnessValue() ? -1 : (this.getBrain().fitnessValue == ((Agent) o).getBrain().getFitnessValue() ? 0 : 1));
    }

    public NeuralNet getBrain() {
        return brain;
    }

    public void setBrain(NeuralNet brain) {
        this.brain = brain;
    }

    public Point getPosition() {
        return position;
    }

    public void setPosition(Point position) {
        this.position = position;
    }

    public Point getVelocity() {
        return velocity;
    }

    public void setVelocity(Point velocity) {
        this.velocity = velocity;
    }

    public Ray[] getRays() {
        return rays;
    }

    public void setRays(Ray[] rays) {
        this.rays = rays;
    }

    public boolean isAlive() {
        return alive;
    }

    public void setAlive(boolean alive) {
        this.alive = alive;
    }

    public int getTimeAlive() {
        return timeAlive;
    }

    public void setTimeAlive(int timeAlive) {
        this.timeAlive = timeAlive;
    }

    public float getAverageSpeed() {
        return averageSpeed;
    }

    public void setAverageSpeed(float averageSpeed) {
        this.averageSpeed = averageSpeed;
    }

    public float getDistanceTravelled() {
        return distanceTravelled;
    }

    public void setDistanceTravelled(float distanceTravelled) {
        this.distanceTravelled = distanceTravelled;
    }

    public float getCumulativeAcceleration() {
        return cumulativeAcceleration;
    }

    public void setCumulativeAcceleration(float cumulativeAcceleration) {
        this.cumulativeAcceleration = cumulativeAcceleration;
    }

    public float getCumulativeSpeed() {
        return cumulativeSpeed;
    }

    public void setCumulativeSpeed(float cumulativeSpeed) {
        this.cumulativeSpeed = cumulativeSpeed;
    }

    public Point getLastVelocity() {
        return lastVelocity;
    }

    public void setLastVelocity(Point lastVelocity) {
        this.lastVelocity = lastVelocity;
    }
}
