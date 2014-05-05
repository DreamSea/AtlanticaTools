package gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

public class MenuBar extends JMenuBar implements ActionListener {
	
	private JMenu baseMenu;
	
	private JMenuItem infoCard;
	private JMenuItem craftCard;
	
	private ActionListener menuBarListener;

	public MenuBar(ActionListener al, JMenu... jm)
	{
		menuBarListener = al;
		
		baseMenu = new JMenu("Tools");
		
		/*
		 * Adding default menu items that appear no matter what tool is loaded
		 */
		infoCard = new JMenuItem("Program Info");
		infoCard.setActionCommand("CardLayout Info");
		infoCard.addActionListener(menuBarListener);
		infoCard.addActionListener(this);
		baseMenu.add(infoCard);
		
		craftCard = new JMenuItem("Crafting Calculator");
		craftCard.setActionCommand("CardLayout Crafting");
		craftCard.addActionListener(menuBarListener);
		craftCard.addActionListener(this);
		baseMenu.add(craftCard);
		
		add(baseMenu);
		
		/*
		 * adds data for specific menu bars that coincide with tools and
		 * hides them until needed
		 */
		for (JMenu hmm : jm)
		{
			add(hmm);
			hmm.setVisible(false);
			hmm.setEnabled(false);
		}
		
		//TODO: temporary fix to autoshow menu bar for crafting w/ initial guimanager card load
		getMenu(1).setEnabled(true);
		getMenu(1).setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String s = (e.getActionCommand().split(" "))[1];
		
		/*
		 * shows specific menu item depending on which card/action is being called out
		 * and hides the rest
		 */
		for (int i = 1; i < getMenuCount(); i++)
		{
			if (getMenu(i).getName().compareTo(s) == 0)
			{
				getMenu(i).setEnabled(true);
				getMenu(i).setVisible(true);
			}
			else
			{
				getMenu(i).setEnabled(false);
				getMenu(i).setVisible(false);
			}
		}
		
	}
	
}
