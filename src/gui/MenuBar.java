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
	public void actionPerformed(ActionEvent e)
	{
		if (e.getSource().equals(save))
		{
			Main.dm.savePrices();
		}
	}
}
