import java.awt.*;
import java.util.ArrayList;
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
        insertPos = pos;
        fontSize = 10;
    }

    private boolean isEditing;

    boolean insertionDisplay;
    int fontSize;

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

    public void addChar(char c){
        if(c == ' '){
            text.add("");
        }
        else if(text.isEmpty()){
            text.add(c+"");
        }
        else{
            text.set(text.size()-1, text.get(text.size()-1)+c);
        }
    }

    @Override
    protected void paint(Graphics2D g){
        g.setColor(Color.black);
        int lineH = g.getFontMetrics().getHeight();
        g.setFont(new Font("New Times Roman",Font.PLAIN,fontSize));
        String line = "";
        int lineN = 0;
        for(String w:text){
            if(g.getFontMetrics().stringWidth(line+w)+ posToParent.x > parent.bound.width){
                if(!line.isEmpty()){
                    g.drawString(line,0,lineN* lineH);
                    lineN+=1;
                }
                while(posToParent.x+g.getFontMetrics().stringWidth(w)>parent.bound.width){
                    for(int i=1; i<=w.length();i++){
                        if(posToParent.x+g.getFontMetrics().stringWidth(w.substring(0,i))>parent.bound.width){
                            line = w.substring(0,i-1);
                            w = w.substring(i-1);
                            break;
                        }
                    }
                    g.drawString(line,0,lineN*lineH);
                    lineN +=1;
                }
                line = w+' ';
            }
            else{
                line += w+' ';
            }
        }
        if(!line.isEmpty()){
            g.drawString(line,0,lineN*lineH);
        }
        insertPos = new Point(g.getFontMetrics().stringWidth(line), lineN*lineH );
        if(isEditing){
            g.setStroke(new BasicStroke(3));
            g.drawLine(insertPos.x, insertPos.y, insertPos.x+5, insertPos.y);
        }
        super.paint(g);
    }
}
