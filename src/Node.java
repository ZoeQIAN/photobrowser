import java.awt.*;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;

/**
 * Created by zqian on 04/10/2016.
 */
public abstract class Node {

    protected Node parent;
    protected ArrayList<Node> children;
    protected boolean isVisible;

    protected Color fillColor;
    protected Color strokeColor;

    protected int width, height;
    protected Point posToParent;

    Rectangle bound;

    public Node(){
        posToParent = new Point(0,0);
        fillColor = new Color(0,0,0);
        strokeColor = new Color(0,0,0);
        children = new ArrayList<>();
        isVisible = true;
    }

    public Node(Node p){
        this();
        parent = p;
    }

    public void addChild(Node n){
        n.setParent(this);
        children.add(n);
    }

    public void removeChild(Node n){
        n.setParent(null);
        children.remove(n);
    }

    public void setParent(Node n){
        parent = n;
    }

    public Node getParent(){
        return parent;
    }

    public void setVisible(boolean v){
        isVisible = v;
    }


    public boolean isVisible(){
        return isVisible;
    }

    protected void paint(Graphics2D g){
        if(isVisible){
            // draw the children
            for (Node nn : children) {
                // apply affine transform to the graphics
                AffineTransform trns = g.getTransform();
                AffineTransform trns_backup = new AffineTransform(g.getTransform());
                trns.translate(nn.getPos().getX(), nn.getPos().getY());
                g.setTransform(trns);
                nn.paint(g);
                // reset the affine transform
                g.setTransform(trns_backup);
            }
        }

    };

    public int getWidth(){
        return width;
    }

    public int getHeight(){
        return height;
    }

    public void setPos(Point p){
        posToParent = p;
    }

    public Point getPos(){
        return posToParent;
    }

    public Rectangle getBound(){
        return bound;
    }

    public void setBound(Rectangle r){
        bound = r;
    }

    public void setColor(Color strokeC, Color fillC){
        strokeColor = strokeC;
        fillColor = fillC;
    }



}
