import java.util.List;

public class PhysicsHelper {
    public static final float INITIAL_MIN_DIST = 999999;
    public static final float EPSILON = 0.05f;

    public static Point getPositionFromVelocity(Point velocity, Point prevPosition){
        float posX = prevPosition.getX() + velocity.getX();
        float posY = prevPosition.getY() + velocity.getY();
        return new Point(posX, posY);
    }

    public static float getSpeedFromVelocity(Point velocity){
        float x = velocity.getX();
        float y = velocity.getY();
        return (float)Math.sqrt(x*x + y*y);
    }

    public static float[] getRayCollisions(List<Line> lines, Ray[] rays, float gradient, float scaler){
        float[] rayCollisions = new float[rays.length];
        for(int i=0;i<rays.length;i++){
            float minDist = INITIAL_MIN_DIST;
            Point minIntersection = null;
            for(Line line:lines){
                Point intersection = GeometryHelper.findIntersection(rays[i].getLine(), line);
                if(GeometryHelper.isPointOnLine(intersection, line) && isRayFacingFront(rays[i], intersection)){
                    float dist = GeometryHelper.getDistance(rays[i].getOrigin(), intersection);
                    if(dist < minDist){
                        minDist = dist;
                        minIntersection = intersection;
                    }
                }
            }
            rays[i].setCollisionPoint(minIntersection);
            rays[i].setCollisionDistance(minDist);
            rayCollisions[i] = processCollisionForNet(minDist,gradient, scaler);
        }
        return rayCollisions;
    }

    public static float processCollisionForNet(float value, float gradient, float scaler){
        return (1f/ (float)Math.exp((value * gradient))) * scaler;
    }

    public static boolean isRayFacingFront(Ray ray, Point point){
        float xComp = (point.getX() - ray.getLine().getStart().getX()) * (float)Math.cos(ray.getAngle());
        float yComp = (point.getY() - ray.getLine().getStart().getY()) * (float)Math.sin(ray.getAngle());
        if(Math.abs(xComp) < EPSILON) xComp = 0;
        if(Math.abs(yComp) < EPSILON) yComp = 0;
        return (xComp >= 0) && (yComp <= 0);
    }
}
