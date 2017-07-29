package jaws.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URL;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import jaws.controller.JawsController;

/**
 * 
 * @author Zehra Punjwani, Meghana Santhosh, Kent Millamena, and Riya Karia
 *
 */
@SuppressWarnings("serial")
public class FavouritesFrame extends JFrame{
	private JPanel jpFavourites;
	private JLabel jlInfoText;
	private JPanel favPane;
	private JawsController controller;
	private JButton jbBackToMenu;
	private JButton jbMap;
	private JLabel shark;

	/**
	 * 
	 * @param controller
	 */
	public FavouritesFrame(JawsController controller){
		super("Favourites");
		
		this.controller = controller;
		
		addFavouritesFrameWidget();
		
		jbBackToMenu.addActionListener(controller);
		jbMap.addActionListener(controller);
		
		GUIDesign();
	}
	
	public void addFavouritesFrameWidget(){
		setLayout(new BorderLayout());
		
		jbBackToMenu = new JButton("Back To Menu");
		jbMap = new JButton("Map");
		
		jpFavourites = new JPanel();
		jpFavourites.setLayout(new BoxLayout(jpFavourites, BoxLayout.PAGE_AXIS));
		jpFavourites.setAlignmentX((Component.LEFT_ALIGNMENT));
		jlInfoText = new JLabel("Your favourite sharks are away from you right now: ", SwingConstants.CENTER);
		favPane = new JPanel();
		favPane.setLayout(new BoxLayout(favPane, BoxLayout.Y_AXIS));
		JPanel panel = new JPanel(new GridLayout());
		panel.add(jbBackToMenu, BorderLayout.NORTH);
		panel.add(jbMap, BorderLayout.SOUTH);

		add(panel, BorderLayout.NORTH);
		add(jpFavourites, BorderLayout.CENTER);
		jpFavourites.add(jlInfoText, BorderLayout.NORTH);
		jpFavourites.add(favPane, BorderLayout.CENTER);
		pack();
	}
	
	/**
	 * 
	 * @param distance
	 * @param name
	 */
	public void addFavToFavPanel(Double distance, final String name){
		shark = new JLabel();
		shark.setText(name + ": " + distance + " " + controller.getCoordinates(name));
    	shark.setName(name);
    	
    	shark.addMouseListener(new MouseAdapter() {
    		public void mouseReleased(MouseEvent e){
    			controller.setSharkName(name);
    			controller.printFavSharksinSearchFrame();
    			
    		}
		});
    	
    	getFavPane().add(shark);
	}
	
	/**
	 * 
	 * @param name
	 * @param video
	 * @return
	 */
	public JPanel addVideo(String name, final String video){
		JPanel videoPanel = new JPanel();
		JLabel videoLabel = new JLabel();
		
		videoPanel.addMouseListener(new MouseAdapter() {
    		public void mouseReleased(MouseEvent e){
    			try {
    		        Desktop.getDesktop().browse(new URL(video).toURI());
    		    } catch (Exception ee) {
    		        ee.printStackTrace();
    		    }
    		}
		});
		
		if(video.contains(":-(")){
			videoLabel.setText(video);
			videoLabel.setForeground(Color.RED.darker());
		}
		else{
			videoLabel.setText("Click Me To check out " + name  + "'s Video ==> "+ video);
			videoLabel.setForeground(Color.GREEN.darker());
		}
		
		videoPanel.add(videoLabel);
		
		return videoPanel;
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
	 * @return JPanel
	 */
	public JPanel getFavPane(){
		return favPane;
	}
	
	/**
	 * 
	 * @return JLabel
	 */
	public JLabel getShark(){
		return shark;
	}
	
	public void GUIDesign(){
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		setMinimumSize(new Dimension(1400,700));
		setResizable(false);
		setLocationRelativeTo(null);
		
		jpFavourites.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
		favPane.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
		jlInfoText.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
		favPane.setBackground(Color.WHITE);
		setMinimumSize(new Dimension(1000,650));
	
		jlInfoText.setFont(new Font("Vrinda", Font.BOLD, 12));
	}
}
