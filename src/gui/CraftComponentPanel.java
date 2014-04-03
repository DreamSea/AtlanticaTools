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
	 * For looping through gui components so that each one doesn't
	 * need to be created individually.
	 */
	private JButton[] ingredientButton;
	private JLabel[] ingredientNumber;
	private JFormattedTextField[] ingredientWorth;
	private JLabel[] ingredientWorthTotal;
	private JLabel[] ingredientDateUpdated;

	// Makes large costs/worths display more nicely
	private NumberFormat numberFormatter;

	// Passes item change requests back to GUIManager
	private ItemChangeNotifier craftComponentNotifier;

	CraftComponentPanel(ItemChangeNotifier icn)
	{
		craftComponentNotifier = icn;
		
		numberFormatter = NumberFormat.getNumberInstance();
		numberFormatter.setGroupingUsed(true);

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
			ingredientButton[i] = new JButton(GUIManager.BLANKSTRING);
			ingredientButton[i].addActionListener(this);

			ingredientNumber[i] = new JLabel(GUIManager.BLANKSTRING);

			ingredientWorth[i] = new JFormattedTextField(numberFormatter);
			ingredientWorth[i].setText(GUIManager.BLANKSTRING);
			ingredientWorth[i].addPropertyChangeListener("value", this);
			

			ingredientWorthTotal[i] = new JLabel(GUIManager.BLANKSTRING);
			ingredientDateUpdated[i] = new JLabel(GUIManager.BLANKSTRING);

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
		ingredientButton[row].setText(GUIManager.BLANKSTRING);
		ingredientButton[row].setEnabled(false);
		ingredientNumber[row].setText(GUIManager.BLANKSTRING);
		ingredientNumber[row].setEnabled(false);
		ingredientWorth[row].setText(GUIManager.BLANKSTRING);
		ingredientWorth[row].setEnabled(false);
		ingredientWorthTotal[row].setText(GUIManager.BLANKSTRING);
		ingredientWorthTotal[row].setEnabled(false);
		ingredientDateUpdated[row].setText(GUIManager.BLANKSTRING);
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
		
		ingredientDateUpdated[row].setEnabled(true);
		ingredientDateUpdated[row].setText(numberFormatter.format(i.getDaysSinceUpdate()));
	}
}