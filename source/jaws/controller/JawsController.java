package jaws.controller;

import jaws.model.JawsModel;
import jaws.view.FavouritesFrame;
import jaws.view.LoginFrameView;
import jaws.view.MapFrameView;
import jaws.view.MenuFrame;
import jaws.view.SearchFrame;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeMap;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import api.jaws.Location;
import api.jaws.Ping;
import api.jaws.Shark;

/**
 * 
 * @author Zehra Punjwani, Meghana Santhosh, Kent Millamena, and Riya Karia
 *
 */
public class JawsController extends Thread implements ActionListener{

	public String logoPath = "../Resources/Images/logo.png";
	public String userAccountsFile = "../Resources/Database/userAccounts.txt";

	public static final Cursor busyCursor = new Cursor(Cursor.WAIT_CURSOR);
    public static final Cursor defaultCursor = new Cursor(Cursor.DEFAULT_CURSOR);
	private String userLoginName;
	private LoginFrameView loginFrameView;
	private JawsModel jawsModel;
	private MenuFrame menuFrameView;
	private FavouritesFrame favFrameView;
	private SearchFrame searchFrameView;
	private String trSelection;
	private String gSelection;
	private String soflSelection;
	private String tlSelection;
	private ArrayList<String> favSharkNames;
	private String finalCoordinates;
	private String location;
	private ArrayList<Shark> favSharks = new ArrayList<Shark>();
	private ArrayList<Ping> past24Hours = new ArrayList<Ping>();
	private ArrayList<Ping> pastWeek = new ArrayList<Ping>();
	private ArrayList<Ping> pastMonth = new ArrayList<Ping>();
	private ArrayList<Ping> pastMonthDltDuplicates = new ArrayList<Ping>();
	private ArrayList<Ping> frequency;
	private ArrayList<String> sharkCoordinates;
	private TreeMap<Double, String> favSharkDistance;
	private double distance;
	private String sharkName;
	
