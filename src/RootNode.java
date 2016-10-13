import java.awt.*;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;

/**
 * Created by zqian on 05/10/2016.
 * Boundary infinite
 * Display nothing
 */
public class RootNode extends Node {

    public RootNode(){
        super();
        parent = null;
    }
    @Override
    protected void paint(Graphics2D g){
        if(isVisible) {
            super.paint(g);
            for (Node n : children) {
                AffineTransform trns = g.getTransform();
                trns.translate(n.getPos().getX(), n.getPos().getY());
                g.setTransform(trns);
                n.paint(g);
            }
        }
    }

}
