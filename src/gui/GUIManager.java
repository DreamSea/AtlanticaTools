package gui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.NumberFormat;

import javax.swing.DefaultListSelectionModel;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;

import events.ItemChangeListener;
import events.ItemChangeNotifier;
import model.DataManager;
import model.Item;

public class GUIManager {
	
	final static int WIDTH = 300;
	final static int HEIGHT = 600;
	
	private MenuBar mb;// = new MenuBar();
	private CraftItemTree cit;// = new CraftItemTree();
	public CraftComponentPanel ccp;// = new CraftComponentPanel();
	public CraftIntoPanel cip;// = new CraftIntoPanel();
	public ItemInfoPanel iip;// = new ItemInfoPanel();
	
	private Item currentItem;
	
	private NumberFormat nf;
	
	private ItemChangeNotifier icn;
	private ActionListener al;
	
	public GUIManager(ItemChangeListener icl, ActionListener al, DataManager dm)
	{
		nf = NumberFormat.getInstance();
		
		this.al = al;
		icn = new ItemChangeNotifier(icl);
		
		createAndShowGUI(dm);
		//System.out.println(icn == null);
	}
	
	private void createAndShowGUI(DataManager dm)
	{	
		mb = new MenuBar(nf, al, dm.getCraftBookList());
		cit = new CraftItemTree(icn, dm.getCraftBookList(), dm.getMaterialList(), dm.getSkillTree());
		ccp = new CraftComponentPanel(icn);
		cip = new CraftIntoPanel(icn);
		iip = new ItemInfoPanel(icn);
		
		JFrame frame = new JFrame("Atlantica");
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
	
	public void showItem(Item i)
	{
		currentItem = i;
		ccp.loadItem(i);
		cip.craftTable.setData(i);
		iip.loadItem(i);
	}
	
	public Item getCurrentItem()
	{
		return currentItem;
	}

	/*//Listening in on: MenuBar
	@Override
	public void actionPerformed(ActionEvent e) {
		System.out.println("GUIManager Action Event: "+e.paramString());
		
	}

	@Override
	public void valueChanged(TreeSelectionEvent e) {
		System.out.println("GUIManager Tree Event: "+
				e.getNewLeadSelectionPath().getLastPathComponent().toString());
		
	}

	@Override
	public void propertyChange(PropertyChangeEvent e) {
		System.out.println("GUIManager Property Event: "+((JFormattedTextField) e.getSource()).getName()+" "+e.getNewValue());

	}

	
	//List Selection Event will need some work... maybe getTable() from CraftIntoPanel?
	@Override
	public void valueChanged(ListSelectionEvent e) {
		//table.getValueAt(e.getFirstIndex(), 0)
		System.out.println("GUIManager List Event: "+((DefaultListSelectionModel)(e.getSource())));
	}*/
	


}
