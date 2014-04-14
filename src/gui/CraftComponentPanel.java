package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.NumberFormat;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import events.ItemChangeEvent;
import events.ItemChangeNotifier;
import model.Craftable;
import model.Item;

/*
 * 		CraftComponentPanel : Contains main interface for switching
 * 			between crafted items and their components. Package private
 */
class CraftComponentPanel extends JPanel implements ActionListener, PropertyChangeListener {

	/*
	 *  TODO: Dimensions/relations between gui objects that I don't
	 *  follow through with very well at the moment...
	 *  
	 *  Eventually may want to relate this to an overall value in
	 *  GUIManager.
	 */
	private final int BUTTONWIDTH = 175;
	private final int WORTHWIDTH = 100;
	private final int SPACE = 35; //for each row
	private final int TOPSPACE = 35; //for top
	private final int ITEMROWS = 10; //guess at max number of components in any craft
	private final int ROWHEIGHT = 30;
	
	/*
	 * 	Title labels to describe columns
	 */
	private ArrayList<JLabel> titleRow;
	
	/*
	 * For looping through gui components so that each one doesn't
	 * need to be created individually.
	 */
	private JLabel[] ingredientProfitRatio;
	private JButton[] ingredientButton;
	private JLabel[] ingredientNumber;
	private JFormattedTextField[] ingredientWorth;
	private JLabel[] ingredientWorthTotal;
	private JLabel[] ingredientLastUpdated;
	private JLabel[] profitRatio;
	
	private static double profitRatioLessBad = 0;	//when profit ratio should be red
	private static double profitRatioMoreGood = 1; 	//when profit ratio should be green 
	private static double lastUpdatedMoreBad = 7;	//when last updated should be red
	
	private static Color colorGood = Color.blue;
	private static Color colorBad = Color.red;
	private static Color colorNeutral = Color.black;//prepare for eventual 'SavedConfigs' file
	
	// Makes large costs/worths display more nicely
	private NumberFormat numberFormatter;

	// Passes item change requests back to GUIManager
	private ItemChangeNotifier craftComponentNotifier;

	CraftComponentPanel(ItemChangeNotifier icn)
	{
		craftComponentNotifier = icn;
		
		numberFormatter = NumberFormat.getNumberInstance();
		numberFormatter.setGroupingUsed(true);

		setBackground(Color.lightGray);
		
		//init title row
		titleRow = new ArrayList<JLabel>();
		titleRow.add(new JLabel("Profit Ratio"));
		titleRow.add(new JLabel("Ingredient Name"));
		titleRow.get(titleRow.size()-1).setPreferredSize(new Dimension(200,20));
		titleRow.add(new JLabel("#"));
		titleRow.get(titleRow.size()-1).setPreferredSize(new Dimension(50,20));
		titleRow.add(new JLabel("Worth"));
		titleRow.get(titleRow.size()-1).setPreferredSize(new Dimension(100,20));
		titleRow.add(new JLabel("Total Cost"));
		titleRow.get(titleRow.size()-1).setPreferredSize(new Dimension(150,20));
		titleRow.add(new JLabel("Last Update (Days)"));
		
		//for loop javax.swing array
		ingredientProfitRatio = new JLabel[ITEMROWS];
		ingredientButton = new JButton[ITEMROWS];
		ingredientNumber = new JLabel[ITEMROWS];
		ingredientWorth = new JFormattedTextField[ITEMROWS];
		ingredientWorthTotal = new JLabel[ITEMROWS];
		ingredientLastUpdated = new JLabel[ITEMROWS];

		/*
		 * gridbag configs
		 */
		setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.insets.top = 5;
		c.insets.left = 10;
		c.insets.bottom = 5;
		
		/*
		 * put title row in gridbag
		 */
		c.gridy = 0;
		for (int col = 0; col < titleRow.size(); col++)
		{
			c.gridx = col;
			add(titleRow.get(col),c);
			titleRow.get(col).setHorizontalAlignment(SwingConstants.CENTER);
		}
		
		/*
		 * TODO specific bounds set for now, to variables later
		 * 
		 * Create the rows on panel with loop
		 */
		for (int i = 0; i < ITEMROWS; i++)
		{
			c.gridy = i + 1; //move past title row
			
			ingredientProfitRatio[i] = new JLabel(GUIManager.BLANKSTRING, SwingConstants.CENTER);
			ingredientProfitRatio[i].setForeground(colorNeutral);
			
			ingredientButton[i] = new JButton(GUIManager.BLANKSTRING);
			ingredientButton[i].addActionListener(this);

			ingredientNumber[i] = new JLabel(GUIManager.BLANKSTRING, SwingConstants.CENTER);

			ingredientWorth[i] = new JFormattedTextField(numberFormatter);
			ingredientWorth[i].setHorizontalAlignment(SwingConstants.CENTER);
			ingredientWorth[i].setText(GUIManager.BLANKSTRING);
			ingredientWorth[i].addPropertyChangeListener("value", this);			

			ingredientWorthTotal[i] = new JLabel(GUIManager.BLANKSTRING, SwingConstants.CENTER);
			ingredientLastUpdated[i] = new JLabel(GUIManager.BLANKSTRING, SwingConstants.CENTER);
			ingredientLastUpdated[i].setForeground(colorNeutral);
			
			/*
			 * add row to gridbag
			 */
			c.gridx = 0;
			add(ingredientProfitRatio[i], c);
			c.gridx = 1;
			add(ingredientButton[i],c);
			c.gridx = 2;
			add(ingredientNumber[i],c);
			c.gridx = 3;
			add(ingredientWorth[i],c);
			c.gridx = 4;
			add(ingredientWorthTotal[i],c);
			c.gridx = 5;
			add(ingredientLastUpdated[i],c);

			/*
			 * init the panel with all rows disabled so that
			 * no actions are sent without a loaded item
			 */
			disableRow(i);
		}
	}

