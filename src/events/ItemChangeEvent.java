package events;

import java.util.EventObject;

/*
 * 		Item Change Event : Currently an event transfered to ManagerManager from
 * 			GUI components to trigger either changing an Item's worth or changing
 * 			what is displayed by the GUI
 */

public class ItemChangeEvent extends EventObject {

	private String itemName;
	private Long worthProposal;
	
	/**
	 * Create event to change displayed Item,
	 * @param source
	 * @param name of Item
	 */
	public ItemChangeEvent(Object source, String name)
	{
		this(source, name, null);
	}
	
	/**
	 * Create event to change price of an Item
	 * @param source
	 * @param name of Item
	 * @param worth to change Item to
	 */
	public ItemChangeEvent(Object source, String name, Long worth) {
		super(source);
		itemName = name;
		worthProposal = worth;
	}
	
	public String getItemName()
	{
		return itemName;
	}
	
	/**
	 * 
	 * @return new/requested worth of Item, null if there are no worth changes to be made
	 */
	public Long getWorthProposal()
	{
		return worthProposal;
	}

}
