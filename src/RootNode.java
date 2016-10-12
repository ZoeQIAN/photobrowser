import java.awt.*;
import java.util.ArrayList;

/**
 * Created by zqian on 05/10/2016.
 * Boundary infinite
 * Display nothing
 */
public class RootNode extends Node {

    public RootNode(){
        parent = null;
        children = new ArrayList<>();
    }
    @Override
    protected void paint(Graphics g){
        for(Node n : children){
            n.paint(g);
        }
    }

}
