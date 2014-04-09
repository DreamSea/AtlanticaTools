package gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.NumberFormat;

import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import javax.swing.text.StyledDocument;

import events.ItemChangeEvent;
import events.ItemChangeNotifier;
import model.CraftBook;
import model.Craftable;
import model.DataManager;
import model.Item;

/*
 * 		ItemInfoPanel : Displays information hopefully
 * 			relevant/useful to items. Package private.
 */

class ItemInfoPanel extends JPanel implements PropertyChangeListener{

	String BLANKSTRING = GUIManager.BLANKSTRING;
	
	private NumberFormat nf;
	private Item loaded;
	
	// Text Pane
	private String[] panelStrings;
	private JTextPane textPane;
	private StyledDocument doc;
	
	// Loaded Item Worth
	private JPanel worthPanel;
	private JLabel worthLabel;
	private JFormattedTextField worthField;
	
	private ItemChangeNotifier itemInfoNotifier;
	
	ItemInfoPanel(ItemChangeNotifier icn)
	{
		this.itemInfoNotifier = icn;
		
		//TODO: can grab numberformat from guimanager?
		nf = NumberFormat.getNumberInstance();
		setPreferredSize(new Dimension(300,200));
		
		//init text display of item information
		createTextPane();


		//part of panel that user can interact with
		worthLabel = new JLabel("Worth: ");
		worthField = new JFormattedTextField(nf);
		worthField.setText(BLANKSTRING);
		worthField.setPreferredSize(new Dimension(100,35));
		worthField.addPropertyChangeListener("value", this);
		worthField.setEnabled(false);

		worthPanel = new JPanel();
		worthPanel.add(worthLabel);
		worthPanel.add(worthField);		
		
		setLayout(new BorderLayout());
		add(textPane, BorderLayout.CENTER);
		add(worthPanel, BorderLayout.SOUTH);
		
		loadItem(null); //init the panel to empty
	}
	
	/**
	 * Sets panel to show different information for different item types
	 * @param itemToShow
	 */
	void loadItem(Item itemToShow)
	{
		//TODO Make field addition/deletion more easy... pretty much redo this entire section
		panelStrings[0] = "Item Name";
		panelStrings[1] = "Workload: "+BLANKSTRING+" ("+BLANKSTRING+" Max)";
		panelStrings[2] = "Size: "+BLANKSTRING;
		panelStrings[3] = "Cost w/ Workload: "+BLANKSTRING;
		panelStrings[4] = "Worth: "+BLANKSTRING;
		panelStrings[5] = "[Updated: "+BLANKSTRING+" days ago]";
		panelStrings[6] = "Profit Ratio: "+BLANKSTRING;
		
		if (itemToShow != null) //for the initial case without an item
		{	
			loaded = itemToShow;
			
			//temporarily disable worthField to keep from firing when value first set
			worthField.setEnabled(false);
			worthField.setValue(itemToShow.getWorth());
			worthField.setName(itemToShow.getName());
			worthField.setEnabled(true);
			
			panelStrings[0] = itemToShow.getName();
			panelStrings[4] = "Worth: "+nf.format(itemToShow.getWorth());
			panelStrings[5] = "[Updated "+nf.format(itemToShow.getDaysSinceUpdate())+" days ago]";
			
			if (itemToShow.getType() == Item.TYPE_CRAFTABLE)
			{
				Craftable loadedCraft = (Craftable) itemToShow;
				
				panelStrings[1] = "Workload: "+nf.format(loadedCraft.getWorkload())+" ("+nf.format(loadedCraft.maxWorkload())+" Max)";
				panelStrings[2] = "Size: "+loadedCraft.getCraftSize();
				//TODO: panelStrings[3] cost w/ workload doing too much work
				panelStrings[3] = "Cost w/ Workload: "+nf.format(loadedCraft.getCost()+loadedCraft.getWorkload()*loadedCraft.getCostPerWorkload()/loadedCraft.getCraftSize());
				panelStrings[6] = "Profit Ratio: "+nf.format(loadedCraft.getProfitRatio());
			}
			else if (itemToShow.getType() == Item.TYPE_CRAFTBOOK)
			{
				CraftBook loadedCraftBook = (CraftBook) itemToShow;
				panelStrings[1] = "Workload provided: "+nf.format(loadedCraftBook.getWorkload());
				panelStrings[6] = "Cost per Workload: "+nf.format(loadedCraftBook.getWorthPerWorkload());
			}
		}
		
		try
		{
			doc.remove(0, doc.getLength());
			
			doc.insertString(doc.getLength(), panelStrings[0]+"\n", doc.getStyle("title"));
			for (int j = 1; j < panelStrings.length; j++)
			{
				doc.insertString(doc.getLength(), panelStrings[j]+"\n", doc.getStyle("normal"));
			}
		} 
		catch (BadLocationException e)
		{
			System.err.println("ItemInfoPanel: loadItem()");
		}
	}
	
	private JTextPane createTextPane()
	{
		panelStrings = new String[7];
		
		textPane = new JTextPane();
		doc = textPane.getStyledDocument();
		addStylesToDocument(doc);
		
		return textPane;
		
	}
	
	private void addStylesToDocument(StyledDocument doc)
	{
		Style def = StyleContext.getDefaultStyleContext()
				.getStyle(StyleContext.DEFAULT_STYLE);
		
		Style normal = doc.addStyle("normal", def);
		StyleConstants.setFontFamily(def, "SansSerif");
		
		Style title = doc.addStyle("title", normal);
		StyleConstants.setFontSize(title, 20);
		StyleConstants.setBold(title, true);
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
