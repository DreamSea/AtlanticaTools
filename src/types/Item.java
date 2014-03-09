package types;

import java.util.ArrayList;

public class Item {

	/*
	 * 	For type casting from item map
	 */
	static final byte TYPE_MATERIAL = 0;
	static final byte TYPE_CRAFTABLE = 1;

	public String name;
	
	
	
	/*
	 * 	For crafting purposes, 2^31 should be enough
	 * 	Init: from .txt during first load
	 * 	Changes: only through gui setting.
	 */
	public long worth; //how much it can be sold for
	
	/*
	 * temp: using long because of how FormattedTextField returns value (long)
	 * 
	 * 	Changes: probably every gui setting of something
	 * in craft tree ancestors
	 */
	public long cost; //cost to buy all of the components
	public byte type;

	public ArrayList<Item> craftsInto;
	
	/**
	 * 
	 * @param name : Item name
	 * @param type : 0 = Material, 1 = Craftable
	 */
	public Item (String name, byte type)
	{
		this.name = name;
		worth = 1;
		cost = 1;
		this.type = type; 
		craftsInto = new ArrayList<Item>();
	}
	
	public void updateCost()
	{
		cost = worth;
	}
	
	public String toString()
	{
		return name;
	}
}
