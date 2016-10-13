import java.awt.*;

/**
 * Created by zqian on 12/10/2016.
 */
public class ImageNode extends Node {
    Image img;
    public ImageNode(Image i){
        img = i;
    }
    @Override
    protected void paint(Graphics2D g){
        g.drawImage(img,posToParent.x,posToParent.y,null);
    }
}
