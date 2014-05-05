package gui;

import java.awt.event.ActionListener;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import model.CraftBook;

/*
 * 		MenuBar : Contains buttons for saving prices as well as
 * 			selecting which craft book to base profit calculations off of.
 * 			Package private.
 */

class MenuCrafting extends JMenu
{	
	private NumberFormat workloadFormatter; //at the moment, for formatting workload numbers
	private ActionListener menuBarListener;
	
	JMenu craftBooksSubMenu;
	JMenuItem[] craftBooksList;
	
	JMenuItem saveItemPrices; //the item for saving item prices
	
	MenuCrafting(NumberFormat nf, ActionListener al, Iterator<CraftBook> bookList)
	{
		workloadFormatter = nf;
		menuBarListener = al;
		
		String name = "Crafting";
		setText(name);
		setName(name); //name used for comparing action command to which menubar to show
		
		saveItemPrices = new JMenuItem("Save Item Prices");
		//saveItemPrices.setMnemonic(KeyEvent.VK_S);
		add(saveItemPrices);
		saveItemPrices.setActionCommand("Save Item Prices");
		saveItemPrices.addActionListener(menuBarListener);
		
		addSeparator();
		
		//craft books for profit calculations
		craftBooksSubMenu = new JMenu("Select Craft Book");
		addCraftBooks(bookList);
		add(craftBooksSubMenu);
	}

	/*
	 * populates craft book list in submenu
	 */
	private void addCraftBooks(Iterator<CraftBook> i)
	{
		ArrayList<CraftBook> bookList = new ArrayList<CraftBook>();
		while (i.hasNext())
		{
			bookList.add(i.next());
		}
		
		craftBooksList = new JMenuItem[bookList.size()];
		
		for (int slot = 0; slot < bookList.size(); slot++)
		{
			CraftBook cb = bookList.get(slot);
			
			craftBooksList[slot] = new JMenuItem(cb.getName() +" ("+workloadFormatter.format(cb.getWorkload())+")");
			craftBooksSubMenu.add(craftBooksList[slot]);
			craftBooksList[slot].addActionListener(menuBarListener);
			craftBooksList[slot].setActionCommand(cb.getName()); //action commands in the form of item name
		}
	}
}
