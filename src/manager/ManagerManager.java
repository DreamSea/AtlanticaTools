package manager;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.LocalTime;

import events.ItemChangeEvent;
import events.ItemChangeListener;
import gui.GUIManager;
import gui.GUIManagerCrafting;
import model.CraftBook;
import model.DataManager;

/*
 * 		ManagerManager : link between data and gui managers
 */
public class ManagerManager implements ItemChangeListener, ActionListener
{
	private DataManager dm;
	private GUIManager gm;
	private GUIManagerCrafting gmc;
	
	private boolean logging;
	
	public ManagerManager(boolean logging)
	{
		this.logging = logging;
		
		DebugLogger.log("Constructing DataManager", logging);
		dm = new DataManager(logging);
		
		DebugLogger.log("Constructing GUIManager", logging);
		/*
		 * TODO: can this be done without passing DataManager
		 * in to init everything?
		 */
		
		gm = new GUIManager(this, this, dm, logging);
		
		gmc = gm.getGUIManagerCrafting();
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
			//dm.refreshItems();
			
			if (gmc.getCurrentItem() != null) //TODO: pretty sure this check isn't needed
			{
				/*
				 * keep display on current item in case price
				 * update relates to one of the crafting components
				 */
				gmc.showItem(gmc.getCurrentItem());
			}
		}
		else //itemChange event requesting panels to show a different item
		{
			gmc.showItem(dm.getItem(e.getItemName()));
		}
	}

	//Events from listening in on MenuBar
	public void actionPerformed(ActionEvent e)
	{
		if (e.getActionCommand().compareTo("Save Item Prices") == 0)
		{
			dm.savePrices();
			gmc.saveDialog();
		}
		else //TODO: at the moment this just relates to changing workload, should be better way to do this
		{
			if (dm.itemExists(e.getActionCommand()))
			{
				DataManager.setCraftBook((CraftBook) dm.getItem(e.getActionCommand()));
				dm.refreshItems();
				if (gmc.getCurrentItem() != null)
				{
					gmc.showItem(gmc.getCurrentItem());
				}
			}
		}
	}
	
	public static void main(String[] args)
	{
		if (args.length == 0)
		{
			new ManagerManager(false);
		}
		else if (args[0].compareToIgnoreCase("-log") == 0)
		{
			new ManagerManager(true);
		}
		else
		{
			new ManagerManager(false);
		}
	}
}
