import java.awt.*;
import java.awt.geom.GeneralPath;

/**
 * Created by zqian on 12/10/2016.
 */
public class PathNode extends Node {
    GeneralPath gPath;
    private boolean empty;
    public PathNode(){
        super();
        gPath = new GeneralPath();
        empty = true;
    }
    public void addPoint(Point p){
        if(empty){
            gPath.moveTo(p.x,p.y);
        }
        else {
            gPath.lineTo(p.getX(), p.getY());
        }
        empty = false;
    }

    public boolean isEmpty(){
        return empty;
    }
    @Override
    protected void paint(Graphics2D g){
        if(isVisible) {
            g.setColor(strokeColor);
            g.draw(gPath);
            super.paint(g);
        }
    }
}
