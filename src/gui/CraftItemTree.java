package gui;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeSelectionModel;

import events.ItemChangeEvent;
import events.ItemChangeNotifier;
import model.CraftBook;
import model.Craftable;
import model.Item;
import model.Material;

/*
 * 		CraftItemTree : Tree navigation between craft skills
 *			and the craftables. Package private.
 */
class CraftItemTree extends JPanel implements TreeSelectionListener
{
	
	private JTree tree;
	private ItemChangeNotifier craftTreeNotifier;
	
	private DefaultMutableTreeNode category = null;
	private DefaultMutableTreeNode book = null;
	
	//TODO: there needs to be a better way of passing information in...
	CraftItemTree(ItemChangeNotifier icn, 
			Iterator<CraftBook> craftBooks,
			Iterator<Material> materials,
			ArrayList<Iterator<Craftable>> skillTree)
	{	
		//System.out.println(icn == null);
		this.craftTreeNotifier = icn;
		
		DefaultMutableTreeNode top =
				new DefaultMutableTreeNode("Items");
		
		createNodes(top, craftBooks, materials, skillTree);
		
		tree = new JTree(top);
		tree.getSelectionModel().setSelectionMode
		(TreeSelectionModel.SINGLE_TREE_SELECTION);

		//Listen for when the selection changes.
		tree.addTreeSelectionListener(this);
	
		//make the tree scrollable
		JScrollPane treeView = new JScrollPane(tree);
		

		treeView.setPreferredSize(new Dimension(200, 280));
	
		add(treeView);
	}

	public void valueChanged(TreeSelectionEvent e)
	{
		DefaultMutableTreeNode node = (DefaultMutableTreeNode)
				tree.getLastSelectedPathComponent();

		if (node == null || !node.isLeaf()) //no item to load if still navigating
		{
			return;
		}
	
		Item selected = (Item) node.getUserObject();
	
		craftTreeNotifier.fireItemChangeEvent(
				new ItemChangeEvent(e.getSource(), selected.getName()));

		//TODO: reset selection after item is displayed?
	}
	
	//tree creation logic
	private void createNodes(DefaultMutableTreeNode top, 
			Iterator<CraftBook> craftBooks,
			Iterator<Material> materials,
			ArrayList<Iterator<Craftable>> skillTree)
	{	
		category = new DefaultMutableTreeNode("Craft Book");
		top.add(category);
		while (craftBooks.hasNext())
		{
			book = new DefaultMutableTreeNode(craftBooks.next());
			category.add(book);
		}
		
		category = new DefaultMutableTreeNode("Material");
		top.add(category);
		while (materials.hasNext())
		{
			book = new DefaultMutableTreeNode(materials.next());
			category.add(book);
		}

		//TODO: TEMPORARY TERRIBLE SHORTCUT
		craftableNodes("Action", skillTree.get(0), top);
		craftableNodes("Crystal", skillTree.get(1), top);
		craftableNodes("Food", skillTree.get(2), top);
		craftableNodes("Machine", skillTree.get(3), top);
		craftableNodes("Medicine", skillTree.get(4), top);
		craftableNodes("Tool", skillTree.get(5), top);
	}
	
	//populate the subfolder with the craftables
	private void craftableNodes(String name, Iterator<Craftable> skill, DefaultMutableTreeNode top)
	{
		category = new DefaultMutableTreeNode(name);
		top.add(category);
		
		while (skill.hasNext())
		{
			book = new DefaultMutableTreeNode(skill.next());
			category.add(book);
		}
	}
}
