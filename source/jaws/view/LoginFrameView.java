package jaws.view;

import javax.swing.*;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;

import jaws.controller.JawsController;

/**
 * 
 * @author Zehra Punjwani, Meghana Santhosh, Kent Millamena, and Riya Karia
 *
 */
public class LoginFrameView extends JFrame{
	
		private JawsController controller;
		private JTabbedPane tabbedPane = new JTabbedPane();
		private JButton jbLogin;
		private JButton jbRegister;
		private JButton jbGeneralLogin;
		private JLabel jlLoginMess;
		private JLabel jlRegMess;
		private JLabel jlLoginUserName;
		private JLabel jlRegUserName;
		private JTextField jtRegUName;
		private JTextField jtLoginUName;
		
		/**
		 * 
		 * @param controller
		 */
		public LoginFrameView(JawsController controller){
			super("login and Registration");
			
			this.controller = controller;
			
			addUserLoginTab();
			addUserRegTab();
			
			jbLogin.addActionListener(controller);
			jbRegister.addActionListener(controller);
			jbGeneralLogin.addActionListener(controller);
			
			GUIDesign();
		}

		public void addUserLoginTab(){
			  jlLoginUserName = new JLabel("User Name");
			  jlLoginMess = new JLabel("", SwingConstants.CENTER);
			  JPanel jPanel = new JPanel();
			  JPanel jPanelCenter = new JPanel();
			  JPanel jPanelSouth = new JPanel();
			  JPanel jpMain = new JPanel();
			  JPanel jpButtons = new JPanel();
			  jbGeneralLogin = new JButton("Default");
			  jbLogin = new JButton("Login");
			  
			  jtLoginUName = new JTextField();

		      jpButtons.setLayout(new GridLayout(1,2));
		      jpButtons.add(jbGeneralLogin);
		      jpButtons.add(jbLogin);
				   
			  jPanel.add(jlLoginUserName);
			  jPanel.add(jtLoginUName);
			  jPanel.setLayout(new GridLayout(0,2));
		      jPanelCenter.setLayout(new BorderLayout());
		           
		      jpMain.setLayout(new BorderLayout());
		      jPanelCenter.add(jPanel, BorderLayout.NORTH);
		      jPanelCenter.add(jlLoginMess, BorderLayout.SOUTH);
		      jPanelSouth.add(jpButtons);
			  jpMain.add(jPanelCenter, BorderLayout.CENTER);
			  jpMain.add(jPanelSouth, BorderLayout.SOUTH);
				
			  tabbedPane.addTab("Login",jpMain);
		}
		
		public void addUserRegTab(){
			jlRegUserName = new JLabel("User Name");
			jlRegMess = new JLabel("", SwingConstants.CENTER);
			JPanel jPanel = new JPanel();
			JPanel jPanelCenter = new JPanel();
			JPanel jPanelSouth = new JPanel();
			JPanel jpMain = new JPanel();
			JPanel jpButtons = new JPanel();
			jbRegister = new JButton("Register");
			  
			jtRegUName = new JTextField();

		    jpButtons.setLayout(new GridLayout(1,1));
		    jpButtons.add(jbRegister);
				   
			jPanel.add(jlRegUserName);
			jPanel.add(jtRegUName);
			jPanel.setLayout(new GridLayout(0,2));
			jPanelCenter.setLayout(new BorderLayout());
		           
		    jpMain.setLayout(new BorderLayout());
		    jPanelCenter.add(jPanel, BorderLayout.NORTH);
		    jPanelCenter.add(jlRegMess, BorderLayout.SOUTH);
		    jPanelSouth.add(jpButtons);
			jpMain.add(jPanelCenter, BorderLayout.CENTER);
			jpMain.add(jPanelSouth, BorderLayout.SOUTH);
			 
			tabbedPane.addTab("Registration", jpMain);
			add(tabbedPane);
			
			pack();
		}
		
		public void inValidUserValidation(){
			jlLoginMess.setText("User does not exist, please Register first!");
			jlLoginMess.setForeground(Color.RED);
			jtLoginUName.setText("");
		}
		
		/**
		 * 
		 * @param jc
		 */
		public void setTextColor(JComponent jc){
			jc.setForeground(Color.GREEN.darker());
		}
		
		public void deleteInput(){
			getJTLoginUserName().setText("");
			getJLLoginMess().setText("");
		}
		
		/**
		 * 
		 * @return JButton
		 */
		public JButton getLoginButton(){
			return jbLogin;
		}
		
		/**
		 * 
		 * @return JButton
		 */
		public JButton getRegButton(){
			return jbRegister;
		}
		
		/**
		 * 
		 * @return JButton
		 */
		public JButton getGeneralButton(){
			return jbGeneralLogin;
		}
		
		/**
		 * 
		 * @return JTextField
		 */
		public JTextField getJTLoginUserName(){
			return jtLoginUName;
		}
		
		/**
		 * 
		 * @return JTextField
		 */
		public JTextField getJTRegUserName(){
			return jtRegUName;
		}
		
		/**
		 * 
		 * @return JLabel
		 */
		public JLabel getJLLoginMess(){
			return jlLoginMess;
		}
		
		/**
		 * 
		 * @return JLabel
		 */
		public JLabel getJLRegMess(){
			return jlRegMess;
		}
		
		public void GUIDesign(){
			setDefaultCloseOperation(EXIT_ON_CLOSE);
			setMinimumSize(new Dimension(400,480));
			setLocationRelativeTo(null);
			
			tabbedPane.setBounds(27, 21, 360, 400);
			setResizable(false);	
			
			jlLoginMess.setFont(new Font("Vrinda", Font.BOLD, 12));
			jlRegMess.setFont(new Font("Vrinda", Font.BOLD, 12));
			jlRegMess.setForeground(Color.RED);
		}
	}
