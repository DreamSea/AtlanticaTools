package events;

import java.util.EventObject;

public class ItemChangeEvent extends EventObject {

	private String itemName;
	private Long worthProposal;
	
	public ItemChangeEvent(Object source, String name)
	{
		this(source, name, null);
	}
	
	public ItemChangeEvent(Object source, String name, Long worth) {
		super(source);
		itemName = name;
		worthProposal = worth;
	}
	
	public String getItemName()
	{
		return itemName;
	}
	
	public Long getWorthProposal()
	{
		return worthProposal;
	}

}
