package gui;

import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.text.NumberFormat;
import java.util.ArrayList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import model.CraftBook;

public class MenuBar extends JMenuBar
{
	private NumberFormat nf;
	private ActionListener al;
	
	
	JMenu menu;
	JMenu craftBooksSubMenu;
	JMenuItem[] craftBooks;
	
	JMenuItem save;
	
	public MenuBar(NumberFormat format, ActionListener al, ArrayList<CraftBook> bookList)
	{
		nf = format;
		this.al = al;
		
		menu = new JMenu("Stuffs.");
		
		add(menu);
		
		save = new JMenuItem("Save Item Prices");
		save.setMnemonic(KeyEvent.VK_S);
		
		menu.add(save);
		//save.addActionListener(this);
		save.setActionCommand("Save Item Prices");
		save.addActionListener(al);
		
		menu.addSeparator();
		
		craftBooksSubMenu = new JMenu("Select Craft Book");
		addCraftBooks(bookList);
		menu.add(craftBooksSubMenu);
	}

	private void addCraftBooks(ArrayList<CraftBook> bookList)
	{
		craftBooks = new JMenuItem[bookList.size()];
				
		for (int i = 0; i < craftBooks.length; i++)
		{
			CraftBook cb = bookList.get(i);
			
			craftBooks[i] = new JMenuItem(cb.getName() +" ("+nf.format(cb.getWorkload())+")");
			craftBooksSubMenu.add(craftBooks[i]);
			craftBooks[i].addActionListener(al);
			craftBooks[i].setActionCommand(cb.getName());
		}
	}
	
	/*@Override
	public void actionPerformed(ActionEvent e)
	{
		if (e.getSource().equals(save))
		{
			Main.dm.savePrices();
		}
		else
		{
			if (Main.dm.itemMap.containsKey(e.getActionCommand()))
			{
				DataManager.setCraftBook((CraftBook) Main.dm.itemMap.get(e.getActionCommand()));
				//System.out.println(DataManager.costPerWorkload);
				Main.dm.refreshItems();
			}
			//System.out.println(e.getActionCommand());
		}
	}*/
}
