package jaws.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
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
public class MenuFrame extends JFrame{
	private String logoPath = "../Resources/Images/logo.png";
	
	private JButton jbMenuSearch;
	private JButton jbMenuFav;
	private JButton jbMenuLogOff;
	private JPanel jpSouthMenuFrame;
	private JPanel jpNorthMenuFrame;
	private JPanel jPanelMenuFrame;
	private JawsController controller;

	/**
	 * 
	 * @param controller
	 */
	public MenuFrame(JawsController controller){	
		super("Anmity Police");
		
		this.controller = controller;
		
		addMenuFrameWidgets();
		
		jbMenuFav.addActionListener(controller);
		jbMenuSearch.addActionListener(controller);
		jbMenuLogOff.addActionListener(controller);
		
		GUIDesign();
	}
	
	private void addMenuFrameWidgets(){
		jbMenuFav = new JButton("Favorites");
		jbMenuSearch = new JButton("Search");
		jbMenuLogOff = new JButton("Log Off");
		
		jPanelMenuFrame = new JPanel(new BorderLayout(2, 1));
		add(jPanelMenuFrame);
		
		jpNorthMenuFrame = new JPanel(new BorderLayout(2, 1));
		jpSouthMenuFrame = new JPanel(new GridLayout(4, 1));
		
		jPanelMenuFrame.add(jpNorthMenuFrame, BorderLayout.NORTH);
		jPanelMenuFrame.add(jpSouthMenuFrame, BorderLayout.CENTER);
		jpSouthMenuFrame.add(jbMenuFav);
		jpSouthMenuFrame.add(jbMenuSearch);
		jpSouthMenuFrame.add(jbMenuLogOff);
		
		JLabel jlImage = new JLabel(new ImageIcon(getImage().getScaledInstance(200,200, Image.SCALE_DEFAULT)));
		jlImage.setBorder(BorderFactory.createEmptyBorder(15,15,15,15));
		jpNorthMenuFrame.add(greatingMessage(), BorderLayout.NORTH);
		jpNorthMenuFrame.add(jlImage, BorderLayout.CENTER);
		
		pack();
	}
	
	/**
	 * 
	 * @return JLabel
	 */
	public JLabel greatingMessage(){
		JLabel greatingMessage = new JLabel("Welcome ");
		greatingMessage.setForeground(Color.WHITE);
		greatingMessage.setHorizontalAlignment(SwingConstants.CENTER);
		greatingMessage.setVerticalAlignment(SwingConstants.CENTER);
		
		return greatingMessage;
	}

	/**
	 * 
	 * @return BufferedImage
	 */
	public BufferedImage getImage(){
		BufferedImage img = null;
		try {
	         img = ImageIO.read(new File(logoPath));
	    } catch (IOException e) {
	         e.printStackTrace();
		}
		
		return img;
	}
	
	public void refresh(){
		revalidate();
		repaint();
	}
	
	/**
	 * 
	 * @return JButton
	 */
	public JButton getJBMenuFav(){
		return jbMenuFav;
	}

	/**
	 * 
	 * @return JButton
	 */
	public JButton getJBMenuSearch(){
		return jbMenuSearch;
	}
	
	/**
	 * 
	 * @return JButton
	 */
	public JButton getJBMenuLogOff(){
		return jbMenuLogOff;
	}
	
	public void GUIDesign(){	
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		setMinimumSize(new Dimension(400,480));
		setResizable(false);
		setLocationRelativeTo(null);
		jPanelMenuFrame.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
		jPanelMenuFrame.setBackground(Color.DARK_GRAY);
		
		setBackground(Color.BLACK);
		jpNorthMenuFrame.setBackground(Color.BLACK);
		jpSouthMenuFrame.setBackground(Color.BLACK);
	    
		jbMenuFav.setBackground(Color.GRAY);
		jbMenuFav.setForeground(Color.BLACK);
		jbMenuFav.setOpaque(true);
		jbMenuFav.setBorderPainted(true);
		
		jbMenuSearch.setBackground(Color.GRAY);
		jbMenuSearch.setForeground(Color.BLACK);
		jbMenuSearch.setOpaque(true);
		jbMenuSearch.setBorderPainted(true);
		
		jbMenuLogOff.setBackground(Color.GRAY);
		jbMenuLogOff.setForeground(Color.BLACK);
		jbMenuLogOff.setOpaque(true);
		jbMenuLogOff.setBorderPainted(true);
		
	}
}
