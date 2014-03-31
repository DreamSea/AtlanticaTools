package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.NumberFormat;

import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;

import events.ItemChangeEvent;
import events.ItemChangeNotifier;
import model.CraftBook;
import model.Craftable;
import model.Item;
import model.Material;


/*
 * 		ItemPanel : Contains main interface for switching
 * 			between crafted items and their components
 */
public class CraftComponentPanel extends JPanel implements ActionListener, PropertyChangeListener {

	final int BUTTONWIDTH = 175;
	final int WORTHWIDTH = 100;
	final int SPACE = 35; //for each row
	final int TOPSPACE = 35; //for top

	final int ITEMROWS = 10;
	final int ROWHEIGHT = 30;

	final String BLANKSTRING = "---";

	//for loop javax.swing items
	private JButton[] ingredientButton;
	private JLabel[] ingredientNumber;
	private JFormattedTextField[] ingredientWorth;
	private JLabel[] ingredientWorthTotal;
	private JLabel[] ingredientDateUpdated;

	private Craftable loaded;
	private Material loadedMat;
	private CraftBook loadedCraftBook;

	private NumberFormat nf;

	//private PropertyChangeListener pcl;
	
	private ItemChangeNotifier icn;

	public CraftComponentPanel(ItemChangeNotifier icn)
	{
		this.icn = icn;
		//this.pcl = pcl;
		
		nf = NumberFormat.getNumberInstance();
		nf.setGroupingUsed(true);

		//for loop javax.swing array
		ingredientButton = new JButton[ITEMROWS];
		ingredientNumber = new JLabel[ITEMROWS];
		ingredientWorth = new JFormattedTextField[ITEMROWS];
		ingredientWorthTotal = new JLabel[ITEMROWS];
		ingredientDateUpdated = new JLabel[ITEMROWS];

		setLayout(null);
		setPreferredSize(new Dimension(
				5*SPACE+BUTTONWIDTH+WORTHWIDTH+WORTHWIDTH, 100+SPACE*ITEMROWS));
		setBackground(Color.lightGray);

		/*
		 * TODO specific bounds set for now, to variables later
		 * 
		 * Create the rows on panel with loop
		 */
		for (int i = 0; i < ITEMROWS; i++)
		{
			ingredientButton[i] = new JButton(BLANKSTRING);
			ingredientButton[i].addActionListener(this);

			ingredientNumber[i] = new JLabel(BLANKSTRING);

			ingredientWorth[i] = new JFormattedTextField(nf);
			ingredientWorth[i].setText(BLANKSTRING);
			ingredientWorth[i].addPropertyChangeListener("value", this);
			

			ingredientWorthTotal[i] = new JLabel(BLANKSTRING);
			ingredientDateUpdated[i] = new JLabel(BLANKSTRING);

			add(ingredientButton[i]);
			add(ingredientNumber[i]);
			add(ingredientWorth[i]);
			add(ingredientWorthTotal[i]);
			add(ingredientDateUpdated[i]);

			ingredientButton[i].setBounds(
					SPACE, TOPSPACE+SPACE*i, BUTTONWIDTH, ROWHEIGHT);
			ingredientNumber[i].setBounds(
					2*SPACE+BUTTONWIDTH, TOPSPACE+SPACE*i, 30, ROWHEIGHT);
			ingredientWorth[i].setBounds(
					3*SPACE+BUTTONWIDTH, TOPSPACE+SPACE*i, WORTHWIDTH, ROWHEIGHT);
			ingredientWorthTotal[i].setBounds(
					4*SPACE+BUTTONWIDTH+WORTHWIDTH, TOPSPACE+SPACE*i, WORTHWIDTH, ROWHEIGHT);
			ingredientDateUpdated[i].setBounds(
					8*SPACE+BUTTONWIDTH+WORTHWIDTH, TOPSPACE+SPACE*i, WORTHWIDTH, ROWHEIGHT);
			
			disableRow(i);
		}
	}


