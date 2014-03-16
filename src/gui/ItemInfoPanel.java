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

import manager.DataManager;
import manager.Main;
import types.Craftable;
import types.Item;
import types.Material;

public class ItemInfoPanel extends JPanel implements PropertyChangeListener{

	final String BLANKSTRING = "---";
	
	private NumberFormat nf;
	private Item loaded;
	private Craftable loadedCraft;
	private Material loadedMat;
	
	// Text Pane
	private String[] panelStrings;
	private JTextPane textPane;
	private StyledDocument doc;
	
	// Loaded Item Worth
	private JPanel worthPanel;
	private JLabel worthLabel;
	private JFormattedTextField worthField;
	
	
	public ItemInfoPanel()
	{
		nf = NumberFormat.getNumberInstance();
		setPreferredSize(new Dimension(300,200));
		
		createTextPane();
		
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
		
		loadItem(null);
	}
	
	public void loadItem(Item i)
	{
		
		panelStrings[0] = "Item Name";
		panelStrings[1] = "Workload: "+BLANKSTRING;
		panelStrings[2] = "Size: "+BLANKSTRING;
		panelStrings[3] = "Cost w/ Workload: "+BLANKSTRING;
		panelStrings[4] = "Worth: "+BLANKSTRING;
		panelStrings[5] = "Profit Ratio: "+BLANKSTRING;
		
		if (i != null)
		{	
			loaded = i;
			
			worthField.setEnabled(true);
			worthField.setText(nf.format(i.worth));
			worthField.setName(i.name);
			
			panelStrings[0] = i.name;
			panelStrings[4] = "Worth: "+nf.format(i.worth);
			
			if (i.type == 1)
			{
				loadedCraft = (Craftable) i;
				
				panelStrings[1] = "Workload: "+nf.format(loadedCraft.workload);
				panelStrings[2] = "Size: "+loadedCraft.numCrafted;
				//TODO: panelStrings[3] cost w/ workload doing too much work
				panelStrings[3] = "Cost w/ Workload: "+nf.format(loaded.cost+loadedCraft.workload*DataManager.costPerWorkload/loadedCraft.numCrafted);
				panelStrings[5] = "Profit Ratio: "+nf.format(loadedCraft.profitRatio);
			}
			else
			{
				loadedMat = (Material) i;
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
		panelStrings = new String[6];
		
		textPane = new JTextPane();
		doc = textPane.getStyledDocument();
		addStylesToDocument(doc);
		
		return textPane;
		
	}
	
	protected void addStylesToDocument(StyledDocument doc)
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
		String tempName = ((JFormattedTextField) e.getSource()).getName();
		//System.out.println(e);
		if (tempName.compareTo(BLANKSTRING) != 0) //only fires when user changes value
		{
			//System.out.print(e.getNewValue()+" "+tempName);
			Item tempItem = Main.dm.itemMap.get(tempName);
			tempItem.worth = (long) e.getNewValue();
			tempItem.updateCost();

			//refreshItem(tempItem);
			
			if (loaded != null)
			{
				Main.gm.cip.craftTable.setData(loaded.name);
				Main.gm.iip.loadItem(loaded);
			}
			else
			{
				Main.gm.cip.craftTable.setData(loadedMat.name);
				Main.gm.iip.loadItem(loadedMat);
			}
			//System.out.println(tempItem.cost);

			
			//refreshPanel();
			loadItem(loaded);
		}
		
	}
	
}