	public JawsController(){
		//Create new instance of a LoginFrame
		loginFrameView = new LoginFrameView(this);
		
		//Create new instance of a JawsModel
		jawsModel = new JawsModel(this);
		
		obtainPast24Hr.start();
		obtainPastWeek.start();
		obtainPastMonth.start();
		
		//Create new instance of a MenuFrame
		menuFrameView = new MenuFrame(this);
		closeWindowOperation(menuFrameView);
		
		//Create new instance of a SearchFrame
		searchFrameView = new SearchFrame(this);
		closeWindowOperation(searchFrameView);
		
		loginFrameView.setVisible(true);
	}
	
	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	//ACTIONLISTENER
	@Override
	public void actionPerformed(ActionEvent e) {
		Object source = e.getSource();
		trSelection = String.valueOf(searchFrameView.getJCTrackingRange().getSelectedItem());
		gSelection = String.valueOf(searchFrameView.getJCGender().getSelectedItem());
		soflSelection = String.valueOf(searchFrameView.getJCStageOfLife().getSelectedItem());
		tlSelection = String.valueOf(searchFrameView.getJCTagLocation().getSelectedItem());
		
		frequency = getFrequency();
		
		if(source instanceof JButton){
			switch (((JButton)source).getText()){
				case("Login"):
					userLoginName = loginFrameView.getJTLoginUserName().getText();
					if(!userLoginName.isEmpty() && userLoginName.trim().length() > 0){
						if(!checkUserExist(userAccountsFile, userLoginName, loginFrameView.getJLLoginMess())){
							if(!validateUser(userAccountsFile, userLoginName)){
								loginFrameView.inValidUserValidation();
							}
						}
						else{
							loading(loginFrameView);
							//Create new instance of FavouritesFrame
							favFrameView = new FavouritesFrame(this);
							closeWindowOperation(favFrameView);
							
							checkIfFavSharkEmpty();
							String fileName = userLoginName;
							readFavToFile(fileName);
							
							ArrayList<Ping> foundPings = findSharkPings(readFavToFile(fileName), sortSharks(pastMonthDltDuplicates));
							for(Ping i : foundPings){
								Shark shark = jawsModel.getShark(i.getName());
								if(shark.getName().equals(i.getName())){
									favSharks.add(shark);
								}
							}
							loadingComplete(loginFrameView);
							loginFrameView.setVisible(false);
							menuFrameView.setVisible(true);
						}
					}
					else{
						invalidInputValidation(loginFrameView.getJLLoginMess(),loginFrameView.getJTLoginUserName());
					}
					break;
				case("Register"):
					String userRegName = loginFrameView.getJTRegUserName().getText();
					if(!userRegName.isEmpty() && userRegName.trim().length() > 0){
						if(!checkUserExist(userAccountsFile, userRegName, loginFrameView.getJLRegMess())){
							saveUserToFile(userAccountsFile, userRegName);
							File file = new File("Resources/Database", userRegName + ".txt");
							try {
								new BufferedWriter(new FileWriter(file));
							} catch (IOException e1) {
								e1.printStackTrace();
								JOptionPane.showMessageDialog(loginFrameView, "File Not Recognised!");
							}
						}
					}
					else{
						invalidInputValidation(loginFrameView.getJLRegMess(),loginFrameView.getJTRegUserName());
					}
					break;
				case("Default"):
					loading(loginFrameView);
					
					userLoginName = "Default";
					
					//Create new instance of FavouritesFrame
					favFrameView = new FavouritesFrame(this);
					closeWindowOperation(favFrameView);
					
					loadingComplete(loginFrameView);
					loginFrameView.setVisible(false);
					menuFrameView.setVisible(true);
					break;
				case("Favourites"):
					loading(menuFrameView);;
					favFrameView.getFavPane().removeAll();
					addFavSharksInOrder(favSharks);
					loadingComplete(menuFrameView);
					menuFrameView.setVisible(false);
					favFrameView.setVisible(true);
					break;
				case("Search"):
					if(source.equals(menuFrameView.getJBMenuSearch())){
						checkIfFavSharkEmpty();
						menuFrameView.setVisible(false);
						searchFrameView.setVisible(true);
					}
					else if(source.equals(searchFrameView.getJBSearchSearch())){
						loading(searchFrameView);
						cleanFrame(searchFrameView.getJPSearchResult(), searchFrameView);
						
						getByTrackingRange(frequency);
						
						refresh(searchFrameView);
						loadingComplete(searchFrameView);
					}
					break;
				case("Follow"):
					loading(searchFrameView);
					favFrameView.getFavPane().removeAll();
					String favSharkName = ((JButton) source).getName();
					if(!favSharks.contains(favSharkName)){
						favSharks.add(jawsModel.getShark(favSharkName));
					}
					((JButton)source).setForeground(Color.GREEN.darker());
					((JButton)source).setOpaque(true);
					((JButton)source).setBorderPainted(true);
					loadingComplete(searchFrameView);
					((JButton)source).setText("Following");
					break;
				case("Following"):
					loading(searchFrameView);
					favFrameView.getFavPane().removeAll();
					String unFavSharkName = ((JButton) source).getName();
					for( Iterator<Shark> iterator = favSharks.iterator(); iterator.hasNext() ; )
				    {
				      Shark i = iterator.next();
				      if( i.getName().equals(unFavSharkName)){
				    	  iterator.remove();
				      }
				    }
					((JButton)source).setForeground(Color.BLACK);
					((JButton)source).setOpaque(true);
					((JButton)source).setBorderPainted(true);
					loadingComplete(searchFrameView);
					((JButton)source).setText("Follow");
					break;
				case("Log Off"):
					if(userLoginName == "Default"){
			    		if (JOptionPane.showConfirmDialog(menuFrameView, "Oops, your Favourite Sharks Can not be saved. Please Register first!", "Really Closing?", JOptionPane.OK_OPTION,JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION){
			    			menuFrameView.setVisible(false);
			    			loginFrameView.deleteInput();
							loginFrameView.setVisible(true);
			    		}
			    	}
					else{
						if(JOptionPane.showConfirmDialog(menuFrameView, "Are you sure you wish to close this window?", "Really Closing?", JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION){
							loading(menuFrameView);
							saveFavSharksToFile(userLoginName, favSharks);
			        		loadingComplete(menuFrameView);
							menuFrameView.setVisible(false);
							loginFrameView.deleteInput();
							loginFrameView.setVisible(true);
						}
					}
					
				case("Back To Menu"):	
					if(source.equals(favFrameView.getJbBackToMenu())){
						favFrameView.setVisible(false);
						menuFrameView.setVisible(true);
					}
					else if(source.equals(searchFrameView.getJbBackToMenu())){
						searchFrameView.setVisible(false);
						menuFrameView.setVisible(true);
					}
				case("Map"):
					MapFrameView mapFrameView = new MapFrameView(this);
					favFrameView.setVisible(false);
					mapFrameView.setVisible(true);
					break;	
			}
			checkIfFavSharkEmpty();
		}
	}
	
	/**
	 * 
	 * @param Window i
	 */
	public void closeWindowOperation(final Window i){
		i.addWindowListener(new WindowAdapter() {
		    public void windowClosing(WindowEvent windowEvent) {
		    	if(userLoginName == "Default"){
		    		if (JOptionPane.showConfirmDialog(i, "Oops, your Favourite Sharks Can not be saved. Please Register first!", "Really Closing?", JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION){
		            	System.exit(0);
		    		}
		    	}
		    	else{
			        if(JOptionPane.showConfirmDialog(i, "Are you sure you wish to close this window?", "Really Closing?", JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION){
			        	saveFavSharksToFile(userLoginName, favSharks);
			        	System.exit(0);
			        }
		    	}
		    }
		});
	}
	
	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	//THREAD
	Thread obtainPast24Hr = new Thread(new Runnable() {

		@Override
        public void run() {
        	for(Ping i : jawsModel.past24Hours()){
        		past24Hours.add(i);
        	}
        }
    });
	
	Thread obtainPastWeek = new Thread(new Runnable() {

		@Override
        public void run() {
        	for(Ping i : jawsModel.pastWeek()){
        		pastWeek.add(i);
        	}
        }
    });
	
	Thread obtainPastMonth = new Thread(new Runnable() {

		@Override
        public void run() {
        	for(Ping i : jawsModel.pastMonth()){
        		pastMonth.add(i);
        	}
        	Set set = new HashSet(pastMonth);
			pastMonthDltDuplicates = new ArrayList<Ping>(set);
        }
    });
	
	/**
	 * 
	 * @param locationSorted
	 */
	public void revealData(final ArrayList<Ping> locationSorted){
		new Thread(new Runnable(){
			
			@Override
			public void run(){
				boolean followShark = true;
				cleanFrame(searchFrameView.getJPSearchResult(), searchFrameView);
				if(locationSorted.isEmpty()){
					searchFrameView.sharkDetailsError();
					refresh(searchFrameView);
				}
				else{
					for(Ping i : locationSorted){
						Shark shark = jawsModel.getShark(i.getName());
						String n = shark.getName();
						String g = shark.getGender();
						String sofl = shark.getStageOfLife();
						String s = shark.getSpecies();
						String l = shark.getLength();
						String w = shark.getWeight();
						String d = shark.getDescription();
						String p = i.getTime();   /////////////////////////////
									
						for(Shark fs : favSharks){
							if(fs.getName().equals(shark.getName())){
								followShark = false;
							}
						}
						searchFrameView.sharkDetails(n, g, sofl, s, l, w, d, p, followShark);
						refresh(searchFrameView);
						followShark = true;
					}
				}
			}
		}).start();
	}
	
	/**
	 * 
	 * @param Shark i
	 */
	public void revealDataByName(final Shark i){
		new Thread(new Runnable(){

			@Override
			public void run() {
				boolean followShark = true;
				for(Shark fs : favSharks){
					if(fs.getName().equals(i.getName())){
						followShark = false;
					}
				}
				String n = i.getName();
				String g = i.getGender();
				String sofl = i.getStageOfLife();
				String s = i.getSpecies();
				String l = i.getLength();
				String w = i.getWeight();
				String d = i.getDescription();
				String p = null;
				for(Ping j : pastMonth){
					if (j.getName() == n){
						p = j.getTime();
					}
				}
				searchFrameView.sharkDetails(n, g, sofl, s, l, w, d, p, followShark);
				refresh(searchFrameView);
				followShark = true;
			}
			
		});
	}
	
	/**
	 * 
	 * @return ArrayList<Ping>
	 */
	public ArrayList<Ping> pastMonthDltDuplicates(){
		return pastMonthDltDuplicates;
	}
	
	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	//USER LOGIN FRAME
	/**
	 * 
	 * @param fileName
	 * @param name
	 * @param jLabel
	 * @return booolean
	 */
	public boolean checkUserExist(String fileName, String name, JLabel jLabel){
		FileReader fileReader;
		BufferedReader bufferedReader;
		String accountInfo;
		boolean exist = false;
		try{
			fileReader = new FileReader(fileName);
			bufferedReader = new BufferedReader(fileReader);
			while((accountInfo = bufferedReader.readLine()) != null){
				if(check(accountInfo, name)){
					jLabel.setText("The account already Exists!");
					exist = true;
					break;
				}
			}
			bufferedReader.close();
			fileReader.close();
		}
		catch(Exception e){
			e.printStackTrace();
			System.out.println("Error In Checking User Existance");
		}
		return exist;
	}
	
	/**
	 * 
	 * @param accountInfo
	 * @param name
	 * @return boolean
	 */
	public boolean check(String accountInfo, String name){
		if(accountInfo.equals(name)){
			return true;
		}
		else{
			return false;
		}
	}
	
	/**
	 * 
	 * @param fileName
	 * @param userName
	 * @return boolean
	 */
	public boolean validateUser(String fileName, String userName){
		FileReader fileReader;
		BufferedReader bufferedReader;
		boolean valid = false;
		String accountInfo;
		try{
			fileReader = new FileReader(fileName);
			bufferedReader = new BufferedReader(fileReader);
			while((accountInfo = bufferedReader.readLine()) != null){
				if(check(accountInfo, userName)){
					valid = true;
					break;
				}
			}
			bufferedReader.close();
			fileReader.close();
		}
		catch(Exception e){
			e.printStackTrace();
			System.out.println("Error in Validating User");
		}
		return valid;
	}
	
	/**
	 * 
	 * @param fileName
	 * @param userName
	 */
	public void saveUserToFile(String fileName, String userName){
		try{
			FileWriter fileWriter = new FileWriter(fileName, true);
			BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
			bufferedWriter.write(userName);
			bufferedWriter.newLine();
			bufferedWriter.flush();
			bufferedWriter.close();
			loginFrameView.getJLRegMess().setText("Account created, please login in as normal!");
			loginFrameView.setTextColor(loginFrameView.getJLRegMess());
			loginFrameView.getJTRegUserName().setText("");			
		}
		catch(IOException e){
			e.printStackTrace();
			System.out.println("Error: Text could not be written to file...");
		}
	}
	
	/**
	 * 
	 * @param errorMess
	 * @param text
	 */
	public void invalidInputValidation(JLabel errorMess, JTextField text){
		errorMess.setText("Please Enter a User Name!");
		text.setText("");
	}
	
	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	//SEARCH FRAME
	/**
	 * 
	 * @return ArrayList<Ping>
	 */
	ArrayList<Ping> getFrequency(){
			ArrayList<Ping> frequency = new ArrayList<Ping>();
			if(trSelection == "Last 24 Hours"){
				for(Ping i : past24Hours){
					frequency.add(i);
				}
			}
			else if(trSelection == "Last Week"){
				for(Ping i : pastWeek){
					frequency.add(i);
				}
			}
			else if(trSelection == "Last Month"){
				for(Ping i : pastMonth){
					frequency.add(i);
				}
			}
			return frequency;
	}
	
	/**
	 * 
	 * @param frequency
	 */
	public void getByTrackingRange(ArrayList<Ping> frequency){
		ArrayList<Ping> genderSorted = new ArrayList<Ping>();
		if(gSelection == "Male" || gSelection == "Female"){
			for(Ping i : frequency){
				Shark shark = jawsModel.getShark(i.getName());
				if(shark.getGender().equals(gSelection)){
					genderSorted.add(i);
				}
			}
		}
		else if(gSelection == "All"){
			for(Ping i : frequency){
				genderSorted.add(i);
			}
		}
		getByStageOfLife(genderSorted);
	}
	
	/**
	 * 
	 * @param genderSorted
	 */
	public void getByStageOfLife(ArrayList<Ping> genderSorted){
		ArrayList<Ping> stageOfLifeSorted = new ArrayList<Ping>();
		if(soflSelection == "Mature" || soflSelection == "Immature" || soflSelection == "Undetermined"){
			for(Ping i : genderSorted){
				Shark shark = jawsModel.getShark(i.getName());
				if(shark.getStageOfLife().equals(soflSelection)){
					stageOfLifeSorted.add(i);
				}
			}
		}
		else if(soflSelection == "All"){
			for(Ping i : genderSorted){
				stageOfLifeSorted.add(i);
			}
		}
		getByLocation(stageOfLifeSorted);
	}

	/**
	 * 
	 * @param stageOfLifeSorted
	 */
	public void getByLocation(ArrayList<Ping> stageOfLifeSorted){
		ArrayList<Ping> locationSorted = new ArrayList<Ping>();
		for(Ping i : stageOfLifeSorted){
			Shark shark = jawsModel.getShark(i.getName());
			if(shark.getTagLocation().equals(tlSelection)){
				locationSorted.add(i);
			}
			else{
				locationSorted.add(i);
			}
		}
		revealData(sortSharks(locationSorted));
	}

	/**
	 * 
	 * @param locationSorted
	 * @return ArrayList<Ping>
	 */
	public ArrayList<Ping> sortSharks(ArrayList<Ping> locationSorted){
		Collections.sort(locationSorted, new Comparator<Ping>(){
			@Override
			public int compare(Ping p1, Ping p2) {
				return p1.getTime().compareTo(p2.getTime());
			}
		});
		Collections.sort(pastMonth, new Comparator<Ping>(){
			@Override
			public int compare(Ping p1, Ping p2) {
				return p1.getTime().compareTo(p2.getTime());
			}
		});
		for (int i = locationSorted.size() - 1; i > 0; i--) {
			for (int j = i - 1; j >= 0; j--) {
				if (locationSorted.get(i).getName().equals(locationSorted.get(j).getName())) {
					locationSorted.remove(j);
					break;
				}
			}
		}
		return locationSorted;
	}
	
	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	//FAVOURITES FRAME
	public void checkIfFavSharkEmpty() {
		if(favSharks.isEmpty()){
			menuFrameView.getJBMenuFav().setEnabled(false);
		}
		else{
			menuFrameView.getJBMenuFav().setEnabled(true);
		}
	}
	
	/**
	 * 
	 * @param fileName
	 * @return ArrayList<String>
	 */
	public ArrayList<String> readFavToFile(String fileName){
		FileReader fileReader;
		BufferedReader bufferedReader;
		String favShark;
		favSharkNames = new ArrayList<String>();
		try{
			fileReader = new FileReader("../Resources/Database/" + fileName + ".txt");
			bufferedReader = new BufferedReader(fileReader);
			while((favShark = bufferedReader.readLine()) != null){
				 favSharkNames.add(favShark);
			}
			bufferedReader.close();
			fileReader.close();
		}
		catch(Exception e){
			e.printStackTrace();
			System.out.println("Error: In reading File");
		}
		return favSharkNames;
	}
	
	/**
	 * 
	 * @param favSharkNames
	 * @param pastMonth
	 * @return ArrayList<Ping>
	 */
	public ArrayList<Ping> findSharkPings(ArrayList<String> favSharkNames, ArrayList<Ping> pastMonth){
		ArrayList<Ping> StringNameToPing = new ArrayList<Ping>();
		for(String i : favSharkNames){
			for(Ping j : pastMonth){
				if(j.getName().equals(i)){
					StringNameToPing.add(j);
				}
			}
		}
		return StringNameToPing;
	}
	
	/**
	 * 
	 * @param favSharks
	 */
	public void addFavSharksInOrder(ArrayList<Shark> favSharks){
		favSharkDistance = new TreeMap<Double, String>();
		for(Shark i : favSharks){	
		   favSharkDistance.put(distance(i), i.getName());
		}
		for(Double distance : favSharkDistance.keySet()){
			favFrameView.addFavToFavPanel(distance, favSharkDistance.get(distance));
		}
	}
	
	/**
	 * 
	 * @param fileName
	 * @param favSharks
	 */
	public void saveFavSharksToFile(String fileName, ArrayList<Shark> favSharks){
		System.out.println(favSharks);
		try {
			PrintWriter writer;
			writer = new PrintWriter("../Resources/Database/" + fileName + ".txt");
			writer.print("");
			writer.close();
		} 
		catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}
		try{
			FileWriter fileWriter = new FileWriter("../Resources/Database/" + fileName + ".txt");
			BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
			for(Shark i : favSharks){
				bufferedWriter.write(i.getName() + "\n");
			}
			bufferedWriter.flush();
			bufferedWriter.close();
			
		}
		catch(IOException e){
			e.printStackTrace();
			System.out.println("Error: Text could not be written to file...");
		}
	}
	
	/**
	 * 
	 * @param shark
	 * @return double
	 */
	private double distance(Shark shark) {
		Location sharkLocation = getLastLocation(shark.getName());
		Location kingsLocation = new Location(51.5119, 0.1161);
		
		double sharkLatitude = sharkLocation.getLatitude();
		double sharkLongitude = sharkLocation.getLongitude();
		double kingsLatitude = kingsLocation.getLatitude();
		double kingLongitude = kingsLocation.getLongitude();
		
		double theta = sharkLongitude - kingLongitude;
		distance = Math.sin(deg2rad(sharkLatitude)) * Math.sin(deg2rad(kingsLatitude)) + Math.cos(deg2rad(sharkLatitude)) * Math.cos(deg2rad(kingsLatitude)) * Math.cos(deg2rad(theta));
		distance = Math.acos(distance);
		distance = rad2deg(distance);
		distance = distance * 60 * 1.1515;
		distance = distance * 0.8684;
		 
		return distance;
	}
	
	/**
	 * 
	 * @param deg
	 * @return double
	 */
	 public double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
     }
	 
	 /**
	  * 
	  * @param rad
	  * @return double
	  */
	 public double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
	 }
	 
	 public void printFavSharksinSearchFrame(){
		loading(favFrameView);
		String pingTime = null;
		for(Ping p : pastMonthDltDuplicates){
			Shark s = jawsModel.getShark(p.getName());
			if(s.getName().equals(sharkName)){
				cleanFrame(searchFrameView.getJPSearchResult(), searchFrameView);
				searchFrameView.sharkDetails(s.getName(), s.getGender(), s.getStageOfLife(), s.getSpecies(), s.getLength(), s.getWeight(), s.getDescription(), p.getTime(), false);
				refresh(searchFrameView);
				searchFrameView.getJPSearchResult().add(favFrameView.addVideo(p.getName(), jawsModel.getVideo(p.getName())));
				loadingComplete(favFrameView);
				favFrameView.setVisible(false);
				searchFrameView.setVisible(true);
			    break;
			}
		}
	}
	 
	 /**
	  * 
	  * @param sharkName
	  * @return String
	  */
	 public String getCoordinates(String sharkName) {
		 finalCoordinates = "";
		 for(Shark s: favSharks){
			 if(s.getName().equals(sharkName)){
			 location = sharkNado(s.getName(),jawsModel.getLastLocation(s.getName()).getLatitude()+","+jawsModel.getLastLocation(s.getName()).getLongitude());
			 break;
			 }
			 
		 }
		 return location;
	 }
	 
		
	 /**
	  * 
	  * @param sharkName
	  * @param finalCoordinates
	  * @return String
	  */
	 public String sharkNado(String sharkName, String finalCoordinates){
		 sharkCoordinates = new ArrayList<String>();
		 String sharkNado = "";
		 
		 URL url;
		 try{
			 
			 url = new URL("https://maps.googleapis.com/maps/api/elevation/json?locations=" + finalCoordinates + "&key=AIzaSyBdzADLdnf6O_hZ-PXUAyiQroqeA8NW-eU");
			
			 BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream(), Charset.forName("UTF-8")));
			 String inputLine = "";
			 while((inputLine=br.readLine()) !=null){
				 for(String i : inputLine.split("\n")){
					 if(i.contains("elevation")){
						 Double elevationRatio = Double.parseDouble(i.split(":")[1].trim().replace(",", ""));
						 if(elevationRatio > 0){
							 sharkNado = " SHARKNADO Event Occured, Shark on Land!";
						 }
					 }
				 }
			 }
			 br.close();
		 }
		 catch(MalformedURLException e){
			 e.printStackTrace();
		 }
		 catch(IOException e){
			 e.printStackTrace();
		 }
		 return sharkNado;
	}
	
	 /**
	  * 
	  * @param sharkName
	  */
	 public void setSharkName(String sharkName){
		this.sharkName = sharkName;
	}
	
	/**
	 * 
	 * @return ArrayList<String>
	 */
	public ArrayList<String> getSharkCoordinates(){
		return sharkCoordinates;
	}
	 
	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	//GENERAL EDITS
	/**
	 * 
	 * @param jComponent
	 * @param frame
	 */
	public void cleanFrame(JComponent jComponent, JFrame frame){
		Component[] searchPanels = jComponent.getComponents();
		for (Component i : searchPanels){
			((Container) i).removeAll();
		}
		refresh(frame);
	}
	
	/**
	 * 
	 * @param frame
	 */
	public void refresh(JFrame frame){ //General refresh method that takes in the frame as a parameter and refreshes that frame
		frame.revalidate();
		frame.repaint();
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
	
	/**
	 * 
	 * @param frame
	 */
	public void loading(Component frame){
		frame.setCursor(busyCursor);
	}
	
	/**
	 * 
	 * @param frame
	 */
	public void loadingComplete(Component frame){
		frame.setCursor(defaultCursor);
	}
	 
	/**
	 * 
	 * @param sharkName
	 * @return location
	 */
	public Location getLastLocation(String sharkName){
		return jawsModel.getLastLocation(sharkName);
	}
	
	/**
	 * 
	 * @return
	 */
	public ArrayList<Shark> getFavSharks(){
		return favSharks;
	}

	/**
	 * 
	 * @param name
	 * @return Shark
	 */
	public Shark getShark(String name){
		return jawsModel.getShark(name);
	}

	/**
	 * 
	 * @return String
	 */
	public String getLastUpdated(){
		return jawsModel.getLastUpdated();
	}

	/**
	 * 
	 * @return ArrayList<String>
	 */
	public ArrayList<String> getSharkNames(){
		return jawsModel.getSharkNames();
	}

	/**
	 * 
	 * @return ArrayList<String>
	 */
	public ArrayList<String> getTagLocations(){
		return jawsModel.getTagLocations();
	}
		
	/**
	 * 
	 * @return String
	 */
	public String getAcknowledgement(){
		return jawsModel.getAcknowledgement();
	}

	/**
	 * 
	 * @param name
	 * @return double
	 */
	public double getSharkLatitudeLocation(String name) {
		return jawsModel.getLastLocation(name).getLatitude();
	}
	
	/**
	 * 
	 * @param name
	 * @return double
	 */
	public double getSharkLongitudeLocation(String name) {
		return jawsModel.getLastLocation(name).getLongitude();
	}
}