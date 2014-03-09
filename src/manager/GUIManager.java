package manager;

import java.awt.BorderLayout;

import gui.CraftItemTree;
import gui.ItemPanel;
import javax.swing.JFrame;

public class GUIManager {
	
	final static int WIDTH = 300;
	final static int HEIGHT = 600;
	
	public CraftItemTree cit = new CraftItemTree();
	public ItemPanel ip = new ItemPanel();
	
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
		
		//frame.add(new TreeDemo(), BorderLayout.EAST);
		frame.add(cit, BorderLayout.WEST);
		frame.add(ip, BorderLayout.SOUTH);
		
		frame.pack();
		frame.setVisible(true);
	}

}
