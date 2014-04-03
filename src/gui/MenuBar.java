package gui;

import java.awt.event.ActionListener;
import java.text.NumberFormat;
import java.util.ArrayList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import model.CraftBook;

/*
 * 		MenuBar : Contains buttons for saving prices as well as
 * 			selecting which craft book to base profit calculations off of.
 * 			Package private.
 */

class MenuBar extends JMenuBar
{
	private NumberFormat workloadFormatter; //at the moment, for formatting workload numbers
	private ActionListener menuBarListener;
	
	JMenu menu;
	JMenu craftBooksSubMenu;
	JMenuItem[] craftBooksList;
	
	JMenuItem saveItemPrices; //the item for saving item prices
	
	MenuBar(NumberFormat nf, ActionListener al, ArrayList<CraftBook> bookList)
	{
		workloadFormatter = nf;
		menuBarListener = al;
		
		//TODO: stuffs is srs bsns
		menu = new JMenu("Stuffs.");
		
		add(menu);
		
		saveItemPrices = new JMenuItem("Save Item Prices");
		//saveItemPrices.setMnemonic(KeyEvent.VK_S);
		menu.add(saveItemPrices);
		saveItemPrices.setActionCommand("Save Item Prices");
		saveItemPrices.addActionListener(menuBarListener);
		
		menu.addSeparator();
		
		//craft books for profit calculations
		craftBooksSubMenu = new JMenu("Select Craft Book");
		addCraftBooks(bookList);
		menu.add(craftBooksSubMenu);
	}

	/*
	 * populates craft book list in submenu
	 */
	private void addCraftBooks(ArrayList<CraftBook> bookList)
	{
		craftBooksList = new JMenuItem[bookList.size()];
				
		for (int i = 0; i < craftBooksList.length; i++)
		{
			CraftBook cb = bookList.get(i);
			
			craftBooksList[i] = new JMenuItem(cb.getName() +" ("+workloadFormatter.format(cb.getWorkload())+")");
			craftBooksSubMenu.add(craftBooksList[i]);
			craftBooksList[i].addActionListener(menuBarListener);
			craftBooksList[i].setActionCommand(cb.getName()); //action commands in the form of item name
		}
	}
}
