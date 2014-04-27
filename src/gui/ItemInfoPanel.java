package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.NumberFormat;
import java.util.HashMap;

import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextPane;
import javax.swing.SwingConstants;
import javax.swing.text.BadLocationException;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import javax.swing.text.StyledDocument;

import events.ItemChangeEvent;
import events.ItemChangeNotifier;
import model.CraftBook;
import model.Craftable;
import model.Item;
import model.ItemType;

/*
 * 		ItemInfoPanel : Displays information hopefully
 * 			relevant/useful to items. Package private.
 */

class ItemInfoPanel extends JPanel implements PropertyChangeListener{

	String BLANKSTRING = GUIManager.BLANKSTRING;
	
	private NumberFormat nf;
	
	// Text Pane final strings
	private HashMap<String, String> panelStrings;
	private final String HM_NAME = "";
	private final String HM_LASTUPDATE = "Last Worth Update (days): ";
	
	private final String HM_CRAFT_WORKLOAD = "Workload: ";
	private final String HM_CRAFT_WORKLOADMAX = "Max WL: ";
	
	private final String HM_CRAFT_SIZE = "Size: ";
	private final String HM_CRAFT_SIZEMAX = "Max Size: ";
	
	private final String HM_CRAFT_WORTH = "Worth w/ Interest: ";
	
	private final String HM_CRAFT_CURRENTBOOK = "Using - ";
	private final String HM_CRAFT_WORKCOST = "Cost from Workload: ";
	private final String HM_CRAFT_COMPONENTCOST = "Cost from Components: ";
	
	private final String HM_CRAFT_PROFIT = "Profit per Batch: ";
	private final String HM_CRAFT_PROFITRATIO = "Profit Ratio: ";
	
	private final String HM_BOOK_WORKPROVIDE = "Workload Provided: ";
	private final String HM_BOOK_WORKRATIO = "Cost per Workload: ";
	
	private final String NEWLINE = "\n";
	
	// Styled doc stuff
	private JTextPane textPane;
	private StyledDocument doc;
	private final String STYLE_TITLE = "title";
	private final String STYLE_NORMAL = "normal";
	private final String STYLE_BAD = "bad";
	private final String STYLE_GOOD = "good";
	
	// Loaded Item Worth
	private JPanel worthPanel;
	private JLabel worthLabel;
	private JFormattedTextField worthField;
	
	// Update time info
	private JLabel updateTitleLabel;
	private JLabel updateTimeLabel;
	
	private ItemChangeNotifier itemInfoNotifier;
	
	private static double profitRatioLessBad = 0;	//when profit ratio should be red
	private static double profitRatioMoreGood = 1; 	//when profit ratio should be green 
	private static double lastUpdatedMoreBad = 7;	//when last updated should be red
	
	private static Color colorGood = Color.blue;
	private static Color colorBad = Color.red;
	private static Color colorNeutral = Color.black;//prepare for eventual 'SavedConfigs' file
	
	ItemInfoPanel(ItemChangeNotifier icn)
	{
		this.itemInfoNotifier = icn;
		
		//TODO: can grab numberformat from guimanager?
		nf = NumberFormat.getNumberInstance();
		
		setPreferredSize(new Dimension(300,280));
		
		createTextPane(); //pane showing item details
		createWorthPanel(); //panel containing last update and editable worth
		
		setLayout(new BorderLayout());
		add(textPane, BorderLayout.CENTER);
		add(worthPanel, BorderLayout.SOUTH);
	}
	
