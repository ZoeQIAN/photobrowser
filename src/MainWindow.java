import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

public class MainWindow extends JFrame{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;



	private enum viewMode{
		BROWSER,
		SPLIT,
		VIEWER
	}

	viewMode mode;

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
	private int photoIdx;
	private JPanel bPhotoPanel;
	private JPanel sPhotoPanel;

	private ArrayList<PhotoComponent> photoSet;

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

		ButtonGroup labelGroup = new ButtonGroup();

		JToggleButton tBtn = new JToggleButton(FAMILY);
		tBtn.setActionCommand(FAMILY);
		tBtn.addActionListener(event->categoryChosen(event));
		toolBar.add(tBtn);
		labelGroup.add(tBtn);

		tBtn = new JToggleButton(VACATION);
		tBtn.setActionCommand(VACATION);
		tBtn.addActionListener(event->categoryChosen(event));
		toolBar.add(tBtn);
		labelGroup.add(tBtn);

		tBtn = new JToggleButton(SCHOOL);
		tBtn.setActionCommand(SCHOOL);
		tBtn.addActionListener(event->categoryChosen(event));
		toolBar.add(tBtn);
		labelGroup.add(tBtn);

		// add next/previous button
		toolBar.addSeparator();
		JButton previousBtn = new JButton("Previous");
		previousBtn.addActionListener(event->switchPhoto(false));
		toolBar.add(previousBtn);

		JButton nextBtn = new JButton("Next");
		nextBtn.addActionListener(event->switchPhoto(true));
		toolBar.add(nextBtn);


	}
	private void switchPhoto(boolean next){
		if(next){
			photoIdx+=1;
		}
		else{
			photoIdx-=1;
		}
		if(photoIdx == photoSet.size()){
			photoIdx = 0;
		}
		else if(photoIdx == -1){
			photoIdx = photoSet.size()-1;
		}
		setDisplayPhoto(photoSet.get(photoIdx));
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


		photoScrPane = new JScrollPane(photo);
		add(photoScrPane, BorderLayout.CENTER);
		photoSet = new ArrayList<>();
		photoIdx = 0;

		mode = viewMode.VIEWER;

		// window size
		setPreferredSize(new Dimension(600,400));
		pack();
	}

	private void setDisplayPhoto(PhotoComponent p){
		if(photo != null){
			removeMouseListener(photo);
			removeMouseMotionListener(photo);
		}
		if(mode == viewMode.VIEWER) {
			photo = p;
			addMouseListener(photo);
			addMouseMotionListener(photo);
			photoScrPane.setViewportView(photo);
		}
		else if(mode == viewMode.SPLIT){
			if(sPhotoPanel == null){
				sPhotoPanel = new JPanel();
				sPhotoPanel.setLayout(new BoxLayout(sPhotoPanel, BoxLayout.LINE_AXIS));

			}
			else{
				sPhotoPanel.removeAll();
				sPhotoPanel.revalidate();
				sPhotoPanel.repaint();
			}
			int prev, next;
			prev = photoIdx-1<0?photoSet.size()-1:photoIdx-1;
			next = photoIdx+1>=photoSet.size()?0:photoIdx+1;
			photo = p;

			photoSet.get(photoIdx).setPreferredSize(new Dimension(getWidth()/2, getHeight()));
			photoSet.get(prev).setPreferredSize(new Dimension(getWidth()/4, getHeight()));
			photoSet.get(next).setPreferredSize(new Dimension(getWidth()/4, getHeight()));
			sPhotoPanel.add(photoSet.get(prev));
			sPhotoPanel.add(photoSet.get(photoIdx));
			sPhotoPanel.add(photoSet.get(next));
			photoScrPane.setViewportView(sPhotoPanel);
		}
		repaint();
	}

	private void setDisplayPhoto(int idx){
		if(idx >= photoSet.size()){
			setDisplayPhoto(null);
		}
		else{
			setDisplayPhoto(photoSet.get(photoIdx));
		}
	}

	private void importFile(){
		JFileChooser fileChooser = new JFileChooser();
		FileNameExtensionFilter filter = new FileNameExtensionFilter("JPG & GIF images","jpg","gif");
		fileChooser.setFileFilter(filter);
		fileChooser.setMultiSelectionEnabled(true);
		int returnVal = fileChooser.showOpenDialog(this);
	    if(returnVal == JFileChooser.APPROVE_OPTION) {
			File[] files = fileChooser.getSelectedFiles();
	        updateStatus("Open file");

			for(File f:files) {
				photo = new PhotoComponent();
				photo.loadPhoto(f.getPath());
				photoSet.add(photo);
			}
			setDisplayPhoto(photoSet.get(0));

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
		photoSet.remove(photo);
		photoScrPane.remove(photo);
		if(photoIdx>=photoSet.size()){
			photoIdx = 0;
		}
		if(photoSet.isEmpty()){
			setDisplayPhoto(null);
		}
		else {
			setDisplayPhoto(photoSet.get(photoIdx));
		}
		repaint();
	}
	
	private void quitApp(){
		updateStatus("Quit");
		dispose();
		System.exit(0);
	}
	
	private void photoViewer(){
		updateStatus("Photo viewer");
//		getContentPane().removeAll();
		mode = viewMode.VIEWER;
		setDisplayPhoto(photoIdx);
	}
	
	private void browser(){
		updateStatus("Browser");
		mode = viewMode.BROWSER;
		if(bPhotoPanel == null) {
			int rowNum = photoSet.size() / 4;
			bPhotoPanel = new JPanel();
			bPhotoPanel.setLayout(new GridLayout(rowNum>3?rowNum:3,4));
		}



			for (PhotoComponent p : photoSet) {
				p.setPreferredSize(new Dimension(200,200));
				bPhotoPanel.add(p);
			}
			bPhotoPanel.setMaximumSize(new Dimension(getWidth(), bPhotoPanel.getHeight()));
//		}
		photoScrPane.setViewportView(bPhotoPanel);
		repaint();
	}
	
	private void splitMode(){
		updateStatus("Split Mode");

		mode = viewMode.SPLIT;
		setDisplayPhoto(photoIdx);
		repaint();
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
