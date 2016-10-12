import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

public class MainWindow extends JFrame{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static void main(String args[]){
		MainWindow win = new MainWindow();
		win.setVisible(true);

	}
	
	private JMenuBar menuBar;
	private JMenu mFile;
	private JMenu mView;
	private JLabel statusBar;
	private JScrollPane photoScrPane;
	private PhotoComponent photo;

	static final private String FAMILY = "Family";
	static final private String VACATION = "Vacation";
	static final private String SCHOOL = "School";

	private void initMenuBar(){
		// menu bar
		menuBar = new JMenuBar();
		mFile = new JMenu("File");
		mView = new JMenu("View");

		menuBar.add(mFile);
		menuBar.add(mView);


		setJMenuBar(menuBar);

		JMenuItem menuItem = new JMenuItem("Import");
		menuItem.addActionListener(event->importFile());
		mFile.add(menuItem);

		menuItem = new JMenuItem("Delete");
		menuItem.addActionListener(event->deletePhoto());
		mFile.add(menuItem);

		menuItem = new JMenuItem("Quit");
		menuItem.addActionListener(event->quitApp());
		mFile.add(menuItem);

		menuItem = new JMenuItem("Photo viewer");
		menuItem.addActionListener(event->photoViewer());
		mView.add(menuItem);

		menuItem = new JMenuItem("Browser");
		menuItem.addActionListener(event->browser());
		mView.add(menuItem);

		menuItem = new JMenuItem("Split mode");
		menuItem.addActionListener(event->splitMode());
		mView.add(menuItem);
	}
	private void initStatusBar(){
		//status bar

		JPanel statusPanel = new JPanel();
		statusPanel.setBorder(new EtchedBorder(EtchedBorder.LOWERED));
		add(statusPanel, BorderLayout.SOUTH);
		statusPanel.setPreferredSize(new Dimension(getWidth(),28));
		statusPanel.setLayout(new BoxLayout(statusPanel, BoxLayout.X_AXIS));
		statusBar = new JLabel("Status bar");
		statusBar.setHorizontalAlignment(SwingConstants.LEFT);
		statusPanel.add(statusBar);
	}
	private void initToolBar(){
		//tool bar
		JToolBar toolBar = new JToolBar();
		add(toolBar,BorderLayout.PAGE_START);

		JToggleButton tBtn = new JToggleButton(FAMILY);
		tBtn.setActionCommand(FAMILY);
		tBtn.addActionListener(event->categoryChosen(event));
		toolBar.add(tBtn);

		tBtn = new JToggleButton(VACATION);
		tBtn.setActionCommand(VACATION);
		tBtn.addActionListener(event->categoryChosen(event));
		toolBar.add(tBtn);

		tBtn = new JToggleButton(SCHOOL);
		tBtn.setActionCommand(SCHOOL);
		tBtn.addActionListener(event->categoryChosen(event));
		toolBar.add(tBtn);
	}
	private void initPhotoComp(){
        photo = new PhotoComponent();
        photoScrPane = new JScrollPane(photo);
        add(photoScrPane,BorderLayout.CENTER);
    }

	public MainWindow(){
		super("Photo Browser");

        //initialization
		initMenuBar();
		initStatusBar();
		initToolBar();
		//initPhotoComp();

		// window size
		setPreferredSize(new Dimension(600,400));
		pack();
	}
	
	private void importFile(){
		JFileChooser fileChooser = new JFileChooser();
		FileNameExtensionFilter filter = new FileNameExtensionFilter("JPG & GIF images","jpg","gif");
		fileChooser.setFileFilter(filter);
		int returnVal = fileChooser.showOpenDialog(this);
	    if(returnVal == JFileChooser.APPROVE_OPTION) {
			String file = fileChooser.getSelectedFile().getPath();
	        updateStatus("Open file: " + file);

            photo=new PhotoComponent();
			photoScrPane = new JScrollPane(photo);
            photo.loadPhoto(file);
            add(photoScrPane,BorderLayout.CENTER);
            addMouseListener(photo);
			addMouseMotionListener(photo);
	     }
	}
	
	private void updateStatus(String str) {
		updateStatus(str,true);
	}
	private void updateStatus(String str, Boolean update){
		if(update){
			statusBar.setText(str);
		}
		else{
			statusBar.setText(statusBar.getText()+" "+str);
		}
	}
	
	private void deletePhoto(){
		updateStatus("Delete");
		photoScrPane.remove(photo);
		remove(photoScrPane);
		photo = null;
		repaint();
	}
	
	private void quitApp(){
		updateStatus("Quit");
		dispose();
		System.exit(0);
	}
	
	private void photoViewer(){
		updateStatus("Photo viewer");
	}
	
	private void browser(){
		updateStatus("Browser");
	}
	
	private void splitMode(){
		updateStatus("Split Mode");
	}
	
	private void categoryChosen(ActionEvent e){
		Object src = e.getSource();
		if(src instanceof JToggleButton){
			JToggleButton btn = (JToggleButton)src;
			if(btn.isSelected()){
				updateStatus(btn.getText()+" toggled");
			}
			else{
				updateStatus(btn.getText()+" untoggled");
			}
		}
	}
	
}
