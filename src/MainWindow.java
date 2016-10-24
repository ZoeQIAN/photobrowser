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
	private boolean filtered;

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
	private ArrayList<PhotoComponent> photoSetToDisplay;

	static final public String ALL = "All";
	static final public String FAMILY = "Family";
	static final public String VACATION = "Vacation";
	static final public String SCHOOL = "School";

	private String category;
	private ButtonGroup labelGroup;
	private JToolBar toolBar;
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
		toolBar = new JToolBar();
		add(toolBar,BorderLayout.PAGE_START);

		ButtonGroup labelGroup = new ButtonGroup();

		JToggleButton tBtn = new JToggleButton(ALL);
		tBtn.setActionCommand(ALL);
		tBtn.addActionListener(event->categoryChosen(event));
		toolBar.add(tBtn);
		labelGroup.add(tBtn);

		tBtn = new JToggleButton(FAMILY);
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

		JButton btn = new JButton("+");
		btn.addActionListener(event->addCategory());
		toolBar.add(btn);

		// add next/previous button
		toolBar.addSeparator();
		JButton previousBtn = new JButton("Previous");
		previousBtn.addActionListener(event->switchPhoto(false));
		toolBar.add(previousBtn);

		JButton nextBtn = new JButton("Next");
		nextBtn.addActionListener(event->switchPhoto(true));
		toolBar.add(nextBtn);


	}

	private void addCategory(){
		String s = (String)JOptionPane.showInputDialog(
				null,
				"Add a category:",
				"Category",
				JOptionPane.PLAIN_MESSAGE
		);
		if(s!=null && s.length()>0){
			JToggleButton newBtn = new JToggleButton(s);

			toolBar.add(newBtn, PhotoComponent.categories.length+1);
			labelGroup.add(newBtn);
			PhotoComponent.addCategory(s);
		}

	}
	private void switchPhoto(boolean next){
		if(next){
			photoIdx+=1;
		}
		else{
			photoIdx-=1;
		}
		if(photoIdx >= photoSetToDisplay.size()){
			photoIdx = 0;
		}
		else if(photoIdx == -1){
			photoIdx = photoSetToDisplay.size()-1;
		}
		setDisplayPhoto(photoSetToDisplay.get(photoIdx));
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

		filtered = false;

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
			prev = photoIdx-1<0?photoSetToDisplay.size()-1:photoIdx-1;
			next = photoIdx+1>=photoSetToDisplay.size()?0:photoIdx+1;
			photo = p;

			photoSetToDisplay.get(photoIdx).setPreferredSize(new Dimension(getWidth()/2, getHeight()));
			photoSetToDisplay.get(prev).setPreferredSize(new Dimension(getWidth()/4, getHeight()));
			photoSetToDisplay.get(next).setPreferredSize(new Dimension(getWidth()/4, getHeight()));
			sPhotoPanel.add(photoSetToDisplay.get(prev));
			sPhotoPanel.add(photoSetToDisplay.get(photoIdx));
			sPhotoPanel.add(photoSetToDisplay.get(next));
			photoScrPane.setViewportView(sPhotoPanel);
		}
		repaint();
	}

	private void setDisplayPhoto(){
		if(photoSetToDisplay.isEmpty()){
			setDisplayPhoto(null);
		}
		else{
			if(photoIdx >= photoSetToDisplay.size()){
				photoIdx = 0;
			}
			setDisplayPhoto(photoSetToDisplay.get(photoIdx));
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
			update();

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

	private void update(){
		filterPhoto();
		if(mode != viewMode.BROWSER) {
				setDisplayPhoto();

		}
		else{
			browser();
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
		setDisplayPhoto();
	}
	
	private void browser(){
		updateStatus("Browser");
		mode = viewMode.BROWSER;
		if(bPhotoPanel == null) {
			bPhotoPanel = new JPanel();
			bPhotoPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		}
		else{
			bPhotoPanel.removeAll();
		}
		int colNum = 4;
		int i=0;
		JPanel tp = new JPanel(new GridLayout(1,colNum));
		for (PhotoComponent p : photoSetToDisplay) {
			if(i%colNum == 0){
				tp = new JPanel(new GridLayout(1,colNum));
				bPhotoPanel.add(tp);
				i = 0;
			}
			p.setPreferredSize(new Dimension(100,100));
			tp.add(p);
			i++;
		}
		bPhotoPanel.add(tp);
//			bPhotoPanel.setMaximumSize(new Dimension(getWidth(), bPhotoPanel.getHeight()));
//		}
		photoScrPane.setViewportView(bPhotoPanel);
		repaint();
	}
	
	private void splitMode(){
		updateStatus("Split Mode");

		mode = viewMode.SPLIT;
		setDisplayPhoto();
		repaint();
	}
	
	private void categoryChosen(ActionEvent e){
		Object src = e.getSource();
		if(src instanceof JToggleButton){
			JToggleButton btn = (JToggleButton)src;
			if(btn.isSelected() && !btn.getText().equals(ALL)){
				filtered = true;
				updateStatus(btn.getText()+" toggled");
				category = btn.getText();
			}
			else{
				updateStatus(btn.getText()+" untoggled");
				filtered = false;
				category = null;
			}
			update();
		}
	}
	private void filterPhoto(){
		if(category == null){
			photoSetToDisplay = photoSet;
		}
		else{
			photoSetToDisplay = new ArrayList<>();
			for(PhotoComponent p : photoSet){
				if(p.isCategory(category)) {
					photoSetToDisplay.add(p);
				}
			}
		}
	}


	
}
