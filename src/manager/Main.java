package manager;

import model.DataManager;
import gui.GUIManager;

/*
 * 	The manager manager. Holds Data/GUI and communicates in between.
 * 
 * 	Package gui holds specific panes (crafting, etc)
 * 
 * 	Package types holds item classes
 */

public class Main {
	
	public static StringBuilder sb = new StringBuilder();
	
	private static ManagerManager mm;
	
	public static DataManager dm;
	public static GUIManager gm;
	
	public static void main(String args[])
	{
		dm = new DataManager();
		
		/*
		 * init() and loadPrices() are moved here out of DataManager constructor
		 * because they now call DataManager data structures -_-;;
		 * 
		 * Result of current fix to loading items, may think up something better
		 */
		dm.init();
		dm.loadPrices();
		
		gm = new GUIManager();
		
		//System.out.println(dm.getItem("Dumpling"));
		//System.out.println(dm.getItem("Grilled Pork"));
		//System.out.println(dm.getItem("Pork").craftsInto.toString());

		
		//mm = new ManagerManager();
	}
}
