import java.awt.*;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by zqian on 12/10/2016.
 */
public class TextNode extends Node {

    ArrayList<String> text;
    public TextNode(Point pos){
        super();
        text = new ArrayList<>();
        insertionDisplay = false;
        posToParent = pos;
    }

    private boolean isEditing;

    Timer tInsertion;
    boolean insertionDisplay;

    public void setEditing(boolean editing) {
        isEditing = editing;
//        if(isEditing){
//            tInsertion = new Timer();
//            tInsertion.schedule(new TimerTask() {
//                @Override
//                public void run() {
//                    insertionDisplay = !insertionDisplay;
//                }
//            },500,500);
//        }
//        else if(tInsertion != null){
//            tInsertion.cancel();
//            tInsertion = null;
//        }

    }

    Point insertPos;
    public void setInsertion(Point pos){
        insertPos = pos;
        setEditing(true);
    }

    @Override
    protected void paint(Graphics2D g){
        super.paint(g);
//        int lineH = g.getFontMetrics().getHeight();
        int lineH = 10;
        if(isEditing && insertionDisplay){
            g.drawLine(insertPos.x, insertPos.y, insertPos.x, insertPos.y+ lineH);
        }
    }
}
