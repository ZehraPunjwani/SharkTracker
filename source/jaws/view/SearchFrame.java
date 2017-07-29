package jaws.view;

import jaws.controller.JawsController;

import static javax.swing.GroupLayout.Alignment.BASELINE;
import static javax.swing.GroupLayout.Alignment.LEADING;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.GroupLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;

/**
 * 
 * @author Zehra Punjwani, Meghana Santhosh, Kent Millamena, and Riya Karia
 *
 */
@SuppressWarnings({"unchecked", "rawtypes", "serial" })
public class SearchFrame extends JFrame{

	private String logoPath = "../Resources/Images/ErrorMessageShark.png";
	
	private JPanel jpOptionsPane;
	private JPanel searchButtonPanel;
	private JLabel jlImage;
	private JLabel jlText;
	private JPanel jpCredit;
	private JLabel jlSharkTracker;
	private JLabel jlTrackingRange;
	private JLabel jlGender;
	private JLabel jlStageOfLife;
	private JLabel jlTagLocation;
	private JPanel jpSearchResult;
	private JLabel jlLastUpdated;
	private JComboBox jcTrackingRange;
	private JComboBox jcGender;
	private JComboBox jcStageOfLife;
	private JComboBox jcTagLocation;
	private JButton jbSearchSearch;
	private JButton jbSearchFollow;
	private JPanel jpOptions;
	private ArrayList<String> tagLocations;
	private JawsController controller;
	private JPanel jpSharkDetails;
	private JPanel infoDetails;
	private JTextArea descriptionLabel;
	private JScrollPane resultsScroll;
	private JButton jbBackToMenu;
	
	/**
	 * 
	 * @param controller
	 */
	public SearchFrame(JawsController controller){
		super("Search");

		this.controller = controller;
		
		addSearchFrameWidget();
		
		jbBackToMenu.addActionListener(controller);
		jbSearchSearch.addActionListener(controller);
		jcTrackingRange.addActionListener(controller);
		jcGender.addActionListener(controller);
		jcStageOfLife.addActionListener(controller);
		jcTagLocation.addActionListener(controller);
		
		GUIDesign();
	}
	
