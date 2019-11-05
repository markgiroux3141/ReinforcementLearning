import java.awt.*;
import java.awt.event.InputEvent;

public interface ISimulator {
    void run(float runTime);
    void drawGraphics(Graphics g);
    void receiveInput(InputEvent e);
}
