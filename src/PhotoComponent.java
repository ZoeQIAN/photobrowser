import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by zqian on 20/09/2016.
 */
public class PhotoComponent extends JComponent implements MouseListener, MouseMotionListener, KeyListener {

    public PhotoComponent() {
        setSize(new Dimension(200,300));
        setPreferredSize(new Dimension(200,300));
        isLoaded = false;
        isFlipped = false;
        drawing = false;
        addMouseListener(this);
        addMouseMotionListener(this);
        strokeSet = new ArrayList<>();

        setFocusable(true);

        addKeyListener(this);
        isTyping = false;
    }


    /**
     *  freehand strokes related functions
     *  mouse listeners
     */
    private ArrayList<ArrayList<Point>> strokeSet;
    private ArrayList<Point> linePnts;
    private int upperBorder, downBorder, leftBorder, rightBorder;
    private boolean drawing;

    public void mouseDragged(MouseEvent e){
        System.out.println("[Debug] Dragged");
        Point p = e.getPoint();
        if(isFlipped && drawing && inBorder(p)){
            linePnts.add(p);
        }
        repaint();
    }

    public void mouseMoved(MouseEvent e){

    }
    public void mouseClicked(MouseEvent e){
        System.out.println("[Debug] Clicked");
        if(e.getClickCount()==2){
            isFlipped = !isFlipped;
            isTyping = false;
        }
        else if(e.getClickCount() ==1){
            isTyping = true;
            lineLen = 0;
            texts = new ArrayList<>();
            texts.add("");
            pos = e.getPoint();
            word="";
            System.out.println("[Debug] Typing mode");
            requestFocusInWindow();
        }
        repaint();
    }

    public void mouseExited(MouseEvent e){
        System.out.println("[Debug] Exited");
    }

    public void mouseEntered(MouseEvent e){
        System.out.println("[Debug] Entered");
    }

    public void mouseReleased(MouseEvent e){
        System.out.println("[Debug] Released");
        drawing = false;
    }

    private boolean inBorder(Point p){
        return p.getX()<=rightBorder && p.getX() >=leftBorder && p.getY()<= downBorder && p.getY()>= upperBorder;
    }

    public void mousePressed(MouseEvent e){
        System.out.println("[Debug] Pressed");
        Point p = e.getPoint();
        if(isFlipped && inBorder(p)){
            drawing = true;
            linePnts = new ArrayList<>();
            linePnts.add(p);
            strokeSet.add(linePnts);
        }
    }


    /**
     * Key listeners
     */
    private boolean isTyping;
    private ArrayList<String> texts;
    private String word;
    private int lineLen;
    private Point pos;
    private final int fontSize = 20;
    public void keyTyped(KeyEvent e){
        if(isTyping){
            if(e.getKeyChar()==' '){
                texts.add("");
            }
            else{
                texts.set(texts.size()-1,texts.get(texts.size()-1)+e.getKeyChar());
            }
            repaint();

        }
    }

    public void keyPressed(KeyEvent e){

    }

    public void keyReleased(KeyEvent e){

    }


    @Override
    protected void paintComponent(Graphics gg){
        super.paintComponent(gg);
        Graphics2D g = (Graphics2D)gg;
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        //drawing a background
        g.setColor(Color.lightGray);
        g.fillRect(0,0,getWidth(),getHeight());
        leftBorder = getWidth()/2-photo.getWidth()/2;
        rightBorder = getWidth()/2+photo.getWidth()/2;
        upperBorder = getHeight()/2-photo.getHeight()/2;
        downBorder = getHeight()/2+photo.getHeight()/2;
        //then load the photo
        if(isLoaded){

            if(isFlipped){
                g.setColor(Color.white);
                g.fillRect(getWidth()/2-photo.getWidth()/2,getHeight()/2-photo.getHeight()/2,photo.getWidth(),photo.getHeight());
                g.setColor(Color.black);

                // freehand strokes
                for(ArrayList<Point> l : strokeSet){
                    for(int i=0; i<l.size()-1; i++){
                        g.drawLine(l.get(i).x, l.get(i).y, l.get(i+1).x,l.get(i+1).y);
                    }
                }
                

                // render text
                g.setFont(new Font("New Times Roman",Font.PLAIN,fontSize));
                String line = "";
                int lineNum = 0;
                for(String w : texts){
                    if(g.getFontMetrics().stringWidth(line+w)+pos.x > rightBorder){
                        if(!line.isEmpty()) {
                            g.drawString(line, pos.x, pos.y + lineNum * g.getFontMetrics().getHeight());
                            lineNum+=1;
                        }
                        while(pos.x+g.getFontMetrics().stringWidth(w)>rightBorder){
                            for(int i=1; i<=w.length();i++){
                                if(pos.x+g.getFontMetrics().stringWidth(w.substring(0,i))>rightBorder){
                                    line = w.substring(0,i-1);
                                    w = w.substring(i-1);
                                    break;
                                }
                            }
                            g.drawString(line,pos.x,pos.y+lineNum*g.getFontMetrics().getHeight());
                            lineNum +=1;
                        }
                        line = w+' ';

                    }
                    else{
                        line+=w+' ';
                    }
                }
                if(!line.isEmpty()){
                    g.drawString(line,pos.x,pos.y+lineNum*g.getFontMetrics().getHeight());
                }

            }
            else{
                g.drawImage(photo,getWidth()/2-photo.getWidth()/2,getHeight()/2-photo.getHeight()/2,this);
            }

        }

    }



    public void loadPhoto(String file){
        try{
            photo = ImageIO.read(new File(file));
        }catch(IOException e){
            System.out.println("[Debug] Cannot open file "+file);
        }
        System.out.println("[Debug] photo loaded");
        isLoaded = true;
        setSize(new Dimension(photo.getWidth(),photo.getHeight()));
        setPreferredSize(new Dimension(photo.getWidth(),photo.getHeight()));
        revalidate();
    }



    /**
    * get and set functions
    */
    public boolean isFlipped() {
        return isFlipped;
    }

    public BufferedImage getPhoto() {
        return photo;
    }

    public String getAnno() {
        return anno;
    }

    public String getStateInfo() {
        return stateInfo;
    }

    public void setAnno(String anno) {
        this.anno = anno;
    }

    public void setFlipped(boolean flipped) {
        isFlipped = flipped;
    }

    public void setStateInfo(String stateInfo) {
        this.stateInfo = stateInfo;
    }

    private String stateInfo;
    private BufferedImage photo;
    private boolean isFlipped;
    private String anno;
    private boolean isLoaded;

}
