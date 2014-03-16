package manager;

import java.awt.BorderLayout;

import gui.CraftIntoPanel;
import gui.CraftItemTree;
import gui.CraftComponentPanel;
import gui.ItemInfoPanel;
import gui.MenuBar;

import javax.swing.JFrame;

public class GUIManager {
	
	final static int WIDTH = 300;
	final static int HEIGHT = 600;
	
	public MenuBar mb = new MenuBar();
	public CraftItemTree cit = new CraftItemTree();
	public CraftComponentPanel ccp = new CraftComponentPanel();
	public CraftIntoPanel cip = new CraftIntoPanel();
	public ItemInfoPanel iip = new ItemInfoPanel();
	
	public GUIManager()
	{
		createAndShowGUI();
	}
	
	private void createAndShowGUI()
	{
		JFrame frame = new JFrame("Atlantica Dreaming");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		frame.setLocation(100, 50);
		//frame.setSize(WIDTH, HEIGHT);
		
		frame.setJMenuBar(mb);
		
		frame.add(cit, BorderLayout.WEST);
		frame.add(iip, BorderLayout.CENTER);
		frame.add(cip, BorderLayout.EAST);
		frame.add(ccp, BorderLayout.SOUTH);
		
		frame.pack();
		frame.setVisible(true);
	}

}
