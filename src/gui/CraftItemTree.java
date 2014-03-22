package gui;

import java.awt.Dimension;
import java.util.ArrayList;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeSelectionModel;

import manager.Main;
import types.CraftBook;
import types.Craftable;
import types.Item;
import types.Material;

public class CraftItemTree extends JPanel implements TreeSelectionListener
{
	
	private JTree tree;
	
	DefaultMutableTreeNode category = null;
	DefaultMutableTreeNode book = null;
	
	public CraftItemTree()
	{	
		DefaultMutableTreeNode top =
				new DefaultMutableTreeNode("Items");
		
		createNodes(top);
		
		tree = new JTree(top);
		tree.getSelectionModel().setSelectionMode
		(TreeSelectionModel.SINGLE_TREE_SELECTION);

		//Listen for when the selection changes.
		tree.addTreeSelectionListener(this);
	

		JScrollPane treeView = new JScrollPane(tree);
		

		treeView.setPreferredSize(new Dimension(200, 200));
	
		add(treeView);
	}

	public void valueChanged(TreeSelectionEvent e)
	{
		DefaultMutableTreeNode node = (DefaultMutableTreeNode)
				tree.getLastSelectedPathComponent();

		if (node == null || !node.isLeaf()) return;
	
		Item selected = (Item) node.getUserObject();
		
		/*System.out.print("Ping: "+ selected.name);
		if (selected.type == 0)
		{
			System.out.println(" (Material)");
			
		}
		else
		{
			System.out.println(" (Craftable)");
		}*/
		//Main.gm.ccp.loadItem(selected);
		Main.gm.showItem(selected);
	}
	
	private void createNodes(DefaultMutableTreeNode top)
	{	
		category = new DefaultMutableTreeNode("Craft Book");
		top.add(category);
		for (CraftBook cb : Main.dm.craftBooks)
		{
			book = new DefaultMutableTreeNode(cb);
			category.add(book);
		}
		
		category = new DefaultMutableTreeNode("Material");
		top.add(category);
		for (Material m : Main.dm.materials)
		{
			book = new DefaultMutableTreeNode(m);
			category.add(book);
		}

		craftableNodes("Action", Main.dm.action, top);
		craftableNodes("Crystal", Main.dm.crystal, top);
		craftableNodes("Food", Main.dm.food, top);
		craftableNodes("Machine", Main.dm.machine, top);
		craftableNodes("Medicine", Main.dm.medicine, top);
		craftableNodes("Tool", Main.dm.tool, top);
	}
	
	private void craftableNodes(String name, ArrayList<Craftable> skill, DefaultMutableTreeNode top)
	{
		category = new DefaultMutableTreeNode(name);
		top.add(category);
		
		for (Craftable c : skill)
		{
			book = new DefaultMutableTreeNode(c);
			category.add(book);
		}
	}
}