	/**
	 * TODO: break this up into smaller parts.
	 * 
	 * Sets panel to show different information for different item types
	 * @param itemToShow
	 */
	void loadItem(Item itemToShow)
	{
		panelStrings.clear(); //reset hashmap for new item
		
		if (itemToShow != null) //for the initial case without an item
		{	
			//temporarily disable worthField to keep from firing when value first set
			worthField.setEnabled(false);
			worthField.setValue(itemToShow.getWorth());
			worthField.setName(itemToShow.getName());
			worthField.setEnabled(true);
			
			updateTimeLabel.setText(nf.format(itemToShow.getDaysSinceUpdate()));
			if (itemToShow.getDaysSinceUpdate() > lastUpdatedMoreBad)
			{
				updateTimeLabel.setForeground(colorBad);
			}
			else
			{
				updateTimeLabel.setForeground(colorNeutral);
			}
			
			/*
			 * populate hashmap based on item type
			 */
			panelStrings.put(HM_NAME, itemToShow.getName());
			panelStrings.put(HM_LASTUPDATE, nf.format(itemToShow.getDaysSinceUpdate()));
					
			
			switch (itemToShow.getType())
			{
				case CRAFTABLE:
				{
					Craftable loadedCraft = (Craftable) itemToShow;
					panelStrings.put(HM_CRAFT_WORKLOAD, nf.format(loadedCraft.getWorkload()));
					panelStrings.put(HM_CRAFT_WORKLOADMAX, nf.format(loadedCraft.getWorkloadMax()));
					panelStrings.put(HM_CRAFT_SIZE, nf.format(loadedCraft.getCraftSize()));
					panelStrings.put(HM_CRAFT_SIZEMAX, nf.format(loadedCraft.getCraftSizeMax()));
					
					panelStrings.put(HM_CRAFT_WORTH, nf.format(loadedCraft.getWorthWithInterestTotal()));
					
					panelStrings.put(HM_CRAFT_WORKCOST, nf.format(loadedCraft.getCostOfWorkloadTotal()));
					panelStrings.put(HM_CRAFT_COMPONENTCOST, nf.format(loadedCraft.getCostTotal()));
					panelStrings.put(HM_CRAFT_CURRENTBOOK, loadedCraft.getSelectedCraftBook()+": "+nf.format(loadedCraft.getCostPerWorkload()));
					
					panelStrings.put(HM_CRAFT_PROFIT, nf.format(loadedCraft.getProfitTotal()));
					panelStrings.put(HM_CRAFT_PROFITRATIO, nf.format(loadedCraft.getProfitRatio()));
					break;
				}
				case CRAFTBOOK:
				{
					CraftBook loadedCraftBook = (CraftBook) itemToShow;
					panelStrings.put(HM_BOOK_WORKPROVIDE, nf.format(loadedCraftBook.getWorkload()));
					panelStrings.put(HM_BOOK_WORKRATIO, nf.format(loadedCraftBook.getWorthPerWorkload()));
					break;
				}
			}
		}
		
		//move information from hashmap to styled doc
		try
		{
			doc.remove(0, doc.getLength());
			
			if (itemToShow != null)
			{
				docPrintln(HM_NAME, STYLE_TITLE);
				if (itemToShow.getDaysSinceUpdate() > 7) //'bad' if last update over a week ago
				{
					docPrintln(HM_LASTUPDATE, STYLE_BAD);
				}
				else
				{
					docPrintln(HM_LASTUPDATE, STYLE_NORMAL);
				}
				docNewline();
				
				switch (itemToShow.getType())
				{
					case MATERIAL: //one day this might be useful
					{
						break;
					}
					case CRAFTABLE:
					{
						docPrint(HM_CRAFT_WORKLOAD, STYLE_NORMAL);
						docPrintParen(HM_CRAFT_WORKLOADMAX, STYLE_NORMAL);
						docNewline();
						docPrint(HM_CRAFT_SIZE, STYLE_NORMAL);
						docPrintParen(HM_CRAFT_SIZEMAX, STYLE_NORMAL);
						docNewline();
						docNewline();
						docPrintln(HM_CRAFT_WORTH, STYLE_NORMAL);
						docPrintln(HM_CRAFT_COMPONENTCOST, STYLE_NORMAL);
						docPrintln(HM_CRAFT_WORKCOST, STYLE_NORMAL);
						docPrintParen(HM_CRAFT_CURRENTBOOK, STYLE_NORMAL);
						docNewline();
						docNewline();
						docPrintln(HM_CRAFT_PROFIT, STYLE_NORMAL);
						
						//'bad' if lose money in the process
						if (Double.parseDouble(panelStrings.get(HM_CRAFT_PROFITRATIO)) < profitRatioLessBad)
						{
							docPrintln(HM_CRAFT_PROFITRATIO, STYLE_BAD);
						}
						else if (Double.parseDouble(panelStrings.get(HM_CRAFT_PROFITRATIO)) > profitRatioMoreGood)
						{
							docPrintln(HM_CRAFT_PROFITRATIO, STYLE_GOOD);
						}
						else
						{
							docPrintln(HM_CRAFT_PROFITRATIO, STYLE_NORMAL);
						}
						break;
					}
					case CRAFTBOOK:
					{
						docPrintln(HM_BOOK_WORKPROVIDE, STYLE_NORMAL);
						docPrintln(HM_BOOK_WORKRATIO, STYLE_NORMAL);
						break;
					}
					default:
						break;
				}
			}
		} 
		catch (BadLocationException e)
		{
			System.err.println("ItemInfoPanel: loadItem()");
		}
	}
	
