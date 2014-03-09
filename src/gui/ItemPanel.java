package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.NumberFormat;

import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;

import manager.Main;
import types.Craftable;
import types.Item;
import types.Material;


/*
 * 		ItemPanel : Contains main interface for switching
 * 			between crafted items and their components
 */
public class ItemPanel extends JPanel implements ActionListener, PropertyChangeListener {
	
	final int BUTTONWIDTH = 100;
	final int WORTHWIDTH = 100;
	final int SPACE = 35; //for each row
	final int TOPSPACE = 80; //for top
	
	final int ITEMROWS = 10;
	final int ROWHEIGHT = 30;
	
	final String BLANKSTRING = "---";
	
	private JLabel nameLabel;				//Item Name
	private JLabel workloadLabel;			//Item Workload
	private JLabel numCraftedLabel;			//Item Size
	private JLabel worthLabel;				//Item Worth
	private JFormattedTextField worthField;	//Field to change Item worth
	
	//for loop javax.swing items
	private JButton[] ingredientButton;
	private JLabel[] ingredientNumber;
	private JFormattedTextField[] ingredientWorth;
	private JLabel[] ingredientWorthTotal;
	
	private Craftable loaded;
	
	private NumberFormat nf;
	
	
	public ItemPanel()
	{
		nf = NumberFormat.getNumberInstance();
		nf.setGroupingUsed(true);
		
		nameLabel = new JLabel("Item: "+BLANKSTRING);
		workloadLabel = new JLabel("Workload:"+BLANKSTRING);
		numCraftedLabel = new JLabel("Size: "+BLANKSTRING);
		worthLabel = new JLabel("Worth:");
		worthField = new JFormattedTextField(nf);
		worthField.setText(BLANKSTRING);
		worthField.addPropertyChangeListener("value", this);
	
		//for loop javax.swing array
		ingredientButton = new JButton[ITEMROWS];
		ingredientNumber = new JLabel[ITEMROWS];
		ingredientWorth = new JFormattedTextField[ITEMROWS];
		ingredientWorthTotal = new JLabel[ITEMROWS];
		
		setLayout(null);
		setPreferredSize(new Dimension(
				5*SPACE+BUTTONWIDTH+WORTHWIDTH+WORTHWIDTH, 100+SPACE*ITEMROWS));
		setBackground(Color.lightGray);
		
		add(nameLabel);
		add(workloadLabel);
		add(numCraftedLabel);
		add(worthLabel);
		add(worthField);
		
		nameLabel.setBounds(10, 5, BUTTONWIDTH, SPACE);
		workloadLabel.setBounds(10, 20, BUTTONWIDTH, SPACE);
		numCraftedLabel.setBounds(10, 35, BUTTONWIDTH, SPACE);
		
		worthLabel.setBounds(10 + BUTTONWIDTH + SPACE, 5, 50, SPACE);
		worthField.setBounds(
				worthLabel.getLocation().x+worthLabel.getWidth(), 5, WORTHWIDTH, SPACE);
		worthField.setEnabled(false);
		
		/*
		 * 	Create the rows on panel with loop
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
			
			add(ingredientButton[i]);
			add(ingredientNumber[i]);
			add(ingredientWorth[i]);
			add(ingredientWorthTotal[i]);
			
			ingredientButton[i].setBounds(
					SPACE, TOPSPACE+SPACE*i, BUTTONWIDTH, ROWHEIGHT);
			ingredientNumber[i].setBounds(
					2*SPACE+BUTTONWIDTH, TOPSPACE+SPACE*i, 30, ROWHEIGHT);
			ingredientWorth[i].setBounds(
					3*SPACE+BUTTONWIDTH, TOPSPACE+SPACE*i, WORTHWIDTH, ROWHEIGHT);
			ingredientWorthTotal[i].setBounds(
					4*SPACE+BUTTONWIDTH+WORTHWIDTH, TOPSPACE+SPACE*i, WORTHWIDTH, ROWHEIGHT);
			
			disableRow(i);
		}
	}
	
	
	/*
	 * 	Sets panel to show information for item
	 */
	public void loadItem(Item i)
	{
		nameLabel.setText("Item: " + i.name);
		worthField.setEnabled(true);
		worthField.setName(BLANKSTRING);
		worthField.setText(String.valueOf(i.worth));
		worthField.setName(i.name);
		
		if (i.type == 0) //Material
		{
			workloadLabel.setText("Workload: "+BLANKSTRING);
			numCraftedLabel.setText("Size: "+BLANKSTRING);
			for (int j = 0; j < ITEMROWS; j++)
			{
				disableRow(j);
			}
		}
		else if (i.type == 1) //Craftable
		{
			loaded = (Craftable) i;
			workloadLabel.setText("Workload: "+loaded.workload);
			numCraftedLabel.setText("Size: "+loaded.numCrafted);
			for (int j = 0; j < loaded.craftedFromItems.length; j++)
			{
				setRow(j, loaded.craftedFromItems[j], loaded.craftedFromNumbers[j], loaded.craftedFromItems[j].worth);
			}
			for (int j = loaded.craftedFromItems.length; j < ITEMROWS; j++)
			{
				disableRow(j);
			}
		}
	}

	public void actionPerformed(ActionEvent e) {
		loadItem(Main.dm.itemMap.get(e.getActionCommand()));
	}
	

	public void propertyChange(PropertyChangeEvent e) {
		String tempName = ((JFormattedTextField) e.getSource()).getName();
		if (tempName.compareTo(BLANKSTRING) != 0) //only fires when user changes value
		{
			//System.out.print(e.getNewValue()+" "+tempName);
			Main.dm.itemMap.get(tempName).worth = (long) e.getNewValue();
			refreshItem();
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
	}
	
	
	/*
	 * 	Helps with setting row information for current loaded item
	 */
	private void setRow(int row, Item i, int number, long worth)
	{
		ingredientButton[row].setEnabled(true);
		ingredientButton[row].setText(i.name);
		ingredientButton[row].setActionCommand(i.name);
		ingredientNumber[row].setEnabled(true);
		ingredientNumber[row].setText(String.valueOf(number));
		
		ingredientWorth[row].setEnabled(true);
		ingredientWorth[row].setName(BLANKSTRING);
		ingredientWorth[row].setValue(worth);
		ingredientWorth[row].setName(i.name);
		
		ingredientWorthTotal[row].setEnabled(true);
		ingredientWorthTotal[row].setText(nf.format(number*worth));
	}
	
	/*
	 * 	Update shown item fields after a change has occurred
	 */
	private void refreshItem()
	{
		for (int j = 0; j < loaded.craftedFromItems.length; j++)
		{
			setRow(j, loaded.craftedFromItems[j], loaded.craftedFromNumbers[j], loaded.craftedFromItems[j].worth);
		}
	}
}