import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.awt.event.InputEvent;

public class MapSimulator implements ISimulator{
    public static final int MOUSE_X_OFFSET = -9;
    public static final int MOUSE_Y_OFFEST = -38;
    private InputEvent inputState;

    private List<Line> lines = new ArrayList<>();
    Point lastPoint = null;
    private boolean drawingShape = false;

    @Override
    public void drawGraphics(Graphics g){
        lines.forEach(l -> {
            g.drawLine((int)l.getStart().getX(),(int)l.getStart().getY(),(int)l.getEnd().getX(), (int)l.getEnd().getY());
        });
    }

    public void drawShapes(){
        MouseEvent e = (MouseEvent)this.inputState;
        int x = e.getX() + MOUSE_X_OFFSET;
        int y = e.getY() + MOUSE_Y_OFFEST;
        if(e.getButton() == MouseEvent.BUTTON1) {
            if(drawingShape == false){
                drawingShape = true;
                lastPoint = new Point(x, y);
            }else{
                Point currentPoint = new Point(x, y);
                Line line = GeometryHelper.createLineFromPoints(lastPoint, currentPoint);
                lines.add(line);
                lastPoint = currentPoint;
            }
        }
        if(e.getButton() == MouseEvent.BUTTON3) {
            drawingShape = false;
        }
    }

    public void printMap(String mapName){
        String str = "";
        for(Line line: lines){
            str += mapName + ".add(GeometryHelper.createLineFromPoints(new Point(" + line.getStart().getX() + "f," + line.getStart().getY() + "f), new Point(" + line.getEnd().getX() + "f," + line.getEnd().getY() + "f)));";
            str += "\n";
        }
        System.out.println(str);
    }

    @Override
    public void run(float runTime){

    }

    @Override
    public void receiveInput(InputEvent e){
        this.inputState = e;
        drawShapes();
    }
}
