import javax.imageio.ImageIO;
import javax.swing.*;
import javax.xml.soap.Text;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by zqian on 20/09/2016.
 */
public class PhotoComponent extends JPanel implements MouseListener, MouseMotionListener, KeyListener {


    private boolean isTyping;
    private Point pos;

    // to choose the shape for annotations
    private JToolBar shapeChooser;
    /**
     *  freehand strokes related functions
     *  mouse listeners
     */
    private ArrayList<ArrayList<Point>> strokeSet;
    private ArrayList<Point> linePnts;
    private int upperBorder, downBorder, leftBorder, rightBorder;
    private boolean drawing;
    private RootNode graphicsRoot;      // the root node for the flipped part
    private ShapeNode back;     // for display the blank background of the flipped side
    private TextNode text;      // The now-editing text node
    private ShapeNode shape;
    private PathNode path;

    final static String ELLIPSE = "Ellipse";
    final static String LINE = "Line";
    final static String RECTANGLE = "Rectangle";
    final static String PATH = "Path";

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


        initShapeChooser();
    }

    private void initShapeChooser(){
        shapeChooser = new JToolBar();
        add(shapeChooser,BorderLayout.NORTH);
        shapeChooser.setVisible(false);

        JToggleButton ellipse = new JToggleButton("Ellipse");
        ellipse.setActionCommand(ELLIPSE);
        ellipse.addActionListener(event->shapeChosen(event));
        shapeChooser.add(ellipse);

        JToggleButton rect = new JToggleButton("Rectangle");
        rect.setActionCommand(RECTANGLE);
        rect.addActionListener(event->shapeChosen(event));
        shapeChooser.add(rect);

        JToggleButton line = new JToggleButton("Line");
        line.setActionCommand(LINE);
        line.addActionListener(event->shapeChosen(event));
        shapeChooser.add(line);

        JToggleButton path = new JToggleButton("Path");
        path.setActionCommand(PATH);
        path.addActionListener(event->shapeChosen(event));
        shapeChooser.add(path);

        ButtonGroup shapeGroup = new ButtonGroup();
        shapeGroup.add(ellipse);
        shapeGroup.add(rect);
        shapeGroup.add(line);
        shapeGroup.add(path);

        shapeGroup.setSelected(line.getModel(),true);
    }

    private void shapeChosen(ActionEvent e){
        System.out.println("Shape chosen");
        chosenShape = e.getActionCommand();
    }

    Point pressedPoint;
    String chosenShape;
    private void updateShape(Point p){
        int x = pressedPoint.x-pos.x;
        int y = pressedPoint.y-pos.y;
        int w = p.x - pressedPoint.x;
        int h = p.y - pressedPoint.y;
        switch (chosenShape){
            case ELLIPSE:
                shape.setShape(new Ellipse2D.Float(x,y,w,h));
                break;
            case RECTANGLE:
                shape.setShape(new Rectangle(x,y,w,h));
                break;
            case LINE:
                shape.setShape(new Line2D.Float(x,y,x+w,y+h));
                break;
            case PATH:
                path.addPoint(new Point(p.x-pos.x, p.y-pos.y));
                break;
            default:
                break;
        }
    }


    public void mouseDragged(MouseEvent e){
        if(text!=null){
            text.setEditing(false);
            text = null;
        }
        Point p = e.getPoint();
        if(isFlipped && inBorder(p)){
            updateShape(e.getPoint());
        }
        repaint();
    }

    public void mouseMoved(MouseEvent e){

    }

    public void mouseClicked(MouseEvent e){
        if(e.getClickCount()==2){
            isFlipped = !isFlipped;
            if(isFlipped){
                shapeChooser.setVisible(true);
                chosenShape = LINE;
            }
            else{
                shapeChooser.setVisible(false);
            }
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
        drawing = false;
        if(shape != null && shape.getShape()==null){
            back.removeChild(shape);
        }
        if(path != null && path.isEmpty()){
            back.removeChild(path);
        }
        repaint();
    }

    private boolean inBorder(Point p){
        return p.getX()<=rightBorder && p.getX() >=leftBorder && p.getY()<= downBorder && p.getY()>= upperBorder;
    }

    public void mousePressed(MouseEvent e){
        Point p = e.getPoint();
        if(isFlipped && inBorder(p)){
            pressedPoint = e.getPoint();
            if(chosenShape == PATH){
                path = new PathNode();
                back.addChild(path);
            }
            else {
                shape = new ShapeNode();
                shape.setColor(Color.black, Color.white);
                back.addChild(shape);
            }
        }
        repaint();
    }


    /**
     * Key listeners
     */

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