	public void addSearchFrameWidget(){
		jpOptionsPane = new JPanel(new BorderLayout());

		jbBackToMenu = new JButton("Back To Menu");
		
		add(jbBackToMenu, BorderLayout.NORTH);
		add(jpOptionsPane, BorderLayout.WEST);
		
		jpOptions = new JPanel(new FlowLayout());
		
		jbSearchSearch = new JButton("Search");
		
		jlSharkTracker = new JLabel("Shark Tracker", SwingConstants.LEFT);		
		jlTrackingRange = new JLabel("Tracking Range", SwingConstants.LEFT);
		jlGender = new JLabel("Gender", SwingConstants.LEFT);
		jlStageOfLife = new JLabel("Stage Of Life", SwingConstants.LEFT);
		jlTagLocation = new JLabel("Tag Location", SwingConstants.LEFT);
		
		String[] trackingRange = {"Last 24 Hours", "Last Week", "Last Month"};
		String[] gender = {"All", "Male", "Female"};
		String[] stageOfLife = {"All", "Mature", "Immature", "Undetermined"};
		
		jcTrackingRange = new JComboBox(trackingRange);
		jcGender = new JComboBox(gender);
		jcStageOfLife = new JComboBox(stageOfLife);
		jcTagLocation = new JComboBox();
		
		jcTagLocation.addItem("All");
		tagLocations = new ArrayList<String>();
		for(String i : controller.getTagLocations()){
			tagLocations.add(i);
		}
		Collections.sort(tagLocations);
		for(String i : tagLocations){
			jcTagLocation.addItem(i);
		}
	
		dropDownLists();
		
		jpOptionsPane.add(jlSharkTracker, BorderLayout.NORTH);
		
		JPanel selectionSearch = new JPanel(new BorderLayout(2,2));
		jlLastUpdated = new JLabel(controller.getLastUpdated(), SwingConstants.CENTER);
		searchButtonPanel = new JPanel(new BorderLayout(2,2));
		
		searchButtonPanel.add(jbSearchSearch,BorderLayout.CENTER);
		searchButtonPanel.add(jlLastUpdated, BorderLayout.SOUTH);
		selectionSearch.add(jpOptions, BorderLayout.CENTER);
		selectionSearch.add(searchButtonPanel,BorderLayout.SOUTH);
		
		jpOptionsPane.add(selectionSearch, BorderLayout.CENTER); 
		jlImage = new JLabel(new ImageIcon(controller.getImage().getScaledInstance(180,180, Image.SCALE_DEFAULT)));
		jpOptionsPane.add(jlImage, BorderLayout.SOUTH);
		
		jpSearchResult = new JPanel(new BorderLayout(0,1));
		jpSearchResult.setLayout(new BoxLayout(jpSearchResult,BoxLayout.Y_AXIS));
		resultsScroll = new JScrollPane(jpSearchResult, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		
		add(resultsScroll, BorderLayout.CENTER);
		
		jpCredit = new JPanel();
		add(jpCredit,BorderLayout.SOUTH);
		
		jlText = new JLabel(controller.getAcknowledgement());
		jpCredit.add(jlText);
		
		pack();
	}
	
	public void dropDownLists(){
		GroupLayout grouplayout = new GroupLayout(jpOptions);
		jpOptions.setLayout(grouplayout);
		grouplayout.setAutoCreateGaps(true);
		grouplayout.setAutoCreateContainerGaps(true);
	
		grouplayout.setHorizontalGroup(grouplayout.createSequentialGroup()
            .addGroup(grouplayout.createParallelGroup(LEADING)
                .addComponent(jlTrackingRange)
                .addComponent(jcTrackingRange)
                .addComponent(jlGender)
                .addComponent(jcGender)
                .addComponent(jlStageOfLife)
                .addComponent(jcStageOfLife)
                .addComponent(jlTagLocation)
                .addComponent(jcTagLocation))
        );
       
		grouplayout.linkSize(SwingConstants.VERTICAL, jlTrackingRange, jcTrackingRange);
		grouplayout.linkSize(SwingConstants.VERTICAL, jlGender, jcGender);
		grouplayout.linkSize(SwingConstants.VERTICAL, jlStageOfLife, jcStageOfLife);
		grouplayout.linkSize(SwingConstants.VERTICAL, jlTagLocation, jcTagLocation);

		grouplayout.setVerticalGroup(grouplayout.createSequentialGroup()
            .addGroup(grouplayout.createParallelGroup(BASELINE)
                .addComponent(jlTrackingRange))
            .addGroup(grouplayout.createParallelGroup(LEADING)
                .addComponent(jcTrackingRange))
            .addGroup(grouplayout.createParallelGroup(BASELINE)
                    .addComponent(jlGender))
            .addGroup(grouplayout.createParallelGroup(LEADING)
                    .addComponent(jcGender))
            .addGroup(grouplayout.createParallelGroup(BASELINE)
                    .addComponent(jlStageOfLife))
            .addGroup(grouplayout.createParallelGroup(LEADING)
                    .addComponent(jcStageOfLife))
            .addGroup(grouplayout.createParallelGroup(BASELINE)
                    .addComponent(jlTagLocation))
            .addGroup(grouplayout.createParallelGroup(LEADING)
                    .addComponent(jcTagLocation))
        );
	}
	
	public void sharkDetailsError(){
		JLabel resultsLabel = new JLabel(":(  Oops, no results found...Please search again!");
		JPanel imgContainer = new JPanel(new GridLayout(2,1));
		resultsLabel.setAlignmentY(0f);
		
		BufferedImage img = null;
		try {
	         img = ImageIO.read(new File(logoPath));
	    } catch (IOException e) {
	         e.printStackTrace();
		}
		
		JLabel jlImage = new JLabel(new ImageIcon(img.getScaledInstance(200,200, Image.SCALE_DEFAULT)));
		imgContainer.add(jlImage, BorderLayout.NORTH);
		imgContainer.add(resultsLabel, BorderLayout.SOUTH);
		jpSearchResult.add(imgContainer);
		
		resultsLabel.setFont(new Font("Vrinda", Font.BOLD, 24));
		resultsLabel.setForeground(Color.WHITE);
		resultsLabel.setBorder(BorderFactory.createEmptyBorder(150,150,150,150));
	}
	
	/**
	 * 
	 * @param n
	 * @param g
	 * @param sofl
	 * @param s
	 * @param l
	 * @param w
	 * @param d
	 * @param p
	 * @param f
	 */
	public void sharkDetails(String n, String g, String sofl, String s, String l, String w, String d, String p, boolean f){
	
		jpSharkDetails = new JPanel(new BorderLayout(0,1));
		jpSharkDetails.setMinimumSize(new Dimension(300, 50));	    
		jpSharkDetails.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
		
		infoDetails = new JPanel(new GridLayout(0,2,100,0));

		JLabel resultsLabel = new JLabel();
		JLabel infoLabel = new JLabel();
		infoLabel.setText("<html>Name: <br> Gender: <br> Stage Of Life: <br> Species: <br> Length: <br> Weight: </html>");
		resultsLabel.setText("<html>"+ n +"<br>"+ g +"<br>"+ sofl +"<br>"+ s +"<br>"+ l +"<br>"+ w +"<br>"+"</html>");
		
		infoDetails.add(infoLabel);
		infoDetails.add(resultsLabel);
		
		JPanel bottomFollowPanel = new JPanel(new BorderLayout());
	    JLabel pingTime = new JLabel("Last Ping: " + p);
	    JPanel followPanel = new JPanel(new FlowLayout());
		JPanel pingTimePanel = new JPanel(new FlowLayout());
		
	    descriptionLabel = new JTextArea("Description: "+ d);
	    descriptionLabel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
		descriptionLabel.setLineWrap(true);
		descriptionLabel.setEditable(false);
		
	    jbSearchFollow = new JButton();
		jbSearchFollow.addActionListener(controller);
	    if(f == true){
	    	jbSearchFollow.setText("Follow");
	    }
	    else{
	    	jbSearchFollow.setText("Following");
	    	jbSearchFollow.setForeground(Color.GREEN.darker());
	    	jbSearchFollow.setOpaque(true);
	    	jbSearchFollow.setBorderPainted(true);
	    }
	    
	    jbSearchFollow.setName(n);
	    
	    followPanel.add(jbSearchFollow);
	    pingTimePanel.add(pingTime);
	    bottomFollowPanel.add(pingTimePanel,BorderLayout.WEST);
	    bottomFollowPanel.add(followPanel, BorderLayout.EAST);
	    
	    jpSharkDetails.add(infoDetails, BorderLayout.NORTH);
		jpSharkDetails.add(descriptionLabel, BorderLayout.CENTER);
	    jpSharkDetails.add(bottomFollowPanel, BorderLayout.SOUTH);
	   
	    jpSharkDetails.setAlignmentY(0f);
		jpSearchResult.add(jpSharkDetails);
	}

	/**
	 * 
	 * @return JButton
	 */
	public JButton getJbBacktoMenu(){
		return jbBackToMenu;
	}
	
	/**
	 * 
	 * @return JButton
	 */
	public JButton getJbBackToMenu(){
		return jbBackToMenu;
	}

	/**
	 * 
	 * @return JFrame
	 */
	public JFrame getSearchFrame(){
		return this;
	}

	/**
	 * 
	 * @return JComponent
	 */
	public JComponent getJPSearchResult(){		
		return jpSearchResult;
	}

	/**
	 * 
	 * @return JButton
	 */
	public JButton getJBSearchSearch(){
		return jbSearchSearch;
	}

	/**
	 * 
	 * @return JButton
	 */
	public JButton getJBSearchFollow(){
		return jbSearchFollow;
	}

	/**
	 * 
	 * @return JComboBox
	 */
	public JComboBox getJCTrackingRange(){
		return jcTrackingRange;
	}

	/**
	 * 
	 * @return JComboBox
	 */
	public JComboBox getJCGender(){
		return jcGender;
	}

	/**
	 * 
	 * @return JComboBox
	 */
	public JComboBox getJCStageOfLife(){
		return jcStageOfLife;
	}

	/**
	 * 
	 * @return JComboBox
	 */
	public JComboBox getJCTagLocation(){
		return jcTagLocation;
	}
	
	public void GUIDesign(){
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		setMinimumSize(new Dimension(1400,700));
		setResizable(false);
		setLocationRelativeTo(null);
		
		jpOptionsPane.setBorder(BorderFactory.createLineBorder(Color.black));
		jpOptionsPane.setBackground(Color.BLACK);
		
		jpOptions.setBackground(Color.DARK_GRAY);
		
		jlSharkTracker.setFont(new Font("Vrinda", Font.BOLD, 24));
		jlSharkTracker.setForeground(Color.WHITE);
		jlSharkTracker.setBorder(BorderFactory.createEmptyBorder(5,5,5,10));
		
		jpSearchResult.setBorder(BorderFactory.createLineBorder(Color.black));
		jpSearchResult.setBackground(Color.GRAY);
		
		jlImage.setBorder(BorderFactory.createEmptyBorder(15,15,15,15));
		
		searchButtonPanel.setBackground(Color.darkGray);
		
		jlLastUpdated.setBackground(Color.darkGray);
		jlLastUpdated.setBorder(BorderFactory.createLineBorder(Color.darkGray));
		jlLastUpdated.setForeground(Color.WHITE);
		jlLastUpdated.setFont(new Font("Vrinda", Font.BOLD, 8));
		
		jpCredit.setBackground(Color.BLACK);
		jpCredit.setBorder(BorderFactory.createEmptyBorder(5,5,5,10));
		jlText.setForeground(Color.WHITE);
		
		jbSearchSearch.setBackground(Color.DARK_GRAY);
		jbSearchSearch.setForeground(Color.BLACK);
		jbSearchSearch.setOpaque(true);
		jbSearchSearch.setBorderPainted(true);
		
		jcTrackingRange.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.GRAY));
		jcGender.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.GRAY));
		jcStageOfLife.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.GRAY));
		jcTagLocation.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.GRAY));
	}
}