	/**
	 * Sets panel to show component information for Craftables
	 * @param itemToShow
	 */
	void loadItem(Item itemToShow)
	{
		if (itemToShow.getType() == Item.TYPE_CRAFTABLE)
		{
			Craftable loaded = (Craftable) itemToShow;
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
		else //Non-Craftable
		{
			for (int j = 0; j < ITEMROWS; j++)
			{
				disableRow(j);
			}
		}
	}

	/*
	 * Creates ItemChangeEvent when user clicks on component buttons
	 */
	public void actionPerformed(ActionEvent e)
	{
		craftComponentNotifier.fireItemChangeEvent(
				new ItemChangeEvent(this, e.getActionCommand()));
	}

	/*
	 * Creates ItemChangeEvent when user adjusts worth text field
	 */
	public void propertyChange(PropertyChangeEvent e)
	{
		String tempName = ((JFormattedTextField) e.getSource()).getName();
		
		/*
		 * compareTo(GUIManager.BLANKSTRING) keeps ItemChangeEvent from firing
		 * when initializing the component worths of an item
		 */
		if (tempName.compareTo(GUIManager.BLANKSTRING) != 0)
		{
			craftComponentNotifier.fireItemChangeEvent(
					new ItemChangeEvent(this, tempName, (long) e.getNewValue()));
		}
	}


	/*
	 * 	Helps with disabling unused rows for current loaded item
	 */
	private void disableRow(int row)
	{
		ingredientProfitRatio[row].setEnabled(false);
		ingredientProfitRatio[row].setText(GUIManager.BLANKSTRING);
		ingredientProfitRatio[row].setForeground(colorNeutral);	
		ingredientButton[row].setText(GUIManager.BLANKSTRING);
		ingredientButton[row].setEnabled(false);
		ingredientNumber[row].setText(GUIManager.BLANKSTRING);
		ingredientNumber[row].setEnabled(false);
		ingredientWorth[row].setText(GUIManager.BLANKSTRING);
		ingredientWorth[row].setEnabled(false);
		ingredientWorthTotal[row].setText(GUIManager.BLANKSTRING);
		ingredientWorthTotal[row].setEnabled(false);
		ingredientLastUpdated[row].setText(GUIManager.BLANKSTRING);
		ingredientLastUpdated[row].setForeground(colorNeutral);
		ingredientLastUpdated[row].setEnabled(false);
	}


	/*
	 * 	Helps with setting row information for current loaded item
	 */
	private void setRow(int row, Item i, int number, long worth)
	{
		Craftable placeHolder = null;
		if (i.getType() == Item.TYPE_CRAFTABLE)
		{
			placeHolder = (Craftable) i;
			ingredientProfitRatio[row].setEnabled(true);
			ingredientProfitRatio[row].setText(numberFormatter.format(placeHolder.getProfitRatio()));
			if (placeHolder.getProfitRatio() < profitRatioLessBad)
			{
				ingredientProfitRatio[row].setForeground(colorBad);
			}
			else if (placeHolder.getProfitRatio() > profitRatioMoreGood)
			{
				ingredientProfitRatio[row].setForeground(colorGood);
			}
			else
			{
				ingredientProfitRatio[row].setForeground(colorNeutral);	
			}
		}
		else
		{
			ingredientProfitRatio[row].setEnabled(false);
			ingredientProfitRatio[row].setForeground(colorNeutral);
			ingredientProfitRatio[row].setText(GUIManager.BLANKSTRING);
		}
		
		ingredientButton[row].setEnabled(true);
		ingredientButton[row].setText(i.getName());
		ingredientButton[row].setActionCommand(i.getName());
		ingredientNumber[row].setEnabled(true);
		ingredientNumber[row].setText(String.valueOf(number));

		/*
		 * The setName GUIManager.BLANKSTRING followed by i.getName() keeps
		 * the propertyChange from firing ItemChangeEvents when
		 * worth is set
		 */
		ingredientWorth[row].setEnabled(true);
		ingredientWorth[row].setName(GUIManager.BLANKSTRING);
		ingredientWorth[row].setValue(worth);
		ingredientWorth[row].setName(i.getName());

		ingredientWorthTotal[row].setEnabled(true);
		ingredientWorthTotal[row].setText(numberFormatter.format(number*worth));
		
		ingredientLastUpdated[row].setEnabled(true);
		ingredientLastUpdated[row].setText(numberFormatter.format(i.getDaysSinceUpdate()));
		if (i.getDaysSinceUpdate() > lastUpdatedMoreBad)
		{
			ingredientLastUpdated[row].setForeground(colorBad);
		}
		else
		{
			ingredientLastUpdated[row].setForeground(colorNeutral);
		}
	}
}