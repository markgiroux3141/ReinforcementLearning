import java.awt.*;
import java.io.Serializable;

public class Ray implements Serializable {
    public static final float INITIAL_COLLISION_DISTANCE = 100f;
    private Point origin;
    private Point collisionPoint;
    private Line line;
    private float angle;
    private float collisionDistance;

    public Ray(Point origin, Line line, float angle){
        this.origin = origin;
        this.line = line;
        this.angle = angle;
        this.collisionPoint = GeometryHelper.getLineEndWithDistance(line, INITIAL_COLLISION_DISTANCE);
        this.line.setEnd(collisionPoint);
        this.collisionDistance = INITIAL_COLLISION_DISTANCE;
    }

    public Ray(Point origin, Point collisionPoint, float collisionDistance, Line line, float angle){
        this.origin = origin;
        this.collisionPoint = collisionPoint;
        this.line = line;
        this.angle = angle;
        this.line.setEnd(collisionPoint);
        this.collisionDistance = collisionDistance;
    }

    public void drawRay(Graphics g){
        Point midPoint = GeometryHelper.getMidPoint(line);
        g.drawString("" + PhysicsHelper.processCollisionForNet(collisionDistance,Simulator.RAY_GRADIENT, Simulator.RAY_SCALING_FACTOR),(int)midPoint.getX(), (int)midPoint.getY());
        g.drawLine((int)line.getStart().getX(), (int)line.getStart().getY(), (int)collisionPoint.getX(), (int)collisionPoint.getY());
    }

    public Point getOrigin() {
        return origin;
    }

    public void setOrigin(Point origin) {
        this.origin = origin;
    }

    public Line getLine() {
        return line;
    }

    public void setLine(Line line) {
        this.line = line;
    }

    public float getAngle() {
        return angle;
    }

    public void setAngle(float angle) {
        this.angle = angle;
    }

    public Point getCollisionPoint() {
        return collisionPoint;
    }

    public void setCollisionPoint(Point collisionPoint) {
        this.collisionPoint = collisionPoint;
    }

    public float getCollisionDistance() {
        return collisionDistance;
    }

    public void setCollisionDistance(float collisionDistance) {
        this.collisionDistance = collisionDistance;
    }
}
