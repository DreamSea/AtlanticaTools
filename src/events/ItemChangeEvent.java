package events;

import java.util.EventObject;

public class ItemChangeEvent extends EventObject {

	private String itemName;
	private long worthProposal;
	
	public ItemChangeEvent(Object source, String name)
	{
		this(source, name, -1);
	}
	
	public ItemChangeEvent(Object source, String name, long worth) {
		super(source);
		itemName = name;
		worthProposal = worth;
	}
	
	public String getItemName()
	{
		return itemName;
	}
	
	public long getWorthProposal()
	{
		return worthProposal;
	}

}