	/**
	 * adds a newline to document
	 */
	private void docNewline()
	{
		try
		{
			doc.insertString(doc.getLength(), NEWLINE, doc.getStyle(STYLE_NORMAL));
		}
		catch (BadLocationException e)
		{
			System.err.println("ItemInfoPanel: docNewline()");
			e.printStackTrace();
		}
	}
	
	/**
	 * adds a newline to document after adding information
	 * @param keyString HashMap key
	 * @param style how to display the string
	 */
	private void docPrintln(String keyString, String style)
	{
		docPrint(keyString, style);
		docNewline();
	}
	
	/**
	 * adds information to document
	 * @param keyString HashMap key
	 * @param style how to display the string
	 */
	private void docPrint(String keyString, String style)
	{
		try
		{
			doc.insertString(doc.getLength(), keyString, doc.getStyle(style));
			doc.insertString(doc.getLength(), panelStrings.get(keyString), doc.getStyle(style));
		}
		catch (BadLocationException e)
		{
			System.err.println("ItemInfoPanel: docPrint()");
			e.printStackTrace();
		}
	}
	
	/**
	 * add information to document surrounded by parenthesis with space before
	 * @param keyString HashMap key
	 * @param style how to display the string
	 */
	private void docPrintParen(String keyString, String style)
	{
		try
		{
			doc.insertString(doc.getLength(), " (", doc.getStyle(style));
			doc.insertString(doc.getLength(), keyString, doc.getStyle(style));
			//System.out.println(keyString+": "+panelStrings.get(keyString));
			doc.insertString(doc.getLength(), panelStrings.get(keyString), doc.getStyle(style));
			doc.insertString(doc.getLength(), ")", doc.getStyle(style));
		}
		catch (BadLocationException e)
		{
			System.err.println("ItemInfoPanel: docPrintParen()");
			e.printStackTrace();
		}
	}
	
	private void createTextPane()
	{
		panelStrings = new HashMap<String, String>();
		
		textPane = new JTextPane();
		doc = textPane.getStyledDocument();
		addStylesToDocument(doc);
	}
	
	private void createWorthPanel()
	{
		//init worthPanel fields
		worthLabel = new JLabel("Worth: ", SwingConstants.RIGHT);
		worthField = new JFormattedTextField(nf);
		worthField.setHorizontalAlignment(SwingConstants.CENTER);
		worthField.setText(BLANKSTRING);
		worthField.setPreferredSize(new Dimension(100,20));
		worthField.addPropertyChangeListener("value", this);
		worthField.setEnabled(false);
		
		updateTitleLabel = new JLabel("Last Updated (days): ", SwingConstants.RIGHT);
		updateTimeLabel = new JLabel(BLANKSTRING, SwingConstants.CENTER);
		
		//laying out worthPanel using gridbags
		worthPanel = new JPanel(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		
		c.insets.top = 5;
		c.insets.bottom = 5;
		
		c.gridx = 0;
		c.gridy = 0;
		worthPanel.add(worthLabel,c);
		
		c.gridx = 1;
		worthPanel.add(worthField,c);
		
		c.gridx = 0;
		c.gridy = 1;
		worthPanel.add(updateTitleLabel,c);
		
		c.gridx = 1;
		worthPanel.add(updateTimeLabel, c);
	}
	
	
	private void addStylesToDocument(StyledDocument doc)
	{
		Style def = StyleContext.getDefaultStyleContext()
				.getStyle(StyleContext.DEFAULT_STYLE);
		
		Style normal = doc.addStyle(STYLE_NORMAL, def);
		StyleConstants.setFontFamily(def, "SansSerif");
		StyleConstants.setForeground(normal, colorNeutral);
		
		Style title = doc.addStyle(STYLE_TITLE, normal);
		StyleConstants.setFontSize(title, 20);
		StyleConstants.setBold(title, true);
		
		Style bad = doc.addStyle(STYLE_BAD, normal);
		StyleConstants.setForeground(bad, colorBad);
		
		Style good = doc.addStyle(STYLE_GOOD, normal);
		StyleConstants.setForeground(good, colorGood);
	}

	@Override
	public void propertyChange(PropertyChangeEvent e) {
		//TODO: consider - if(e.getSource().getClass().equals(JFormattedTextField.class))
		if (((JFormattedTextField) e.getSource()).isEnabled()) //only fires when component is enabled
		{
			itemInfoNotifier.fireItemChangeEvent(
					new ItemChangeEvent(e.getSource(), ((JFormattedTextField) e.getSource()).getName(), (long)e.getNewValue()));
		}
	}
	
}
