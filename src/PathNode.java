import java.awt.*;
import java.awt.geom.GeneralPath;

/**
 * Created by zqian on 12/10/2016.
 */
public class PathNode extends Node {
    GeneralPath gPath;
    public PathNode(GeneralPath gp){
        gPath = gp;
    }
    @Override
    protected void paint(Graphics g){

    }
}
