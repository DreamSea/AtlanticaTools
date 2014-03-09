package manager;

/*
 * 	The manager manager. Holds Data/GUI and communicates in between.
 * 
 * 	Package gui holds specific panes (crafting, etc)
 * 
 * 	Package types holds item classes
 */

public class Main {
	
	public static StringBuilder sb = new StringBuilder();
	
	public static DataManager dm;
	public static GUIManager gm;
	
	public static void main(String args[])
	{		
		dm = new DataManager();
		gm = new GUIManager();
		
		//System.out.println(dm.getItem("Dumpling"));
		//System.out.println(dm.getItem("Grilled Pork"));
		//System.out.println(dm.getItem("Pork").craftsInto.toString());
	}
}
