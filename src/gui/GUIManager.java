package gui;

import java.awt.BorderLayout;
import java.awt.event.ActionListener;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.LocalTime;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import events.ItemChangeListener;
import events.ItemChangeNotifier;
import manager.DebugLogger;
import model.CraftBook;
import model.Craftable;
import model.DataManager;
import model.Item;
import model.Material;

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
	
	private JFrame frame;
	
	private boolean logging;
	
	public GUIManager(ItemChangeListener icl, ActionListener al, DataManager dm, boolean logging)
	{
		this.logging = logging;
		//TODO: one numberformatter for all?
		nf = NumberFormat.getInstance();
		
		guiActionListener = al;
		mainItemChangeNotifier = new ItemChangeNotifier(icl);
		
		createAndShowGUI(dm);
	}
	
	private void createAndShowGUI(DataManager dm)
	{
		DebugLogger.log("Building MenuBar", logging);
		guiMenuBar = new MenuBar(nf, guiActionListener, dm.getCraftBookListIterator());
		
		DebugLogger.log("Building CraftItemTree", logging);
		guiCraftItemTree = new CraftItemTree(mainItemChangeNotifier, dm.getCraftBookListIterator(), dm.getMaterialListIterator(), dm.getSkillTreeIterators());
		
		DebugLogger.log("Building CraftComponentPanel", logging);
		guiCraftComponentPanel = new CraftComponentPanel(mainItemChangeNotifier);
		
		DebugLogger.log("Building CraftIntoPanel", logging);
		guiCraftIntoPanel = new CraftIntoPanel(mainItemChangeNotifier);
		
		DebugLogger.log("Building ItemInfoPanel", logging);
		guiItemInfoPanel = new ItemInfoPanel(mainItemChangeNotifier);
		
		DebugLogger.log("Packing and showing GUI", logging);
		frame = new JFrame("Atlantica");
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
	 * Trying to get most of the type casting done here, though
	 * there may still be some casting in the gui classes (currently
	 * CraftComponentPanel for deciding how to render rows).
	 * 
	 * @param item Item to display on GUI
	 */
	public void showItem(Item item)
	{
		//when messing with craft book selector w/o item first loaded
		if (item == null)
		{
			return;
		}
		
		currentItem = item;

		//CraftIntoPanel treats all Items equally.
		guiCraftIntoPanel.craftTable.setData(item);
		
		//Casting for CraftComponentPanel and ItemInfoPanel.
		switch(item.getType())
		{
		case MATERIAL:
			showMaterial((Material) item);
			break;
		case CRAFTABLE:
			showCraftable((Craftable) item);
			break;
		case CRAFTBOOK:
			showCraftBook((CraftBook) item);
			break;
		default:
			try {
				throw new Exception();
			} catch (Exception e) {
				System.err.println("Unknown item type.");
				e.printStackTrace();
			}
			break;
		}
	}
	
	/*
	 * Casted versions of showItem();
	 */
	private void showMaterial(Material toShow)
	{
		guiCraftComponentPanel.loadItem(toShow);
		guiItemInfoPanel.loadMaterial(toShow);
	}
	
	private void showCraftable(Craftable toShow)
	{
		guiCraftComponentPanel.loadItem(toShow);
		guiItemInfoPanel.loadCraftable(toShow);
	}
	
	private void showCraftBook(CraftBook toShow)
	{
		guiCraftComponentPanel.loadItem(toShow);
		guiItemInfoPanel.loadCraftBook(toShow);
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
	
	public void saveDialog()
	{
		JOptionPane.showMessageDialog(frame, "Item Prices Saved.");
	}
}
