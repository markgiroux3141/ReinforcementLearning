import java.io.Serializable;

public class Point implements Serializable {
    float X;
    float Y;

    public Point(float x, float y){
        this.X = x;
        this.Y = y;
    }

    public float getX() {
        return X;
    }

    public void setX(float x) {
        X = x;
    }

    public float getY() {
        return Y;
    }

    public void setY(float y) {
        Y = y;
    }
}
