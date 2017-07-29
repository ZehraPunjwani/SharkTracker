package jaws.model;

import api.jaws.Jaws;
import jaws.controller.JawsController;

/**
 * 
 * @author Zehra Punjwani, Meghana Santhosh, Kent Millamena, and Riya Karia
 *
 */
public class JawsModel extends Jaws{
	
	private JawsController controller;

	/**
	 * 
	 * @param controller
	 */
	public JawsModel(JawsController controller){
		super("2EK8rTzj6mqOUR8l", "h8646GpNNLpHbC7R");
		
		this.controller = controller;
	}
}
