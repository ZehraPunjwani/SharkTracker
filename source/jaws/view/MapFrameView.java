package jaws.view;

/**
 * 
 * @author Zehra Punjwani, Meghana Santhosh, Kent Millamena, and Riya Karia
 *
 */
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;

import api.jaws.Shark;
import jaws.controller.JawsController;

public class MapFrameView extends JPanel{
	private JawsController controller;
	private BufferedImage mapImage;
	private String location="";
	private JPanel southPanel;
	private JFrame frame;
	private JPanel panel;
	private int latitude = 0;
	private int longitude = 0;
	
	public MapFrameView(JawsController controller) {
		super();
		this.controller = controller;
		favSharkLocations();
		MapGUI();
		GUIDesign();
	
	}
	public void favSharkLocations(){
		for(Shark shark : controller.getFavSharks()){
			double latitude = controller.getSharkLatitudeLocation(shark.getName());
			double longitude =  controller.getSharkLongitudeLocation(shark.getName());
			location  += "markers=color:blue%7Clabel:S%7C" + latitude + "," + longitude + "&";
		}
	}
	
	@Override
	protected void paintComponent(Graphics g){
		super.paintComponent(g);
		if(mapImage != null){
			int x = (getWidth() - mapImage.getWidth());
			int y = (getHeight() - mapImage.getHeight());
			g.drawImage(mapImage, x, y,this);
			
		}
	}
	
	public void MapGUI(){
		int latCounter = 0;
		int longCounter = 0;
		InputStream is;
		mapImage =  null;
		latitude = latCounter;
		longitude = longCounter;
		try{
			String mainURL = "https://maps.googleapis.com/maps/api/staticmap?&center=0,0&zoom=1&";
			URL map = new URL (mainURL+"&size=600x300&maptype=satellite&"+location+"key=AIzaSyCBxU6GLeXIOyJucDbm2JYCDE3ZiVpYVSs");
			is = map.openStream();
			mapImage = ImageIO.read(is);
			
		}
		catch (Exception exp){
			
		}
		frame = new JFrame();
		southPanel = new JPanel(new GridLayout(0,4));
		panel = new JPanel(new GridLayout(2,0));
		
		frame.pack();
		frame.setVisible(true);
	}
	
	public void removePanel() {
		this.removeAll();	
		refresh();		
	}

	
	public void refresh() {
		frame.revalidate();
		frame.repaint();
	}
	
	public void GUIDesign(){
		frame.setMinimumSize(new Dimension(600,350));
		frame.setMaximumSize(new Dimension(600,350));
		frame.setLayout(new BorderLayout());
		frame.add(this, BorderLayout.CENTER);
		frame.add(southPanel, BorderLayout.SOUTH);
		frame.add(panel, BorderLayout.NORTH);
		frame.setLocationRelativeTo(null);
	}
	
}
