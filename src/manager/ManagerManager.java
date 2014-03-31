package manager;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import events.ItemChangeEvent;
import events.ItemChangeListener;
import gui.GUIManager;
import model.CraftBook;
import model.DataManager;

public class ManagerManager implements ItemChangeListener, ActionListener
{
	private DataManager dm;
	private GUIManager gm;
	
	public ManagerManager()
	{
		dm = new DataManager();
		gm = new GUIManager(this, this, dm);
	}

	/*
	 * Information requested from DataManager();
	 */
	
	
	
	/*
	 * Events received from gui
	 */
	public void itemChangeRecieved(ItemChangeEvent e) {
		//System.out.print("ManagerManager: ");
		if (e.getWorthProposal() > 0)
		{
			//System.out.println("(Worth Change) "+e.getItemName()+" "+e.getWorthProposal());
			
			dm.setItemWorth(e.getItemName(), e.getWorthProposal());
			
			dm.refreshItems();
			if (gm.getCurrentItem() != null)
			{
				gm.showItem(gm.getCurrentItem());
			}
		}
		else
		{
			//System.out.println("(Item Change) "+e.getItemName());
			gm.showItem(dm.getItem(e.getItemName()));
		}
	}

	public void actionPerformed(ActionEvent e)
	{
		//System.out.println("GUIManager Action Event: "+e.paramString());
		if (e.getActionCommand().compareTo("Save Item Prices") == 0)
		{
			dm.savePrices();
		}
		else
		{
			if (dm.itemExists(e.getActionCommand()))
			{
				DataManager.setCraftBook((CraftBook) dm.getItem(e.getActionCommand()));
				//System.out.println(DataManager.costPerWorkload);
				dm.refreshItems();
				if (gm.getCurrentItem() != null)
				{
					gm.showItem(gm.getCurrentItem());
				}
			}
			//System.out.println(e.getActionCommand());
		}
	}
	
	
	
	
}
