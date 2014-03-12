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

import manager.Main;
import types.Craftable;
import types.Material;

public class MenuBar extends JMenuBar implements ActionListener
{
	JMenu menu;
	JMenuItem save;
	
	public MenuBar()
	{
		menu = new JMenu("Stuffs.");
		
		add(menu);
		
		save = new JMenuItem("Save Item Costs");
		save.setMnemonic(KeyEvent.VK_S);
		
		menu.add(save);
		
		save.addActionListener(this);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		//System.out.println(e.getSource());
		//System.out.println(save);
		if (e.getSource().equals(save))
		{
			Main.dm.savePrices();
		}
	}
	
	/*
	private void loadPrices()
	{
		try
		{
			Scanner input = new Scanner(new File("prices.txt"));

			while (input.hasNext())
			{
				String temp = input.next().replace('_', ' ');
				
				if (itemMap.containsKey(temp))
				{
					itemMap.get(temp).worth = input.nextInt();
				}
				else
				{
					input.nextInt();
				}
			}
			
			input.close();
			
			for (Material m : materials)
			{
				m.updateCost();
			}
			
			for (ArrayList<Craftable> s : skillTree)
			{
				//System.out.println("ping");
				for (Craftable c : s)
				{
					c.updateCost();
				}
			}
		}
		catch(FileNotFoundException e)
		{
			
		}
	}
	 */
}
