package gui;

import java.awt.CardLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JPanel;

import manager.DebugLogger;
import model.DataManager;
import events.ItemChangeListener;

public class GUIManager implements ActionListener {

	private ItemChangeListener icl;
	private ActionListener al;
	//?? private DataManager dm;
	private boolean logging;
	
	private GUIManagerCrafting gmc;
	
	private JFrame frame;
	private CardLayout cl;
	private JPanel cards;
	
	public GUIManager(ItemChangeListener icl, ActionListener al, DataManager dm, boolean logging)
	{
		this.icl = icl;
		this.al = al;
		//?? this.dm = dm;
		this.logging = logging;
		createAndShowGUI(dm);
	}
	
	private void createAndShowGUI(DataManager dm)
	{	
		DebugLogger.log("Packing and showing GUI", logging);
		frame = new JFrame("Atlantica");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		frame.setLocation(25, 25);
		
		gmc = new GUIManagerCrafting(icl, al, dm, logging);
		
		cl = new CardLayout();
		cards = new JPanel(cl);
		
		/*
		 * cards.add( --- , Object constraints), constraints named after 
		 * JMenuBar action commands.
		 */
		cards.add(gmc, "Crafting");
		cards.add(new GUIManagerProgramInfo(), "Info");

		
		frame.add(cards);
		
		frame.pack();

		/*
		 * defaults program to crafting tool
		 */
		cl.show(cards, "Crafting");
		
		frame.setJMenuBar(new MenuBar(this, gmc.getMenuBar()));
		frame.setVisible(true);
	}

	public GUIManagerCrafting getGUIManagerCrafting() {
		return gmc;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String s = (e.getActionCommand().split(" "))[1];
		cl.show(cards, s);		
	}
	
}
