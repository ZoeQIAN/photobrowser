import java.awt.*;

/**
 * Created by zqian on 12/10/2016.
 */
public class ShapeNode extends Node {
    Shape shape;
    public ShapeNode(Shape s){
        shape = s;
    }
    @Override
    protected void paint(Graphics g){
        Graphics2D g2 = (Graphics2D)g;
        g2.draw(shape);
    }
}
