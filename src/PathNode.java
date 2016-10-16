import java.awt.*;
import java.awt.geom.GeneralPath;

/**
 * Created by zqian on 12/10/2016.
 */
public class PathNode extends Node {
    GeneralPath gPath;
    public PathNode(){
        super();
        gPath = new GeneralPath();
        gPath.moveTo(0,0);
    }
    public void addPoint(Point p){
        gPath.lineTo(p.getX(), p.getY());
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
