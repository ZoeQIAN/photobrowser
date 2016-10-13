import java.awt.*;

/**
 * Created by zqian on 12/10/2016.
 */
public class ShapeNode extends Node {
    Shape shape;
    public ShapeNode(){
        super();
    }
    public ShapeNode(Shape s){
        super();
        shape = s;
    }
    public void setShape(Shape s){
        shape = s;
    }
    @Override
    protected void paint(Graphics2D g){
        if(isVisible) {
            super.paint(g);
            g.draw(shape);
            g.setColor(fillColor);
            g.fill(shape);
        }
    }
}
