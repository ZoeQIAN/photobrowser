import com.sun.tools.hat.internal.model.Root;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

/**
 * Created by zqian on 12/10/2016.
 */
public class SceneGraphDisplay extends JComponent {
    private RootNode rNode;
    public SceneGraphDisplay(){

    }
    public SceneGraphDisplay(RootNode r){
        rNode = r;
    }

    static public void main(String[] args) throws IOException {
        JFrame mwin = new JFrame("Test scene graph");
        SceneGraphDisplay display = new SceneGraphDisplay();
        mwin.add(display, BorderLayout.CENTER);

        RootNode root = new RootNode();
        Image img = ImageIO.read(new File("/Users/zoe/Desktop/test.jpg"));
        ImageNode iNode = new ImageNode(img);
        iNode.setPos(new Point(10,10));

        root.addChild(iNode);

        display.setRoot(root);

        mwin.setVisible(true);
        mwin.setPreferredSize(new Dimension(600,400));
        mwin.setSize(new Dimension(600,400));
    }

    public void setRoot(RootNode r){
        rNode = r;
    }

    @Override
    public void paintComponent(Graphics g){
        if(rNode != null){
            rNode.paint(g);
        }
    }
}