	/*
	 * 	Sets panel to show information for item
	 */
	public void loadItem(Item i)
	{
		if (i.getType() == 0) //Material
		{
			loaded = null;
			loadedMat = (Material) i;
			for (int j = 0; j < ITEMROWS; j++)
			{
				disableRow(j);
			}
		}
		else if (i.getType() == 1) //Craftable
		{
			loaded = (Craftable) i;
			for (int j = 0; j < loaded.getCraftedFromLength(); j++)
			{
				setRow(j, loaded.getCraftedFromItems(j), 
						loaded.getCraftedFromNumbers(j), 
						loaded.getCraftedFromItems(j).getWorth());
			}
			for (int j = loaded.getCraftedFromLength(); j < ITEMROWS; j++)
			{
				disableRow(j);
			}
		}
		else if (i.getType() == 2)
		{
			loadedCraftBook = (CraftBook) i;
			for (int j = 0; j < ITEMROWS; j++)
			{
				disableRow(j);
			}
		}

		//TODO: move these into data Manager?
		//Main.gm.cip.craftTable.setData(i.name);
		//Main.gm.iip.loadItem(i);
		
	}

	public void actionPerformed(ActionEvent e) {
		//Main.gm.showItem(Main.dm.itemMap.get(e.getActionCommand()));
		icn.fireItemChangeEvent(new ItemChangeEvent(this, e.getActionCommand()));
	}


	public void propertyChange(PropertyChangeEvent e) {
		String tempName = ((JFormattedTextField) e.getSource()).getName();
		if (tempName.compareTo(BLANKSTRING) != 0) //only fires when user changes value
		{
			icn.fireItemChangeEvent(new ItemChangeEvent(this, tempName, (long) e.getNewValue()));
			
		/*	//System.out.print(e.getNewValue()+" "+tempName);
			Item tempItem = Main.dm.itemMap.get(tempName);
			tempItem.setWorth((long) e.getNewValue());

			refreshItem(tempItem);
			
			if (loaded != null)
			{
				//Main.gm.cip.craftTable.setData(loaded.name);
				//Main.gm.iip.loadItem(loaded);
				Main.gm.showItem(loaded);
			}
			else
			{
				//Main.gm.cip.craftTable.setData(loadedMat.name);
				//Main.gm.iip.loadItem(loadedMat);
				Main.gm.showItem(loadedMat);
			}
			//System.out.println(tempItem.cost);

			refreshPanel();*/
		}
	}


	/*
	 * 	Helps with disabling unused rows for current loaded item
	 */
	private void disableRow(int row)
	{
		ingredientButton[row].setText(BLANKSTRING);
		ingredientButton[row].setEnabled(false);
		ingredientNumber[row].setText(BLANKSTRING);
		ingredientNumber[row].setEnabled(false);
		ingredientWorth[row].setText(BLANKSTRING);
		ingredientWorth[row].setEnabled(false);
		ingredientWorthTotal[row].setText(BLANKSTRING);
		ingredientWorthTotal[row].setEnabled(false);
		ingredientDateUpdated[row].setText(BLANKSTRING);
		ingredientDateUpdated[row].setEnabled(false);
	}


	/*
	 * 	Helps with setting row information for current loaded item
	 */
	private void setRow(int row, Item i, int number, long worth)
	{
		ingredientButton[row].setEnabled(true);
		ingredientButton[row].setText(i.getName());
		ingredientButton[row].setActionCommand(i.getName());
		ingredientNumber[row].setEnabled(true);
		ingredientNumber[row].setText(String.valueOf(number));

		//ingredientWorth[row].removePropertyChangeListener("value", pcl);
		ingredientWorth[row].setEnabled(true);
		ingredientWorth[row].setName(BLANKSTRING);
		ingredientWorth[row].setValue(worth);
		ingredientWorth[row].setName(i.getName());
		//ingredientWorth[row].addPropertyChangeListener("value", pcl);

		ingredientWorthTotal[row].setEnabled(true);
		ingredientWorthTotal[row].setText(nf.format(number*worth));
		
		ingredientDateUpdated[row].setEnabled(true);
		ingredientDateUpdated[row].setText(String.valueOf(i.getTimeSinceUpdate()));
	}

	/*
	 * 	Update object item fields
	 */
	/*private void refreshItem(Item i)
	{
		i.updateCost();
	}*/

	/*
	 * 	Update shown item fields after a change has occurred
	 */
	
	/*private void refreshPanel()	//TODO clean this up vs original loadItem(Item i)
	{
		if (loaded != null)
		{
			loaded.updateCost();
			for (int j = 0; j < loaded.craftedFromItems.length; j++)
			{
				setRow(j, loaded.craftedFromItems[j], 
						loaded.craftedFromNumbers[j], 
						loaded.craftedFromItems[j].worth);
			}
		}
	}*/
}