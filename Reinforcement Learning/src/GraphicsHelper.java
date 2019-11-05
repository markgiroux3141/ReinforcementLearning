import java.awt.*;

public class GraphicsHelper {
    public static void drawCircle(int x, int y, int radius, Graphics g){
        g.drawOval(x - radius, y - radius, radius * 2, radius * 2);
    }
}
