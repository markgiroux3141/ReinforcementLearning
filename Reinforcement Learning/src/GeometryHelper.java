public class GeometryHelper {

    public static Line createLineFromPoints(Point point1, Point point2){
        float x1 = point1.getX();
        float x2 = point2.getX();
        float y1 = point1.getY();
        float y2 = point2.getY();

        float A = y1 - y2;
        float B = x2 - x1;
        float C = x1*y2 - x2*y1;

        return new Line(point1, point2, A, B, C);
    }

    public static Line createLineFromPointAndAngle(Point point, float angle){
        float x1 = point.getX();
        float y1 = point.getY();
        float x2 = x1 + (float)Math.cos(angle);
        float y2 = y1 - (float)Math.sin(angle);
        return createLineFromPoints(point, new Point(x2,y2));
    }

    public static void setLineEndWithDistance(Line line, float dist){
        float a = line.getA();
        float b = line.getB();
        float normFactor = (float)Math.sqrt(a*a + b*b);
        float xBasis = b/normFactor;
        float yBasis = -a/normFactor;
        line.setEnd(new Point(line.getStart().getX() + (xBasis * dist),line.getStart().getY() + (yBasis * dist)));
    }

    public static Point getLineEndWithDistance(Line line, float dist){
        float a = line.getA();
        float b = line.getB();
        float normFactor = (float)Math.sqrt(a*a + b*b);
        float xBasis = -b/normFactor;
        float yBasis = a/normFactor;
        return new Point(line.getStart().getX() + (xBasis * dist),line.getStart().getY() + (yBasis * dist));
    }

    public static Point findIntersection(Line line1, Line line2){
        float a1 = line1.getA();
        float a2 = line2.getA();
        float b1 = line1.getB();
        float b2 = line2.getB();
        float c1 = line1.getC();
        float c2 = line2.getC();

        float x = (b1*c2 - b2*c1)/(a1*b2 - a2*b1);
        float y = (c1*a2 - c2*a1)/(a1*b2 - a2*b1);
        return new Point(x,y);
    }

    public static boolean isPointOnLine(Point point,Line line) {
        Point start = line.getStart();
        Point end = line.getEnd();
        float dotproduct = (point.getX() - start.getX()) * (end.getX() - start.getX()) + (point.getY() - start.getY())*(end.getY() - start.getY());
        float squaredlengthba = (end.getX() - start.getX())*(end.getX() - start.getX()) + (end.getY() - start.getY())*(end.getY() - start.getY());
        return (dotproduct >= 0 && dotproduct < squaredlengthba)? true: false;
    }

    public static float getDistance(Point point1, Point point2){
        return (float)Math.sqrt(getSqrDistance(point1, point2));
    }

    public static float getSqrDistance(Point point1, Point point2){
        float x1 = point1.getX();
        float y1 = point1.getY();
        float x2 = point2.getX();
        float y2 = point2.getY();
        return ((x2 - x1) * (x2 - x1)) + ((y2 - y1) * (y2 - y1));
    }

    public static float dotProduct(Point point1, Point point2){
        return (point1.getX() * point2.getX()) + (point1.getY() * point2.getY());
    }

    public static Point getMidPoint(Line line){
        float x = (line.getEnd().getX() + line.getStart().getX())/2f;
        float y = (line.getEnd().getY() + line.getStart().getY())/2f;
        return new Point(x,y);
    }

    public static float getAngleFromPoints(Point center, Point edge){
        float x = edge.getX() - center.getX();
        float y = center.getY() - edge.getY();
        float result = (float)Math.atan2(y,x);
        return (y < 0)? result + 2*Simulator.PI: result;
    }
}
