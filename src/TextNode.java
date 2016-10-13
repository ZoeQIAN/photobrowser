import java.awt.*;
import java.util.ArrayList;

/**
 * Created by zqian on 12/10/2016.
 */
public class TextNode extends Node {

    ArrayList<String> text;
    public TextNode(){
        super();
        text = new ArrayList<>();
    }

    @Override
    protected void paint(Graphics2D g){
        super.paint(g);
    }
}
