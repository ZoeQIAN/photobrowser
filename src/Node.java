import java.awt.*;
import java.util.ArrayList;

/**
 * Created by zqian on 04/10/2016.
 */
public abstract class Node {

    protected Node parent;
    protected ArrayList<Node> children;
    protected boolean isVisible;

    protected Color color;

    protected int width, height;
    protected Point posToParent;

    Rectangle bound;

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

    protected abstract void paint(Graphics g);

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



}
