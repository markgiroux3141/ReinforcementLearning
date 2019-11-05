import java.io.Serializable;

public class Line implements Serializable {
    float A;
    float B;
    float C;
    Point start;
    Point end;

    public Line(Point origin, float a, float b, float c){
        this.start = origin;
        this.A =a;
        this.B = b;
        this.C = c;
    }

    public Line(Point start, Point end, float a, float b, float c){
        this.start = start;
        this.end = end;
        this.A =a;
        this.B = b;
        this.C = c;
    }

    public Line(float a, float b, float c){
        this.A =a;
        this.B = b;
        this.C = c;
    }

    public float getA() {
        return A;
    }

    public void setA(float a) {
        A = a;
    }

    public float getB() {
        return B;
    }

    public void setB(float b) {
        B = b;
    }

    public float getC() {
        return C;
    }

    public void setC(float c) {
        C = c;
    }

    public Point getStart() {
        return start;
    }

    public void setStart(Point start) {
        this.start = start;
    }

    public Point getEnd() {
        return end;
    }

    public void setEnd(Point end) {
        this.end = end;
    }
}
