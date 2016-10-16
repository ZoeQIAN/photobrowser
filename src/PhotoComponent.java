import javax.imageio.ImageIO;
import javax.swing.*;
import javax.xml.soap.Text;
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
        textSet = new ArrayList<>();
        textPos = new ArrayList<>();

    }


    /**
     *  freehand strokes related functions
     *  mouse listeners
     */
    private ArrayList<ArrayList<Point>> strokeSet;
    private ArrayList<Point> linePnts;
    private int upperBorder, downBorder, leftBorder, rightBorder;
    private boolean drawing;
    private RootNode graphicsRoot;
    private ShapeNode back;
    private TextNode text;

    public void mouseDragged(MouseEvent e){
        if(text!=null){
            text.setEditing(false);
            text = null;
        }
        Point p = e.getPoint();
        if(isFlipped && drawing && inBorder(p)){
            linePnts.add(p);
        }
        repaint();
    }

    public void mouseMoved(MouseEvent e){

    }

    public void mouseClicked(MouseEvent e){
        if(e.getClickCount()==2){
            isFlipped = !isFlipped;
            isTyping = false;
            if(graphicsRoot == null){
                graphicsRoot = new RootNode();
                back = new ShapeNode(new Rectangle(photo.getWidth(),photo.getHeight()));
                back.setColor(Color.black, Color.white);
                graphicsRoot.addChild(back);
            }
            back.setPos(pos);
        }
        else if(e.getClickCount() ==1 && isFlipped){
            if(text != null && isTyping){
                text.setEditing(false);
                text = null;
            }
            isTyping = true;
            text = new TextNode(new Point(e.getX()-back.getPos().x, e.getY()-back.getPos().y));
            text.setEditing(true);
            back.addChild(text);
            requestFocusInWindow();
        }
        repaint();
    }

    public void mouseExited(MouseEvent e){
    }

    public void mouseEntered(MouseEvent e){
    }

    public void mouseReleased(MouseEvent e){
        System.out.println("[] Released");
        drawing = false;
    }

    private boolean inBorder(Point p){
        return p.getX()<=rightBorder && p.getX() >=leftBorder && p.getY()<= downBorder && p.getY()>= upperBorder;
    }

    public void mousePressed(MouseEvent e){
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
    private ArrayList< ArrayList <String> > textSet;
    private String word;
    private int lineLen;
    private Point pos;
    private ArrayList<Point> textPos;
    private final int fontSize = 20;
    public void keyTyped(KeyEvent e){
        if(isTyping){
            text.addChar(e.getKeyChar());
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
            pos = new Point(getWidth()/2-photo.getWidth()/2,getHeight()/2-photo.getHeight()/2);
            if(isFlipped){
                back.setPos(pos);
                graphicsRoot.paint(g);
            }
            else{
                g.drawImage(photo,pos.x,pos.y,null);
            }

        }

    }

    public void loadPhoto(String file){
        try{
            photo = ImageIO.read(new File(file));
        }catch(IOException e){
        }
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

    public String getStateInfo() {
        return stateInfo;
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
    private boolean isLoaded;

}
