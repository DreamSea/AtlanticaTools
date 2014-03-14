package manager;

import java.awt.BorderLayout;

import gui.CraftIntoPanel;
import gui.CraftItemTree;
import gui.ItemPanel;
import gui.MenuBar;

import javax.swing.JFrame;

public class GUIManager {
	
	final static int WIDTH = 300;
	final static int HEIGHT = 600;
	
	public MenuBar mb = new MenuBar();
	public CraftItemTree cit = new CraftItemTree();
	public ItemPanel ip = new ItemPanel();
	public CraftIntoPanel cip = new CraftIntoPanel();
	
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
		
		//frame.add(new TreeDemo(), BorderLayout.EAST);
		frame.add(cit, BorderLayout.WEST);
		frame.add(cip, BorderLayout.EAST);
		frame.add(ip, BorderLayout.SOUTH);
		
		frame.pack();
		frame.setVisible(true);
	}

}
