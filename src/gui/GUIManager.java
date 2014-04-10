package gui;

import java.awt.BorderLayout;
import java.awt.event.ActionListener;
import java.text.NumberFormat;

import javax.swing.JFrame;
import events.ItemChangeListener;
import events.ItemChangeNotifier;
import model.DataManager;
import model.Item;

/*
 * 		GUIManager : The public face of the gui package, holds all the
 * 			gui components and creates frame.
 */

public class GUIManager {
	
	/*
	 * Strings being immutable or something so figured this is used enough
	 * to warrant keepin in memory. The default string for when information
	 * doesn't apply to loaded item
	 */
	final static String BLANKSTRING = "---";
	
	private MenuBar guiMenuBar;
	private CraftItemTree guiCraftItemTree;
	private CraftComponentPanel guiCraftComponentPanel;
	private CraftIntoPanel guiCraftIntoPanel;
	private ItemInfoPanel guiItemInfoPanel;
	
	private Item currentItem; //holds info of item currently displayed
	
	private NumberFormat nf;
	
	private ItemChangeNotifier mainItemChangeNotifier;
	private ActionListener guiActionListener;
	
	public GUIManager(ItemChangeListener icl, ActionListener al, DataManager dm)
	{
		//TODO: one numberformatter for all?
		nf = NumberFormat.getInstance();
		
		guiActionListener = al;
		mainItemChangeNotifier = new ItemChangeNotifier(icl);
		
		createAndShowGUI(dm);
	}
	
	private void createAndShowGUI(DataManager dm)
	{
		//TODO: create a loader class/helper so that I can make datamanager item arraylists private?
		guiMenuBar = new MenuBar(nf, guiActionListener, dm.getCraftBookList());
		guiCraftItemTree = new CraftItemTree(mainItemChangeNotifier, dm.getCraftBookList(), dm.getMaterialList(), dm.getSkillTree());
		guiCraftComponentPanel = new CraftComponentPanel(mainItemChangeNotifier);
		guiCraftIntoPanel = new CraftIntoPanel(mainItemChangeNotifier);
		guiItemInfoPanel = new ItemInfoPanel(mainItemChangeNotifier);
		
		JFrame frame = new JFrame("Atlantica");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		frame.setLocation(25, 25);
		
		frame.setJMenuBar(guiMenuBar);
		
		frame.add(guiCraftItemTree, BorderLayout.WEST);
		frame.add(guiItemInfoPanel, BorderLayout.CENTER);
		frame.add(guiCraftIntoPanel, BorderLayout.EAST);
		frame.add(guiCraftComponentPanel, BorderLayout.SOUTH);
		
		frame.pack();
		frame.setVisible(true);
	}
	
	/**
	 * @param item Item to display on GUI
	 */
	public void showItem(Item item)
	{
		currentItem = item;
		guiCraftComponentPanel.loadItem(item);
		guiCraftIntoPanel.craftTable.setData(item);
		guiItemInfoPanel.loadItem(item);
	}
	
	/**
	 * For updating item property fields for events that come
	 * from menu bar (such as changing what craftbook is used
	 * for calculations)
	 * 
	 * @return Item currently being displayed
	 */
	public Item getCurrentItem()
	{
		return currentItem;
	}
}
