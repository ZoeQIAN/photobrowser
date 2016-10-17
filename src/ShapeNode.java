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
        bound = shape.getBounds();
    }
    public void setShape(Shape s){
        shape = s;
    }
    public Shape getShape(){
        return shape;
    }
    @Override
    protected void paint(Graphics2D g){

        if(isVisible) {
            System.out.println("Drawing shape :"+ shape.toString());
            g.setColor(strokeColor);
            g.draw(shape);
            g.setColor(fillColor);
            g.fill(shape);
            super.paint(g);
        }
    }
}
