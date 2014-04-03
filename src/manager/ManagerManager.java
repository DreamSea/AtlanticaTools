package manager;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import events.ItemChangeEvent;
import events.ItemChangeListener;
import gui.GUIManager;
import model.CraftBook;
import model.DataManager;

/*
 * 		ManagerManager : link between data and gui managers
 */
public class ManagerManager implements ItemChangeListener, ActionListener
{
	private DataManager dm;
	private GUIManager gm;
	
	public ManagerManager()
	{
		dm = new DataManager();
		
		/*
		 * TODO: can this be done without passing DataManager
		 * in to init everything?
		 */
		gm = new GUIManager(this, this, dm);
	}	
	
	//Events received from gui relating to item displayed (besides craftbook price)
	public void itemChangeRecieved(ItemChangeEvent e)
	{
		if (e.getWorthProposal() != null) //itemChangeEvent requesting price update
		{	
			long newWorth = e.getWorthProposal();
			if (newWorth <= 0)
			{
				newWorth = 1; //an Item can at least be auto-sold for 1;
			}
			
			dm.setItemWorth(e.getItemName(), newWorth);	
			dm.refreshItems();
			
			if (gm.getCurrentItem() != null) //TODO: pretty sure this check isn't needed
			{
				/*
				 * keep display on current item in case price
				 * update relates to one of the crafting components
				 */
				gm.showItem(gm.getCurrentItem());
			}
		}
		else //itemChange event requesting panels to show a different item
		{
			gm.showItem(dm.getItem(e.getItemName()));
		}
	}

	//Events from listening in on MenuBar
	public void actionPerformed(ActionEvent e)
	{
		if (e.getActionCommand().compareTo("Save Item Prices") == 0)
		{
			dm.savePrices();
		}
		else //TODO: at the moment this just relates to changing workload, should be better way to do this
		{
			if (dm.itemExists(e.getActionCommand()))
			{
				DataManager.setCraftBook((CraftBook) dm.getItem(e.getActionCommand()));
				dm.refreshItems();
				if (gm.getCurrentItem() != null)
				{
					gm.showItem(gm.getCurrentItem());
				}
			}
		}
	}
	
	public static void main(String[] args)
	{
		new ManagerManager();
	}
}
