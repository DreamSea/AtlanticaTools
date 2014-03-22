package gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import manager.DataManager;
import manager.Main;
import types.CraftBook;
import types.Craftable;
import types.Material;

public class MenuBar extends JMenuBar implements ActionListener
{
	JMenu menu;
	JMenu craftBooksSubMenu;
	JMenuItem[] craftBooks;
	
	JMenuItem save;
	
	public MenuBar()
	{
		menu = new JMenu("Stuffs.");
		
		add(menu);
		
		save = new JMenuItem("Save Item Costs");
		save.setMnemonic(KeyEvent.VK_S);
		
		menu.add(save);
		save.addActionListener(this);
		
		menu.addSeparator();
		
		craftBooksSubMenu = new JMenu("Select Craft Book");
		addCraftBooks();
		menu.add(craftBooksSubMenu);
	}

	private void addCraftBooks()
	{
		craftBooks = new JMenuItem[Main.dm.craftBooks.size()];
		for (int i = 0; i < craftBooks.length; i++)
		{
			craftBooks[i] = new JMenuItem(Main.dm.craftBooks.get(i).name);
			craftBooksSubMenu.add(craftBooks[i]);
			craftBooks[i].addActionListener(this);
		}
	}
	
	@Override
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
				DataManager.costPerWorkload = ((CraftBook) Main.dm.itemMap.get(e.getActionCommand())).getWorthPerWorkload();
				//System.out.println(DataManager.costPerWorkload);
				Main.dm.refreshItems();
			}
			//System.out.println(e.getActionCommand());
		}
	}
}
