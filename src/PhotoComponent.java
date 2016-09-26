import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by zqian on 20/09/2016.
 */
public class PhotoComponent extends JComponent implements MouseListener, MouseMotionListener {

    public PhotoComponent() {
        setSize(new Dimension(200,300));
        setPreferredSize(new Dimension(200,300));
        isLoaded = false;
        isFlipped = false;
        drawing = false;
        addMouseListener(this);
        addMouseMotionListener(this);
        strokeSet = new ArrayList<>();
    }
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

        if(e.getClickCount()==2){
            isFlipped = !isFlipped;
            System.out.println("[Debug] Flipped");
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
    @Override
    protected void paintComponent(Graphics g){
        super.paintComponent(g);
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


                for(ArrayList<Point> l : strokeSet){
                    for(int i=0; i<l.size()-1; i++){
                        g.drawLine(l.get(i).x, l.get(i).y, l.get(i+1).x,l.get(i+1).y);
                    }
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
