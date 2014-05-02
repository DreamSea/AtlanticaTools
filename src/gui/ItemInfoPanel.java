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
import model.Material;

/*
 * 		ItemInfoPanel : Displays information hopefully
 * 			relevant/useful to items. Package private.
 */

class ItemInfoPanel extends JPanel implements PropertyChangeListener{

	String BLANKSTRING = GUIManager.BLANKSTRING;
	
	private NumberFormat nf;
	
	private enum HashMapKey
	{
		NAME(""), LASTUPDATE("Last Worth Update (days): "),
		
		CRAFT_WORKLOAD("Workload: "), CRAFT_WORKLOADMAX("Max WL: "),
		CRAFT_SIZE("Size: "), CRAFT_SIZEMAX("Max Size: "),
		CRAFT_WORTH("Worth w/ Interest: "),
		CRAFT_CURRENTBOOK("Using - "),
		CRAFT_WORKCOST("Cost from Workload: "),
		CRAFT_COMPONENTCOST("Cost from Components: "),
		
		CRAFT_PROFIT("Profit per Batch: "),
		CRAFT_PROFITRATIO("Profit Ratio: "),
		
		BOOK_WORKPROVIDE("Workload Provided: "),
		BOOK_WORKRATIO("Cost per Workload: ");
		
		String keyString;
		
		HashMapKey(String s)
		{
			keyString = s;
		}
		
		private String getString()
		{
			return keyString;
		}
	}
	
	// Text Pane final strings
	private HashMap<HashMapKey, String> panelStrings;
	
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
	
	/* 
	 * Sets up worth information in panel for Item. 
	 * 
	 * This method is called first in the other more specific versions to
	 * put HashMap content common to all items.
	 */
	private void loadItem(Item itemToShow)
	{
		//panelStrings.clear(); //reset hashmap for new item
		
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
			
			//HashMap field common to all items
			panelStrings.put(HashMapKey.NAME, itemToShow.getName());
			panelStrings.put(HashMapKey.LASTUPDATE, nf.format(itemToShow.getDaysSinceUpdate()));			
		}
	}
	
	void loadMaterial(Material m)
	{
		loadItem(m);
		
		displayMaterial(m);
	}
	
	void loadCraftable(Craftable c)
	{
		loadItem(c);
		
		panelStrings.put(HashMapKey.CRAFT_WORKLOAD, nf.format(c.getWorkload()));
		panelStrings.put(HashMapKey.CRAFT_WORKLOADMAX, nf.format(c.getWorkloadMax()));
		panelStrings.put(HashMapKey.CRAFT_SIZE, nf.format(c.getCraftSize()));
		panelStrings.put(HashMapKey.CRAFT_SIZEMAX, nf.format(c.getCraftSizeMax()));
		
		panelStrings.put(HashMapKey.CRAFT_WORTH, nf.format(c.getWorthWithInterestTotal()));
		
		panelStrings.put(HashMapKey.CRAFT_WORKCOST, nf.format(c.getCostOfWorkloadTotal()));
		panelStrings.put(HashMapKey.CRAFT_COMPONENTCOST, nf.format(c.getCostTotal()));
		panelStrings.put(HashMapKey.CRAFT_CURRENTBOOK, c.getSelectedCraftBook()+": "+nf.format(c.getCostPerWorkload()));
		
		panelStrings.put(HashMapKey.CRAFT_PROFIT, nf.format(c.getProfitTotal()));
		panelStrings.put(HashMapKey.CRAFT_PROFITRATIO, nf.format(c.getProfitRatio()));
		
		displayCraftable(c);
	}
	
	void loadCraftBook(CraftBook cb)
	{
		loadItem(cb);
		
		panelStrings.put(HashMapKey.BOOK_WORKPROVIDE, nf.format(cb.getWorkload()));
		panelStrings.put(HashMapKey.BOOK_WORKRATIO, nf.format(cb.getWorthPerWorkload()));
		
		displayCraftBook(cb);
	}
	
	/*
	 * Displays information common to all items. Called first from more specific
	 * versions of display____.
	 */
	private void displayItem(Item itemToShow)
	{
		try
		{
			doc.remove(0, doc.getLength());

			docPrintln(HashMapKey.NAME, STYLE_TITLE);
			if (itemToShow.getDaysSinceUpdate() > 7) //'bad' if last update over a week ago
			{
				docPrintln(HashMapKey.LASTUPDATE, STYLE_BAD);
			}
			else
			{
				docPrintln(HashMapKey.LASTUPDATE, STYLE_NORMAL);
			}
			docNewline();
		} 
		catch (BadLocationException e) //from doc.remove()
		{
			System.err.println("ItemInfoPanel: loadItem()");
		}
	}
	
	private void displayMaterial(Material m)
	{
		displayItem(m);
	}
	
	private void displayCraftable(Craftable c)
	{
		displayItem(c);
		
		docPrint(HashMapKey.CRAFT_WORKLOAD, STYLE_NORMAL);
		docPrintParen(HashMapKey.CRAFT_WORKLOADMAX, STYLE_NORMAL);
		docNewline();
		docPrint(HashMapKey.CRAFT_SIZE, STYLE_NORMAL);
		docPrintParen(HashMapKey.CRAFT_SIZEMAX, STYLE_NORMAL);
		docNewline();
		docNewline();
		docPrintln(HashMapKey.CRAFT_WORTH, STYLE_NORMAL);
		docPrintln(HashMapKey.CRAFT_COMPONENTCOST, STYLE_NORMAL);
		docPrintln(HashMapKey.CRAFT_WORKCOST, STYLE_NORMAL);
		docPrintParen(HashMapKey.CRAFT_CURRENTBOOK, STYLE_NORMAL);
		docNewline();
		docNewline();
		docPrintln(HashMapKey.CRAFT_PROFIT, STYLE_NORMAL);

		//'bad' if lose money in the process
		if (c.getProfitRatio() < profitRatioLessBad)
		{
			docPrintln(HashMapKey.CRAFT_PROFITRATIO, STYLE_BAD);
		}
		else if (c.getProfitRatio() > profitRatioMoreGood)
		{
			docPrintln(HashMapKey.CRAFT_PROFITRATIO, STYLE_GOOD);
		}
		else
		{
			docPrintln(HashMapKey.CRAFT_PROFITRATIO, STYLE_NORMAL);
		}
	}
	
	private void displayCraftBook(CraftBook cb)
	{
		displayItem(cb);
		
		docPrintln(HashMapKey.BOOK_WORKPROVIDE, STYLE_NORMAL);
		docPrintln(HashMapKey.BOOK_WORKRATIO, STYLE_NORMAL);
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
	private void docPrintln(HashMapKey key, String style)
	{
		docPrint(key, style);
		docNewline();
	}
	
	/**
	 * adds information to document
	 * @param keyString HashMap key
	 * @param style how to display the string
	 */
	private void docPrint(HashMapKey key, String style)
	{
		try
		{
			doc.insertString(doc.getLength(), key.getString(), doc.getStyle(style));
			doc.insertString(doc.getLength(), panelStrings.get(key), doc.getStyle(style));
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
	private void docPrintParen(HashMapKey key, String style)
	{
		try
		{
			doc.insertString(doc.getLength(), " (", doc.getStyle(style));
			doc.insertString(doc.getLength(), key.getString(), doc.getStyle(style));
			//System.out.println(keyString+": "+panelStrings.get(keyString));
			doc.insertString(doc.getLength(), panelStrings.get(key), doc.getStyle(style));
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
		panelStrings = new HashMap<HashMapKey, String>();
		
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
